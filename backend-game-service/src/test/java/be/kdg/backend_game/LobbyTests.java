package be.kdg.backend_game;

import be.kdg.backend_game.domain.GameTypeEnum;
import be.kdg.backend_game.domain.lobby.Lobby;
import be.kdg.backend_game.repository.LobbyRepository;
import be.kdg.backend_game.service.AccountService;
import be.kdg.backend_game.service.dto.lobby.LobbyInfoDto;
import be.kdg.backend_game.service.dto.lobby.NewLobbyDto;
import be.kdg.backend_game.service.game.GameService;
import be.kdg.backend_game.service.game.LobbyService;
import org.hibernate.annotations.SQLInsert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;


import java.util.List;
import java.util.UUID;


@SpringBootTest
@AutoConfigureMockMvc
public class LobbyTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private LobbyService lobbyService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private WebApplicationContext webApplicationContext;

    private static LobbyInfoDto lobby;
    @BeforeEach
    public void init() {
        lobbyService = webApplicationContext.getBean(LobbyService.class);
        accountService = webApplicationContext.getBean(AccountService.class);

        var newLobbyDto = new NewLobbyDto();
        newLobbyDto.setLobbyName("Lobby testing");
        newLobbyDto.setMaxPlayers(4);
        newLobbyDto.setGameTypeEnum(GameTypeEnum.SHORT);
        lobby = lobbyService.createLobby(newLobbyDto, UUID.fromString("58540443-4003-49e6-9b4c-336f2e915a34"));
    }

    @Test
    public void testCreateLobbyShouldReturnLobbyWithOneParticipant() throws Exception {
        var newLobbyDto = new NewLobbyDto();
        newLobbyDto.setLobbyName("LobbyTest");
        newLobbyDto.setMaxPlayers(4);
        newLobbyDto.setGameTypeEnum(GameTypeEnum.SHORT);

        var subjectId = UUID.fromString("58540443-4003-49e6-9b4c-336f2e915a34");
        var newCreatedLobby = lobbyService.createLobby(newLobbyDto, subjectId);
        assertEquals(newCreatedLobby.getLobbyParticipants().length, 1);
    }

    @Test
    public void testCreateLobbyShouldReturnStatusCreatedAndLobbyWithSameName() throws Exception {
        LobbyInfoDto expectedLobbyInfo = new LobbyInfoDto();
        expectedLobbyInfo.setLobbyName("Cool guys lobby");
        expectedLobbyInfo.setMaxPlayers(4);

        mockMvc.perform(post("/api/lobby/create-lobby")
                        .accept(MediaType.APPLICATION_JSON).contentType(APPLICATION_JSON).content(
                                """
                                        {
                                        "lobbyName": "Cool guys lobby",
                                        "maxPlayers": 4,
                                        "gameTypeEnum": "LONG"                                                       }
                                        """)
                        .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                .jwt(jwt -> jwt
                                        .claim(StandardClaimNames.SUB, "e5e92098-fc3b-4daa-b337-be740fb610e5")
                                        .claim(StandardClaimNames.GIVEN_NAME, "Pavelski")
                                        .claim(StandardClaimNames.FAMILY_NAME, "Pavelski")
                                        .claim(StandardClaimNames.EMAIL, "pavel.ski@gmail.com"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lobbyName").value(expectedLobbyInfo.getLobbyName()))
                .andReturn();
    }

    @Test
    public void testGetLobbyInformationShouldReturnStatusOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/lobby/get-lobby?lobbyId=1bc3f9bb-6e16-4526-a82a-af8c25604ef7")
                        .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                .jwt(jwt -> jwt
                                        .claim(StandardClaimNames.SUB, "e5e92098-fc3b-4daa-b337-be740fb610e5")
                                        .claim(StandardClaimNames.GIVEN_NAME, "Pavelski")
                                        .claim(StandardClaimNames.FAMILY_NAME, "Pavelski")
                                        .claim(StandardClaimNames.EMAIL, "pavel.ski@gmail.com"))))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllOpenLobbiesShouldReturnStatusOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/lobby/get-all-open-lobbies")
                .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                        .jwt(jwt -> jwt
                                .claim(StandardClaimNames.SUB, "e5e92098-fc3b-4daa-b337-be740fb610e5")
                                .claim(StandardClaimNames.GIVEN_NAME, "Pavelski")
                                .claim(StandardClaimNames.FAMILY_NAME, "Pavelski")
                                .claim(StandardClaimNames.EMAIL, "pavel.ski@gmail.com"))))
                .andExpect(status().isOk());
    }
}
