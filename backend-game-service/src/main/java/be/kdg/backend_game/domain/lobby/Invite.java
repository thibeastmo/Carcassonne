package be.kdg.backend_game.domain.lobby;

import be.kdg.backend_game.domain.user_management.Account;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Invite {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID inviteId;
    @ManyToOne
    private Lobby lobby;
    @ManyToOne
    private Account invitee;
    @ManyToOne
    private Account inviter;

    public Invite(Lobby lobby, Account invitee,Account inviter) {
        this.lobby = lobby;
        this.invitee = invitee;
        this.inviter = inviter;
    }

    public Invite() {

    }
}
