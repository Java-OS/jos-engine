package ir.moke.jos.module;

import ir.moke.jos.common.Environment;
import ir.moke.jos.common.exception.JosModuleException;

import java.nio.file.Path;
import java.util.List;

public interface ModuleContext {

    /**
     * Step :
     * 1) copy archive
     * 2) validate
     *
     * @param path of jos module file
     */
    static void install(Path path) throws JosModuleException {
        try {
            ModuleUtils.copyArchive(path);
            ModuleUtils.tupleFinder(Environment.MODULE_AVAILABLE_PATH);
        } catch (JosModuleException e) {
            String fileName = path.toFile().getName();
            ModuleUtils.removeArchive(Environment.MODULE_AVAILABLE_PATH.resolve(fileName));
            throw e;
        }

        try {
            ModuleUtils.checkDuplicateJosNames();
        } catch (JosModuleException e) {
            String fileName = path.toFile().getName();
            ModuleUtils.removeArchive(Environment.MODULE_AVAILABLE_PATH.resolve(fileName));
            throw e;
        }
    }

    static void remove(String name) {

    }

    static List<JosModule> list() throws JosModuleException {
        return ModuleUtils.moduleList();
    }

    static void enable(String name) throws JosModuleException {
        ModuleUtils.enableModule(name);
    }

    static void disable(String name) {

    }
}
