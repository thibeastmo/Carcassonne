package be.kdg.backend_game.service.game.dto;

import be.kdg.backend_game.domain.game.TilePlacement;

public class PlacementDto {
    private int xValue;
    private int yValue;
    private int orientation;

    public PlacementDto(TilePlacement placement) {
        this.xValue = placement.getXValue();
        this.yValue = placement.getYValue();
        this.orientation = placement.getOrientation().ordinal();
    }

    public PlacementDto() {
    }

    public int getxValue() {
        return xValue;
    }

    public void setxValue(int xValue) {
        this.xValue = xValue;
    }

    public int getyValue() {
        return yValue;
    }

    public void setyValue(int yValue) {
        this.yValue = yValue;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
}
