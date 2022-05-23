package ir.moke.jos.shell.command;


import picocli.CommandLine;

@CommandLine.Command(name = "module", mixinStandardHelpOptions = true, subcommands = {ModuleInstall.class, ModuleList.class,ModuleEnable.class},
        description = "Jos module manager")
public class Module implements Runnable {

    @Override
    public void run() {

    }
}
