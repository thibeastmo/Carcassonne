package be.kdg.backend_game.service.game;

import be.kdg.backend_game.domain.game.Tile;
import be.kdg.backend_game.domain.game.TileTypeEnum;
import be.kdg.backend_game.service.dto.AvailableTileZonesDto;
import be.kdg.backend_game.service.exception.TileNotFoundException;
import be.kdg.backend_game.service.game.tile_calculations.CityScoreService;
import be.kdg.backend_game.service.game.tile_calculations.RoadScoreService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class SerfPlacementService {
    private final TileService tileService;
    private final TurnService turnService;
    private final GameRulesService gameRulesService;
    private final RoadScoreService roadScoreService;
    private final CityScoreService cityScoreService;

    public SerfPlacementService(TileService tileService, TurnService turnService, GameRulesService gameRulesService, RoadScoreService roadScoreService, CityScoreService cityScoreService) {
        this.tileService = tileService;
        this.turnService = turnService;
        this.gameRulesService = gameRulesService;
        this.roadScoreService = roadScoreService;
        this.cityScoreService = cityScoreService;
    }

    public AvailableTileZonesDto checkAvailableTileZones(UUID tileId){
        Set<Integer> availableTileZones = new HashSet<>();
        Tile tile = tileService.retrieveTile(tileId).orElseThrow(() -> new TileNotFoundException("Tile not found for id " + tileId.toString()));
        if (!tile.isPlaced() || (tile.getX() == 0 && tile.getY() == 0)) {
            return new AvailableTileZonesDto(availableTileZones);
        }

        if (tile.getTileZones().contains(TileTypeEnum.CITY)){
            var availableCityZones = cityScoreService.getAvailableCityZones(tile);
            availableTileZones.addAll(availableCityZones);
        }
        if (tile.getTileZones().contains(TileTypeEnum.STREET)){
            availableTileZones.addAll(roadScoreService.getAvailableStreetZones(tile));
        }
        if (tile.getTileZones().contains(TileTypeEnum.MONASTERY)) availableTileZones.add(4);
        return new AvailableTileZonesDto(availableTileZones);
    }
}
