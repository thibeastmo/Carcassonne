package be.kdg.backend_game.service.exception;

public class TurnNotFoundException extends RuntimeException{
    public TurnNotFoundException() {
        super();
    }

    public TurnNotFoundException(String message) {
        super(message);
    }

    public TurnNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
