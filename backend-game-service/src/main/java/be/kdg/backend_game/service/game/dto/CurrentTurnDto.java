package be.kdg.backend_game.service.game.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Getter
public class CurrentTurnDto {
    private final long beginTurn;
    private final int playerNumber;

    public CurrentTurnDto(LocalDateTime beginTurn, int playerNumber) {
        this.beginTurn = beginTurn.toInstant(ZoneOffset.UTC).toEpochMilli() ;
        this.playerNumber = playerNumber;
    }
}
