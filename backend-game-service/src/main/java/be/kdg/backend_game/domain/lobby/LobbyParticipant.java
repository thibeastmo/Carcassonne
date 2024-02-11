package be.kdg.backend_game.domain.lobby;

import be.kdg.backend_game.domain.user_management.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class LobbyParticipant {
    @EmbeddedId
    private LobbyParticipantId lobbyParticipantId;
    public LobbyParticipant() {
        this.lobbyParticipantId = new LobbyParticipantId();
    }

    public LobbyParticipant(Lobby lobby, Account account) {
        this.lobby = lobby;
        this.account = account;
        this.lobbyParticipantId = new LobbyParticipantId();
        this.lobbyParticipantId.setLobby(lobby);
        this.lobbyParticipantId.setAccount(account);
    }

    @ManyToOne
    @MapsId("lobbyId")
    private Lobby lobby;
    @ManyToOne
    @MapsId("accountId")
    private Account account;
}
