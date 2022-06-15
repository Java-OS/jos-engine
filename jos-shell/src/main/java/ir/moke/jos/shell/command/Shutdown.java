package ir.moke.jos.shell.command;


import ir.moke.jsysbox.JSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

@CommandLine.Command(name = "shutdown",
        version = "0.1",
        mixinStandardHelpOptions = true,
        subcommands = {CommandLine.HelpCommand.class},
        description = "Shutdown system")
public class Shutdown implements Runnable {

    @Override
    public void run() {
        JSystem.shutdown();
    }
}
