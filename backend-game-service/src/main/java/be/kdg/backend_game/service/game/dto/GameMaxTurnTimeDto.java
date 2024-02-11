package be.kdg.backend_game.service.game.dto;

import lombok.Getter;

@Getter
public class GameMaxTurnTimeDto {
    private final int maxTurnDurationInMinutes;

    public GameMaxTurnTimeDto(int maxTurnDurationInMinutes) {
        this.maxTurnDurationInMinutes = maxTurnDurationInMinutes;
    }
}
