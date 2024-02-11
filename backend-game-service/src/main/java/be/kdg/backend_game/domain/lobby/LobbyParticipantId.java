package be.kdg.backend_game.domain.lobby;

import be.kdg.backend_game.domain.user_management.Account;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class LobbyParticipantId implements Serializable {
    @ManyToOne
    private Lobby lobby;
    @ManyToOne
    private Account account;
    public LobbyParticipantId() {
    }
}
