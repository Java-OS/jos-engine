package ir.moke.jos.core;

import ir.moke.jos.module.ModuleContext;
import ir.moke.jos.module.ModuleContextImpl;
import ir.moke.jos.shell.JShellContainer;

public class AppRunner {

    private static final ModuleContext moduleContext = new ModuleContextImpl();

    public static void main(String[] args) {
        moduleContext.init();

        JShellContainer.run();
    }
}
