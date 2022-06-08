package ir.moke.jos.shell.command.module;


import picocli.CommandLine;

@CommandLine.Command(name = "mm",
        version = "0.1",
        mixinStandardHelpOptions = true,
        subcommands = {
                Install.class,
                List.class,
                Enable.class,
                Remove.class,
                Disable.class,
                Start.class,
                Stop.class,
                Dependencies.class,
        },
        synopsisSubcommandLabel = "COMMAND",
        description = "Operating system module manager")
public class ModuleManager implements Runnable {
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @Override
    public void run() {
        throw new CommandLine.ParameterException(spec.commandLine(), "Missing required subcommand");
    }
}
