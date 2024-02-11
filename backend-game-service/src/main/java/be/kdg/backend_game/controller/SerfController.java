package be.kdg.backend_game.controller;

import be.kdg.backend_game.domain.game.Serf;
import be.kdg.backend_game.service.dto.AvailableTileZonesDto;
import be.kdg.backend_game.service.exception.TileNotFoundException;
import be.kdg.backend_game.service.game.PlayerService;
import be.kdg.backend_game.service.game.SerfPlacementService;
import be.kdg.backend_game.service.game.SerfService;
import be.kdg.backend_game.service.game.TurnService;
import be.kdg.backend_game.service.game.dto.PlayersSerfsDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/serf")
public class SerfController {
    private static final Logger logger = Logger.getLogger(SerfController.class.getPackageName());
    private SerfService serfService;
    private TurnService turnService;
    private final SerfPlacementService serfPlacementService;

    public SerfController(SerfService serfService, TurnService turnService, SerfPlacementService serfPlacementService) {
        this.serfService = serfService;
        this.turnService = turnService;
        this.serfPlacementService = serfPlacementService;
    }

    @GetMapping("/used")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<PlayersSerfsDto> getPlayersSerfs(String gameId) {
        var uuid = UUID.fromString(gameId);
        List<Serf> usedSerfs = serfService.getUsedSerfsByGameId(uuid);
        var dto = new PlayersSerfsDto(usedSerfs, uuid);
        return ResponseEntity.ok(dto);
    }
    @GetMapping("/get-legal-tilezones")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> getLegalTilezones(@RequestParam UUID gameId) {
        try {
            UUID tileId = turnService.retrieveCurrentTileIdByGame(gameId);
            AvailableTileZonesDto dto = serfPlacementService.checkAvailableTileZones(tileId);
            return ResponseEntity.ok(dto);
        } catch (TileNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PatchMapping("/place")
    @PreAuthorize("hasAuthority('player')")
    public HttpStatusCode setSerfOnTileByPlayer(@RequestParam UUID gameId, int tileZoneId) {
        var optionalTurn = turnService.retrieveCurrentTurnByGame(gameId);
        if (optionalTurn.isEmpty()) return ResponseEntity.notFound().build().getStatusCode();
        var turn = optionalTurn.get();
        var tile = turn.getTile();
        if (!tile.isPlaced() || !serfPlacementService.checkAvailableTileZones(tile.getTileId()).getAvailableZones().contains(tileZoneId)) return ResponseEntity.badRequest().build().getStatusCode();
        var serfsOnTile = serfService.retrieveSerfsByTileId(tile.getTileId());
        if (serfsOnTile == null || !serfsOnTile.isEmpty()) return ResponseEntity.badRequest().build().getStatusCode();
        try {
            if (serfService.setTileForSerf(tileZoneId, tile, turn) != null) {
                return ResponseEntity.ok().build().getStatusCode();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage()).getStatusCode();
        }
        return ResponseEntity.internalServerError().build().getStatusCode();
    }
}
