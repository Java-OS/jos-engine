package ir.moke.jos.shell.command.module;


import ir.moke.jos.common.exception.JosException;
import ir.moke.jos.module.ModuleContext;
import ir.moke.jos.module.ModuleContextImpl;
import picocli.CommandLine;

@CommandLine.Command(name = "start", description = "Start module services")
public class Start implements Runnable {

    @CommandLine.Parameters(description = "Module name")
    private String name;

    @Override
    public void run() {
        try {
            ModuleContext moduleContext = new ModuleContextImpl();
            moduleContext.start(name);
        } catch (JosException e) {
            System.out.println(e.getMessage());
        }
    }
}
