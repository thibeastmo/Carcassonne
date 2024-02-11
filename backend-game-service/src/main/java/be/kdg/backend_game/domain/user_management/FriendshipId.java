package be.kdg.backend_game.domain.user_management;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class FriendshipId implements Serializable {
    @ManyToOne
    private Account account1;

    @ManyToOne
    private Account account2;

    public FriendshipId() {
    }

    public FriendshipId(Account account1, Account account2) {
        this.account1 = account1;
        this.account2 = account2;
    }
}
