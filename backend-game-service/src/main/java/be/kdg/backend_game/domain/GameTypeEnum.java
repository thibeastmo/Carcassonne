package be.kdg.backend_game.domain;

import lombok.Getter;

@Getter
public enum GameTypeEnum {
    SHORT(2),LONG(40);
    final int turnDuration;

    GameTypeEnum(int turnDuration) {
        this.turnDuration = turnDuration;
    }
}
