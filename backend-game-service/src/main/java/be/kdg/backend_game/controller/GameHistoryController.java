package be.kdg.backend_game.controller;

import be.kdg.backend_game.service.JwtExtractorHelper;
import be.kdg.backend_game.service.game.GameHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/gamehistory")
public class GameHistoryController {
    private static final Logger logger = Logger.getLogger(GameHistoryController.class.getPackageName());
    private final GameHistoryService gameHistoryService;

    public GameHistoryController(GameHistoryService gameHistoryService) {
        this.gameHistoryService = gameHistoryService;
    }


    @GetMapping("/all")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> getAllGameHistories(@AuthenticationPrincipal Jwt token) {
        try {
            var subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
            return ResponseEntity.ok(gameHistoryService.retrieveGameHistoriesBySubjectId(subjectId));
        }
        catch (Exception ex){
            logger.log(Level.SEVERE, "Could not find game histories by subject id");
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> getAllGameHistoriesByGameId(@AuthenticationPrincipal Jwt token, String gameId) {
        try {
            var subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
            var dtos = gameHistoryService.retrieveGameHistoryByGameId(subjectId, UUID.fromString(gameId));
            if (dtos.isEmpty()) return ResponseEntity.noContent().build();
            return ResponseEntity.ok(dtos);
        }
        catch (Exception ex){
            logger.log(Level.SEVERE, "Could not find game histories by game id: " + gameId);
            return ResponseEntity.internalServerError().build();
        }
    }
}
