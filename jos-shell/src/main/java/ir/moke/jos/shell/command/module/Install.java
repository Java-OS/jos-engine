package ir.moke.jos.shell.command.module;


import ir.moke.jos.common.exception.JosException;
import ir.moke.jos.module.ModuleContext;
import ir.moke.jos.module.ModuleContextImpl;
import picocli.CommandLine;

import java.nio.file.Path;

@CommandLine.Command(name = "install", description = "Install modules")
public class Install implements Runnable {

    @CommandLine.Parameters(description = "Module archive path")
    private Path archive;

    @Override
    public void run() {
        try {
            ModuleContext moduleContext = new ModuleContextImpl();
            moduleContext.install(archive);
        } catch (JosException e) {
            System.out.println(e.getMessage());
        }
    }
}
