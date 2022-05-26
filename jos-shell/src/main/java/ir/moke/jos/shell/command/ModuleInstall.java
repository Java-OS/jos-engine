package ir.moke.jos.shell.command;


import ir.moke.jos.common.exception.JosModuleException;
import ir.moke.jos.module.ModuleContext;
import picocli.CommandLine;

import java.nio.file.Path;

@CommandLine.Command(name = "install", description = "Install jos modules")
public class ModuleInstall implements Runnable {

    @CommandLine.Parameters
    private Path jarPath;

    @Override
    public void run() {
        try {
            ModuleContext.install(jarPath);
        } catch (JosModuleException e) {
            System.out.println(e.getMessage());
        }
    }
}
