package be.kdg.backend_game.controller;

import be.kdg.backend_game.domain.game.OrientationEnum;
import be.kdg.backend_game.domain.game.Tile;
import be.kdg.backend_game.service.game.*;
import be.kdg.backend_game.service.game.dto.PlacedTilesDto;
import be.kdg.backend_game.service.game.dto.PlacementDto;
import be.kdg.backend_game.service.game.dto.TileDto;
import be.kdg.backend_game.service.game.tile_calculations.ScoreService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/tile")
public class TileController {
    private static final Logger logger = Logger.getLogger(TileController.class.getPackageName());
    private final TileService tileService;
    private final TurnService turnService;
    private final GameRulesService gameRulesService;
    private final ScoreService scoreService;
    private final SerfService serfService;

    public TileController(TileService tileService, TurnService turnService, GameRulesService gameRulesService, ScoreService scoreService, SerfService serfService) {
        this.tileService = tileService;
        this.turnService = turnService;
        this.gameRulesService = gameRulesService;
        this.scoreService = scoreService;
        this.serfService = serfService;
    }

    @GetMapping("/currentTile")
    public ResponseEntity<TileDto> getCurrentTile(@RequestParam UUID gameId) {
        TileDto tileDto = turnService.retrieveCurrentTileDtoByGameId(gameId);
        if (tileDto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(tileDto);
    }

    @GetMapping("/placedTiles")
    public ResponseEntity<PlacedTilesDto> getPlacedTiles(@RequestParam UUID gameId) {
        PlacedTilesDto placedTilesDto = tileService.retrievePlacedTilesWithTileCount(gameId);
        if (placedTilesDto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(placedTilesDto);
    }

    @PatchMapping("/placeTile")
    public ResponseEntity<?> placeTile(@RequestParam UUID gameId, @RequestBody @Valid PlacementDto placementDto) {
        try {
            Tile tile = turnService.retrieveCurrentTileByGame(gameId);
            if (tile == null) return ResponseEntity.notFound().build();
            if (!gameRulesService.checkIfValidTilePlacement(tile, placementDto.getxValue(), placementDto.getyValue(), OrientationEnum.values()[placementDto.getOrientation()]))
                return ResponseEntity.badRequest().build();

            tileService.placeTile(tile.getTileId(), placementDto.getxValue(), placementDto.getyValue(), OrientationEnum.values()[placementDto.getOrientation()]);
            //TODO: check edge case when player places a serf on a monastery and finishes it
            scoreService.resolveMonasteryCompletion(gameId);
            if (!serfService.hasCurrentTurnPlayerAtLeastOneSerf(gameId)) {
                if (turnService.createTurn(gameId).isEmpty()) {
                    logger.log(Level.WARNING, "Could not create a new turn for game when no serfs available and tile placed: " + gameId);
                    return ResponseEntity.internalServerError().build();
                }
                else {
                    logger.log(Level.INFO, "New turn created when no serfs available and tile placed: "+ gameId);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Something went wrong in TileController > placeTile: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }


}
