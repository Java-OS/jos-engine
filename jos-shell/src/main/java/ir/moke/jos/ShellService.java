package ir.moke.jos;

import ir.moke.jos.api.GenericService;

public class ShellService implements GenericService {
    @Override
    public void start() {
        Shell.instance.run();
    }

    @Override
    public void stop() {
        /*
         * NOTE : do not implement this.
         * */
    }
}
