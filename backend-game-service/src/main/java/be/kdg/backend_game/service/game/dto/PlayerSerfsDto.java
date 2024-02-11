package be.kdg.backend_game.service.game.dto;

import be.kdg.backend_game.domain.game.Serf;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PlayerSerfsDto {
    private int playerNumber;
    private List<PlayerSerfDto> playerSerfs;
    public PlayerSerfsDto() {}

    public PlayerSerfsDto(List<Serf> playerSerfs, int playerNumber) {
        this.playerSerfs = new ArrayList<>();
        for (var serf : playerSerfs) {
            this.playerSerfs.add(new PlayerSerfDto(
                    serf.getTileZoneId(),
                    serf.getTile() != null ? serf.getTile().getX() : -1,
                    serf.getTile() != null ? serf.getTile().getY() : -1
            ));
        }
        this.playerNumber = playerNumber;
    }
}
