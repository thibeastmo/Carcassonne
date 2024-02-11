package be.kdg.backend_game.domain.user_management;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Friendship {
    @EmbeddedId
    private FriendshipId friendshipId;
    private LocalDateTime friendedOn;

    public Friendship(Account account1, Account account2) {
        this.friendshipId = new FriendshipId(account1,account2);
        this.friendedOn = LocalDateTime.now();
    }

    public Friendship() {
    }
}
