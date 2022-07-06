package ir.moke.jos.core;

import ir.moke.jsysbox.system.FileSystemType;
import ir.moke.jsysbox.system.JSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ir.moke.jsysbox.system.JSystem.checkMountPoint;
import static ir.moke.jsysbox.system.MountOption.*;

public class FileSystemManager {
    private static final String SUCCESS_MSG = "Mount %s";
    private static final String FAILED_MSG = "Failed to mount %s";
    private static final Logger logger = LoggerFactory.getLogger(FileSystemManager.class.getName());

    public static void mountFileSystems() {
        dev();
        proc();
        sys();
    }

    private static void proc() {
        String target = "/proc";
        boolean mounted = checkMountPoint(target);
        if (mounted) return;
        int mountFlag = mapToBit(NOEXEC, NODEV, RELATIME, NOSUID);
        logger.info(String.format(SUCCESS_MSG, target));
        mounted = JSystem.mount("proc", target, FileSystemType.PROC.getType(), mountFlag, null);
        if (!mounted) logger.error(String.format(FAILED_MSG, target));
    }

    private static void dev() {
        String target = "/dev";
        boolean mounted = checkMountPoint(target);
        if (mounted) return;
        int mountFlag = mapToBit(NOSUID, RELATIME);
        logger.info(String.format(SUCCESS_MSG, target));
        mounted = JSystem.mount("udev", target, FileSystemType.DEV_TMPFS.getType(), mountFlag, "mode=0755");
        if (!mounted) logger.error(String.format(FAILED_MSG, target));
    }

    private static void sys() {
        String target = "/sys";
        boolean mounted = checkMountPoint(target);
        if (mounted) return;
        int mountFlag = mapToBit(NOEXEC, NODEV, RELATIME, NOSUID);
        logger.info(String.format(SUCCESS_MSG, target));
        mounted = JSystem.mount("sysfs", target, FileSystemType.SYSFS.getType(), mountFlag, null);
        if (!mounted) logger.error(String.format(FAILED_MSG, target));
    }
}
