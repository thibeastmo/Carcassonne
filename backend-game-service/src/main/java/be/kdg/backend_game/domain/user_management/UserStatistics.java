package be.kdg.backend_game.domain.user_management;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class UserStatistics {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID userStatisticsId;
    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;
    private int gamesWon;
    private int gamesPlayed;
    private int tilesPlaced;
    private int serfsPlaced;
    private int contestedLandWon;
    private int contestedLandLost;
    private int totalScoreAchieved;

    public UserStatistics(Account account) {
        this.account = account;
    }
    public UserStatistics() {}
}
