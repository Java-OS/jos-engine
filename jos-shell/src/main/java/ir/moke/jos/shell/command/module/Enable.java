package ir.moke.jos.shell.command.module;


import ir.moke.jos.common.exception.JosException;
import ir.moke.jos.module.ModuleContext;
import ir.moke.jos.module.ModuleContextImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

@CommandLine.Command(name = "enable", description = "Enable modules layer")
public class Enable implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Enable.class.getName());
    @CommandLine.Parameters(description = "Module name")
    private String name;

    @Override
    public void run() {
        ModuleContext moduleContext = new ModuleContextImpl();
        try {
            moduleContext.enable(name);
        } catch (JosException e) {
            logger.error(e.getMessage());
        }
    }
}
