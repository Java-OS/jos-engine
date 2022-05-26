import ir.moke.jos.module.ModuleUtils;

module jos.core {
    requires jos.api;
    requires jos.module;
    uses ir.moke.jos.api.GenericService;
    uses ModuleUtils;
    requires jos.shell;
}