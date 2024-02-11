package be.kdg.backend_game.domain.game;

import lombok.Data;

import java.util.Objects;
import java.util.UUID;

@Data
public class TilePlacement {
    private int xValue;
    private int yValue;
    private OrientationEnum orientation;

    public TilePlacement(int xValue, int yValue, OrientationEnum orientation) {
        this.xValue = xValue;
        this.yValue = yValue;
        this.orientation = orientation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TilePlacement that)) return false;
        return xValue == that.xValue && yValue == that.yValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xValue, yValue);
    }
}
