package ir.moke.jos.module;

import ir.moke.jos.common.CliTransient;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class JosModule {
    private String name;
    private String version;
    private String description;
    private boolean enable;
    private boolean activeService;
    private List<String> dependencies = new ArrayList<>();
    private ModuleLayer layer;

    private ExecutorService executorService;

    private Path archive;

    @CliTransient
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @CliTransient
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @CliTransient
    public List<String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies;
    }

    @CliTransient
    public ModuleLayer getLayer() {
        return layer;
    }

    public void setLayer(ModuleLayer layer) {
        this.layer = layer;
    }

    public String getFullName() {
        return name + ":" + version;
    }

    @CliTransient
    public Path getArchive() {
        return archive;
    }

    public void setArchive(Path archive) {
        this.archive = archive;
    }

    public boolean isActiveService() {
        return activeService;
    }

    public void setActiveService(boolean activeService) {
        this.activeService = activeService;
    }

    @CliTransient
    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public boolean isLock() {
        return !childModules().isEmpty();
    }

    public List<JosModule> childModules() {
        Set<JosModule> josModules = ModuleRepository.instance.list();
        List<JosModule> childs = new ArrayList<>();
        for (JosModule module : josModules) {
            List<String> modDeps = module.getDependencies();
            for (String modDep : modDeps) {
                if (modDep.equals(getFullName())) childs.add(module);
            }
        }
        return childs;
    }

    public boolean hasChildModules() {
        return !childModules().isEmpty();
    }

    public boolean canDisableOrStop() {
        return childModules().stream().anyMatch(JosModule::isEnable);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JosModule josModule = (JosModule) o;
        return Objects.equals(name, josModule.name) &&
                Objects.equals(version, josModule.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, version);
    }
}
