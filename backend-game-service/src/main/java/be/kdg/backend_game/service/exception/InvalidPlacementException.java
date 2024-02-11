package be.kdg.backend_game.service.exception;

public class InvalidPlacementException extends RuntimeException{
    public InvalidPlacementException() {
        super();
    }

    public InvalidPlacementException(String message) {
        super(message);
    }

    public InvalidPlacementException(String message, Throwable cause) {
        super(message, cause);
    }

}