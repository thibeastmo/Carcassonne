package be.kdg.backend_game.service.exception;

public class TileNotFoundException extends RuntimeException {
    public TileNotFoundException() {
        super();
    }

    public TileNotFoundException(String message) {
        super(message);
    }

    public TileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
