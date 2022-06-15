package ir.moke.jos.core;

import ir.moke.jos.module.ModuleContext;
import ir.moke.jos.module.ModuleContextImpl;
import ir.moke.jos.shell.JShellContainer;
import ir.moke.jsysbox.FileSystemType;
import ir.moke.jsysbox.JSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppRunner {
    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class.getName());
    private static final ModuleContext moduleContext = new ModuleContextImpl();

    static {
        mountFileSystems();
    }

    public static void main(String[] args) {
        moduleContext.init();
        JShellContainer.run();
    }

    public static void mountFileSystems() {
        JSystem.mount("udev","/dev", FileSystemType.DEV_TMPFS.getType());
        JSystem.mount("proc","/proc", FileSystemType.PROC.getType());
    }
}
