package ir.moke.jos.module;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ModuleRepository {
    public static final ModuleRepository instance = new ModuleRepository();
    private static final Set<JosModule> MODULES = new HashSet<>();

    private ModuleRepository() {
    }

    public void update(JosModule josModule) {
        Objects.requireNonNull(josModule);
        MODULES.remove(josModule);
        MODULES.add(josModule);
    }

    public boolean isExists(String fullName) {
        return MODULES.stream()
                .anyMatch(item -> item.getFullName().equals(fullName));
    }

    public JosModule get(String name) {
        return MODULES.stream()
                .filter(item -> item.getFullName().equals(name))
                .findFirst().orElse(null);
    }

    public Set<JosModule> list() {
        return MODULES;
    }

    public Set<JosModule> enableList() {
        return MODULES.stream().filter(JosModule::isEnable).collect(Collectors.toSet());
    }

    public void remove(String fullName) {
        MODULES.removeIf(item -> item.getFullName().equals(fullName));
    }

    public void remove(JosModule module) {
        Objects.requireNonNull(module);
        MODULES.remove(module);
    }
}
