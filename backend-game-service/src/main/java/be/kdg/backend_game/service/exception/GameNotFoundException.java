package be.kdg.backend_game.service.exception;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException() {
        super();
    }

    public GameNotFoundException(String message) {
        super(message);
    }

    public GameNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }



}
