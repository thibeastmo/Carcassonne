package be.kdg.backend_game.domain.user_management;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID friendRequestId;
    @ManyToOne
    private Account requester;
    @ManyToOne
    private Account requested;

    public FriendRequest(Account requester, Account requested) {
        this.requester = requester;
        this.requested = requested;
    }

    public FriendRequest() {
    }
}
