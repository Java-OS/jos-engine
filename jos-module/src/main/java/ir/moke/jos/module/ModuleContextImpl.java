package ir.moke.jos.module;

import ir.moke.jos.api.GenericService;
import ir.moke.jos.common.ArchiveUtils;
import ir.moke.jos.common.FileUtils;
import ir.moke.jos.common.Mime;
import ir.moke.jos.common.exception.JosException;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.lang.module.Configuration;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ir.moke.jos.common.Environment.MODULE_AVAILABLE_PATH;
import static ir.moke.jos.common.Environment.MODULE_ENABLED_PATH;
import static ir.moke.jos.common.exception.ExceptionMessages.*;

public class ModuleContextImpl extends AbstractModuleContext {
    @Override
    protected List<ModuleLayer> enableDependencies(JosModule josModule) throws JosException {
        List<JosModule> moduleList = josModule.getDependencies().stream()
                .map(ModuleRepository.instance::get)
                .filter(Objects::nonNull).toList();

        for (JosModule module : moduleList) {
            if (!module.isEnable()) {
                linkModule(module.getFullName());
                enableModule(module, List.of(ModuleLayer.boot()));
            }
            if (!module.isActiveService()) startService(module);
        }

        return getDependencyLayers(josModule).stream().toList();
    }

    @Override
    protected void unlinkModule(String fullName) {
        try {
            Files.delete(getEnablePath(fullName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected List<Path> getInstalledArchives() {
        try (Stream<Path> list = Files.list(MODULE_AVAILABLE_PATH)) {
            return list.filter(item -> !Files.isDirectory(item)).toList();
        } catch (Exception ignore) {
            return null;
        }
    }

    @Override
    protected ModuleLayer createLayer(JosModule josModule, List<ModuleLayer> dependencyLayers) {
        Path target = getEnablePath(josModule.getFullName());

        ModuleFinder moduleFinder = ModuleFinder.of(target);
        List<String> moduleNames = moduleFinder.findAll()
                .stream()
                .map(ModuleReference::descriptor)
                .map(ModuleDescriptor::name)
                .toList();

        if (dependencyLayers == null || dependencyLayers.isEmpty()) dependencyLayers = List.of(ModuleLayer.boot());

        Configuration configuration = Configuration.resolveAndBind(
                moduleFinder,
                dependencyLayers.stream().map(ModuleLayer::configuration).collect(Collectors.toList()),
                ModuleFinder.of(),
                moduleNames);
        return ModuleLayer.defineModulesWithOneLoader(configuration, dependencyLayers, ClassLoader.getSystemClassLoader()).layer();
    }

    private Path getEnablePath(String fullName) {
        Path of = Path.of(fullName);
        return MODULE_ENABLED_PATH.resolve(of);
    }

    private Path getAvailablePath(String fullName) {
        Path of = Path.of(fullName);
        return MODULE_AVAILABLE_PATH.resolve(of);
    }

    private List<ModuleLayer> getDependencyLayers(JosModule josModule) throws JosException {
        List<JosModule> dependencies = josModule.getDependencies()
                .stream()
                .map(ModuleRepository.instance::get)
                .filter(Objects::nonNull)
                .toList();
        if (dependencies.isEmpty() && !josModule.getDependencies().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            josModule.getDependencies().forEach(item -> sb.append(item).append("\n"));
            throw new JosException(MODULE_DEPENDENCY, sb.toString());
        }

        List<ModuleLayer> dependencyConfigurations = dependencies.stream()
                .map(JosModule::getLayer)
                .toList();
        return dependencyConfigurations.isEmpty() ? List.of(ModuleLayer.boot()) : dependencyConfigurations;
    }

    @Override
    protected void linkModule(String fullName) {
        Path of = Path.of(fullName);
        Path layerPath = MODULE_AVAILABLE_PATH.resolve(of);
        Path target = MODULE_ENABLED_PATH.resolve(of);
        try {
            Files.createSymbolicLink(target, layerPath);
        } catch (IOException ignore) {
        }
    }

    public Path copyArchive(Path archive) throws JosException {
        try {
            String fileType = Files.probeContentType(archive);
            if (fileType != null && fileType.equals(Mime.APPLICATION_ZIP)) {
                String fileName = archive.toFile().getName();
                return Files.copy(archive, MODULE_AVAILABLE_PATH.resolve(fileName));
            } else {
                throw new JosException(INVALID_FILE_MIME);
            }
        } catch (IOException e) {
            if (e instanceof NoSuchFileException) {
                throw new JosException(FILE_NOT_FOUND);
            } else if (e instanceof FileAlreadyExistsException) {
                throw new JosException(ARCHIVE_ALREADY_EXISTS);
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void extract(Path archive, Path extractPath) throws JosException {
        try {
            ArchiveUtils.unzip(archive, extractPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected JosModule extractManifest(Path archive) throws JosException {
        var yaml = new Yaml(new Constructor(JosModule.class));
        JosModule josModule;
        try {
            byte[] fileBytes = ArchiveUtils.extractFile(archive, "manifest.yaml");
            josModule = yaml.load(new String(fileBytes));
        } catch (IOException e) {
            try {
                byte[] fileBytes = ArchiveUtils.extractFile(archive, "manifest.yml");
                josModule = yaml.load(new String(fileBytes));
            } catch (IOException ex) {
                throw new JosException(MANIFEST_NOT_FOUND);
            }
        }
        if (josModule.getVersion() == null) throw new JosException(MODULE_VERSION);
        josModule.setArchive(archive);
        return josModule;
    }

    @Override
    protected void deleteModule(JosModule josModule) {
        Path availablePath = getAvailablePath(josModule.getFullName());
        FileUtils.deleteDirectory(availablePath);
        FileUtils.delete(josModule.getArchive());
    }

    @Override
    protected void startService(JosModule josModule) {
        ExecutorService es = Executors.newSingleThreadExecutor();
        ModuleLayer layer = josModule.getLayer();
        ServiceLoader.load(layer, GenericService.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .forEach(item -> es.submit(item::start));
        josModule.setActiveService(true);
        josModule.setExecutorService(es);
        ModuleRepository.instance.update(josModule);
    }

    @Override
    protected void stopService(JosModule josModule) {
        ExecutorService es = josModule.getExecutorService();
        ModuleLayer layer = josModule.getLayer();
        ServiceLoader.load(layer, GenericService.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .forEach(item -> es.submit(item::stop));
        es.shutdownNow();

        josModule.setActiveService(false);
        josModule.setExecutorService(null);
        ModuleRepository.instance.update(josModule);
    }
}
