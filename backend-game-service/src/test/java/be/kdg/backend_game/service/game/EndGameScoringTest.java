package be.kdg.backend_game.service.game;

import be.kdg.backend_game.domain.GameTypeEnum;
import be.kdg.backend_game.domain.game.*;
import be.kdg.backend_game.repository.GameRepository;
import be.kdg.backend_game.repository.TurnRepository;
import be.kdg.backend_game.service.dto.lobby.NewLobbyDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EndGameScoringTest {
    @Autowired
    private LobbyService lobbyService;
    @Autowired
    private GameService gameService;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private TileService tileService;
    @Autowired
    private TurnService turnService;
    @Autowired
    private TurnRepository turnRepository;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private SerfService serfService;
    private Game game;

    @BeforeEach
    public void setup() {
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
    public void testUnfinishedCityWithSerfShouldReturnCorrectScore() throws Exception {
        // Arrange
        Tile tile8 = tileService.retrieveTileByNameAndGameId("TILE8", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Tile tile7 = tileService.retrieveTileByNameAndGameId("TILE7", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        int player1Number = createTurn(tile8, 0, -1, 0);
        int player2Number = createTurn(tile7, -1, -1, -1);

        game.setGameOver(true);
        gameRepository.save(game);
        turnService.createTurn(game.getGameId());
        var player1 = playerService.retrievePlayerByPlayerNumber(player1Number).get();
        var player2 = playerService.retrievePlayerByPlayerNumber(player2Number).get();
        // Assert
        assertEquals(3, player1.getPoints(), "Checking if player has correct amount of points");
        assertEquals(0, player2.getPoints(), "Checking if player has correct amount of points");
    }

    @Test
    public void testUnfinishedMonasteryWithSerfShouldReturnCorrectScore() throws Exception {
        // Arrange
        Tile tile41 = tileService.retrieveTileByNameAndGameId("TILE41", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Tile tile42 = tileService.retrieveTileByNameAndGameId("TILE42", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Tile tile35 = tileService.retrieveTileByNameAndGameId("TILE35", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        int player1Number = createTurn(tile41, 0, 1, 4);
        int player2Number = createTurn(tile42, 1, 1, -1);
        createTurn(tile35, -1, 1, -1);
        game.setGameOver(true);
        gameRepository.save(game);
        turnService.createTurn(game.getGameId());
        var player1 = playerService.retrievePlayerByPlayerNumber(player1Number).get();
        var player2 = playerService.retrievePlayerByPlayerNumber(player2Number).get();
        // Assert
        assertEquals(4, player1.getPoints(), "Checking if player has correct amount of points");
        assertEquals(0, player2.getPoints(), "Checking if player has correct amount of points");
    }

    @Test
    public void testUnfinishedRoadWithSerfShouldReturnCorrectScore() throws Exception {
        // Arrange
        Tile tile2 = tileService.retrieveTileByNameAndGameId("TILE2", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Tile tile3 = tileService.retrieveTileByNameAndGameId("TILE3", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));

        int player1Number = createTurn(tile2, 1, 0, 4);
        int player2Number = createTurn(tile3, -1, 0, -1);
        game.setGameOver(true);
        gameRepository.save(game);
        turnService.createTurn(game.getGameId());
        var player1 = playerService.retrievePlayerByPlayerNumber(player1Number).get();
        var player2 = playerService.retrievePlayerByPlayerNumber(player2Number).get();
        // Assert
        assertEquals(3, player1.getPoints(), "Checking if player has correct amount of points");
        assertEquals(0, player2.getPoints(), "Checking if player has correct amount of points");
    }

    @Test
    public void testUnfinishedRoadWith2SerfsShouldReturnCorrectScore() throws Exception {
        // Arrange
        Tile tile2 = tileService.retrieveTileByNameAndGameId("TILE2", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Tile tile3 = tileService.retrieveTileByNameAndGameId("TILE3", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        int player1Number = createTurn(tile2, 1, 0, 4);
        int player2Number = createTurn(tile3, -1, 0, 4);
        game.setGameOver(true);
        gameRepository.save(game);
        turnService.createTurn(game.getGameId());
        var player1 = playerService.retrievePlayerByPlayerNumber(player1Number).get();
        var player2 = playerService.retrievePlayerByPlayerNumber(player2Number).get();
        // Assert
        assertEquals(3, player1.getPoints(), "Checking if player has correct amount of points");
        assertEquals(3, player2.getPoints(), "Checking if player has correct amount of points");
    }
    @Test
    public void testUnfinishedRoadWithCrossroadsAnd3SerfsShouldReturnCorrectScore() throws Exception {
        // Arrange
        Tile tile46 = tileService.retrieveTileByNameAndGameId("TILE46", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Tile tile51 = tileService.retrieveTileByNameAndGameId("TILE51", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Tile tile52 = tileService.retrieveTileByNameAndGameId("TILE52", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        int player1Number = createTurn(tile46, -1, 0, 5);
        int player2Number = createTurn(tile51, -1, 1, 4);
        game.setGameOver(true);
        gameRepository.save(game);
        createTurn(tile52, -1, -1, 4);
        var player1 = playerService.retrievePlayerByPlayerNumber(player1Number).get();
        var player2 = playerService.retrievePlayerByPlayerNumber(player2Number).get();
        // Assert
        assertEquals(4, player1.getPoints(), "Checking if player has correct amount of points");
        assertEquals(2, player2.getPoints(), "Checking if player has correct amount of points");
    }
    @Test
    public void testUnfinishedCrossRoadWithSideNotExtendedAndSerfShouldReturnCorrectScore() throws Exception {
        // Arrange
        Tile tile46 = tileService.retrieveTileByNameAndGameId("TILE46", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Tile tile51 = tileService.retrieveTileByNameAndGameId("TILE51", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        int player1Number = createTurn(tile46, -1, 0, 7);
        int player2Number = createTurn(tile51, -1, 1, 4);
        game.setGameOver(true);
        gameRepository.save(game);
        turnService.createTurn(game.getGameId());
        var player1 = playerService.retrievePlayerByPlayerNumber(player1Number).get();
        var player2 = playerService.retrievePlayerByPlayerNumber(player2Number).get();
        // Assert
        assertEquals(1, player1.getPoints(), "Checking if player has correct amount of points");
        assertEquals(2, player2.getPoints(), "Checking if player has correct amount of points");
    }
    @Test
    public void testCrossRoadsAndCityShouldReturnCorrectScore() throws Exception {
        // Arrange
        Tile tile16 = tileService.retrieveTileByNameAndGameId("TILE16", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Tile tile19 = tileService.retrieveTileByNameAndGameId("TILE19", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        int player1Number = createTurn(tile16, -1, 0, 3);
        int player2Number = createTurn(tile19, -2, 0, -1);
        game.setGameOver(true);
        gameRepository.save(game);
        turnService.createTurn(game.getGameId());
        var player1 = playerService.retrievePlayerByPlayerNumber(player1Number).get();
        var player2 = playerService.retrievePlayerByPlayerNumber(player2Number).get();
        // Assert
        assertEquals(2, player1.getPoints(), "Checking if player has correct amount of points");
        assertEquals(0, player2.getPoints(), "Checking if player has correct amount of points");
    }

    @Test
    public void testSerfOnSplitCityAndCityWithCoatOfArmsShouldReturnCorrectScore() throws Exception {
        // Arrange
        Tile tile66 = tileService.retrieveTileByNameAndGameId("TILE66", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Tile tile42 = tileService.retrieveTileByNameAndGameId("TILE42", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        int player1Number = createTurn(tile66, -1, 0, 6);
        int player2Number = createTurn(tile42, -2, 0, 1);
        game.setGameOver(true);
        gameRepository.save(game);
        turnService.createTurn(game.getGameId());
        var player1 = playerService.retrievePlayerByPlayerNumber(player1Number).get();
        var player2 = playerService.retrievePlayerByPlayerNumber(player2Number).get();
        // Assert
        assertEquals(3, player1.getPoints(), "Checking if player has correct amount of points");
        assertEquals(1, player2.getPoints(), "Checking if player has correct amount of points");
    }

    private int createTurn(Tile tile, int x, int y, int tileZoneId) throws Exception {
        var currentTurnOptional = turnService.retrieveCurrentTurnByGame(game.getGameId());
        var currentTurn = currentTurnOptional.get();
        currentTurn.setTile(tile);
        tile = tileService.placeTile(tile.getTileId(), x, y, OrientationEnum.ROTATION_0);
        Player player = turnService.retrieveCurrentTurnByGame(game.getGameId()).get().getPlayer();
        if (tileZoneId != -1) serfService.setTileForSerf(tileZoneId, tile, player);
        turnRepository.save(currentTurn);
        turnService.createTurn(game.getGameId());
        return player.getPlayerNumber();
    }
}
