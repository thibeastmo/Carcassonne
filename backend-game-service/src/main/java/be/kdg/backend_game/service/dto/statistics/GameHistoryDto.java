package be.kdg.backend_game.service.dto.statistics;

import be.kdg.backend_game.domain.GameTypeEnum;
import be.kdg.backend_game.domain.user_management.history.GameHistory;
import lombok.Data;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class GameHistoryDto {
    private UUID gameHistoryId;
    private UUID gameId;
    private String avatarImage;
    private GameTypeEnum gameType;
    private String nickname;
    private short rank;
    private int points;
    private String color;
    private long creationDate;

    public GameHistoryDto(GameHistory gameHistory) {
        gameHistoryId = gameHistory.getGameHistoryId();
        if (gameHistory.getAvatarImage() != null) {
            avatarImage = gameHistory.getAvatarImage();
        }
        gameId = gameHistory.getGame().getGameId();
        gameType = gameHistory.getGameType();
        nickname = gameHistory.getNickname();
        rank = gameHistory.getRank();
        points = gameHistory.getPoints();
        ZonedDateTime zdt = ZonedDateTime.of(gameHistory.getCreationDate(), ZoneId.systemDefault());
        creationDate = zdt.toInstant().toEpochMilli();
        color = gameHistory.getColor().name().toLowerCase();
    }
}
