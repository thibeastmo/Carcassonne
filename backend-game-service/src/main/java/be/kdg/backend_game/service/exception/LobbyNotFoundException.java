package be.kdg.backend_game.service.exception;

public class LobbyNotFoundException extends RuntimeException {
    public LobbyNotFoundException() {
        super();
    }

    public LobbyNotFoundException(String message) {
        super(message);
    }

    public LobbyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
