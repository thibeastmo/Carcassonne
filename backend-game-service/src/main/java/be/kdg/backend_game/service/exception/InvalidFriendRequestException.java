package be.kdg.backend_game.service.exception;

public class InvalidFriendRequestException extends RuntimeException{
    public InvalidFriendRequestException() {
        super();
    }

    public InvalidFriendRequestException(String message) {
        super(message);
    }

    public InvalidFriendRequestException(String message, Throwable cause) {
        super(message, cause);
    }

}
