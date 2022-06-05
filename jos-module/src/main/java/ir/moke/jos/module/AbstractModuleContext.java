package ir.moke.jos.module;

import ir.moke.jos.common.exception.JosException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static ir.moke.jos.common.Environment.MODULE_AVAILABLE_PATH;
import static ir.moke.jos.common.Environment.MODULE_ENABLED_PATH;
import static ir.moke.jos.common.exception.ExceptionMessages.*;

public abstract class AbstractModuleContext implements ModuleContext {
    @Override
    public void init() {
        List<Path> archivePaths = getInstalledArchives();
        List<JosModule> enableModules = new ArrayList<>();
        for (Path availableModule : archivePaths) {
            try {
                JosModule josModule = extractManifest(availableModule);
                boolean exists = Files.exists(MODULE_ENABLED_PATH.resolve(josModule.getFullName()));

                if (exists) enableModules.add(josModule);
                ModuleRepository.instance.update(josModule);
            } catch (JosException e) {
                throw new RuntimeException(e);
            }
        }

        for (JosModule josModule : enableModules) {
            try {
                enable(josModule.getFullName());
            } catch (JosException ignore) {
            }
        }
    }

    @Override
    public void install(Path archive) throws JosException {
        Path path = copyArchive(archive);
        JosModule josModule = extractManifest(path);
        String fullName = josModule.getFullName();
        Path extractPath = MODULE_AVAILABLE_PATH.resolve(fullName);

        boolean exists = ModuleRepository.instance.isExists(fullName);
        if (exists) throw new JosException(DUPLICATE_MODULE);

        extract(archive, extractPath);
        ModuleRepository.instance.update(josModule);

    }

    @Override
    public void delete(String fullName) throws JosException {
        JosModule josModule = getJosModule(fullName);
        if (josModule.isLock()) throw new JosException(MODULE_LOCK);
        if (josModule.isEnable()) disable(fullName);

        deleteModule(josModule);
        ModuleRepository.instance.remove(josModule);
    }

    @Override
    public Set<JosModule> list() throws JosException {
        return ModuleRepository.instance.list();
    }

    @Override
    public void enable(String fullName) throws JosException {
        JosModule josModule = getJosModule(fullName);
        if (josModule.isEnable()) throw new JosException(MODULE_ALREADY_ENABLED);

        var dependencyLayers = enableDependencies(josModule);

        linkModule(josModule.getFullName());
        enableModule(josModule, dependencyLayers);
        startService(josModule);
    }

    @Override
    public void disable(String fullName) throws JosException {
        JosModule josModule = getJosModule(fullName);
        if (!josModule.isEnable()) throw new JosException(MODULE_ALREADY_DISABLE);
        if (josModule.canDisableOrStop()) throw new JosException(MODULE_LOCK);
        if (josModule.isActiveService()) stopService(josModule);

        unlinkModule(fullName);
        josModule.setLayer(null);
        josModule.setEnable(false);
        ModuleRepository.instance.update(josModule);
    }

    @Override
    public void start(String fullName) throws JosException {
        JosModule josModule = getJosModule(fullName);
        if (!josModule.isEnable()) throw new JosException(MODULE_DISABLED);
        if (josModule.isActiveService()) throw new JosException(MODULE_SERVICES_ALREADY_ACTIVATED);
        startService(josModule);
    }

    @Override
    public void start() throws JosException {
        List<String> moduleNames = ModuleRepository.instance.list()
                .stream()
                .map(JosModule::getFullName)
                .toList();
        for (String moduleName : moduleNames) {
            start(moduleName);
        }
    }

    @Override
    public void stop(String fullName) throws JosException {
        JosModule josModule = getJosModule(fullName);
        if (josModule.canDisableOrStop()) throw new JosException(MODULE_LOCK);
        if (!josModule.isActiveService()) throw new JosException(MODULE_SERVICES_ALREADY_DEACTIVATED);

        stopService(josModule);
    }

    @Override
    public void stop() throws JosException {
        List<String> moduleNames = ModuleRepository.instance.list()
                .stream()
                .map(JosModule::getFullName)
                .toList();
        for (String moduleName : moduleNames) {
            stop(moduleName);
        }
    }

    @Override
    public List<String> dependencies(String fullName) throws JosException {
        JosModule josModule = getJosModule(fullName);
        return josModule.getDependencies();
    }

    /*
     * Abstract methods
     * */
    protected abstract void unlinkModule(String fullName);

    protected abstract List<Path> getInstalledArchives();

    protected abstract ModuleLayer createLayer(JosModule josModule, List<ModuleLayer> dependencyLayers) throws JosException;

    protected abstract void linkModule(String fullName);

    // Abstract methods
    protected abstract Path copyArchive(Path archive) throws JosException;

    protected abstract JosModule extractManifest(Path archive) throws JosException;

    protected abstract void extract(Path archive, Path extractPath) throws JosException;

    protected abstract void deleteModule(JosModule josModule);

    protected abstract void startService(JosModule josModule);

    protected abstract void stopService(JosModule josModule);

    protected abstract List<ModuleLayer> enableDependencies(JosModule josModule) throws JosException;


    /*
     * Other Methods
     * */
    private JosModule getJosModule(String fullName) throws JosException {
        var josModule = ModuleRepository.instance.get(fullName);
        if (josModule == null) throw new JosException(MODULE_NOT_FOUND);
        return josModule;
    }

    void enableModule(JosModule josModule, List<ModuleLayer> dependencyLayers) throws JosException {
        ModuleLayer moduleLayer = createLayer(josModule, dependencyLayers);
        josModule.setLayer(moduleLayer);
        josModule.setEnable(true);
        ModuleRepository.instance.update(josModule);
    }
}
