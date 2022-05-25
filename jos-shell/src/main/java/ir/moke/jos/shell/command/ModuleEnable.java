package ir.moke.jos.shell.command;


import ir.moke.jos.module.ModuleContainer;
import picocli.CommandLine;

@CommandLine.Command(name = "enable",description = "enable jos modules")
public class ModuleEnable implements Runnable {

    @CommandLine.Parameters(description = "Jos module name")
    private String josModuleName;

    @Override
    public void run() {
        ModuleContainer.enableModule(josModuleName);
    }
}
