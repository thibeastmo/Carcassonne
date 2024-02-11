package be.kdg.backend_game.service.dto.statistics;

import lombok.Data;

@Data
public class GameResultDto {
    private String nickname;
    private String avatarUrl;
    private int points;
}
