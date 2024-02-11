package be.kdg.backend_game.service.game.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class PlayerSerfDto {
    private int zoneId;
    private int x;
    private int y;

    public PlayerSerfDto() {}

    public PlayerSerfDto(int zoneId, int x, int y) {
        this.zoneId = zoneId;
        this.x = x;
        this.y = y;
    }
}
