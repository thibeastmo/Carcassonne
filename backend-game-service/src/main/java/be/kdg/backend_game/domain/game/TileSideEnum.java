package be.kdg.backend_game.domain.game;

import lombok.Getter;

@Getter
public enum TileSideEnum {
    TOP(1), RIGHT(5), BOTTOM(7), LEFT(3);
    private final int ordinalValue;

    TileSideEnum(int ordinalValue) {
        this.ordinalValue = ordinalValue;
    }

    public TileSideEnum opposite() {
        return switch (this) {
            case TOP -> BOTTOM;
            case RIGHT -> LEFT;
            case BOTTOM -> TOP;
            case LEFT -> RIGHT;
        };
    }
    public static TileSideEnum getByOrdinalValue(int value) {
        for (TileSideEnum side : values()) {
            if (side.ordinalValue == value) {
                return side;
            }
        }
        throw new IllegalArgumentException("No enum constant with ordinal value " + value);
    }
}
