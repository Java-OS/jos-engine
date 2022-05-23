package ir.moke.jos.shell.command;


import picocli.CommandLine;

@CommandLine.Command(name = "echo", mixinStandardHelpOptions = true, subcommands = {CommandLine.HelpCommand.class},
        description = "print strings")
public class Echo implements Runnable {

    @CommandLine.Parameters(description = "strs")
    private String parameter;

    @Override
    public void run() {
        System.out.println(parameter);
    }
}
