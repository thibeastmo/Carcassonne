package be.kdg.backend_game.service.game.dto;

import be.kdg.backend_game.domain.game.*;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class TileDto {
    private final UUID tileId;
    private final String tileName;
    private final List<TileTypeEnum> tileZones;
    private final String tileImage;
    private final boolean placed;
    private final int orientation;

    public TileDto(Tile tile) {
        this.tileId = tile.getTileId();
        this.tileName = tile.getTileName();
        this.tileZones = tile.getTileZones();
        this.tileImage = tile.getTileImage();
        this.placed = tile.isPlaced();
        this.orientation = tile.getOrientation().ordinal();
    }
}
