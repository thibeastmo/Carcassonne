package be.kdg.backend_game.service.game;

import be.kdg.backend_game.domain.GameTypeEnum;
import be.kdg.backend_game.domain.game.Game;
import be.kdg.backend_game.domain.game.OrientationEnum;
import be.kdg.backend_game.domain.game.Player;
import be.kdg.backend_game.domain.game.Tile;
import be.kdg.backend_game.service.AccountService;
import be.kdg.backend_game.service.dto.lobby.NewLobbyDto;
import be.kdg.backend_game.service.game.tile_calculations.ScoreService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.fail;

@SpringBootTest
public class ScoreForMonasteryTest {
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
    private SerfService serfService;
    @Autowired
    private AccountService acccountService;
    @Autowired
    private ScoreService scoreService;

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
    public void testSingleMonasteryWithoutSerfShouldReturn0() {
        //arrange

        Tile MonasteryTile = tileService.retrieveTileByNameAndGameId("TILE41", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Tile upperTile1 = tileService.retrieveTileByNameAndGameId("TILE11", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Tile upperTile2 = tileService.retrieveTileByNameAndGameId("TILE10", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Tile upperTile3 = tileService.retrieveTileByNameAndGameId("TILE5", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Tile upperTile4 = tileService.retrieveTileByNameAndGameId("TILE6", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Tile upperTile5 = tileService.retrieveTileByNameAndGameId("TILE7", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Tile upperTile6 = tileService.retrieveTileByNameAndGameId("TILE8", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Tile upperTile7 = tileService.retrieveTileByNameAndGameId("TILE9", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Tile upperTile8 = tileService.retrieveTileByNameAndGameId("TILE4", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));


        //act
        tileService.placeTile(MonasteryTile.getTileId(), 0, 1, OrientationEnum.ROTATION_0);
        tileService.placeTile(upperTile1.getTileId(), 0, 2, OrientationEnum.ROTATION_0);
        tileService.placeTile(upperTile3.getTileId(), -1, 2, OrientationEnum.ROTATION_0);
        tileService.placeTile(upperTile4.getTileId(), 1, 2, OrientationEnum.ROTATION_0);
        tileService.placeTile(upperTile5.getTileId(), 1, 1, OrientationEnum.ROTATION_0);
        tileService.placeTile(upperTile6.getTileId(), -1, 1, OrientationEnum.ROTATION_0);
        tileService.placeTile(upperTile7.getTileId(), -1, 0, OrientationEnum.ROTATION_0);
        tileService.placeTile(upperTile8.getTileId(), 1, 0, OrientationEnum.ROTATION_0);

//        var result = gameRulesService.resolveMonasteryCompletion(game.getGameId());

        //assert
//        assertEquals(0, result.size());
    }

    @Test
    public void testSingleMonasteryWithSerfShouldReturn1() throws Exception {
        //arrange
        Tile monasteryTile = tileService.retrieveTileByNameAndGameId("TILE53", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Tile upperTile1 = tileService.retrieveTileByNameAndGameId("TILE11", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Tile upperTile2 = tileService.retrieveTileByNameAndGameId("TILE10", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Tile upperTile3 = tileService.retrieveTileByNameAndGameId("TILE5", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Tile upperTile4 = tileService.retrieveTileByNameAndGameId("TILE6", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Tile upperTile5 = tileService.retrieveTileByNameAndGameId("TILE7", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Tile upperTile6 = tileService.retrieveTileByNameAndGameId("TILE8", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Tile upperTile7 = tileService.retrieveTileByNameAndGameId("TILE9", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        Tile upperTile8 = tileService.retrieveTileByNameAndGameId("TILE4", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));

        tileService.placeTile(upperTile1.getTileId(), 0, 2, OrientationEnum.ROTATION_0);
        tileService.placeTile(upperTile2.getTileId(), 0, 0, OrientationEnum.ROTATION_0);
        tileService.placeTile(upperTile3.getTileId(), -1, 2, OrientationEnum.ROTATION_0);
        tileService.placeTile(upperTile4.getTileId(), 1, 2, OrientationEnum.ROTATION_0);
        tileService.placeTile(upperTile5.getTileId(), 1, 1, OrientationEnum.ROTATION_0);
        tileService.placeTile(upperTile6.getTileId(), -1, 1, OrientationEnum.ROTATION_0);
        tileService.placeTile(upperTile7.getTileId(), -1, 0, OrientationEnum.ROTATION_0);
        tileService.placeTile(upperTile8.getTileId(), 1, 0, OrientationEnum.ROTATION_0);

        Player player = playerService.retrievePlayersByGame(game.getGameId())
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Player not found"));

        var serfPlace = serfService.setTileForSerf(4, monasteryTile, player);
        tileService.placeTile(monasteryTile.getTileId(), 0, 1, OrientationEnum.ROTATION_0);

        Tile monasteryTile2 = tileService.retrieveTileByNameAndGameId("TILE53", game.getGameId())
                .orElseThrow(() -> new RuntimeException("Tile not found"));
        //act
        var result = scoreService.resolveMonasteryCompletion(game.getGameId());

        //assert
        var PlayerAfterPoints = playerService.retrievePlayerByPlayerNumber(player.getPlayerNumber());
        if (PlayerAfterPoints.isEmpty()) throw new RuntimeException("Player not found");
        assertTrue(monasteryTile2.isPlaced());
        if (serfPlace == null){
            fail("Serf wasn't placed correctly");
        }
        assertEquals(1, result.size());
        assertEquals(9, PlayerAfterPoints.get().getPoints());
    }


}
