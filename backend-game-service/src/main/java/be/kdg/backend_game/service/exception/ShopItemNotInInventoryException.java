package be.kdg.backend_game.service.exception;

public class ShopItemNotInInventoryException extends RuntimeException {
    public ShopItemNotInInventoryException() {
        super();
    }

    public ShopItemNotInInventoryException(String message) {
        super(message);
    }

    public ShopItemNotInInventoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
