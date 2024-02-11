package be.kdg.backend_game.service.dto.lobby;

import be.kdg.backend_game.domain.GameTypeEnum;
import be.kdg.backend_game.domain.lobby.Lobby;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class LobbyInfoDto implements Serializable {
    private UUID lobbyId;
    private String lobbyName;
    private int maxPlayers;
    private GameTypeEnum gameTypeEnum;
    private AccountLobbyInfoDto[] lobbyParticipants;
    private boolean isJoined;
    private boolean isHost;
    private UUID gameId;

    public LobbyInfoDto() {
    }

    public void setLobby(Lobby lobby){
        lobbyName = lobby.getLobbyName();
        maxPlayers = lobby.getMaxPlayers();
        gameTypeEnum = lobby.getGameTypeEnum();
        lobbyId = lobby.getLobbyId();
        lobbyParticipants = lobby.getLobbyParticipants()
                .stream()
                .map(lobbyParticipant -> {
                    boolean isHost = (lobbyParticipant.getAccount().getAccountId().equals(lobby.getOwner().getAccountId()));
                    return new AccountLobbyInfoDto(
                            lobbyParticipant.getAccount().getAccountId(),
                            lobbyParticipant.getAccount().getNickName(),
                            lobbyParticipant.getAccount().getLoyaltyPoints(),
                            lobbyParticipant.getAccount().getExperiencePoints(),
                            lobbyParticipant.getAccount().getAvatar(),
                            isHost
                    );
                })
                .toArray(AccountLobbyInfoDto[]::new);
    }
}
