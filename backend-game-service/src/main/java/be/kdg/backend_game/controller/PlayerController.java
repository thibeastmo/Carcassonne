package be.kdg.backend_game.controller;

import be.kdg.backend_game.domain.game.Player;
import be.kdg.backend_game.service.game.PlayerService;
import be.kdg.backend_game.service.game.dto.PlayerPointsDto;
import be.kdg.backend_game.service.game.dto.PlayersAvatarDto;
import be.kdg.backend_game.service.game.dto.PlayersDataDto;
import be.kdg.backend_game.service.game.dto.PlayersPointsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/player")
public class PlayerController {
    private static final Logger logger = Logger.getLogger(PlayerController.class.getPackageName());
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/score")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<PlayerPointsDto> getPlayerScore(String gameId, int playerNumber) {
        var uuid = UUID.fromString(gameId);
        Optional<Player> optionalPlayer = playerService.retrievePlayerByPlayerNumber(playerNumber);
        if (optionalPlayer.isEmpty()) return ResponseEntity.notFound().build();
        var player = optionalPlayer.get();
        return ResponseEntity.ok(
                new PlayerPointsDto(
                        player.getPlayerNumber(),
                        player.getPoints(),
                        player.getAccount().getNickName()
                )
        );
    }

    @GetMapping("/scores")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<PlayersPointsDto> getPlayersScores(String gameId) {
        var uuid = UUID.fromString(gameId);
        List<Player> players = playerService.retrievePlayersByGame(uuid);
        if (players.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(
                new PlayersPointsDto(players)
        );
    }

    @GetMapping("/avatars")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<PlayersAvatarDto> getPlayersAvatars(String gameId) {
        var uuid = UUID.fromString(gameId);
        List<Player> players = playerService.retrievePlayersByGame(uuid);
        if (players.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new PlayersAvatarDto(players)
        );
    }

    @GetMapping("/playerData")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<PlayersDataDto> getPlayerData(@RequestParam UUID gameId) {
        PlayersDataDto playersDataDto = playerService.retrieveplayersData(gameId);
        if (playersDataDto.getPlayersConstantData().isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(playersDataDto);
    }
}
