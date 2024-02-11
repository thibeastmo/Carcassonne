package be.kdg.backend_game.service.dto;

import be.kdg.backend_game.domain.GameTypeEnum;
import be.kdg.backend_game.domain.TimeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;
@Getter
@AllArgsConstructor
public class NotificationDto {
    private String lobbyName;
    private UUID gameId;
    private GameTypeEnum gameType;
    private int timePassed;
    private TimeEnum timeEnum;
    private List<String> nicknames;
}
