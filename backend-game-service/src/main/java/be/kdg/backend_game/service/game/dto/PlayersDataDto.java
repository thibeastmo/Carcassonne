package be.kdg.backend_game.service.game.dto;

import be.kdg.backend_game.domain.game.Player;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
public class PlayersDataDto {
    private List<PlayerDataDto> playersConstantData;

    public PlayersDataDto(List<Player> players) {
        this.playersConstantData = new ArrayList<>();
        for (Player player : players){
            this.playersConstantData.add(new PlayerDataDto(player));
        }
    }
}
