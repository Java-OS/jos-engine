package ir.moke.jos.shell.command.module;


import ir.moke.jos.common.exception.JosException;
import ir.moke.jos.module.JosModule;
import ir.moke.jos.module.ModuleContext;
import ir.moke.jos.module.ModuleContextImpl;
import ir.moke.jos.shell.ConsoleUtils;
import org.nocrala.tools.texttablefmt.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.util.Set;

@CommandLine.Command(name = "list", description = "List jos modules")
public class List implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(List.class.getName());

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
            logger.info(table.render());
        } catch (JosException e) {
            logger.error(e.getMessage());
        }
    }
}
