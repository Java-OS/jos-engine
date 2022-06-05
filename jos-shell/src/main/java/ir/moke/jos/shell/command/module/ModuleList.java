package ir.moke.jos.shell.command.module;


import ir.moke.jos.common.exception.JosException;
import ir.moke.jos.module.JosModule;
import ir.moke.jos.module.ModuleContext;
import ir.moke.jos.module.ModuleContextImpl;
import ir.moke.jos.shell.ConsoleUtils;
import org.nocrala.tools.texttablefmt.Table;
import picocli.CommandLine;

import java.util.Set;

@CommandLine.Command(name = "list", description = "list jos modules")
public class ModuleList implements Runnable {

    @Override
    public void run() {
        try {
            ModuleContext context = new ModuleContextImpl();
            Set<JosModule> josModuleList = context.list();
            if (josModuleList.isEmpty()) return;
            Table table = ConsoleUtils.formatListToTextTable(josModuleList);
            table.setColumnWidth(0, 25, 50);
            table.setColumnWidth(1, 15, 50);
            table.setColumnWidth(2, 20, 50);
            table.setColumnWidth(3, 15, 50);
            table.setColumnWidth(4, 15, 50);
            System.out.println(table.render());
        } catch (JosException e) {
            System.out.println(e.getMessage());
        }
    }
}
