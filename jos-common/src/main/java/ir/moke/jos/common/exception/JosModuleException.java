package ir.moke.jos.common.exception;

import java.nio.file.Path;

public class JosModuleException extends JosException {
    private JosExceptionTypes types;
    private String fileName;

    private String moduleName;

    public JosModuleException(String message) {
        super(message);
    }

    public JosModuleException(JosExceptionTypes type, Path path) {
        super((String) null);
        this.types = type;
        this.fileName = path.toFile().getName();
    }

    public JosModuleException(JosExceptionTypes type, String moduleName) {
        super((String) null);
        this.types = type;
        this.moduleName = moduleName;
    }

    @Override
    public String getMessage() {
        if (types == null) return super.getMessage();
        return switch (types) {
            case ALREADY_INSTALLED -> String.format("Module with same jar file already installed (%s)", fileName);
            case DUPLICATE_MODULE_NAME -> String.format("Module with same name already installed (%s)", moduleName);
            case MODULE_NOT_FOUND -> null;
            case FILE_NOT_FOUND -> String.format("File not found (%s)", fileName);
            case INVALID_FILE_MIME -> String.format("File is not jos module (%s)", fileName);
            default -> super.getMessage();
        };
    }
}
