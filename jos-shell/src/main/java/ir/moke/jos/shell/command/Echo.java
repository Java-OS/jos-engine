package ir.moke.jos.shell.command;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

@CommandLine.Command(name = "echo",
        mixinStandardHelpOptions = true,
        subcommands = {CommandLine.HelpCommand.class},
        description = "print strings")
public class Echo implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Echo.class.getName());
    @CommandLine.Parameters(description = "strings")
    private String parameter;

    @Override
    public void run() {
        logger.info(parameter);
    }
}
