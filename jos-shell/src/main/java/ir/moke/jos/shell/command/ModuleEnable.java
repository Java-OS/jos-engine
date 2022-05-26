package ir.moke.jos.shell.command;


import ir.moke.jos.common.exception.JosModuleException;
import ir.moke.jos.module.ModuleContext;
import picocli.CommandLine;

@CommandLine.Command(name = "enable", description = "enable jos modules")
public class ModuleEnable implements Runnable {

    @CommandLine.Parameters(description = "Jos module name")
    private String josModuleName;

    @Override
    public void run() {
        try {
            ModuleContext.enable(josModuleName);
        } catch (JosModuleException e) {
            System.out.println(e.getMessage());
        }
    }
}
