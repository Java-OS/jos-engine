package ir.moke.jos.shell.command.module;


import ir.moke.jos.common.exception.JosException;
import ir.moke.jos.module.ModuleContext;
import ir.moke.jos.module.ModuleContextImpl;
import picocli.CommandLine;

@CommandLine.Command(name = "disable", description = "disable jos modules")
public class ModuleDisable implements Runnable {

    @CommandLine.Parameters(description = "Jos module name")
    private String name;

    @Override
    public void run() {
        ModuleContext moduleContext = new ModuleContextImpl();
        try {
            moduleContext.disable(name);
        } catch (JosException e) {
            System.out.println(e.getMessage());
        }
    }
}
