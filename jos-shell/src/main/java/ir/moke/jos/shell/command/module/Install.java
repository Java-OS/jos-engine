package ir.moke.jos.shell.command.module;


import ir.moke.jos.common.exception.JosException;
import ir.moke.jos.module.ModuleContext;
import ir.moke.jos.module.ModuleContextImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.nio.file.Path;

@CommandLine.Command(name = "install", description = "Install modules")
public class Install implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Install.class.getName());
    @CommandLine.Parameters(description = "Module archive path")
    private Path archive;

    @Override
    public void run() {
        try {
            ModuleContext moduleContext = new ModuleContextImpl();
            moduleContext.install(archive);
        } catch (JosException e) {
            logger.error(e.getMessage());
        }
    }
}
