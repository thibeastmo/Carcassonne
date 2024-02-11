package be.kdg.backend_game.service.exception;

public class ShopItemNotFoundException extends RuntimeException{
    public ShopItemNotFoundException() {
        super();
    }

    public ShopItemNotFoundException(String message) {
        super(message);
    }

    public ShopItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
