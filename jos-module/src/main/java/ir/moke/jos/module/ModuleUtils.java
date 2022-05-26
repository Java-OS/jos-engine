package ir.moke.jos.module;

import ir.moke.jos.api.GenericService;
import ir.moke.jos.api.ModuleInfo;
import ir.moke.jos.common.exception.JosExceptionTypes;
import ir.moke.jos.common.exception.JosModuleException;
import ir.moke.jos.common.tuple.Tuple2;

import java.io.IOException;
import java.lang.module.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import static ir.moke.jos.common.Environment.MODULE_AVAILABLE_PATH;
import static ir.moke.jos.common.Environment.MODULE_ENABLED_PATH;

public class ModuleUtils {
    private static final ModuleLayer moduleLayer = ModuleLayer.boot();

    private static final String APPLICATION_JAVA_ARCHIVE = "application/java-archive";

    /**
     * Exceptions :
     * 1) Same jar file already installed
     * 2) Module with same name already installed
     * 3) Jar file not found
     * 4) This file is not jos module.
     *
     * @param path jar file
     */
    public static void copyArchive(Path path) throws JosModuleException {

        try {
            String fileType = Files.probeContentType(path);
            if (fileType != null && fileType.equals(APPLICATION_JAVA_ARCHIVE)) {
                String jarFileName = path.toFile().getName();
                Files.copy(path, MODULE_AVAILABLE_PATH.resolve(jarFileName));
            } else {
                throw new JosModuleException(JosExceptionTypes.INVALID_FILE_MIME, path);
            }
        } catch (IOException e) {
            if (e instanceof NoSuchFileException) {
                throw new JosModuleException(JosExceptionTypes.FILE_NOT_FOUND, path);
            } else if (e instanceof FileAlreadyExistsException) {
                throw new JosModuleException(JosExceptionTypes.ALREADY_INSTALLED, path);
            }
            throw new RuntimeException(e);
        }
    }

    public static List<JosModule> moduleList() throws JosModuleException {
        ModuleLayer availableModuleLayer = getModuleLayer(MODULE_AVAILABLE_PATH);

        Set<Module> moduleList = availableModuleLayer.modules();

        List<JosModule> josModuleList = new ArrayList<>();
        for (Module module : moduleList) {
            if (module.isAnnotationPresent(ModuleInfo.class)) {
                ModuleInfo moduleInfo = module.getAnnotation(ModuleInfo.class);
                String josName = moduleInfo.name();
                String name = module.getName();
                String version = moduleInfo.version();
                String description = moduleInfo.description();
                Path jarFilePath = availableModuleLayer.configuration().findModule(name)
                        .map(ResolvedModule::reference)
                        .flatMap(ModuleReference::location)
                        .map(item -> Paths.get(item.getPath()))
                        .orElse(null);

                assert jarFilePath != null;

                boolean isEnable = isModuleEnable(jarFilePath);
                JosModule josModule = new JosModule(josName, name, version, description, isEnable, jarFilePath);
                josModuleList.add(josModule);
            }
        }

        return josModuleList;
    }

    public static Tuple2<ModuleFinder, List<String>> tupleFinder(Path path) throws JosModuleException {
        ModuleFinder moduleFinder = null;
        List<String> moduleNames = null;
        try {
            moduleFinder = ModuleFinder.of(path);
            moduleNames = moduleFinder.findAll()
                    .stream()
                    .map(ModuleReference::descriptor)
                    .map(ModuleDescriptor::name)
                    .toList();
        } catch (Exception e) {
            if (e instanceof FindException) {
                throw new JosModuleException(e.getMessage());
            } else {
                e.printStackTrace();
            }
        }
        return new Tuple2<>(moduleFinder, moduleNames);
    }

    private static ModuleLayer getModuleLayer(Path path) throws JosModuleException {
        Tuple2<ModuleFinder, List<String>> tupleFinder = tupleFinder(path);
        var configuration = moduleLayer.configuration().resolveAndBind(tupleFinder.r1(), ModuleFinder.of(), tupleFinder.r2());

        return moduleLayer.defineModulesWithOneLoader(configuration, ClassLoader.getSystemClassLoader());
    }

    public static boolean isModuleEnable(Path jarFile) {
        String fileName = jarFile.toFile().getName();
        return Files.exists(MODULE_ENABLED_PATH.resolve(fileName), LinkOption.NOFOLLOW_LINKS);
    }

    public static void enableModule(String moduleName) throws JosModuleException {
        linkModule(moduleName);
        var moduleLayer = getModuleLayer(MODULE_ENABLED_PATH);
        ServiceLoader.load(moduleLayer, GenericService.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .forEach(GenericService::start);
    }

    public static void linkModule(String moduleName) throws JosModuleException {
        Optional<JosModule> josModule = moduleList().stream()
                .filter(item -> item.josName().equals(moduleName))
                .findFirst();

        josModule.ifPresent(item -> {
            Path jarFilePath = item.jarFile();
            String name = jarFilePath.toFile().getName();
            try {
                Files.createSymbolicLink(MODULE_ENABLED_PATH.resolve(name), jarFilePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void removeArchive(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void checkDuplicateJosNames() throws JosModuleException {
        try {

            List<JosModule> josModuleList = moduleList();
            Optional<String> optionalModuleName = josModuleList.stream()
                    .collect(Collectors.groupingBy(JosModule::josName))
                    .entrySet()
                    .stream()
                    .filter(item -> item.getValue().size() > 1)
                    .map(Map.Entry::getKey)
                    .findFirst();
            if (optionalModuleName.isPresent()) {
                throw new JosModuleException(JosExceptionTypes.DUPLICATE_MODULE_NAME, optionalModuleName.get());
            }
        } catch (LayerInstantiationException e) {
            throw new JosModuleException(e.getMessage());
        }
    }
}
