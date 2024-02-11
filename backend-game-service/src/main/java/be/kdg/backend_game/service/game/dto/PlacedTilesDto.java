package be.kdg.backend_game.service.game.dto;

import be.kdg.backend_game.domain.game.Tile;
import lombok.Getter;

import java.util.List;
@Getter
public class PlacedTilesDto {
    private final List<TilePlacementDto> tilePlacements;
    private final int totalTilesInGame;

    public PlacedTilesDto(List<Tile> tiles) {
        this.totalTilesInGame = tiles.size();
        this.tilePlacements = tiles.stream().filter(Tile::isPlaced).map(TilePlacementDto::new).toList();
    }
}
