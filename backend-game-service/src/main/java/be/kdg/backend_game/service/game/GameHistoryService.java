package be.kdg.backend_game.service.game;

import be.kdg.backend_game.domain.user_management.history.GameHistory;
import be.kdg.backend_game.repository.GameHistoryRepository;
import be.kdg.backend_game.service.dto.statistics.GameHistoryDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class GameHistoryService {
    private static final Logger logger = Logger.getLogger(GameHistoryService.class.getPackageName());
    private final GameHistoryRepository gameHistoryRepository;

    public GameHistoryService(GameHistoryRepository gameHistoryRepository) {
        this.gameHistoryRepository = gameHistoryRepository;
    }

    public List<GameHistoryDto> retrieveGameHistoriesBySubjectId(UUID subjectId) {
        List<GameHistory> gameHistories = gameHistoryRepository.findGameHistoriesByAccount_SubjectId(subjectId);
        List<GameHistoryDto> gameHistoryDtos = new ArrayList<>();
        gameHistories.forEach(g -> gameHistoryDtos.add(new GameHistoryDto(g)));
        return gameHistoryDtos;
    }
    public List<GameHistoryDto> retrieveGameHistoryByGameId(UUID subjectId, UUID gameId) {
        List<GameHistory> gameHistories = gameHistoryRepository.findGameHistoryByGameIdOrderByPlayerNumberAsc(gameId);
        List<GameHistoryDto> gameHistoryDtos = new ArrayList<>();
        if (!gameHistories.isEmpty()) {
            logger.log(Level.INFO, "Game ended: Found game history by subjectId ("+subjectId+") and gameId (" + gameId + ")");
            gameHistories.forEach(g -> gameHistoryDtos.add(new GameHistoryDto(g)));
        }
        return gameHistoryDtos;
    }
    public void disconnectFromGame(UUID gameId){
        gameHistoryRepository.updateDisconnectFromGame(gameId);
    }
}
