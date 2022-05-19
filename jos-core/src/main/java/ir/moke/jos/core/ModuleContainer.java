package ir.moke.jos.core;

import ir.moke.jos.api.GenericService;

import java.util.ServiceLoader;

public class ModuleContainer {

    public static void startBuiltinServices() {
        ServiceLoader.load(ModuleLayer.boot(), GenericService.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .forEach(GenericService::start);
    }
}
