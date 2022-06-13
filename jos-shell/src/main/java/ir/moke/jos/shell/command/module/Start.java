package ir.moke.jos.shell.command.module;


import ir.moke.jos.common.exception.JosException;
import ir.moke.jos.module.ModuleContext;
import ir.moke.jos.module.ModuleContextImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

@CommandLine.Command(name = "start", description = "Start module services")
public class Start implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Start.class.getName());
    @CommandLine.Parameters(description = "Module name")
    private String name;

    @Override
    public void run() {
        try {
            ModuleContext moduleContext = new ModuleContextImpl();
            moduleContext.start(name);
        } catch (JosException e) {
            logger.error(e.getMessage());
        }
    }
}
