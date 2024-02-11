package be.kdg.backend_game.service.exception;

public class InvalidInviteException extends RuntimeException{
    public InvalidInviteException() {
        super();
    }

    public InvalidInviteException(String message) {
        super(message);
    }

    public InvalidInviteException(String message, Throwable cause) {
        super(message, cause);
    }

}
