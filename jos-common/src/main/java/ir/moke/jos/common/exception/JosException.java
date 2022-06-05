package ir.moke.jos.common.exception;

public class JosException extends Exception {
    public JosException() {
        super();
    }

    public JosException(String message) {
        super(message);
    }

    public JosException(String message, Throwable cause) {
        super(message, cause);
    }

    public JosException(Throwable cause) {
        super(cause);
    }

    public JosException(ExceptionMessages messages) {
        super(String.format("[%d] %s", messages.getCode(), messages.getMessage()));
    }

    public JosException(ExceptionMessages messages, String message) {
        super(String.format("[%d] %s\n%s", messages.getCode(), messages.getMessage(), message));
    }
}
