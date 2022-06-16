package ir.moke.jos.shell.command;


import ir.moke.jsysbox.JSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

@CommandLine.Command(name = "get", mixinStandardHelpOptions = true, subcommands = {CommandLine.HelpCommand.class},
        description = "Get environment variable")
public class GetEnv implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(GetEnv.class.getName());
    @CommandLine.Parameters(description = "key")
    private String key;

    @Override
    public void run() {
        logger.info(JSystem.getEnv(key));
    }
}
