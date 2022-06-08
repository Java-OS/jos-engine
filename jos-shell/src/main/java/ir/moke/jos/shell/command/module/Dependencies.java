package ir.moke.jos.shell.command.module;


import ir.moke.jos.common.exception.JosException;
import ir.moke.jos.module.ModuleContext;
import ir.moke.jos.module.ModuleContextImpl;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command(name = "deps", description = "Print module dependencies")
public class Dependencies implements Runnable {

    @CommandLine.Parameters(description = "Module name")
    private String name;

    @Override
    public void run() {
        ModuleContext moduleContext = new ModuleContextImpl();
        try {
            StringBuilder sb = new StringBuilder();
            List<String> dependencies = moduleContext.dependencies(name);
            dependencies.forEach(item -> sb.append(item).append("\n"));
            System.out.println(sb);
        } catch (JosException e) {
            System.out.println(e.getMessage());
        }
    }
}
