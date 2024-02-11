package be.kdg.backend_game.domain.gamerules;

import be.kdg.backend_game.domain.GameTypeEnum;
import be.kdg.backend_game.domain.game.*;
import be.kdg.backend_game.service.dto.lobby.NewLobbyDto;
import be.kdg.backend_game.service.game.*;
import be.kdg.backend_game.service.game.tile_calculations.CityScoreService;
import be.kdg.backend_game.service.game.tile_calculations.ScoreService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import be.kdg.backend_game.service.game.PlayerService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ScoreForCityTest {
    @Autowired
    private GameService gameService;
    @Autowired
    private LobbyService lobbyService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private TileService tileService;
    @Autowired
    private GameRulesService gameRulesService;
    @Autowired
    private CityScoreService cityScoreService;
    @Autowired
    private ScoreService scoreService;
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
    public void testSimpleTileScenarioShouldReturnCorrectScore() {
        // Arrange
        Tile tileToCheck = tileService.retrieveTileByNameAndGameId("TILE5", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));

        // Act
        var placedTile = tileService.placeTile(tileToCheck.getTileId(), 0, -1, OrientationEnum.ROTATION_180);

        Player player = playerService.retrievePlayersByGame(game.getGameId())
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Player not found"));

        try {
            serfService.setTileForSerf(7, placedTile, player);
        } catch (Exception e) {
            fail("Something went wrong when setting a serf: " + e.getMessage());
        }

        // Assert
        var result = scoreService.checkTile(tileToCheck.getTileId());
        assertTrue(result.isPresent(), "Checking if result has value");

        assertEquals(4, result.get().get(player.getPlayerNumber()), "Checking player score");
    }

    @Test
    public void testSimpleTileScenarioWithWeaponShouldReturnCorrectScore() {
        // Arrange
        Tile tile40 = tileService.retrieveTileByNameAndGameId("TILE40", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile40 not found"));

        Tile tileToCheck = tileService.retrieveTileByNameAndGameId("TILE72", game.getGameId())
                .orElseThrow(() -> new RuntimeException("TileToCheck not found"));

        // Act
        var tilePlaced40  = tileService.placeTile(tile40.getTileId(), 0, -1, OrientationEnum.ROTATION_0);
        var otherTile = tileService.placeTile(tileToCheck.getTileId(), 0, -2, OrientationEnum.ROTATION_90);

        Player player = playerService.retrievePlayersByGame(game.getGameId())
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Player not found"));

        try {
            serfService.setTileForSerf(4, tilePlaced40, player);
        } catch (Exception e) {
            fail("Something went wrong when setting a serf: " + e.getMessage());
        }

        // Assert
        var result = scoreService.checkTile(tileToCheck.getTileId());
        assertTrue(result.isPresent(), "Checking if result has value");

        assertEquals(8, result.get().get(player.getPlayerNumber()), "Checking player score");
    }

    @Test
    public void testScencarioWithUnfinishedCityShouldReturnEmpty() {
        // Arrange
        Tile tileToCheck = tileService.retrieveTileByNameAndGameId("TILE40", game.getGameId())
                .orElseThrow(() -> new RuntimeException("TileToCheck not found"));

        // Act
        tileService.placeTile(tileToCheck.getTileId(), 0, -1, OrientationEnum.ROTATION_0);

        Player player = playerService.retrievePlayersByGame(game.getGameId())
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Player not found"));

        try {
            serfService.setTileForSerf(4, tileToCheck, player);
        } catch (Exception e) {
            fail("Something went wrong when setting a serf: " + e.getMessage());
        }

        // Assert
        var result = scoreService.checkTile(tileToCheck.getTileId());
        assertTrue(result.isPresent(), "Checking if result has value");
        assertTrue(result.get().keySet().isEmpty(), "Checking if result has no scores");

    }

    @Test
    public void testScenarioWithTileFinishingOneCityAndAddingOnAnotherShouldOnlyReturnScoreForSerfOnFinishedCity() {
        // Arrange
        Tile tileToCheck = tileService.retrieveTileByNameAndGameId("TILE12", game.getGameId())
                .orElseThrow(() -> new RuntimeException("TileToCheck not found"));

        Tile tile20 = tileService.retrieveTileByNameAndGameId("TILE20", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile20 not found"));

        Tile tile6 = tileService.retrieveTileByNameAndGameId("TILE6", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile6 not found"));

        Tile tile5 = tileService.retrieveTileByNameAndGameId("TILE5", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile5 not found"));

        Tile tile68 = tileService.retrieveTileByNameAndGameId("TILE68", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile68 not found"));

        tileService.placeTile(tileToCheck.getTileId(), 1, -1, OrientationEnum.ROTATION_90);
        var tile20Placed = tileService.placeTile(tile20.getTileId(), 0, -1, OrientationEnum.ROTATION_90);
        tileService.placeTile(tile6.getTileId(), 1, 0, OrientationEnum.ROTATION_270);
        tileService.placeTile(tile5.getTileId(), 2, 0, OrientationEnum.ROTATION_0);
        var tile68Placed = tileService.placeTile(tile68.getTileId(), 2, -1, OrientationEnum.ROTATION_0);

        Player player1 = playerService.retrievePlayersByGame(game.getGameId()).get(0);
        Player player2 = playerService.retrievePlayersByGame(game.getGameId()).get(1);

        try {
            serfService.setTileForSerf(4, tile68Placed, player1);
            serfService.setTileForSerf(0, tile20Placed, player2);
        } catch (Exception e) {
            fail("Something went wrong when setting a serf: " + e.getMessage());
        }

        // Act
        var result = scoreService.checkTile(tileToCheck.getTileId());

        // Assert
        assertTrue(result.isPresent(), "Checking if result has value");
        assertEquals(1, result.get().size(), "Checking if result has correct amount of player scores");
        assertEquals(6, result.get().get(player2.getPlayerNumber()), "Checking if player 2 received correct score");
    }

    @Test
    public void testScenarioWithTileFinishingOneCityWithTileWithTwoCityAreasShouldOnlyReturnOneCityResult() {
        // Arrange
        Tile tileToCheck = tileService.retrieveTileByNameAndGameId("TILE12", game.getGameId())
                .orElseThrow(() -> new RuntimeException("TileToCheck not found"));

        Tile tile10 = tileService.retrieveTileByNameAndGameId("TILE10", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile10 not found"));

        Tile tile8 = tileService.retrieveTileByNameAndGameId("TILE8", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile8 not found"));

        Tile tile67 = tileService.retrieveTileByNameAndGameId("TILE67", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile67 not found"));

        Tile tile35 = tileService.retrieveTileByNameAndGameId("TILE35", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile35 not found"));

        Tile tile34 = tileService.retrieveTileByNameAndGameId("TILE34", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile34 not found"));

        tileService.placeTile(tileToCheck.getTileId(), 1, -2, OrientationEnum.ROTATION_90);
        var tile10Placed = tileService.placeTile(tile10.getTileId(), 0, -1, OrientationEnum.ROTATION_0);
        tileService.placeTile(tile8.getTileId(), 0, -2, OrientationEnum.ROTATION_90);
        tileService.placeTile(tile67.getTileId(), 1, -1, OrientationEnum.ROTATION_0);
        tileService.placeTile(tile35.getTileId(), 2, -1, OrientationEnum.ROTATION_270);
        tileService.placeTile(tile34.getTileId(), 2, -2, OrientationEnum.ROTATION_180);

        Player player = playerService.retrievePlayersByGame(game.getGameId())
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Player not found"));
        try {
            serfService.setTileForSerf(4, tile10Placed, player);
        } catch (Exception e) {
            fail("Something went wrong when setting a serf: " + e.getMessage());
        }

        // Act
        var result = scoreService.checkTile(tileToCheck.getTileId());

        // Assert
        assertTrue(result.isPresent(), "Checking if result has value");
        assertEquals(1, result.get().size(), "Checking if result has correct amount of player scores");
        assertEquals(14, result.get().get(player.getPlayerNumber()), "Checking if player received correct score");

    }

    @Test
    public void testContestedCityWithTiedSerfsShouldReturnCorrectScoresForPlayers() {
        // Arrange
        Tile tileToCheck = tileService.retrieveTileByNameAndGameId("TILE8", game.getGameId())
                .orElseThrow(() -> new RuntimeException("TileToCheck not found"));

        Tile tile12 = tileService.retrieveTileByNameAndGameId("TILE12", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile12 not found"));

        var tileToCheckPlaced = tileService.placeTile(tileToCheck.getTileId(), 0, -1, OrientationEnum.ROTATION_90);
        var placedTile = tileService.placeTile(tile12.getTileId(), 1, -1, OrientationEnum.ROTATION_90);

        Player player1 = playerService.retrievePlayersByGame(game.getGameId())
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Player 1 not found"));

        Player player2 = playerService.retrievePlayersByGame(game.getGameId())
                .stream()
                .skip(1)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Player 2 not found"));
        try{
        serfService.setTileForSerf(0, tileToCheckPlaced, player1);
        serfService.setTileForSerf(7, placedTile, player2);
        } catch (Exception e) {
            fail("Something went wrong when setting a serf: " + e.getMessage());
        }

        // Act
        var result = scoreService.checkTile(tileToCheck.getTileId());

        // Assert
        assertTrue(result.isPresent(), "Checking if result has value");
        assertEquals(2, result.get().size(), "Checking if result has correct amount of player scores");
        assertEquals(6, result.get().get(player1.getPlayerNumber()), "Checking if player 1 received correct score");
        assertEquals(6, result.get().get(player2.getPlayerNumber()), "Checking if player 2 received correct score");
    }
}
