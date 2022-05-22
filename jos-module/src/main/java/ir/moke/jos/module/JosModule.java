package ir.moke.jos.module;

import java.nio.file.Path;

public record JosModule(String josName,
                        String moduleName,
                        String version,
                        String description,
                        boolean isEnable,
                        Path jarFile
) {
}
