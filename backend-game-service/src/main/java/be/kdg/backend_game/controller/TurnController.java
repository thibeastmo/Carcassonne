package be.kdg.backend_game.controller;

import be.kdg.backend_game.service.AccountService;
import be.kdg.backend_game.service.game.GameRulesService;
import be.kdg.backend_game.service.game.TileService;
import be.kdg.backend_game.service.game.TurnService;
import be.kdg.backend_game.service.game.dto.TilePlacementDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/turn")
public class TurnController {
    private static final Logger logger = Logger.getLogger(TurnController.class.getPackageName());
    private final GameRulesService gameRulesService;
    private final TileService tileService;
    private final TurnService turnService;
    private final AccountService accountService;

    public TurnController(GameRulesService gameRulesService, TileService tileService, TurnService turnService, AccountService accountService) {
        this.gameRulesService = gameRulesService;
        this.tileService = tileService;
        this.turnService = turnService;
        this.accountService = accountService;
    }


    @GetMapping("/getLegalPlacements")
    public ResponseEntity<List<TilePlacementDto>> getLegalPlacements(@AuthenticationPrincipal Jwt token, @RequestParam UUID gameId) {
        var tileplacements = gameRulesService.retrieveLegalTilePlacementsDto(gameId, token);
        if (tileplacements.isEmpty()) {
            return new ResponseEntity<>(NO_CONTENT);
        }
        return new ResponseEntity<>(tileplacements.get(), OK);
    }
}
