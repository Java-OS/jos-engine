package ir.moke.jos.shell.command;


import ir.moke.jos.common.exception.JosException;
import ir.moke.jos.module.JosModule;
import ir.moke.jos.module.ModuleContext;
import ir.moke.jos.module.ModuleContextImpl;
import ir.moke.jos.shell.ConsoleUtils;
import org.nocrala.tools.texttablefmt.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.nio.file.Path;
import java.util.Set;

@CommandLine.Command(name = "mm",
        version = "0.1",
        mixinStandardHelpOptions = true,
        subcommands = {
                ModuleManager.Install.class,
                ModuleManager.List.class,
                ModuleManager.Enable.class,
                ModuleManager.Remove.class,
                ModuleManager.Disable.class,
                ModuleManager.Start.class,
                ModuleManager.Stop.class,
                ModuleManager.Dependencies.class,
        },
        synopsisSubcommandLabel = "COMMAND",
        description = "Operating system module manager")
public class ModuleManager implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ModuleManager.class.getName());
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @Override
    public void run() {
        throw new CommandLine.ParameterException(spec.commandLine(), "Missing required subcommand");
    }

    /*
     * List Command
     * */
    @CommandLine.Command(name = "list", description = "List jos modules")
    protected static class List implements Runnable {

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

    /*
     * Module dependencies command
     * */
    @CommandLine.Command(name = "deps", description = "Print module dependencies")
    protected static class Dependencies implements Runnable {
        @CommandLine.Parameters(description = "Module name")
        private String name;

        @Override
        public void run() {
            ModuleContext moduleContext = new ModuleContextImpl();
            try {
                StringBuilder sb = new StringBuilder();
                java.util.List<String> dependencies = moduleContext.dependencies(name);
                dependencies.forEach(item -> sb.append(item).append("\n"));
                logger.info(sb.toString());
            } catch (JosException e) {
                logger.error(e.getMessage());
            }
        }
    }

    /*
     * Install command
     * */
    @CommandLine.Command(name = "install", description = "Install modules")
    protected static class Install implements Runnable {
        @CommandLine.Parameters(description = "Module archive path")
        private Path archive;

        @Override
        public void run() {
            try {
                ModuleContext moduleContext = new ModuleContextImpl();
                moduleContext.install(archive);
            } catch (JosException e) {
                logger.error(e.getMessage());
            }
        }
    }

    /*
     * Remove command
     * */
    @CommandLine.Command(name = "remove", description = "Remove modules")
    protected static class Remove implements Runnable {
        @CommandLine.Parameters(description = "Module name")
        private String name;

        @Override
        public void run() {
            ModuleContext moduleContext = new ModuleContextImpl();
            try {
                moduleContext.delete(name);
            } catch (JosException e) {
                java.lang.System.out.println(e.getMessage());
            }
        }
    }

    /*
     * Enable Command
     * */
    @CommandLine.Command(name = "enable", description = "Enable modules layer")
    protected static class Enable implements Runnable {
        @CommandLine.Parameters(description = "Module name")
        private String name;

        @Override
        public void run() {
            ModuleContext moduleContext = new ModuleContextImpl();
            try {
                moduleContext.enable(name);
            } catch (JosException e) {
                logger.error(e.getMessage());
            }
        }
    }

    /*
     * Disable Command
     * */
    @CommandLine.Command(name = "disable", description = "Disable modules layer")
    protected static class Disable implements Runnable {
        @CommandLine.Parameters(description = "Module name")
        private String name;

        @Override
        public void run() {
            ModuleContext moduleContext = new ModuleContextImpl();
            try {
                moduleContext.disable(name);
            } catch (JosException e) {
                logger.error(e.getMessage());
            }
        }
    }

    /*
     * Start module services command
     * */
    @CommandLine.Command(name = "start", description = "Start module services")
    protected static class Start implements Runnable {
        @CommandLine.Parameters(description = "Module name")
        private String name;

        @Override
        public void run() {
            try {
                ModuleContext moduleContext = new ModuleContextImpl();
                moduleContext.start(name);
            } catch (JosException e) {
                logger.error(e.getMessage());
            }
        }
    }

    /*
     * Stop module services command
     * */
    @CommandLine.Command(name = "stop", description = "Stop module services")
    protected static class Stop implements Runnable {
        @CommandLine.Parameters(description = "Module name")
        private String name;

        @Override
        public void run() {
            ModuleContext moduleContext = new ModuleContextImpl();
            try {
                moduleContext.stop(name);
            } catch (JosException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
