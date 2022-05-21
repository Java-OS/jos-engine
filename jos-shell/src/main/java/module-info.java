module jos.shell {
    requires jos.api;
    requires org.jline;
    requires info.picocli;
    requires org.fusesource.jansi;
    uses ir.moke.jos.api.GenericService;
    opens ir.moke.jos.command;
    provides ir.moke.jos.api.GenericService with ir.moke.jos.ShellService;
}