package ir.moke.jos.shell.command;


import ir.moke.jsysbox.JSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

@CommandLine.Command(name = "set", mixinStandardHelpOptions = true, subcommands = {CommandLine.HelpCommand.class},
        description = "Set environment variable")
public class SetEnv implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(SetEnv.class.getName());
    @CommandLine.Parameters(description = "environment , ex: key=value")
    private String str;

    @Override
    public void run() {
        String[] split = str.split("=");
        if (split.length < 2) {
            logger.error("Invalid environment , ex: key=value");
            return;
        }

        System.out.println(split[0] + " -> " + split[1]);
        boolean set = JSystem.setEnv(split[0], split[1]);
        if (!set) logger.warn("operation error");
    }
}
