package ir.moke.jos.core;

import ir.moke.jos.module.ModuleContext;
import ir.moke.jos.module.ModuleContextImpl;
import ir.moke.jos.shell.JShellContainer;
import ir.moke.jsysbox.system.FileSystemType;
import ir.moke.jsysbox.system.JSystem;
import ir.moke.jsysbox.system.MountOptionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ir.moke.jsysbox.system.MountOption.*;

public class AppRunner {
//    private static final String TERM = "xterm-256color";
    private static final String TERM = "linux";
    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class.getName());
    private static final ModuleContext moduleContext = new ModuleContextImpl();

    static {
        JSystem.setEnv("TERM", TERM);
        mountFileSystems();
    }

    public static void main(String[] args) {
        moduleContext.init();
        JShellContainer.run();
    }

    public static void mountFileSystems() {
        String mountOptions = new MountOptionBuilder(RW, NOSUID, NOEXEC, RELATIME).create();
        logger.info("Mount /dev");
        JSystem.mount("udev", "/dev", FileSystemType.DEV_TMPFS.getType(), mountOptions);
        logger.info("Mount /proc");
        JSystem.mount("proc", "/proc", FileSystemType.PROC.getType(), mountOptions);
    }
}
