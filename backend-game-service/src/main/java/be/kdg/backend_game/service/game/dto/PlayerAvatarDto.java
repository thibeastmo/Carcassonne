package be.kdg.backend_game.service.game.dto;

import lombok.Getter;

@Getter
public class PlayerAvatarDto {
    private int playerNumber;
    private String avatarUrl;

    public PlayerAvatarDto() {}

    public PlayerAvatarDto(int playerNumber, String avatarUrl) {
        this.playerNumber = playerNumber;
        this.avatarUrl = avatarUrl;
    }
}
