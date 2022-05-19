module jos.shell {
    requires jos.api;
    uses ir.moke.jos.api.GenericService;
    provides ir.moke.jos.api.GenericService with ir.moke.jos.ShellService ;
}