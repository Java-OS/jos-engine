package ir.moke.jos.shell.command;


import ir.moke.jos.module.JosModule;
import ir.moke.jos.module.ModuleContainer;
import ir.moke.jos.shell.StringUtils;
import org.nocrala.tools.texttablefmt.Table;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command(name = "list", description = "list jos modules")
public class ModuleList implements Runnable {

    @Override
    public void run() {
        List<JosModule> josModuleList = ModuleContainer.moduleList();
        Table table = StringUtils.formatListToTextTable(josModuleList);
        table.setColumnWidth(0, 15, 50);
        table.setColumnWidth(1, 15, 50);
        table.setColumnWidth(2, 15, 50);
        table.setColumnWidth(3, 20, 50);
        table.setColumnWidth(4, 8, 8);
        table.setColumnWidth(5, 35, 50);
        System.out.println(table.render());
    }
}
