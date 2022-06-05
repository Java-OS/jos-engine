package ir.moke.jos.common.exception;

public enum ExceptionMessages {
    FILE_NOT_FOUND(1001, "File not found"),
    INVALID_FILE_MIME(1002, "File is not jos module"),

    ARCHIVE_ALREADY_EXISTS(3000, "Module with same name already installed"),

    MODULE_NOT_FOUND(3001, "Module does not exists"),

    MANIFEST_NOT_FOUND(3002, "Archive has not manifest.yaml or manifest.yml"),

    MODULE_VERSION(3003, "Invalid manifest [version]"),

    DUPLICATE_MODULE(3004, "Module with same name already installed"),

    MODULE_ENABLED(3005, "Module enabled"),

    MODULE_DISABLED(3006, "Module disabled"),

    MODULE_ALREADY_ENABLED(3007, "Module already enabled"),

    MODULE_ALREADY_DISABLE(3008, "Module already disabled"),

    MODULE_SERVICES_ALREADY_ACTIVATED(3009, "Module services already activated"),

    MODULE_SERVICES_ALREADY_DEACTIVATED(3010, "Module services already deactivated"),
    MODULE_DEPENDENCY(3011, "Module dependencies not installed"),
    MODULE_LOCK(3012, "Module is lock"),

    ;
    private final int code;
    private final String message;

    ExceptionMessages(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
