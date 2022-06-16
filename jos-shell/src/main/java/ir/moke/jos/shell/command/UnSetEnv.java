package ir.moke.jos.shell.command;


import ir.moke.jsysbox.JSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

@CommandLine.Command(name = "unset", mixinStandardHelpOptions = true, subcommands = {CommandLine.HelpCommand.class},
        description = "Unset environment variable")
public class UnSetEnv implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(UnSetEnv.class.getName());
    @CommandLine.Parameters(description = "key")
    private String str;

    @Override
    public void run() {
        boolean set = JSystem.unSetEnv(str);
        if (!set) logger.warn("operation error");
    }
}
