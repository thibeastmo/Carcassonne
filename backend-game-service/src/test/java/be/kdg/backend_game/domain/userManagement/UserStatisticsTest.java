package be.kdg.backend_game.domain.userManagement;

import be.kdg.backend_game.domain.GameTypeEnum;
import be.kdg.backend_game.domain.game.Game;
import be.kdg.backend_game.domain.game.OrientationEnum;
import be.kdg.backend_game.domain.game.Player;
import be.kdg.backend_game.domain.user_management.Account;
import be.kdg.backend_game.repository.GameHistoryRepository;
import be.kdg.backend_game.repository.GameRepository;
import be.kdg.backend_game.repository.PlayerRepository;
import be.kdg.backend_game.service.dto.lobby.NewLobbyDto;
import be.kdg.backend_game.service.game.*;
import be.kdg.backend_game.service.statistics.UserStatisticsService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserStatisticsTest {
    @Autowired
    private GameService gameService;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private TileService tileService;
    @Autowired
    private SerfService serfService;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private UserStatisticsService userStatisticsService;
    @Autowired
    private TurnService turnService;
    @Autowired
    private GameHistoryRepository gameHistoryRepository;
    @Autowired
    private MockMvc mockMvc;
    private static String gameId;
    private static UUID subjectIdPlayer1;
    private static UUID subjectIdPlayer2;
    private static WebApplicationContext webApplicationContext;
    private static List<Player> players;

    @BeforeAll
    public static void init(@Autowired WebApplicationContext webApplicationContext) {
        var lobbyService = webApplicationContext.getBean(LobbyService.class);
        var gameService = webApplicationContext.getBean(GameService.class);
        var turnService = webApplicationContext.getBean(TurnService.class);
        var playerService = webApplicationContext.getBean(PlayerService.class);

        //create lobby
        var newLobbyDto = new NewLobbyDto();
        newLobbyDto.setLobbyName("Statistics testing");
        newLobbyDto.setMaxPlayers(2);
        newLobbyDto.setGameTypeEnum(GameTypeEnum.SHORT);
        subjectIdPlayer1 = UUID.fromString("c557502a-0e3d-4946-8d6f-cf57c3170bab");
        subjectIdPlayer2 = UUID.fromString("bcf903ae-997d-466c-b4ac-410d1fe420be");
        var lobbyInfoDto = lobbyService.createLobby(newLobbyDto, subjectIdPlayer1);
        lobbyService.joinLobby(lobbyInfoDto.getLobbyId(), subjectIdPlayer2);

        //create a game
        gameId = gameService.createGame(lobbyInfoDto.getLobbyId()).get().getGameId().toString();

        //check who is the first player
        var optionalTurn = turnService.retrieveCurrentTurnByGame(UUID.fromString(gameId));
        if (optionalTurn.isEmpty()) {
            fail("No current turn found");
            return;
        }
        var currentTurn = optionalTurn.get();
        var optionalPlayer = playerService.retrievePlayerWithAccountByPlayerNumber(currentTurn.getPlayer().getPlayerNumber());
        if (optionalPlayer.isEmpty()) {
            fail("Could not retrieve player by playernumber: " + currentTurn.getPlayer().getPlayerNumber());
        }
    }

    @Test
    @Order(1)
    public void doUserStatisticsUpdateCorrectly() {
        var gameUUID = UUID.fromString(gameId);
        var optionalGame = gameService.retrieveGameWithPlayers(gameUUID);
        if (optionalGame.isEmpty()) {
            fail("No game found with id: " + gameId);
            return;
        }

        var game = optionalGame.get();
        players = game.getPlayers();
        players.get(0).setPoints(40);
        subjectIdPlayer1 = players.get(0).getAccount().getAccountId();
        subjectIdPlayer2 = players.get(1).getAccount().getAccountId();
        players.get(1).setPoints(20);
        playerRepository.saveAll(players);

        //make sure the players are ordered by playernumber ascending
        players = players.stream()
                .sorted(Comparator.comparingInt(Player::getPlayerNumber))
                .toList();

        //get tile is the first turn
        var tile = turnService.retrieveCurrentTileByGame(gameUUID);
        tileService.discardTile(tile.getTileId());

        //create turns
        //first turn has automatically been created when the game has been created
        createTurn(game, true, true); //turn2 //player 2
        createTurn(game, true, true); //turn3 //player 1
        createTurn(game, true, true); //turn4 //player 2
        createTurn(game, false, true); //turn5 //player 1
        createTurn(game, false, false); //turn6 //player 2
        createTurn(game, true, true); //turn7 //player 1
        createTurn(game, true, true); //turn8 //player 2
        createTurn(game, false, true); //turn9 //player 1
        //first player has: 2 serfs & 4 tiles
        //second player has: 3 serfs & 3 tiles
        game.setGameOver(true);
        gameRepository.save(game);

        if (!userStatisticsService.updateStatisticsAndLoyaltyPointsByGame(game.getGameId())) {
            fail("Could not successfully update the userStatistics for gameId: " + gameUUID);
            return;
        }

        var optionalUserStatistics1 = userStatisticsService.retrieveUserStatisticsByAccountId(subjectIdPlayer1);
        if (optionalUserStatistics1.isEmpty()) {
            fail("Could not find userStatistics of player with account id: " + subjectIdPlayer1);
            return;
        }
        var userStatistics1 = optionalUserStatistics1.get();
        var optionalUserStatistics2 = userStatisticsService.retrieveUserStatisticsByAccountId(subjectIdPlayer2);
        if (optionalUserStatistics2.isEmpty()) {
            fail("Could not find userStatistics of player with account id: " + subjectIdPlayer2);
            return;
        }
        var userStatistics2 = optionalUserStatistics2.get();

        // Player 1
        if (userStatistics1.getTilesPlaced() == 4) {
            assertEquals(4, userStatistics1.getTilesPlaced());
            assertEquals(2, userStatistics1.getSerfsPlaced());
            assertEquals(40, players.get(0).getPoints());
            assertEquals(20, players.get(1).getPoints());
            assertEquals(40, userStatistics1.getTotalScoreAchieved());
            assertEquals(20, userStatistics2.getTotalScoreAchieved());
            assertEquals(1, userStatistics1.getGamesWon());
            assertEquals(0, userStatistics2.getGamesWon());

            players = playerRepository.findPlayerByGame_GameIdOrderByPlayerNumber(gameUUID).get();
            Account account1 = players.get(1).getAccount();
            Account account2 = players.get(0).getAccount();
            assertEquals(13, account1.getLoyaltyPoints()); //must be 20/2+3
            assertEquals(48, account2.getLoyaltyPoints()); //must be 40+(4*2)
            if (account1.getNickName().equals("Atrophius")){
                assertEquals(1030, account1.getExperiencePoints()); //half of points gained because lost
                assertEquals(140, account2.getExperiencePoints()); //half of points gained because lost
            } else {
                assertEquals(110, account1.getExperiencePoints()); //half of points gained because lost
                assertEquals(1060, account2.getExperiencePoints()); //full points gained because won
            }
        }
        else {
            assertEquals(3, userStatistics1.getTilesPlaced());
            assertEquals(3, userStatistics1.getSerfsPlaced());
        }

        // Player 2
        if (userStatistics2.getTilesPlaced() == 3) {
            assertEquals(3, userStatistics2.getTilesPlaced());
            assertEquals(3, userStatistics2.getSerfsPlaced());
        } else {
            assertEquals(4, userStatistics2.getTilesPlaced());
            assertEquals(2, userStatistics2.getSerfsPlaced());
            assertEquals(40, players.get(0).getPoints());
            assertEquals(20, players.get(1).getPoints());
            assertEquals(40, userStatistics1.getTotalScoreAchieved());
            assertEquals(20, userStatistics2.getTotalScoreAchieved());
            assertEquals(1, userStatistics1.getGamesWon());
            assertEquals(0, userStatistics2.getGamesWon());

            players = playerRepository.findPlayerByGame_GameIdOrderByPlayerNumber(gameUUID).get();
            Account account1 = players.get(0).getAccount();
            Account account2 = players.get(1).getAccount();
            assertEquals(46, account2.getLoyaltyPoints()); //must be 20/2+3
            assertEquals(14, account1.getLoyaltyPoints()); //must be 40+(4*2)
            if (account1.getNickName().equals("Atrophius")){
                assertEquals(1030, account1.getExperiencePoints()); //half of points gained because lost
                assertEquals(140, account2.getExperiencePoints()); //half of points gained because lost
            } else {
                assertEquals(110, account1.getExperiencePoints()); //half of points gained because lost
                assertEquals(1060, account2.getExperiencePoints()); //full points gained because won
            }
        }

        assertEquals(1, userStatistics1.getGamesPlayed());
        assertEquals(1, userStatistics2.getGamesPlayed());


    }

    private void createTurn(Game game, boolean placeSerf, boolean placeTile) {
        if (turnService.createTurn(game.getGameId()).isEmpty()) {
            fail("Could not create turn");
            return;
        }
        var optionalTurn = turnService.retrieveCurrentTurnByGame(game.getGameId());
        if (optionalTurn.isEmpty()) {
            fail("No current turn found");
            return;
        }
        var turn = optionalTurn.get();
        var tile = turn.getTile();
        if (placeTile) {
            tile = tileService.placeTile(tile.getTileId(), 0, 0, OrientationEnum.ROTATION_0);
        } else {
            tileService.discardTile(tile.getTileId());
        }
        if (placeTile && placeSerf) {
            try {
                turn.setPlacedSerf(true);
                serfService.setTileForSerf(0, tile, turn);
            } catch (Exception e) {
                fail("Something went wrong when setting a serf: " + e.getMessage());
            }
        }
    }

    @Test
    @Order(2)
    public void hasGameHistoryBeenCreatedCorrectly() {
        //make sure the players are ordered by points descending
        players = players.stream()
                .sorted(Comparator.comparingInt(Player::getPoints))
                .toList();
        for (var i = 0; i < players.size(); i++) {
            var player = players.get(i);
            var optionalGameHistory = gameHistoryRepository.findFirstGameHistoryByAccount_AccountIdOrderByCreationDate(player.getAccount().getAccountId());
            if (optionalGameHistory.isEmpty()) {
                fail("No game history for player found");
                return;
            }
            var gameHistory = optionalGameHistory.get();
            assertEquals(i + 1, gameHistory.getRank());
        }
    }

    @Test
    @Order(3)
    public void infoForEndOfGameResultsAreRetrievableCorrectly() throws Exception {
        mockMvc.perform(get("/api/gamehistory?gameId=" + gameId)
                        .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                        .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                .jwt(jwt -> jwt
                                        .claim(StandardClaimNames.SUB, "c557502a-0e3d-4946-8d6f-cf57c3170bab")
                                        .claim(StandardClaimNames.GIVEN_NAME, "thibeastmo"))))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Order(4)
    public void userStatisticsAreRetrievableCorrectly() throws Exception {
        mockMvc.perform(get("/api/statistics")
                        .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                        .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                .jwt(jwt -> jwt
                                        .claim(StandardClaimNames.SUB, "c557502a-0e3d-4946-8d6f-cf57c3170bab")
                                        .claim(StandardClaimNames.GIVEN_NAME, "thibeastmo"))))
                .andExpect(status().isOk())
                .andReturn();
        gameService.deleteGame(UUID.fromString(gameId));
    }
}
