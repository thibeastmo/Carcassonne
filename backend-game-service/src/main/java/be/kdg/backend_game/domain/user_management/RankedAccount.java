package be.kdg.backend_game.domain.user_management;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RankedAccount extends Account {
    private int rank;

    public RankedAccount() {
        super();
        this.rank = -1;
    }

    public RankedAccount(Account account, int rank) {
        super(account.getAccountId(), account.getSubjectId(), account.getNickName(),
                account.getEmail(), account.getLoyaltyPoints(), account.getExperiencePoints());
        this.rank = rank;
    }
}
