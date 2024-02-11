package be.kdg.backend_game.service.game.dto;

import be.kdg.backend_game.domain.game.Player;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
public class PlayersAvatarDto {
    private List<PlayerAvatarDto> playerAvatars;

    public PlayersAvatarDto() {}

    public PlayersAvatarDto(List<Player> players) {
        playerAvatars = new ArrayList<>();
        for (var player : players) {
            if (player.getAccount().getAvatar() == null) {
                playerAvatars.add(new PlayerAvatarDto(player.getPlayerNumber(), "/images/avatars/default.png"));
            }
            else {
                playerAvatars.add(new PlayerAvatarDto(player.getPlayerNumber(), player.getAccount().getAvatar().getUrl()));
            }
        }
    }
}
