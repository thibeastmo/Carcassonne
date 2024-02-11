package be.kdg.backend_game.service.dto.lobby;

import be.kdg.backend_game.domain.user_management.AvatarImage;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class AccountLobbyInfoDto implements Serializable {
    private UUID accountId;
    private String nickname;
    private int loyaltyPoints;
    private int experiencePoints;
    private AvatarImage avatar;
    private boolean isHost;

    public AccountLobbyInfoDto() {
    }

    public AccountLobbyInfoDto(UUID accountId, String nickname, int loyaltyPoints, int experiencePoints, AvatarImage avatar, boolean isHost) {
        this.accountId = accountId;
        this.nickname = nickname;
        this.loyaltyPoints = loyaltyPoints;
        this.experiencePoints = experiencePoints;
        this.avatar = avatar;
        this.isHost = isHost;
    }
}
