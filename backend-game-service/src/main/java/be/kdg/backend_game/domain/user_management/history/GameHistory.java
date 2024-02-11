package be.kdg.backend_game.domain.user_management.history;

import be.kdg.backend_game.domain.GameTypeEnum;
import be.kdg.backend_game.domain.game.Color;
import be.kdg.backend_game.domain.game.Game;
import be.kdg.backend_game.domain.user_management.Account;
import jakarta.persistence.*;
import lombok.Data;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class GameHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID gameHistoryId;
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;
    private String avatarImage; //saved here and not linked to account to show if new avatar was used
    private GameTypeEnum gameType;
    private String nickname;
    private short rank;
    private int points;
    private Color color;
    private LocalDateTime creationDate;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public GameHistory() {}

    public GameHistory(short rank, int points, Color color, Account account, Game game) {
        if (account != null) {
            if (account.getAvatar() != null) {
                this.avatarImage = account.getAvatar().getUrl();
            }
            this.nickname = account.getNickName();
        }
        this.game = game;
        this.gameType = game.getGameTypeEnum();
        this.rank = rank;
        this.points = points;
        this.account = account;
        this.creationDate = LocalDateTime.now();
        this.color = color;
    }
}
