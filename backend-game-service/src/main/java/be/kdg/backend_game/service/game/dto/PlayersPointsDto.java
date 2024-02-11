package be.kdg.backend_game.service.game.dto;

import be.kdg.backend_game.domain.game.Player;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PlayersPointsDto {
    private List<PlayerPointsDto> playerPoints;

    public PlayersPointsDto(List<Player> players) {
        playerPoints = new ArrayList<>();
        for (var player : players) {
            playerPoints.add(new PlayerPointsDto(player.getPlayerNumber(), player.getPoints(), player.getAccount().getNickName()));
        }
    }
}
