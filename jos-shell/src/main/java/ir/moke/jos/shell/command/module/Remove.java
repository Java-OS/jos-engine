package ir.moke.jos.shell.command.module;


import ir.moke.jos.common.exception.JosException;
import ir.moke.jos.module.ModuleContext;
import ir.moke.jos.module.ModuleContextImpl;
import picocli.CommandLine;

@CommandLine.Command(name = "remove", description = "Remove modules")
public class Remove implements Runnable {

    @CommandLine.Parameters(description = "Module name")
    private String name;

    @Override
    public void run() {
        ModuleContext moduleContext = new ModuleContextImpl();
        try {
            moduleContext.delete(name);
        } catch (JosException e) {
            System.out.println(e.getMessage());
        }
    }
}