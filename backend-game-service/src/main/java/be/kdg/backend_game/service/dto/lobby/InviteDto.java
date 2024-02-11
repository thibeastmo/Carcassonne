package be.kdg.backend_game.service.dto.lobby;

import be.kdg.backend_game.domain.GameTypeEnum;
import be.kdg.backend_game.domain.lobby.Invite;
import lombok.Data;

import java.util.UUID;
@Data
public class InviteDto {
    private UUID inviteId;
    private String nickname;
    private String lobbyName;
    private GameTypeEnum gameTypeEnum;
    private int maxPlayers;

    public InviteDto(Invite invite) {
        this.inviteId = invite.getInviteId();
        this.nickname = invite.getInviter().getNickName();
        this.lobbyName = invite.getLobby().getLobbyName();
        this.gameTypeEnum = invite.getLobby().getGameTypeEnum();
        this.maxPlayers = invite.getLobby().getMaxPlayers();
    }

}
