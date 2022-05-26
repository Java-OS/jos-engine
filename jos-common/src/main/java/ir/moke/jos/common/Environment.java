package ir.moke.jos.common;

import java.nio.file.Path;
import java.nio.file.Paths;

public interface Environment {
    Path MODULE_AVAILABLE_PATH = Paths.get("/modules/available/");
    Path MODULE_ENABLED_PATH = Paths.get("/modules/enabled/");
}
