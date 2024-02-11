package be.kdg.backend_game.domain.game;

import be.kdg.backend_game.domain.GameTypeEnum;
import be.kdg.backend_game.service.AccountService;
import be.kdg.backend_game.service.dto.lobby.NewLobbyDto;
import be.kdg.backend_game.service.game.*;
import be.kdg.backend_game.service.game.dto.TilePlacementDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@SpringBootTest
@AutoConfigureMockMvc
public class SerfPlacementTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SerfService serfService;
    @Autowired
    private TurnService turnService;
    @Autowired
    private SerfPlacementService serfPlacementService;
    private static String gameId;
    private static WebApplicationContext webApplicationContext;

    @BeforeAll
    public static void init(@Autowired WebApplicationContext webApplicationContext) {
        //TODO: create game by owner of game
        var lobbyService = webApplicationContext.getBean(LobbyService.class);
        var gameService = webApplicationContext.getBean(GameService.class);
        var turnService = webApplicationContext.getBean(TurnService.class);
        var gameRulesService = webApplicationContext.getBean(GameRulesService.class);
        var tileService = webApplicationContext.getBean(TileService.class);

        //create lobby
        var newLobbyDto = new NewLobbyDto();
        newLobbyDto.setLobbyName("Serf testing");
        newLobbyDto.setMaxPlayers(4);
        newLobbyDto.setGameTypeEnum(GameTypeEnum.SHORT);
        var lobbyInfoDto = lobbyService.createLobby(newLobbyDto, UUID.fromString("58540443-4003-49e6-9b4c-336f2e915a34"));

        //create a game
        gameId = gameService.createGame(lobbyInfoDto.getLobbyId()).get().getGameId().toString();
        var gameUUID = UUID.fromString(gameId);

        //get first tile that is placable
        var tileToPlace = turnService.retrieveCurrentTileByGame(gameUUID);
        Optional<List<TilePlacement>> legalPlacements = gameRulesService.retrieveLegalTilePlacementsForAllSides(gameUUID, tileToPlace.getTileId());
        var firstTile = legalPlacements.get().get(0);

        //place tile
        try {
            var tilePlaced = tileService.placeTile(tileToPlace.getTileId(), firstTile.getXValue(), firstTile.getYValue(), firstTile.getOrientation());

        } catch (Exception e){
            fail("Something went wrong when placing a tile: " + e.getMessage());
        }
    }

    @Test
    public void placeSerfOnTileCreatesReferenceInSerf () throws Exception {
        var tile = turnService.retrieveCurrentTileByGame(UUID.fromString(gameId));
        var legalPlacement = serfPlacementService.checkAvailableTileZones(tile.getTileId()).getAvailableZones().stream().findFirst();
        mockMvc.perform(patch("/api/serf/place?gameId=" + gameId + "&tileZoneId=" + legalPlacement.get())
                        .accept(MediaType.APPLICATION_JSON).contentType(APPLICATION_JSON)
                        .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                .jwt(jwt -> jwt
                                        .claim(StandardClaimNames.SUB, "a0a29b50-b8e4-4de8-bac8-b1d52ed45073")
                                        .claim(StandardClaimNames.GIVEN_NAME, "kvdw"))))
                .andExpect(status().isOk())
                .andReturn();

        var usedSerfs = serfService.getUsedSerfsByGameId(UUID.fromString(gameId));
        assertEquals(1, usedSerfs.size());
    }
}
