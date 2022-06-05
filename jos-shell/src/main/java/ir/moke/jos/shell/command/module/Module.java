package ir.moke.jos.shell.command.module;


import picocli.CommandLine;

@CommandLine.Command(name = "module", version = "0.1", mixinStandardHelpOptions = true,
        subcommands = {
                ModuleInstall.class,
                ModuleList.class,
                ModuleEnable.class,
                ModuleDelete.class,
                ModuleDisable.class,
                ModuleStart.class,
                ModuleStop.class,
                ModuleDependencies.class,
        },
        description = "Jos module manager")
public class Module implements Runnable {

    @Override
    public void run() {

    }
}
