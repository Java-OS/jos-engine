package ir.moke.jos.shell.command;


import picocli.CommandLine;

@CommandLine.Command(name = "module", version = "0.1", mixinStandardHelpOptions = true, subcommands = {ModuleInstall.class, ModuleList.class,ModuleEnable.class},
        description = "Jos module manager")
public class Module implements Runnable {

    @Override
    public void run() {

    }
}
