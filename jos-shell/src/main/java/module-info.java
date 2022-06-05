import ir.moke.jos.module.ModuleContextImpl;

module jos.shell {
    requires text.table.formatter;

    requires jos.module;
    requires org.jline;
    requires info.picocli;
    requires org.fusesource.jansi;
    requires jos.common;
    uses ModuleContextImpl;
    opens ir.moke.jos.shell.command;
    exports ir.moke.jos.shell;
    opens ir.moke.jos.shell.command.module;
}