package be.kdg.backend_game.service.game.dto;

import lombok.Getter;

@Getter
public class PlayerPointsDto {
    private int playerNumber;
    private String nickname;
    private int points;

    public PlayerPointsDto(int playerNumber, int points, String nickname) {
        this.playerNumber = playerNumber;
        this.points = points;
        this.nickname = nickname;
    }
}
