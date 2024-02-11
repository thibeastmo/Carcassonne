package be.kdg.backend_game.service.dto;

import be.kdg.backend_game.domain.user_management.FriendRequest;
import lombok.Data;

import java.util.UUID;
@Data
public class FriendRequestDto {

    private UUID friendRequestId;
    private String nickname;

    public FriendRequestDto(FriendRequest friendRequest) {
        this.friendRequestId = friendRequest.getFriendRequestId();
        this.nickname = friendRequest.getRequester().getNickName();
    }
}
