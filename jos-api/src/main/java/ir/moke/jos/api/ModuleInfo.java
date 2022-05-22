package ir.moke.jos.api;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.MODULE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(MODULE)
public @interface ModuleInfo {
    String name();

    String version();

    String description();
}
