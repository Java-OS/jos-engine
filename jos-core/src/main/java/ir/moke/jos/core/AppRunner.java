package ir.moke.jos.core;

import ir.moke.jos.module.ModuleContext;
import ir.moke.jos.module.ModuleContextImpl;
import ir.moke.jos.shell.JShellContainer;
import ir.moke.jsysbox.system.JSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppRunner {
    //    private static final String TERM = "xterm-256color";
    private static final String TERM = "linux";
    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class.getName());
    private static final ModuleContext moduleContext = new ModuleContextImpl();

    static {
        JSystem.setEnv("TERM", TERM);
        FileSystemManager.mountFileSystems();
    }

    public static void main(String[] args) {
        moduleContext.init();
        JShellContainer.run();
    }
}
