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
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;

public class ModuleContainer {

    private static final Path MODULE_AVAILABLE_PATH = Paths.get("/modules/available/");
    private static final Path MODULE_ENABLED_PATH = Paths.get("/modules/enabled/");

    private static final String APPLICATION_JAVA_ARCHIVE = "application/java-archive";

    public static void startBuiltinServices() {
        ServiceLoader.load(ModuleLayer.boot(), GenericService.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .forEach(GenericService::start);
    }

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
        var moduleLayer = ModuleLayer.boot();
        var moduleFinder = ModuleFinder.of(MODULE_AVAILABLE_PATH);
        var moduleNames = moduleFinder.findAll()
                .stream()
                .map(ModuleReference::descriptor)
                .map(ModuleDescriptor::name)
                .toList();
        var configuration = moduleLayer.configuration().resolveAndBind(moduleFinder, ModuleFinder.of(), moduleNames);

        var afterLoadModuleLayer = moduleLayer.defineModulesWithOneLoader(configuration, ClassLoader.getSystemClassLoader());

        Set<Module> moduleList = afterLoadModuleLayer.modules();

        List<JosModule> josModuleList = new ArrayList<>();
        for (Module module : moduleList) {
            if (module.isAnnotationPresent(ModuleInfo.class)) {
                ModuleInfo moduleInfo = module.getAnnotation(ModuleInfo.class);
                String josName = moduleInfo.name();
                String name = module.getName();
                String version = moduleInfo.version();
                String description = moduleInfo.description();
                Path jarFilePath = afterLoadModuleLayer.configuration().findModule(name)
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

    public static boolean isModuleEnable(Path jarFile) {
        String fileName = jarFile.toFile().getName();
        return Files.exists(MODULE_ENABLED_PATH.resolve(fileName), LinkOption.NOFOLLOW_LINKS);
    }

}
