package be.kdg.backend_game.service.dto.lobby;

import be.kdg.backend_game.domain.GameTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewLobbyDto {
    private String lobbyName;
    private int maxPlayers;
    private GameTypeEnum gameTypeEnum;
    public NewLobbyDto() {
    }
}
