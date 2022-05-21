package ir.moke.jos;

import ir.moke.jos.api.GenericService;

public class ShellService implements GenericService {
    @Override
    public void start() {
        JShellContainer.run();
    }

    @Override
    public void stop() {

    }
}
