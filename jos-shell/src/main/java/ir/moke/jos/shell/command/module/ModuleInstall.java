package ir.moke.jos.shell.command.module;


import ir.moke.jos.common.exception.JosException;
import ir.moke.jos.module.ModuleContext;
import ir.moke.jos.module.ModuleContextImpl;
import picocli.CommandLine;

import java.nio.file.Path;

@CommandLine.Command(name = "install", description = "Install jos modules")
public class ModuleInstall implements Runnable {

    @CommandLine.Parameters
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
