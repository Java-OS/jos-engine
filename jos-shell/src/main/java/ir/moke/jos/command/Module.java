package ir.moke.jos.command;


import picocli.CommandLine;

@CommandLine.Command(name = "module", mixinStandardHelpOptions = true, subcommands = {ModuleInstall.class, ModuleList.class},
        description = "Jos module manager")
public class Module implements Runnable {

    @Override
    public void run() {

    }
}
