package be.kdg.backend_game.service.game.dto;

import be.kdg.backend_game.domain.game.Tile;
import lombok.Data;

@Data
public class TilePlacementDto {
    private String image_url;
    private int orientation;
    private CoordinatesDto coordinates;
    private LocationDto location;

    public TilePlacementDto(Tile tile) {
        this.image_url = tile.getTileImage();
        this.orientation = tile.getOrientation().ordinal();
        this.coordinates = new CoordinatesDto(tile.getX(), tile.getY());
        this.location = new LocationDto();
    }

    public TilePlacementDto(String image_url, int orientation, int x, int y) {
        this.image_url = image_url;
        this.orientation = orientation;
        this.coordinates = new CoordinatesDto(x,y);
        this.location = new LocationDto();
    }
    public int getCoordinateX() {
        return this.coordinates.getX();
    }

    public int getCoordinateY() {
        return this.coordinates.getY();
    }

    @Data
    private static class CoordinatesDto {
        private int x;
        private int y;

        public CoordinatesDto(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    @Data
    private static class LocationDto {
        private int top;
        private int left;

        public LocationDto() {
            this.top = 0;
            this.left = 0;
        }
    }
}
