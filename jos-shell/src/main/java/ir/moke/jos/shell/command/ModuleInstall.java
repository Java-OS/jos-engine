package ir.moke.jos.shell.command;


import ir.moke.jos.module.ModuleContainer;
import picocli.CommandLine;

import java.nio.file.Path;

@CommandLine.Command(name = "install", mixinStandardHelpOptions = true,
        description = "Install jos modules")
public class ModuleInstall implements Runnable {

    @CommandLine.Parameters
    private Path jarPath ;

    @Override
    public void run() {
        ModuleContainer.installModule(jarPath);
    }
}
