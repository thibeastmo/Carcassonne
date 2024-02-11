package be.kdg.backend_game.service.game.dto;

import be.kdg.backend_game.domain.game.Player;
import lombok.Getter;

import java.util.Random;

@Getter
public class PlayerDataDto {
    private int playerNumber;
    private String nickname;
    private String avatarUrl;
    private String color;

    public PlayerDataDto(Player player) {
        this.playerNumber = player.getPlayerNumber();
        this.nickname = player.getAccount().getNickName();
        if (player.getAccount() != null && player.getAccount().getAvatar() != null){
            this.avatarUrl = player.getAccount().getAvatar().getUrl();
        } else this.avatarUrl = "/images/avatars/default.png";
        this.color = player.getColor().getHexCode();
    }
}
