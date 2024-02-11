package be.kdg.backend_game.service.lobby;


import be.kdg.backend_game.domain.GameTypeEnum;
import be.kdg.backend_game.domain.lobby.Lobby;
import be.kdg.backend_game.domain.user_management.Account;
import be.kdg.backend_game.repository.AccountRepository;
import be.kdg.backend_game.repository.LobbyRepository;
import be.kdg.backend_game.service.AccountService;
import be.kdg.backend_game.service.dto.lobby.NewLobbyDto;
import be.kdg.backend_game.service.game.LobbyService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class QuickJoinTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private LobbyService lobbyService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private LobbyRepository lobbyRepository;


    Account account1;
    Account account2;
    Account account3;
    Account account4;
    Lobby lobby1;
    Lobby lobby2;
    @BeforeEach
    public void init() {
        Account newAccount1 = new Account(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "TestUser",
                "test@mail.com",
                1000,
                6600
        );
        account1 = accountRepository.save(newAccount1);

        Account newAccount2 = new Account(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "TestUser2",
                "test@mail.com",
                1000,
                6050
        );
        account2 = accountRepository.save(newAccount2);

        Account newAccount3 = new Account(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "TestUser3",
                "test@mail.com",
                1000,
                6100
        );
        account3 = accountRepository.save(newAccount3);

        Account newAccount4 = new Account(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "TestUser3",
                "test@mail.com",
                1000,
                12500
        );
        account4 = accountRepository.save(newAccount4);

        var newLobbyDto = new NewLobbyDto();
        newLobbyDto.setLobbyName("LobbyTest");
        newLobbyDto.setMaxPlayers(4);
        newLobbyDto.setGameTypeEnum(GameTypeEnum.SHORT);

        var newLobby1 = lobbyService.createLobby(newLobbyDto, account2.getSubjectId());
        Optional<Lobby> createdLobby1 = lobbyService.retrieveLobby(newLobby1.getLobbyId());
        assertTrue(createdLobby1.isPresent());
        lobby1 = createdLobby1.get();

        var newLobby2 = lobbyService.createLobby(newLobbyDto, account3.getSubjectId());
        Optional<Lobby> createdLobby2 = lobbyService.retrieveLobby(newLobby2.getLobbyId());
        assertTrue(createdLobby2.isPresent());
        lobby2 = createdLobby2.get();
    }

    @AfterEach
    public void afterEach() {
        accountRepository.delete(account1);
        accountRepository.delete(account2);
        accountRepository.delete(account3);
        accountRepository.delete(account4);
        lobbyRepository.delete(lobby1);
        lobbyRepository.delete(lobby2);
    }

    @Test
    public void testQuickjoinLobbyShouldReturnBestMatchingLobby() {
        lobbyService.joinLobby(lobby1.getLobbyId(), account3.getSubjectId());
        lobbyService.joinLobby(lobby2.getLobbyId(), account2.getSubjectId());
        lobbyService.joinLobby(lobby2.getLobbyId(), account4.getSubjectId());

        var joinedLobbyDto = lobbyService.quickJoinAvailableLobby(account1.getSubjectId(), GameTypeEnum.SHORT);
        assertTrue(joinedLobbyDto.isPresent());
        assertEquals(lobby1.getLobbyId(), joinedLobbyDto.get().getLobbyId());
    }

    @Test
    public void testQuickJoinLobbyWithoutLobbiesAvailableSHouldReturnNoLobby() {
        var joinedLobbyDto = lobbyService.quickJoinAvailableLobby(account1.getSubjectId(), GameTypeEnum.LONG);
        assertFalse(joinedLobbyDto.isPresent());
    }

}