package be.kdg.backend_game.service.dto.statistics;

import be.kdg.backend_game.domain.user_management.Account;
import be.kdg.backend_game.domain.user_management.RankedAccount;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class LeaderboardDto {
    private final List<RankDto> ranks;
    public LeaderboardDto(List<RankedAccount> leaderboardAccounts) {
        ranks = new ArrayList<>();
        for (RankedAccount leaderboardAccount : leaderboardAccounts) {
            ranks.add(new RankDto(leaderboardAccount));
        }
    }
}
