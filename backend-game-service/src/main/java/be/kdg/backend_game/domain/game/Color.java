package be.kdg.backend_game.domain.game;

import lombok.Getter;

@Getter
public enum Color {
    BLACK("#000000"),
    BLUE("#0000FF"),
    GREEN("#00FF00"),
    RED("#FF0000"),
    YELLOW("#FFFF00");

    private final String hexCode;

    Color(String hexCode) {
        this.hexCode = hexCode;
    }

}

