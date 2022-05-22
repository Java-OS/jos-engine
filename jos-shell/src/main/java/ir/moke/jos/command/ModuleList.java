package ir.moke.jos.command;


import ir.moke.jos.module.JosModule;
import ir.moke.jos.module.ModuleContainer;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command(name = "list", mixinStandardHelpOptions = true,
        description = "list jos modules")
public class ModuleList implements Runnable {

    @Override
    public void run() {
        List<JosModule> josModuleList = ModuleContainer.moduleList();
        for (JosModule josModule : josModuleList) {
            System.out.printf("%s |%s |%s |%s%n",josModule.josName(),josModule.version(),josModule.isEnable(),josModule.description());
        }
    }
}
