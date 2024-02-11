package be.kdg.backend_game.controller;

import be.kdg.backend_game.domain.game.Turn;
import be.kdg.backend_game.service.game.dto.GameMaxTurnTimeDto;
import be.kdg.backend_game.service.game.dto.TileDto;
import be.kdg.backend_game.service.game.GameService;
import be.kdg.backend_game.service.game.LobbyService;
import be.kdg.backend_game.service.game.TileService;
import be.kdg.backend_game.service.game.TurnService;
import be.kdg.backend_game.service.game.dto.CurrentTurnDto;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/game")
public class GameController {
    private static final Logger logger = Logger.getLogger(GameController.class.getPackageName());
    private final ModelMapper modelMapper;
    private final GameService gameService;
    private final LobbyService lobbyService;
    private final TurnService turnService;
    private final TileService tileService;

    public GameController(ModelMapper modelMapper, GameService gameService, LobbyService lobbyService, TurnService turnService, TileService tileService) {
        this.modelMapper = modelMapper;
        this.gameService = gameService;
        this.lobbyService = lobbyService;
        this.turnService = turnService;
        this.tileService = tileService;
    }

    /**
     * Creates a game based on the info of the lobby.
     *
     * @return gameId
     */
    @PostMapping("/start")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> startGame(@RequestParam("lobbyId") String lobbyId) {
        UUID uuid = UUID.fromString(lobbyId);
        var lobby = lobbyService.retrieveLobby(uuid);
        if (lobby.isEmpty()) return ResponseEntity.notFound().build();
        try {
            var gameOptional = gameService.createGame(uuid);
            if (gameOptional.isPresent()) {
                return ResponseEntity.ok(gameOptional.get().getGameId());
            }
        } catch (DataIntegrityViolationException e) {
            logger.log(Level.INFO, e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

    @PostMapping("/nextTurn")
    @PreAuthorize("hasAuthority('player')")
    public HttpStatusCode setNextTurn(String gameId) {
        var uuid = UUID.fromString(gameId);

        logger.log(Level.INFO, "Creating new turn for game: " + gameId);
        if (turnService.createTurn(uuid).isEmpty()) return ResponseEntity.ok().build().getStatusCode();
        return ResponseEntity.badRequest().build().getStatusCode();
    }

    @GetMapping("/currentTurn")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<CurrentTurnDto> getCurrentTurn(String gameId) {
        var uuid = UUID.fromString(gameId);
        Optional<Turn> optionalTurn = turnService.retrieveCurrentTurnByGame(uuid);
        if (optionalTurn.isEmpty()) return ResponseEntity.notFound().build();
        var turn = optionalTurn.get();
        return ResponseEntity.ok(
                new CurrentTurnDto(
                        turn.getBeginTurn(),
                        turn.getPlayer().getPlayerNumber()
                )
        );
    }

    @GetMapping("/maxTurnDuration")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<GameMaxTurnTimeDto> getMaxTurnDuration(String gameId) {
        var uuid = UUID.fromString(gameId);
        try {
            int maxTurnDuration = gameService.retrieveMaxTurnDuration(uuid);
            return ResponseEntity.ok(
                    new GameMaxTurnTimeDto(maxTurnDuration)
            );
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
