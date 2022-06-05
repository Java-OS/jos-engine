module jos.module {
    requires jos.api;
    requires jos.common;
    uses ir.moke.jos.api.GenericService;
    exports ir.moke.jos.module;
    requires org.yaml.snakeyaml;
    opens ir.moke.jos.module to jos.shell;
}