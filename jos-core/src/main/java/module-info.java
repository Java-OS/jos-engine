import ir.moke.jos.module.ModuleContextImpl;

module jos.core {
    requires jos.api;
    requires jos.module;
    uses ir.moke.jos.api.GenericService;
    uses ModuleContextImpl;
    requires jos.shell;
    requires org.slf4j;
    requires jsysbox;
}