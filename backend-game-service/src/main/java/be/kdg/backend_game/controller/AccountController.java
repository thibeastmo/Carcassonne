package be.kdg.backend_game.controller;

import be.kdg.backend_game.domain.user_management.shop.shop_items.Theme;
import be.kdg.backend_game.service.AccountService;
import be.kdg.backend_game.service.JwtExtractorHelper;
import be.kdg.backend_game.service.dto.FriendDto;
import be.kdg.backend_game.service.dto.FriendRequestDto;
import be.kdg.backend_game.service.dto.NotificationsDto;
import be.kdg.backend_game.service.dto.lobby.InviteDto;
import be.kdg.backend_game.service.dto.statistics.LeaderboardDto;
import be.kdg.backend_game.service.dto.statistics.LoyaltyPointsDto;
import be.kdg.backend_game.service.dto.statistics.UserStatisticsDto;
import be.kdg.backend_game.service.exception.AccountNotFoundException;
import be.kdg.backend_game.service.exception.FriendshipNotFoundException;
import be.kdg.backend_game.service.exception.InvalidFriendRequestException;
import be.kdg.backend_game.service.exception.ShopItemNotInInventoryException;
import be.kdg.backend_game.service.game.GameService;
import be.kdg.backend_game.service.game.InviteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    private final AccountService accountService;
    private final InviteService inviteService;
    private final GameService gameService;
    private final Logger logger = LoggerFactory.getLogger(AccountController.class.toString());

    public AccountController(AccountService accountService, InviteService inviteService, GameService gameService) {
        this.accountService = accountService;
        this.inviteService = inviteService;
        this.gameService = gameService;
    }

    @PostMapping("/sendAll")
    @PreAuthorize("hasAuthority('player')")
    public @ResponseBody String sendAllAccounts() {
        accountService.sendAllAccounts();
        return "All accounts sent successfully!";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> registerAccountOrUpdateLastLogin(@AuthenticationPrincipal Jwt jwt) {
        logger.info("Registering account or updating last login");
        if (!accountService.doesAccountExist(jwt)) {
            var accountDto = accountService.registerAccount(jwt);
            return ResponseEntity.status(HttpStatus.CREATED).body(accountDto);
        } else {
            var accountDto = accountService.updateLastloginAndUpdateForUpdatedUser(jwt);
            return ResponseEntity.status(HttpStatus.OK).body(accountDto);
        }
    }

    @GetMapping("/get-invites")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> getInvites(@AuthenticationPrincipal Jwt token) {
        try {
            UUID subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
            List<InviteDto> invites = inviteService.retrieveInvitesWithSubjectId(subjectId);
            return ResponseEntity.status(HttpStatus.OK).body(invites);
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-friend-requests")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> getFriendRequests(@AuthenticationPrincipal Jwt token) {
        try {
            UUID subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
            List<FriendRequestDto> friendRequests = accountService.retrieveFriendRequestsWithSubjectId(subjectId);
            return ResponseEntity.status(HttpStatus.OK).body(friendRequests);
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-friends")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> getFriends(@AuthenticationPrincipal Jwt token) {
        try {
            UUID subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
            List<FriendDto> friends = accountService.retrieveFriendsWithSubjectId(subjectId);
            return ResponseEntity.status(HttpStatus.OK).body(friends);
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/send-friend-request-with-account-id")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> sendFriendRequestWithAccountId(@AuthenticationPrincipal Jwt token, @RequestParam UUID accountId) {
        try {
            UUID subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
            FriendRequestDto friendRequest = accountService.createFriendRequestWithAccountId(subjectId, accountId);
            return ResponseEntity.status(HttpStatus.CREATED).body(friendRequest);
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidFriendRequestException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/send-friend-request-with-username-or-email")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> sendFriendRequestWithUsernameOrEmail(@AuthenticationPrincipal Jwt token, @RequestParam String usernameOrEmail) {
        try {
            UUID subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
            FriendRequestDto friendRequest = accountService.createFriendRequestWithUsernameOrEmail(subjectId, usernameOrEmail);
            return ResponseEntity.status(HttpStatus.CREATED).body(friendRequest);
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidFriendRequestException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/accept-friend-request")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> acceptFriendRequest(@AuthenticationPrincipal Jwt token, @RequestParam UUID friendRequestId) {
        try {
            UUID subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
            accountService.acceptFriendRequest(friendRequestId, subjectId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidFriendRequestException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-friend-request")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> deleteFriendRequest(@AuthenticationPrincipal Jwt token, @RequestParam UUID friendRequestId) {
        try {
            UUID subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
            accountService.removeFriendRequest(friendRequestId, subjectId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidFriendRequestException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-friendship")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> deleteFriendship(@AuthenticationPrincipal Jwt token, @RequestParam UUID friendId) {
        try {
            UUID subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
            accountService.removeFriendship(friendId, subjectId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (AccountNotFoundException | FriendshipNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/notifications")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> notifications(@AuthenticationPrincipal Jwt token) {
        try {
            UUID subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
            NotificationsDto notifications = gameService.getNotifications(subjectId);
            return ResponseEntity.status(HttpStatus.OK).body(notifications);
        } catch (AccountNotFoundException | FriendshipNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/loyaltypoints")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> loyaltyPoints(@AuthenticationPrincipal Jwt token) {
        try {
            UUID subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
            LoyaltyPointsDto loyaltyPoints = accountService.getLoyaltyPoints(subjectId);
            return ResponseEntity.status(HttpStatus.OK).body(loyaltyPoints);
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/equip-shop-item")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> equipAvatar(@AuthenticationPrincipal Jwt token, @RequestParam UUID shopItemId) {
        var subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
        try {
            accountService.equipItem(subjectId, shopItemId);
            return ResponseEntity.ok("Equipped item");
        } catch (ShopItemNotInInventoryException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/bought-themes")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> getBoughtThemes(@AuthenticationPrincipal Jwt token) {
        var subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
        try {
            var themes = accountService.getAllBoughtThemes(subjectId);
            return ResponseEntity.ok(themes);
        } catch (AccountNotFoundException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/bought-avatars")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> getBoughtAvatars(@AuthenticationPrincipal Jwt token) {
        var subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
        try {
            var avatars = accountService.getAllBoughtAvatars(subjectId);
            return ResponseEntity.ok(avatars);
        } catch (AccountNotFoundException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PatchMapping("/set-theme")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> setThemeOfUser(@AuthenticationPrincipal Jwt token, @RequestParam String theme) {
        var subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
        try {
            accountService.setThemeOfUser(subjectId, Theme.valueOf(theme));
            return ResponseEntity.ok("Succesfully set theme to " + theme);
        } catch (AccountNotFoundException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/selected-theme")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> getSelectedThemeOfUser(@AuthenticationPrincipal Jwt token) {
        var subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
        try {
            var theme = accountService.getThemeOfAccount(subjectId);
            return ResponseEntity.ok(theme);
        } catch (AccountNotFoundException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @GetMapping("/leaderboard")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> getLeaderboardForPlayer(@AuthenticationPrincipal Jwt token) {
        try {
            var subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
            var optionalLeaderboardAccounts = accountService.retrieveLeaderboardBySubjectId(subjectId);
            if (optionalLeaderboardAccounts.isEmpty()) return ResponseEntity.noContent().build();
            return ResponseEntity.ok(new LeaderboardDto(optionalLeaderboardAccounts));
        }
        catch (Exception ex){
            logger.error("Could not return leaderboard by subjectId (" + token.getId() + ") because: " + ex.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
