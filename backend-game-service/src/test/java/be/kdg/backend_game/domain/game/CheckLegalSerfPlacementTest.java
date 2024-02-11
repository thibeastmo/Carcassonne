package be.kdg.backend_game.domain.game;

import be.kdg.backend_game.domain.GameTypeEnum;
import be.kdg.backend_game.service.dto.AvailableTileZonesDto;
import be.kdg.backend_game.service.dto.lobby.NewLobbyDto;
import be.kdg.backend_game.service.exception.TileNotFoundException;
import be.kdg.backend_game.service.game.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CheckLegalSerfPlacementTest {
    @Autowired
    private SerfPlacementService serfPlacementService;
    @Autowired
    private LobbyService lobbyService;
    @Autowired
    private GameService gameService;
    @Autowired
    private TileService tileService;
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
    public void testSimpleTileScenarioShouldReturnCorrectTilezones() {
        // Arrange
        Tile tileToCheck = tileService.retrieveTileByNameAndGameId("TILE11", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));

        // Act
        tileService.placeTile(tileToCheck.getTileId(), 0, -1, OrientationEnum.ROTATION_90);

        // Assert
        var availableTileZonesDto = serfPlacementService.checkAvailableTileZones(tileToCheck.getTileId());
        assertFalse(availableTileZonesDto.getAvailableZones().isEmpty(), "Checking if result has value");
        Set<Integer> testSet = new HashSet<>();
        testSet.add(0);
        testSet.add(3);
        testSet.add(6);
        AvailableTileZonesDto testDto = new AvailableTileZonesDto(testSet);
        assertEquals(testDto.getAvailableZones(), availableTileZonesDto.getAvailableZones(), "Checking if tilezones are correct");
    }

    @Test
    public void testRightExceptionsShouldBeThrown() {
        Tile tileToCheck = tileService.retrieveTileByNameAndGameId("TILE11", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        assertThrowsExactly(TileNotFoundException.class, () -> serfPlacementService.checkAvailableTileZones(UUID.randomUUID()));
        var availableTileZonesDto = serfPlacementService.checkAvailableTileZones(tileToCheck.getTileId());
        assertTrue(availableTileZonesDto.getAvailableZones().isEmpty(), "Checking if non placed tile returns an empty list");
    }

    @Test
    public void testScenarioCityWithSerfPresentShouldNotReturnAvailableTileZones() throws Exception {
        // Arrange
        Tile tileToCheck = tileService.retrieveTileByNameAndGameId("TILE11", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Tile tile8 = tileService.retrieveTileByNameAndGameId("TILE8", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Player player = playerService.retrievePlayersByGame(game.getGameId())
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Player not found"));
        // Act
        tile8 = tileService.placeTile(tile8.getTileId(), 0, -1, OrientationEnum.ROTATION_0);
        tileToCheck = tileService.placeTile(tileToCheck.getTileId(), -1, -1, OrientationEnum.ROTATION_180);

        serfService.setTileForSerf(0, tile8, player);

        // Assert
        var availableTileZonesDto = serfPlacementService.checkAvailableTileZones(tileToCheck.getTileId());
        assertTrue(availableTileZonesDto.getAvailableZones().isEmpty(), "Checking if result is empty");
    }

    @Test
    public void testScenarioTileWithMonasteryOnlyShouldReturnMiddleOfTile() {
        // Arrange
        Tile tileToCheck = tileService.retrieveTileByNameAndGameId("TILE41", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        // Act
        tileService.placeTile(tileToCheck.getTileId(), 0, 1, OrientationEnum.ROTATION_0);

        // Assert
        var availableTileZonesDto = serfPlacementService.checkAvailableTileZones(tileToCheck.getTileId());
        assertFalse(availableTileZonesDto.getAvailableZones().isEmpty(), "Checking if result has value");
        Set<Integer> testSet = new HashSet<>();
        testSet.add(4);
        AvailableTileZonesDto testDto = new AvailableTileZonesDto(testSet);
        assertEquals(testDto.getAvailableZones(), availableTileZonesDto.getAvailableZones(), "Checking if tilezone is correct");
    }

    @Test
    public void testScenarioWithTwoCitiesWhereOneHasSerfShouldOnlyReturnCityZonesWithoutSerfs() throws Exception {
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

        // Act

        tileToCheck = tileService.placeTile(tileToCheck.getTileId(), 1, -1, OrientationEnum.ROTATION_90);
        tileService.placeTile(tile20.getTileId(), 0, -1, OrientationEnum.ROTATION_90);
        tileService.placeTile(tile6.getTileId(), 1, 0, OrientationEnum.ROTATION_270);
        tileService.placeTile(tile5.getTileId(), 2, 0, OrientationEnum.ROTATION_0);
        tile68 = tileService.placeTile(tile68.getTileId(), 2, -1, OrientationEnum.ROTATION_0);

        Player player = playerService.retrievePlayersByGame(game.getGameId()).get(0);

        serfService.setTileForSerf(4, tile68, player);


        // Assert
        var availableTileZonesDto = serfPlacementService.checkAvailableTileZones(tileToCheck.getTileId());
        assertFalse(availableTileZonesDto.getAvailableZones().isEmpty(), "Checking if result has value");
        Set<Integer> testSet = new HashSet<>();
        testSet.add(6);
        testSet.add(7);
        testSet.add(8);
        AvailableTileZonesDto testDto = new AvailableTileZonesDto(testSet);
        assertEquals(testDto.getAvailableZones(), availableTileZonesDto.getAvailableZones(), "Checking if tilezones are correct");

    }

    @Test
    public void testScenarioTileWithRoadWithoutSerfsShouldReturnRoadIndexes() {
        // Arrange
        Tile tileToCheck = tileService.retrieveTileByNameAndGameId("TILE2", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        // Act
        tileService.placeTile(tileToCheck.getTileId(), 1, 0, OrientationEnum.ROTATION_0);

        // Assert
        var availableTileZonesDto = serfPlacementService.checkAvailableTileZones(tileToCheck.getTileId());
        assertFalse(availableTileZonesDto.getAvailableZones().isEmpty(), "Checking if result has value");
        Set<Integer> testSet = new HashSet<>();
        testSet.add(4);
        testSet.add(3);
        testSet.add(7);
        AvailableTileZonesDto testDto = new AvailableTileZonesDto(testSet);
        assertEquals(testDto.getAvailableZones(), availableTileZonesDto.getAvailableZones(), "Checking if tilezones are correct");
    }

    @Test
    public void testScenarioTileWithRoadWithSerfShouldReturnNoIndexes() throws Exception {
        // Arrange
        Tile tile2 = tileService.retrieveTileByNameAndGameId("TILE2", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Tile tileToCheck = tileService.retrieveTileByNameAndGameId("TILE3", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Player player = playerService.retrievePlayersByGame(game.getGameId()).get(0);
        // Act
        tile2 = tileService.placeTile(tile2.getTileId(), 1, 0, OrientationEnum.ROTATION_0);
        tileService.placeTile(tileToCheck.getTileId(), 1, -1, OrientationEnum.ROTATION_270);

        serfService.setTileForSerf(4, tile2, player);

        // Assert
        var availableTileZonesDto = serfPlacementService.checkAvailableTileZones(tileToCheck.getTileId());
        assertTrue(availableTileZonesDto.getAvailableZones().isEmpty(), "Checking if result has no value");
    }

    @Test
    public void testScenarioTileWithCrossroadsAndNoSerfsOnRoadsShouldReturnAllRoadZones() {
        // Arrange
        Tile tileToCheck = tileService.retrieveTileByNameAndGameId("TILE46", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        // Act
        tileService.placeTile(tileToCheck.getTileId(), 1, 0, OrientationEnum.ROTATION_180);

        // Assert
        var availableTileZonesDto = serfPlacementService.checkAvailableTileZones(tileToCheck.getTileId());
        assertFalse(availableTileZonesDto.getAvailableZones().isEmpty(), "Checking if result has value");
        Set<Integer> testSet = new HashSet<>();
        testSet.add(1);
        testSet.add(5);
        testSet.add(7);
        AvailableTileZonesDto testDto = new AvailableTileZonesDto(testSet);
        assertEquals(testDto.getAvailableZones(), availableTileZonesDto.getAvailableZones(), "Checking if tilezones are correct");
    }

    @Test
    public void testScenarioTileWithCrossroadsAndASerfOnRoadsShouldOnlyReturnValidZones() throws Exception {
        // Arrange
        Tile tile2 = tileService.retrieveTileByNameAndGameId("TILE2", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Tile tileToCheck = tileService.retrieveTileByNameAndGameId("TILE46", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Player player = playerService.retrievePlayersByGame(game.getGameId()).get(0);
        // Act
        tile2 = tileService.placeTile(tile2.getTileId(), 1, 0, OrientationEnum.ROTATION_0);
        tileService.placeTile(tileToCheck.getTileId(), 1, -1, OrientationEnum.ROTATION_0);

        serfService.setTileForSerf(4, tile2, player);

        // Assert
        var availableTileZonesDto = serfPlacementService.checkAvailableTileZones(tileToCheck.getTileId());
        assertFalse(availableTileZonesDto.getAvailableZones().isEmpty(), "Checking if result has value");
        Set<Integer> testSet = new HashSet<>();
        testSet.add(5);
        testSet.add(7);
        AvailableTileZonesDto testDto = new AvailableTileZonesDto(testSet);
        assertEquals(testDto.getAvailableZones(), availableTileZonesDto.getAvailableZones(), "Checking if tilezones are correct");
    }
    @Test
    public void testScenarioTileWith2CityPartsCompletesCityWithSerfShouldReturnOnlyCityPartThatHasNoSerf() throws Exception {
        // Arrange
        Tile tile36 = tileService.retrieveTileByNameAndGameId("TILE36", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Tile tile44 = tileService.retrieveTileByNameAndGameId("TILE44", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Tile tileToCheck = tileService.retrieveTileByNameAndGameId("TILE61", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Player player = playerService.retrievePlayersByGame(game.getGameId()).get(0);
        // Act
        tile36 = tileService.placeTile(tile36.getTileId(), 1, 0, OrientationEnum.ROTATION_270);
        tile44 = tileService.placeTile(tile44.getTileId(), 2, 0, OrientationEnum.ROTATION_0);
        tileToCheck = tileService.placeTile(tileToCheck.getTileId(), 2, -1, OrientationEnum.ROTATION_90);

        serfService.setTileForSerf(3, tile44, player);

        // Assert
        var availableTileZonesDto = serfPlacementService.checkAvailableTileZones(tileToCheck.getTileId());
        assertFalse(availableTileZonesDto.getAvailableZones().isEmpty(), "Checking if result has value");
        Set<Integer> testSet = new HashSet<>();
        testSet.add(7);
        testSet.add(8);
        AvailableTileZonesDto testDto = new AvailableTileZonesDto(testSet);
        assertEquals(testDto.getAvailableZones(), availableTileZonesDto.getAvailableZones(), "Checking if tilezones are correct");
    }


}
