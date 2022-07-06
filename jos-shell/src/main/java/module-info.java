module jos.shell {
    requires text.table.formatter;

    requires jos.module;
    requires info.picocli;
    requires org.fusesource.jansi;
    requires jos.common;
    opens ir.moke.jos.shell.command;
    exports ir.moke.jos.shell;
    requires org.slf4j;
    requires jsysbox;
    requires org.jline.reader;
    requires org.jline.terminal;
    requires org.jline.console;
    requires org.jline.builtins;
}