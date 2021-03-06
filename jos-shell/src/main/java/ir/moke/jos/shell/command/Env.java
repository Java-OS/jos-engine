package ir.moke.jos.shell.command;

import ir.moke.jsysbox.system.JSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command(name = "env",
        version = "0.1",
        mixinStandardHelpOptions = true,
        subcommands = {
                Env.EnvGet.class,
                Env.EnvSet.class,
                Env.EnvUnSet.class,
                Env.EnvList.class
        },
        synopsisSubcommandLabel = "COMMAND",
        description = "Environment variables")
public class Env implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Env.class.getName());

    @Override
    public void run() {
        printListEnvironments();
    }

    /*
     * Set Command
     * */
    @CommandLine.Command(name = "set",
            mixinStandardHelpOptions = true,
            description = "Set environment variable")
    protected static class EnvSet implements Runnable {
        @CommandLine.Parameters(description = "environment , ex: key=value")
        private String str;

        @Override
        public void run() {
            String[] split = str.split("=");
            if (split.length < 2) {
                logger.error("Invalid environment , ex: key=value");
                return;
            }

            logger.info(split[0] + "=" + split[1]);
            boolean set = JSystem.setEnv(split[0], split[1]);
            if (!set) logger.warn("operation error");
        }
    }

    /*
     * Unset Command
     * */
    @CommandLine.Command(name = "unset",
            mixinStandardHelpOptions = true,
            description = "Unset environment variable")
    public static class EnvUnSet implements Runnable {
        @CommandLine.Parameters(description = "key")
        private String str;

        @Override
        public void run() {
            boolean set = JSystem.unSetEnv(str);
            if (!set) logger.warn("operation error");
        }
    }


    /*
     * Get Command
     * */
    @CommandLine.Command(name = "get",
            mixinStandardHelpOptions = true,
            description = "Get environment variable")
    protected static class EnvGet implements Runnable {
        @CommandLine.Parameters(description = "key")
        private String key;

        public void run() {
            logger.info(JSystem.getEnv(key));
        }
    }

    /*
     * Get Command
     * */
    @CommandLine.Command(name = "list",
            mixinStandardHelpOptions = true,
            description = "List current variables")
    protected static class EnvList implements Runnable {
        @Override
        public void run() {
            printListEnvironments();
        }
    }

    protected static void printListEnvironments() {
        List<String> environments = JSystem.environments();
        for (String env : environments) {
            System.out.println(env);
        }
    }
}

