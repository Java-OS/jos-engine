package ir.moke.jos.module;

import ir.moke.jos.api.GenericService;
import ir.moke.jos.api.ModuleInfo;

import java.io.IOException;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.lang.module.ResolvedModule;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ModuleContainer {
    private static final ModuleLayer moduleLayer = ModuleLayer.boot();
    private static final Path MODULE_AVAILABLE_PATH = Paths.get("/modules/available/");
    private static final Path MODULE_ENABLED_PATH = Paths.get("/modules/enabled/");

    private static final String APPLICATION_JAVA_ARCHIVE = "application/java-archive";

    public static void installModule(Path src) {
        try {
            String fileType = Files.probeContentType(src);
            if (fileType != null && fileType.equals(APPLICATION_JAVA_ARCHIVE)) {
                String jarFileName = src.toFile().getName();
                Files.copy(src, MODULE_AVAILABLE_PATH.resolve(jarFileName));
            } else {
                System.out.println("This file is not jos module");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<JosModule> moduleList() {
        ModuleLayer availableModuleLayer = availableModuleLayer();

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

    private static ModuleLayer availableModuleLayer() {
        var moduleFinder = ModuleFinder.of(MODULE_AVAILABLE_PATH);
        var moduleNames = moduleFinder.findAll()
                .stream()
                .map(ModuleReference::descriptor)
                .map(ModuleDescriptor::name)
                .toList();
        var configuration = moduleLayer.configuration().resolveAndBind(moduleFinder, ModuleFinder.of(), moduleNames);

        return moduleLayer.defineModulesWithOneLoader(configuration, ClassLoader.getSystemClassLoader());
    }

    private static ModuleLayer getEnableModuleLayer() {
        var moduleFinder = ModuleFinder.of(MODULE_ENABLED_PATH);
        var moduleNames = moduleFinder.findAll()
                .stream()
                .map(ModuleReference::descriptor)
                .map(ModuleDescriptor::name)
                .toList();
        var configuration = moduleLayer.configuration().resolve(moduleFinder, ModuleFinder.of(), moduleNames);

        return moduleLayer.defineModulesWithOneLoader(configuration, ClassLoader.getSystemClassLoader());
    }

    public static boolean isModuleEnable(Path jarFile) {
        String fileName = jarFile.toFile().getName();
        return Files.exists(MODULE_ENABLED_PATH.resolve(fileName), LinkOption.NOFOLLOW_LINKS);
    }

    public static void enableModule(String moduleName) {
        linkModule(moduleName);
        var moduleLayer = getEnableModuleLayer();
        ServiceLoader.load(moduleLayer, GenericService.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .forEach(GenericService::start);
    }

    public static void linkModule(String moduleName) {
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

}
