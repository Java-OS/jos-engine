package ir.moke.jos;

import ir.moke.jos.command.Echo;
import ir.moke.jos.command.Module;
import org.fusesource.jansi.AnsiConsole;
import org.jline.console.SystemRegistry;
import org.jline.console.impl.SystemRegistryImpl;
import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import picocli.CommandLine;

import java.io.PrintWriter;

public class JShellContainer {

    private static final String PS1 = "jshell:> ";

    @CommandLine.Command(name = "",
            description = {
                    "Example interactive shell with completion and autosuggestions. " +
                            "Hit @|magenta <TAB>|@ to see available commands.",
                    "Hit @|magenta ALT-S|@ to toggle tailtips.",
                    ""},
            footer = {"", "Press Ctrl-D to exit."})
    static class CliCommands implements Runnable {
        PrintWriter out;

        CliCommands() {
        }

        public void setReader(LineReader reader) {
            out = reader.getTerminal().writer();
        }

        public void run() {
            out.println(new CommandLine(this).getUsageMessage());
        }
    }

    public static void run() {
        AnsiConsole.systemInstall();
        try {

            CliCommands cliCommands = new CliCommands();
            CommandLine.IFactory defaultFactory = CommandLine.defaultFactory();
            CommandLine cmd = new CommandLine(cliCommands, defaultFactory);

            cmd.addSubcommand(Echo.class);
            cmd.addSubcommand(Module.class);
            cmd.addSubcommand(CommandLine.HelpCommand.class);

            ShellRegistry shellRegistry = new ShellRegistry(cmd);

            Parser parser = new DefaultParser();
            try (Terminal terminal = TerminalBuilder.builder().build()) {
                SystemRegistry systemRegistry = new SystemRegistryImpl(parser, terminal, null, null);
                systemRegistry.setCommandRegistries(shellRegistry);
                systemRegistry.register("help", shellRegistry);

                LineReader reader = LineReaderBuilder.builder()
                        .terminal(terminal)
                        .completer(systemRegistry.completer())
                        .parser(parser)
                        .variable(LineReader.LIST_MAX, 50)   // max tab completion candidates
                        .build();
                cliCommands.setReader(reader);

                String line;
                while (true) {
                    try {
                        systemRegistry.cleanUp();
                        line = reader.readLine(PS1, null, (MaskingCallback) null, null);
                        systemRegistry.execute(line);
                    } catch (UserInterruptException e) {
                        // Ignore
                    } catch (EndOfFileException e) {
                        return;
                    } catch (Exception e) {
                        systemRegistry.trace(e);
                    }
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            AnsiConsole.systemUninstall();
        }
    }
}