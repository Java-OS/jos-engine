package ir.moke.jos.shell.command.module;


import ir.moke.jos.common.exception.JosException;
import ir.moke.jos.module.ModuleContext;
import ir.moke.jos.module.ModuleContextImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command(name = "deps", description = "Print module dependencies")
public class Dependencies implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Dependencies.class.getName());
    @CommandLine.Parameters(description = "Module name")
    private String name;

    @Override
    public void run() {
        ModuleContext moduleContext = new ModuleContextImpl();
        try {
            StringBuilder sb = new StringBuilder();
            List<String> dependencies = moduleContext.dependencies(name);
            dependencies.forEach(item -> sb.append(item).append("\n"));
            logger.info(sb.toString());
        } catch (JosException e) {
            logger.error(e.getMessage());
        }
    }
}
