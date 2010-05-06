package swinbank.server.policy;

/**
 *
 * @author Daniel
 */
public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(Throwable cause) {
        super(cause);
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException() {
    }
}
