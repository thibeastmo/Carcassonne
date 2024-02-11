package be.kdg.backend_game.service.dto.statistics;

import be.kdg.backend_game.domain.user_management.UserStatistics;
import lombok.Data;

@Data
public class UserStatisticsDto {
    private int gamesWon;
    private int gamesPlayed;
    private int tilesPlaced;
    private int serfsPlaced;
    private int contestedLandWon;
    private int contestedLandLost;
    private int totalScoreAchieved;
    public UserStatisticsDto() {}
    public UserStatisticsDto(UserStatistics userStatistics) {
        gamesWon = userStatistics.getGamesWon();
        gamesPlayed = userStatistics.getGamesPlayed();
        tilesPlaced = userStatistics.getTilesPlaced();
        serfsPlaced = userStatistics.getSerfsPlaced();
        contestedLandWon = userStatistics.getContestedLandWon();
        contestedLandLost = userStatistics.getContestedLandLost();
        totalScoreAchieved = userStatistics.getTotalScoreAchieved();
    }
}
