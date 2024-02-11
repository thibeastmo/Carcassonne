package be.kdg.backend_game.domain.game;

import be.kdg.backend_game.domain.lobby.Lobby;
import be.kdg.backend_game.service.game.*;
import be.kdg.backend_game.service.game.tile_calculations.ScoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GameTest {
    @Autowired
    private GameService gameService;
    @Autowired
    private LobbyService lobbyService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private TurnService turnService;
    @Autowired
    private TileService tileService;
    @Autowired
    private GameRulesService gameRulesService;
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private SerfService serfService;
    private static final Logger logger = Logger.getLogger(TurnService.class.getPackageName());


    @Test
    public void createGameBasedOnLobbyCorrectly() {
        final UUID lobbyId = UUID.fromString("1bc3f9bb-6e16-4526-a82a-af8c25604ef7");

        var optionalLobby = lobbyService.retrieveLobby(lobbyId);

        if (optionalLobby.isEmpty()) {
            fail("No lobby found with this uuid: " + lobbyId);
            return;
        }
        var lobby = optionalLobby.get();
        try {
            var optionalGameId = gameService.createGame(lobbyId);

            if (optionalGameId.isPresent()) {
                optionalLobby = lobbyService.retrieveLobby(lobbyId);
                if (optionalLobby.isEmpty()) {
                    fail("No lobby found after creating game. Lobby with this uuid: " + lobbyId);
                    return;
                }
                lobby = optionalLobby.get();
                assertTrue(lobby.isClosed());

                var optionalGame = gameService.retrieveGameWithLobbyId(lobbyId);
                if (optionalGame.isEmpty()) {
                    fail("No game found with this lobbyId: " + lobbyId);
                    return;
                }
                var game = optionalGame.get();

                //check if lobby is closed
                assertTrue(lobby.isClosed());

                var players = playerService.retrievePlayersByGame(game.getGameId());
                if (players == null) {
                    fail("No players were initialized.");
                    return;
                }

                //check if players were initialized properly
                final int lobbyPlayersCount = lobbyService.retrieveLobbyParticipantsByLobbyId(lobby.getLobbyId()).size();
                final int gamePlayersCount = players.size();
                assertEquals(lobbyPlayersCount, gamePlayersCount);
                assertTrue(lobbyPlayersCount <= lobby.getMaxPlayers());
                assertTrue(lobbyPlayersCount >= 2);

                //check turn is player with lowest playerNumber
                var optionalTurn = turnService.retrieveCurrentTurnByGame(game.getGameId());
                if (optionalTurn.isEmpty()) {
                    fail("No turn found with this gameId: " + game.getGameId());
                    return;
                }
                var turn = optionalTurn.get();
                assertEquals(turn.getPlayer().getPlayerNumber(), players.get(0).getPlayerNumber());

                //check right amount of serfs
                for (var player : players) {
                    var serfsOfPlayer = gameService.retrieveSerfsByPlayer(player.getPlayerNumber());
                    if (serfsOfPlayer != null) {
                        assertEquals(GameService.TOTAL_SERFS_COUNT, serfsOfPlayer.size());
                    } else {
                        fail("No serfs were found for player: " + player.getPlayerNumber());
                    }
                    assertNotNull(player.getGame());
                    assertNotNull(player.getAccount());
                }

                //check game type
                assertEquals(lobby.getGameTypeEnum(), game.getGameTypeEnum());

                //check turn duration
                assertEquals(lobby.getGameTypeEnum().getTurnDuration(), game.getMaxTurnDurationTime());


                assertEquals(72, gameService.retrieveTilesByGame(game.getGameId()).size());
                gameService.deleteGame(game.getGameId());
            }
            else{
                fail("Could not create game based on lobbyId.");
            }
        } catch (DataIntegrityViolationException e) {
            fail(e.getMessage());
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }


    @Test
    public void checkIfRoadIsFinishedScenario1() {
        final UUID lobbyId = UUID.fromString("1bc3f9bb-6e16-4526-a82a-af8c25604ef8");

        var optionalLobby = lobbyService.retrieveLobby(lobbyId);

        if (optionalLobby.isEmpty()) {
            fail("No lobby found with this uuid: " + lobbyId);
        }

        var optionalGame = gameService.createGame(lobbyId);
        assertTrue(optionalGame.isPresent());
        Game game = optionalGame.get();
        Optional<Tile> tile54StraightRoadOptional = tileService.retrieveTileByNameAndGameId("TILE54", optionalGame.get().getGameId());
        Optional<Tile> tile57CrossroadsOptional = tileService.retrieveTileByNameAndGameId("TILE57", optionalGame.get().getGameId());
        Optional<Tile> tile74CrossroadsOptional = tileService.retrieveTileByNameAndGameId("TILE74", optionalGame.get().getGameId());
        Optional<Tile> tile28CornerRoadOptional = tileService.retrieveTileByNameAndGameId("TILE28", optionalGame.get().getGameId());
        Optional<Tile> tile29CornerRoadOptional = tileService.retrieveTileByNameAndGameId("TILE29", optionalGame.get().getGameId());

        // Make sure that all the tiles are present
        assertTrue(tile54StraightRoadOptional.isPresent());
        assertTrue(tile57CrossroadsOptional.isPresent());
        assertTrue(tile74CrossroadsOptional.isPresent());
        assertTrue(tile28CornerRoadOptional.isPresent());
        assertTrue(tile29CornerRoadOptional.isPresent());

        // Assert the tilezones of one tile to check if the tilezone is correct
        int[] expectedTileZones = {2,0,2,2,0,2,2,0,2};
        int[] receivedTileZones = tile54StraightRoadOptional.get()
                .getTileZones()
                .stream()
                .mapToInt(Enum::ordinal)
                .toArray();
        assertArrayEquals(expectedTileZones, receivedTileZones);

        Tile tile54StraightRoad = tile54StraightRoadOptional.get();
        Tile tile57Crossroads = tile57CrossroadsOptional.get();
        Tile tile74Crossroads = tile74CrossroadsOptional.get();
        Tile tile28CornerRoad = tile28CornerRoadOptional.get();
        Tile tile29CornerRoad = tile29CornerRoadOptional.get();

        // Place a serf on the tile
        Serf serf1Player1 = null;
        Serf serf2Player1 = null;
        Serf serf1Player2 = null;
        Game gameWithPlayers = gameService.retrieveGameWithPlayers(game.getGameId()).get();
        assertNotNull(gameWithPlayers);
        var player = gameWithPlayers.getPlayers().get(0);
        var player2 = gameWithPlayers.getPlayers().get(1);

        // Simulate a possible game scenario, with 2 roads disconnected by 1 more road
        var tile28CornerRoadPlaced = tileService.placeTile(tile28CornerRoad.getTileId(), 9, 10, OrientationEnum.ROTATION_0);
        var tile29CornerRoadPlaced = tileService.placeTile(tile29CornerRoad.getTileId(), 10, 10, OrientationEnum.ROTATION_90);
        var tile74CrossroadsPlaced = tileService.placeTile(tile74Crossroads.getTileId(), 9, 9, OrientationEnum.ROTATION_180);
        var tile57CrossroadsPlaced = tileService.placeTile(tile57Crossroads.getTileId(), 10, 12, OrientationEnum.ROTATION_0);

        var optionalTurn = turnService.retrieveCurrentTurnByGame(game.getGameId());
        if (optionalTurn.isEmpty()) {
            fail("No current turn found");
            return;
        }

        try {
            serf1Player1 = serfService.setTileForSerf(4, tile28CornerRoadPlaced, player);
            serf2Player1 = serfService.setTileForSerf(4, tile54StraightRoad, player);
            serf1Player2 = serfService.setTileForSerf(7, tile74CrossroadsPlaced, player2);
        } catch (Exception e) {
            fail("Something went wrong!: " + e.getMessage());
        }

        // Connect the road and finish it
        var placedTile = tileService.placeTile(tile54StraightRoad.getTileId(), 10, 11, OrientationEnum.ROTATION_0);

        //var roadResult = gameRulesService.checkIfRoadIsFinished(placedTile);
        //assertTrue(roadResult.isFinished());

        // Any value should be equal to the amount of tiles that have been placed
        Map<Player, Integer> scoresToAddMap = scoreService.calculateScoreOfPlacedRoadOrCrossroad(placedTile);
        Optional<Integer> anyValue = scoresToAddMap.values().stream().findAny();
        anyValue.ifPresent(integer -> {
            assertEquals(5, integer);
        });

        // Player 1 should win in this scenario, because they have more serfs
        assertEquals(1, scoresToAddMap.keySet().size());
        assertNotNull(player);
        gameService.deleteGame(game.getGameId());
    }

    @Test
    public void checkIfRoadIsFinishedScenario2() {
        final UUID lobbyId = UUID.fromString("89c2f9fa-6e16-45c6-a82a-af8c2520aefb");

        var optionalLobby = lobbyService.retrieveLobby(lobbyId);

        if (optionalLobby.isEmpty()) {
            fail("No lobby found with this uuid: " + lobbyId);
        }
        optionalLobby.get().setGame(null);


        var optionalGame = gameService.createGame(lobbyId);
        assertTrue(optionalGame.isPresent());
        Game game = optionalGame.get();
        Optional<Tile> tile54StraightRoadOptional = tileService.retrieveTileByNameAndGameId("TILE54", optionalGame.get().getGameId());
        Optional<Tile> tile74CrossroadsOptional = tileService.retrieveTileByNameAndGameId("TILE74", optionalGame.get().getGameId());
        Optional<Tile> tile45MonasteryRoadEndOptional = tileService.retrieveTileByNameAndGameId("TILE45", optionalGame.get().getGameId());

        // Make sure that all the tiles are present
        assertTrue(tile54StraightRoadOptional.isPresent());
        assertTrue(tile74CrossroadsOptional.isPresent());
        assertTrue(tile45MonasteryRoadEndOptional.isPresent());

        Tile tile54StraightRoad = tile54StraightRoadOptional.get();
        Tile tile74Crossroads = tile74CrossroadsOptional.get();
        Tile tile45MonasteryRoad = tile45MonasteryRoadEndOptional.get();

        var tile74CrossroadsPlaced = tileService.placeTile(tile74Crossroads.getTileId(), 10, 10, OrientationEnum.ROTATION_0);
        var tile45MonasteryRoadPlaced = tileService.placeTile(tile45MonasteryRoad.getTileId(), 10, 12, OrientationEnum.ROTATION_0);
        var tile54StraightRoadPlaced = tileService.placeTile(tile54StraightRoad.getTileId(), 10, 11, OrientationEnum.ROTATION_0);


        Serf serf1Player1 = null;
        Serf serf2Player1 = null;
        Serf serf1Player2 = null;
        Game gameWithPlayers = gameService.retrieveGameWithPlayers(game.getGameId()).get();
        var player = gameWithPlayers.getPlayers().get(0);
        var player2 = gameWithPlayers.getPlayers().get(1);

        assertNotNull(gameWithPlayers);
        try {
            serf2Player1 = serfService.setTileForSerf(7, tile45MonasteryRoadPlaced, player);
            serf1Player2 = serfService.setTileForSerf(1, tile74CrossroadsPlaced, player2);
        } catch (Exception e) {
            fail("Something went wrong!: " + e.getMessage());
        }

        Map<Player, Integer> scoresToAddMap = scoreService.calculateScoreOfPlacedRoadOrCrossroad(tile74CrossroadsPlaced);
        Optional<Integer> anyValue = scoresToAddMap.values().stream().findAny();
        anyValue.ifPresent(integer -> {
            assertEquals(3, integer);
        });

        assertEquals(2, scoresToAddMap.keySet().size());
        gameService.deleteGame(game.getGameId());
    }
}
