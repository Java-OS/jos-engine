package ir.moke.jos.shell;


import org.jline.builtins.Options.HelpException;
import org.jline.console.ArgDesc;
import org.jline.console.CmdDesc;
import org.jline.console.CommandRegistry;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.SystemCompleter;
import org.jline.utils.AttributedString;
import picocli.CommandLine;
import picocli.CommandLine.Help;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Model.OptionSpec;

import java.util.*;
import java.util.stream.Collectors;

public class ShellRegistry implements CommandRegistry {

    private final CommandLine cmd;
    private final Set<String> commands;
    private final Map<String, String> aliasCommand = new HashMap<>();

    public ShellRegistry(CommandLine cmd) {
        this.cmd = cmd;
        commands = cmd.getCommandSpec().subcommands().keySet();
        for (String c : commands) {
            for (String a : cmd.getSubcommands().get(c).getCommandSpec().aliases()) {
                aliasCommand.put(a, c);
            }
        }
    }

    public boolean hasCommand(String command) {
        return commands.contains(command) || aliasCommand.containsKey(command);
    }


    public SystemCompleter compileCompleters() {
        SystemCompleter out = new SystemCompleter();
        List<String> all = new ArrayList<>();
        all.addAll(commands);
        all.addAll(aliasCommand.keySet());
        out.add(all, new PicocliCompleter());
        return out;
    }

    private class PicocliCompleter extends ArgumentCompleter implements Completer {

        public PicocliCompleter() {
            super(NullCompleter.INSTANCE);
        }

        @Override
        public void complete(LineReader reader, ParsedLine commandLine, List<Candidate> candidates) {
            Objects.requireNonNull(commandLine);
            Objects.requireNonNull(candidates);

            String word = commandLine.word();
            List<String> words = commandLine.words();
            CommandLine sub = findSubcommandLine(words, commandLine.wordIndex());
            if (sub == null) {
                return;
            }
            if (word.startsWith("-")) {
                String buffer = word.substring(0, commandLine.wordCursor());
                int eq = buffer.indexOf('=');
                for (OptionSpec option : sub.getCommandSpec().options()) {
                    if (option.arity().max() == 0 && eq < 0) {
                        addCandidates(candidates, Arrays.asList(option.names()));
                    } else {
                        if (eq > 0) {
                            String opt = buffer.substring(0, eq);
                            if (Arrays.asList(option.names()).contains(opt) && option.completionCandidates() != null) {
                                addCandidates(candidates, option.completionCandidates(), buffer.substring(0, eq + 1), "", true);
                            }
                        } else {
                            addCandidates(candidates, Arrays.asList(option.names()), "", "=", false);
                        }
                    }
                }
            } else {
                addCandidates(candidates, sub.getSubcommands().keySet());
                for (CommandLine s : sub.getSubcommands().values()) {
                    addCandidates(candidates, Arrays.asList(s.getCommandSpec().aliases()));
                }
            }
        }

        private void addCandidates(List<Candidate> candidates, Iterable<String> cands) {
            addCandidates(candidates, cands, "", "", true);
        }

        private void addCandidates(List<Candidate> candidates, Iterable<String> cands, String preFix, String postFix, boolean complete) {
            for (String s : cands) {
                candidates.add(new Candidate(AttributedString.stripAnsi(preFix + s + postFix), s, null, null, null, null, complete));
            }
        }

    }

    private CommandLine findSubcommandLine(List<String> args, int lastIdx) {
        CommandLine out = cmd;
        for (int i = 0; i < lastIdx; i++) {
            if (!args.get(i).startsWith("-")) {
                out = findSubcommandLine(out, args.get(i));
                if (out == null) {
                    break;
                }
            }
        }
        return out;
    }

    private CommandLine findSubcommandLine(CommandLine cmdline, String command) {
        for (CommandLine s : cmdline.getSubcommands().values()) {
            if (s.getCommandName().equals(command) || Arrays.asList(s.getCommandSpec().aliases()).contains(command)) {
                return s;
            }
        }
        return null;
    }

    @Override
    public CmdDesc commandDescription(List<String> args) {
        CommandLine sub = findSubcommandLine(args, args.size());
        if (sub == null) {
            return null;
        }
        CommandSpec spec = sub.getCommandSpec();
        Help cmdhelp = new picocli.CommandLine.Help(spec);
        List<AttributedString> main = new ArrayList<>();
        Map<String, List<AttributedString>> options = new HashMap<>();
        String synopsis = AttributedString.stripAnsi(spec.usageMessage().sectionMap().get("synopsis").render(cmdhelp));
        main.add(HelpException.highlightSyntax(synopsis.trim(), HelpException.defaultStyle()));
        // using JLine help highlight because the statement below does not work well...
        //        main.add(new AttributedString(spec.usageMessage().sectionMap().get("synopsis").render(cmdhelp).toString()));
        for (OptionSpec o : spec.options()) {
            String key = Arrays.stream(o.names()).collect(Collectors.joining(" "));
            List<AttributedString> val = new ArrayList<>();
            for (String d : o.description()) {
                val.add(new AttributedString(d));
            }
            if (o.arity().max() > 0) {
                key += "=" + o.paramLabel();
            }
            options.put(key, val);
        }
        return new CmdDesc(main, ArgDesc.doArgNames(Arrays.asList("")), options);
    }

    @Override
    public List<String> commandInfo(String command) {
        List<String> out = new ArrayList<>();
        CommandSpec spec = cmd.getSubcommands().get(command).getCommandSpec();
        Help cmdhelp = new picocli.CommandLine.Help(spec);
        String description = AttributedString.stripAnsi(spec.usageMessage().sectionMap().get("description").render(cmdhelp).toString());
        out.addAll(Arrays.asList(description.split("\\r?\\n")));
        return out;
    }

    // For JLine >= 3.16.0
    @Override
    public Object invoke(org.jline.console.CommandRegistry.CommandSession session, String command, Object[] args) throws Exception {
        List<String> arguments = new ArrayList<>();
        arguments.add(command);
        arguments.addAll(Arrays.stream(args).map(Object::toString).collect(Collectors.toList()));
        cmd.execute(arguments.toArray(new String[0]));
        return null;
    }

    // @Override This method was removed in JLine 3.16.0; keep it in case this component is used with an older version of JLine
    public Object execute(org.jline.console.CommandRegistry.CommandSession session, String command, String[] args) throws Exception {
        List<String> arguments = new ArrayList<>();
        arguments.add(command);
        arguments.addAll(Arrays.asList(args));
        cmd.execute(arguments.toArray(new String[0]));
        return null;
    }

    @Override
    public Set<String> commandNames() {
        return commands;
    }

    @Override
    public Map<String, String> commandAliases() {
        return aliasCommand;
    }

    // @Override This method was removed in JLine 3.16.0; keep it in case this component is used with an older version of JLine
    public CmdDesc commandDescription(String command) {
        return null;
    }
}
