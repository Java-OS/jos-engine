package ir.moke.jos.module;

import ir.moke.jos.common.exception.JosException;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public interface ModuleContext {

    void init();

    void install(Path archive) throws JosException;

    void delete(String fullName) throws JosException;

    Set<JosModule> list() throws JosException;

    void enable(String fullName) throws JosException;

    void disable(String fullName) throws JosException;

    void start(String fullName) throws JosException;

    void stop(String fullName) throws JosException;

    void start() throws JosException;

    List<String> dependencies(String fullName) throws JosException;
}
