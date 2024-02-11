package be.kdg.backend_game.service.exception;

public class FriendshipNotFoundException extends RuntimeException{
    public FriendshipNotFoundException() {
        super();
    }

    public FriendshipNotFoundException(String message) {
        super(message);
    }

    public FriendshipNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
