package be.kdg.backend_game.service.exception;

public class NoSerfsAvailableException extends RuntimeException {
    public NoSerfsAvailableException() {
        super();
    }

    public NoSerfsAvailableException(String message) {
        super(message);
    }

    public NoSerfsAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
