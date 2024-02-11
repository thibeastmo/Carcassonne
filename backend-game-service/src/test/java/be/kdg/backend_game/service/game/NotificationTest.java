package be.kdg.backend_game.service.game;

import be.kdg.backend_game.domain.GameTypeEnum;
import be.kdg.backend_game.domain.TimeEnum;
import be.kdg.backend_game.domain.game.Game;
import be.kdg.backend_game.repository.GameRepository;
import be.kdg.backend_game.service.dto.NotificationDto;
import be.kdg.backend_game.service.dto.lobby.NewLobbyDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class NotificationTest {
    @Autowired
    private GameService gameService;
    @Autowired
    private LobbyService lobbyService;
    @Autowired
    private TurnService turnService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private GameRepository gameRepository;

    private Game game;

    @BeforeEach
    public void setup() {
        gameRepository.deleteAll();
        NewLobbyDto dto = new NewLobbyDto();
        dto.setMaxPlayers(2);
        dto.setLobbyName("testLobby");
        dto.setGameTypeEnum(GameTypeEnum.SHORT);
        UUID lobbyId = lobbyService.createLobby(dto, UUID.fromString("60e2f67a-bf9f-4f2f-aca2-ba8d113bfa2b")).getLobbyId();
        lobbyService.joinLobby(lobbyId, UUID.fromString("bcf903ae-997d-466c-b4ac-410d1fe420be"));
        Optional<Game> optionalGame = gameService.createGame(lobbyId);
        optionalGame.ifPresent(value -> game = value);
    }
    @AfterEach
    public void cleanup() {
        gameService.deleteGame(game.getGameId());
    }

    @Test
    public void testGameWith2PlayersShouldReturnOneNotificationForOnePlayerAnd0ForOther() {
        // Assert
        var currentTurn = turnService.retrieveCurrentTurnByGame(game.getGameId()).orElseThrow();
        var players = playerService.retrievePlayersByGame(game.getGameId());
        int[] playerOrder = {players.get(0).getPlayerNumber(), players.get(1).getPlayerNumber()};
        if (currentTurn.getPlayer().getPlayerNumber() == players.get(1).getPlayerNumber()) {
            playerOrder = new int[]{players.get(1).getPlayerNumber(), players.get(0).getPlayerNumber()};
        }
        var player1 = playerService.retrievePlayerWithAccountByPlayerNumber(playerOrder[0]).orElseThrow();
        var player2 = playerService.retrievePlayerWithAccountByPlayerNumber(playerOrder[1]).orElseThrow();
        var result1 = gameService.getNotifications(player1.getAccount().getSubjectId());
        var result2 = gameService.getNotifications(player2.getAccount().getSubjectId());
        assertEquals(1, result1.getAmountOfNotifications());
        NotificationDto testDto = new NotificationDto("testLobby", game.getGameId(), GameTypeEnum.SHORT, 0, TimeEnum.MINUTE, List.of(players.get(0).getAccount().getNickName(), players.get(1).getAccount().getNickName()));
        assertEquals(testDto.getGameId(), result1.getNotificationDtos().get(0).getGameId());
        assertEquals(testDto.getGameType(), result1.getNotificationDtos().get(0).getGameType());
        assertEquals(testDto.getLobbyName(), result1.getNotificationDtos().get(0).getLobbyName());
        assertTrue(testDto.getNicknames().contains(result1.getNotificationDtos().get(0).getNicknames().get(0)));
        assertTrue(testDto.getNicknames().contains(result1.getNotificationDtos().get(0).getNicknames().get(1)));
        assertEquals(2, result1.getNotificationDtos().get(0).getNicknames().size());
        assertEquals(0, result2.getAmountOfNotifications());
        assertTrue(result2.getNotificationDtos().isEmpty());
    }
}
