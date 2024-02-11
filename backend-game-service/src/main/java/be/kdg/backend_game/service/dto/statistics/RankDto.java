package be.kdg.backend_game.service.dto.statistics;

import be.kdg.backend_game.domain.user_management.Account;
import be.kdg.backend_game.domain.user_management.RankedAccount;
import lombok.Getter;

@Getter
public class RankDto {
    private final int rank;
    private final long experiencePoints;
    private final String nickname;

    public RankDto(RankedAccount rankedAccount) {
        this.rank = rankedAccount.getRank();
        this.experiencePoints = rankedAccount.getExperiencePoints();
        this.nickname = rankedAccount.getNickName();
    }
}
