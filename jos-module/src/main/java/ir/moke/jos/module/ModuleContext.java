package ir.moke.jos.module;

import ir.moke.jos.api.GenericService;
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

    static void uninstall(String name) throws JosModuleException {
        disable(name);
        ModuleUtils.removeArchive(name);
    }

    static List<JosModule> list() throws JosModuleException {
        return ModuleUtils.moduleList();
    }

    static void enable(String name) throws JosModuleException {
        ModuleUtils.enableModule(name);
    }


    /**
     * steps :
     * 1) check module enabled
     * 2) call {@link GenericService#stop()}
     * 3) remove link
     * @param name module name
     */
    static void disable(String name) throws JosModuleException {
        boolean isEnabled = ModuleUtils.isModuleEnable(name);
        if (isEnabled) {
            ModuleUtils.callStopService(name);
            ModuleUtils.removeLink(name);
        }
    }
}
