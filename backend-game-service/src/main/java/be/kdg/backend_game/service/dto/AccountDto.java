package be.kdg.backend_game.service.dto;


// TODO: check if folder UserManagement is user_management (folder should be lowercase)
import be.kdg.backend_game.domain.user_management.Account;
import be.kdg.backend_game.domain.user_management.AvatarImage;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AccountDto {
    private UUID subjectId;
    private String nickname;
    private AvatarImage avatar;

    public AccountDto() {
    }

    public AccountDto(Account account) {
        this.subjectId = account.getSubjectId();
        this.nickname = account.getNickName();
        this.avatar = account.getAvatar();
    }
}
