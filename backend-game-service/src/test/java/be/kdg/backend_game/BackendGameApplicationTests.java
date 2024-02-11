package be.kdg.backend_game;

import be.kdg.backend_game.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackendGameApplicationTests {
    @Autowired
    private UserStatisticsRepository userStatisticsRepository;
    @Autowired
    private TurnRepository turnRepository;
    @Autowired
    private TileRepository tileRepository;
    @Autowired
    private SerfRepository serfRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private LobbyParticipantRepository lobbyParticipantRepository;
    @Autowired
    private LobbyRepository lobbyRepository;
    @Autowired
    private GameHistoryRepository gameHistoryRepository;
    @Autowired
    private GameRepository gameRepository;

    @Test
    void deleteAllData() {
        userStatisticsRepository.deleteAll();
        turnRepository.deleteAll();
        tileRepository.deleteAll();
        serfRepository.deleteAll();
        playerRepository.deleteAll();
        lobbyParticipantRepository.deleteAll();
        lobbyRepository.deleteAll();
        gameHistoryRepository.deleteAll();
        gameRepository.deleteAll();
    }

}
