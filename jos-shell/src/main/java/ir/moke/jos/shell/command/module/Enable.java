package ir.moke.jos.shell.command.module;


import ir.moke.jos.common.exception.JosException;
import ir.moke.jos.module.ModuleContext;
import ir.moke.jos.module.ModuleContextImpl;
import picocli.CommandLine;

@CommandLine.Command(name = "enable", description = "Enable modules layer")
public class Enable implements Runnable {

    @CommandLine.Parameters(description = "Module name")
    private String name;

    @Override
    public void run() {
        ModuleContext moduleContext = new ModuleContextImpl();
        try {
            moduleContext.enable(name);
        } catch (JosException e) {
            System.out.println(e.getMessage());
        }
    }
}
