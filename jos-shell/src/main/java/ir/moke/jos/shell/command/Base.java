package ir.moke.jos.shell.command;

import picocli.CommandLine;

@CommandLine.Command(name = "system",
        version = "0.1",
        mixinStandardHelpOptions = true,
        subcommands = {
                Base.Reboot.class,
                Base.Shutdown.class,
        },
        synopsisSubcommandLabel = "COMMAND",
        description = "Operating system base commands")
public class Base implements Runnable {

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @Override
    public void run() {
        throw new CommandLine.ParameterException(spec.commandLine(), "Missing required subcommand");
    }

    /*
     * Reboot Command
     * */
    @CommandLine.Command(name = "reboot",
            version = "0.1",
            mixinStandardHelpOptions = true,
            subcommands = {CommandLine.HelpCommand.class},
            description = "Reboot system")
    protected static class Reboot implements Runnable {
        @Override
        public void run() {
            ir.moke.jsysbox.system.JSystem.reboot();
        }
    }

    /*
     * Shutdown Command
     * */
    @CommandLine.Command(name = "shutdown",
            version = "0.1",
            mixinStandardHelpOptions = true,
            subcommands = {CommandLine.HelpCommand.class},
            description = "Shutdown system")
    protected static class Shutdown implements Runnable {

        @Override
        public void run() {
            ir.moke.jsysbox.system.JSystem.shutdown();
        }
    }
}
