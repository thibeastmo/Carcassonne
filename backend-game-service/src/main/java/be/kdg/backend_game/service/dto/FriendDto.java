package be.kdg.backend_game.service.dto;

import be.kdg.backend_game.domain.user_management.Account;
import lombok.Data;

import java.util.UUID;
@Data
public class FriendDto {
    private UUID accountId;
    private String nickname;
    private int experiencePoints;

    public FriendDto(Account account) {
        this.accountId = account.getAccountId();
        this.nickname = account.getNickName();
        this.experiencePoints = account.getExperiencePoints();
    }
}
