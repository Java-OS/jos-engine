package ir.moke.jos.core;

import ir.moke.jos.module.ModuleContext;
import ir.moke.jos.module.ModuleContextImpl;
import ir.moke.jos.shell.JShellContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppRunner {
    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class.getName());
    private static final ModuleContext moduleContext = new ModuleContextImpl();

    public static void main(String[] args) {
        logger.info("Initialize Modules");
        moduleContext.init();

        JShellContainer.run();
    }
}
