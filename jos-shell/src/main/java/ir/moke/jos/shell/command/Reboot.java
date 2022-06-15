package ir.moke.jos.shell.command;


import ir.moke.jsysbox.JSystem;
import picocli.CommandLine;

@CommandLine.Command(name = "reboot",
        version = "0.1",
        mixinStandardHelpOptions = true,
        subcommands = {CommandLine.HelpCommand.class},
        description = "Reboot system")
public class Reboot implements Runnable {
    @Override
    public void run() {
        JSystem.reboot();
    }
}
