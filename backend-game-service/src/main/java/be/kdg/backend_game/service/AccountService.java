package be.kdg.backend_game.service;

import be.kdg.backend_game.domain.user_management.*;
import be.kdg.backend_game.domain.user_management.shop.ShopItemBuyState;
import be.kdg.backend_game.domain.user_management.shop.shop_items.AvatarItem;
import be.kdg.backend_game.domain.user_management.shop.shop_items.Theme;
import be.kdg.backend_game.domain.user_management.shop.shop_items.ThemeItem;
import be.kdg.backend_game.message.AccountMessage;
import be.kdg.backend_game.repository.AccountRepository;
import be.kdg.backend_game.sender.AccountSender;
import be.kdg.backend_game.service.dto.AccountDto;
import be.kdg.backend_game.service.dto.FriendDto;
import be.kdg.backend_game.service.dto.FriendRequestDto;
import be.kdg.backend_game.service.dto.shop.AvatarItemDto;
import be.kdg.backend_game.service.dto.statistics.LoyaltyPointsDto;
import be.kdg.backend_game.service.exception.AccountNotFoundException;
import be.kdg.backend_game.service.exception.FriendshipNotFoundException;
import be.kdg.backend_game.service.exception.InvalidFriendRequestException;
import be.kdg.backend_game.service.exception.ShopItemNotInInventoryException;
import be.kdg.backend_game.service.mail.Mailservice;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class.toString());
    private final AccountRepository accountRepository;
    private final FriendshipService friendshipService;
    private final AccountSender accountSender;
    private final Mailservice mailservice;

    public AccountService(AccountRepository accountRepository, FriendshipService friendshipService, AccountSender accountSender, Mailservice mailservice) {
        this.accountRepository = accountRepository;
        this.friendshipService = friendshipService;
        this.accountSender = accountSender;
        this.mailservice = mailservice;
    }

    public boolean doesAccountExist(Jwt jwt) {
        UUID subjectId = JwtExtractorHelper.convertjwtToSubjectId(jwt);
        return accountRepository.findBysubjectId(subjectId).isPresent();
    }

    @Transactional
    public AccountDto registerAccount(Jwt jwt) {
        UUID subjectId = JwtExtractorHelper.convertjwtToSubjectId(jwt);
        String nickName = JwtExtractorHelper.convertjwtToUsername(jwt);
        String email = JwtExtractorHelper.convertjwtToEmail(jwt);
        Account registeringAccount = new Account();
        registeringAccount.setSubjectId(subjectId);
        registeringAccount.setNickName(nickName);
        registeringAccount.setEmail(email);
        registeringAccount.setLastLoginDate(LocalDateTime.now());
        var account = accountRepository.save(registeringAccount);
        AvatarImage avatarImage = new AvatarImage(account, "/images/avatars/default.png");
        account.setAvatar(avatarImage);
        var accountWithImage = accountRepository.save(registeringAccount);
        return new AccountDto(accountWithImage);
    }

    public AccountDto updateLastloginAndUpdateForUpdatedUser(Jwt jwt) {
        UUID subjectId = JwtExtractorHelper.convertjwtToSubjectId(jwt);
        Optional<Account> accountOptional = accountRepository.findBysubjectId(subjectId);
        if (accountOptional.isPresent()) {
            Account account = compareJwtToStoredUserAndUpdate(jwt, accountOptional.get());
            account.setLastLoginDate(LocalDateTime.now());
            accountRepository.save(account);
            return new AccountDto(account);
        } else throw new AccountNotFoundException();
    }

    private Account compareJwtToStoredUserAndUpdate(Jwt jwt, Account account) {
        String nickNameJwt = JwtExtractorHelper.convertjwtToUsername(jwt);
        String emailJwt = JwtExtractorHelper.convertjwtToEmail(jwt);
        if (!nickNameJwt.equals(account.getNickName())) {
            account.setNickName(nickNameJwt);
        }
        if (!emailJwt.equals(account.getEmail())) {
            account.setEmail(emailJwt);
        }
        return account;
    }

    public void sendAllAccounts() {
        List<Account> accounts = getAllAccounts();
        List<AccountMessage> accountMessages = accounts.stream().map(this::convertToAccountMessage).collect(Collectors.toList());

        accountSender.sendAccounts(accountMessages);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    private AccountMessage convertToAccountMessage(Account account) {
        return new AccountMessage(account.getAccountId(), account.getNickName(), account.getLoyaltyPoints(), account.getExperiencePoints());
    }

    public FriendRequestDto createFriendRequestWithAccountId(UUID subjectId, UUID accountId) {
        Account requester = accountRepository.findAccountBySubjectId(subjectId).orElseThrow(() -> new AccountNotFoundException("Could not find an account for subject id " + subjectId));
        Account requested = accountRepository.findAccountByAccountId(accountId).orElseThrow(() -> new AccountNotFoundException("Could not find an account for account id " + accountId));
        return friendshipService.createFriendRequest(requester, requested);
    }

    public FriendRequestDto createFriendRequestWithUsernameOrEmail(UUID subjectId, String usernameOrEmail) {

        Account requester = accountRepository.findAccountBySubjectId(subjectId).orElseThrow(() -> new AccountNotFoundException("Could not find an account for subject id " + subjectId));
        Optional<Account> requested = accountRepository.findAccountByUsernameOrEmail(usernameOrEmail);
        if (requested.isEmpty()) {
            if (EmailValidatorHelper.isValidEmail(usernameOrEmail)) {
                //todo: fill in subdomain
                logger.info("Sending email to " + usernameOrEmail);
                mailservice.sendEmail(usernameOrEmail, "Carcassonne Friend Request", "You have received a friend request from " + requester.getNickName() + " on Carcassonne. Please login to accept or decline the request.");
                logger.info("email sent");
                return null;
            } else {
                throw new AccountNotFoundException("Could not find an account for username or email " + usernameOrEmail);
            }
        }

        if (requester.getAccountId().equals(requested.get().getAccountId()))
            throw new InvalidFriendRequestException("Requested and requester are the same account");
        return friendshipService.createFriendRequest(requester, requested.get());
    }

    public void acceptFriendRequest(UUID friendRequestId, UUID subjectId) {
        Account requested = accountRepository.findAccountBySubjectId(subjectId).orElseThrow(() -> new AccountNotFoundException("Could not find an account for subject id " + subjectId));
        friendshipService.createFriendship(requested, friendRequestId);
    }

    public void removeFriendRequest(UUID friendRequestId, UUID subjectId) {
        Account account = accountRepository.findAccountBySubjectId(subjectId).orElseThrow(() -> new AccountNotFoundException("Could not find an account for subject id " + subjectId));
        FriendRequest friendRequest = friendshipService.retrieveFriendRequestByFriendRequestId(friendRequestId).orElseThrow(() -> new InvalidFriendRequestException("Could not find a friendrequest for friendrequest id " + friendRequestId));
        if (!friendRequest.getRequested().getAccountId().equals(account.getAccountId())) {
            throw new InvalidFriendRequestException("Requester id does not match account with subject id " + subjectId);
        }
        friendshipService.removeFriendRequest(friendRequest);
    }

    public List<FriendRequestDto> retrieveFriendRequestsWithSubjectId(UUID subjectId) {
        Account account = accountRepository.findAccountBySubjectId(subjectId).orElseThrow(() -> new AccountNotFoundException("Could not find an account for subject id " + subjectId));
        return friendshipService.retrieveFriendRequestsByRequestedId(account.getAccountId());
    }

    public void removeFriendship(UUID friendId, UUID subjectId) {
        Account account = accountRepository.findAccountBySubjectId(subjectId).orElseThrow(() -> new AccountNotFoundException("Could not find an account for subject id " + subjectId));
        Account friend = accountRepository.findAccountByAccountId(friendId).orElseThrow(() -> new AccountNotFoundException("Could not find an account for account id " + friendId));
        Friendship friendship = friendshipService.retrieveFriendshipByAccountIdAndFriendId(account, friend).orElseThrow(() -> new FriendshipNotFoundException("Could not find friendship for account id " + account.getAccountId() + " and friend id " + friendId));
        friendshipService.removeFriendship(friendship);
    }

    public List<FriendDto> retrieveFriendsWithSubjectId(UUID subjectId) {
        Account account = accountRepository.findAccountBySubjectId(subjectId).orElseThrow(() -> new AccountNotFoundException("Could not find an account for subject id " + subjectId));
        List<Friendship> friendships = friendshipService.retrieveAllFriendshipsByAccount(account);
        List<FriendDto> friendDtos = new ArrayList<>();
        for (Friendship friendship : friendships) {
            if (friendship.getFriendshipId().getAccount1().getAccountId().equals(account.getAccountId())) {
                friendDtos.add(new FriendDto(friendship.getFriendshipId().getAccount2()));
            } else friendDtos.add(new FriendDto(friendship.getFriendshipId().getAccount1()));
        }
        return friendDtos;
    }

    public LoyaltyPointsDto getLoyaltyPoints(UUID subjectId) {
        Account account = accountRepository.findAccountBySubjectId(subjectId).orElseThrow(() -> new AccountNotFoundException("Could not find an account for account id " + subjectId));
        return new LoyaltyPointsDto(account.getLoyaltyPoints());
    }

    public boolean updateLoyaltyPoints(UUID accountId, int extraLoyaltyPoints) {
        try {
            var optionalAccount = accountRepository.findAccountByAccountId(accountId);
            if (optionalAccount.isEmpty()) {
                logger.error("Could not update the loyalty points for account with accountId: " + accountId + " with " + extraLoyaltyPoints + " extra LoyaltyPoints");
                return false;
            }
            var account = optionalAccount.get();
            account.setLoyaltyPoints(account.getLoyaltyPoints() + extraLoyaltyPoints);
            accountRepository.save(account);
            return true;
        } catch (Exception ex) {
            logger.error("Something went wrong while updating the loyalty points of an account: " + ex.getMessage());
        }
        return false;
    }

    public void equipItem(UUID subjectId, UUID shopItemId) {
        Account account = accountRepository.findAccountBySubjectIdWithShopItemBuyStates(subjectId).orElseThrow(() -> new AccountNotFoundException("Could not find an account for subject id " + subjectId));
        Optional<ShopItemBuyState> shopItemBuyStateOptional = account.getShopItemBuyStates().stream().filter(shopItemBuyState -> shopItemBuyState.getShopItem().getShopItemId().equals(shopItemId)).findFirst();

        if (shopItemBuyStateOptional.isPresent()) {
            var shopItem = shopItemBuyStateOptional.get().getShopItem();
            if (shopItem instanceof AvatarItem avatarShopItem) {
                account.setAvatar(new AvatarImage(account, avatarShopItem.getAvatarItemUrl()));
                accountRepository.save(account);
            } else if (shopItem instanceof ThemeItem themeItem) {
                account.setTheme(themeItem.getTheme());
                accountRepository.save(account);
            }
        } else throw new ShopItemNotInInventoryException("The shop item was not in your inventory!");
    }

    public List<Theme> getAllBoughtThemes(UUID subjectId) {
        Account account = accountRepository.findAccountBySubjectIdWithShopItemBuyStates(subjectId).orElseThrow(() -> new AccountNotFoundException("Could not find an account for subject id " + subjectId));
        List<Theme> themeItems = new ArrayList<>();
        List<ShopItemBuyState> shopItems = account.getShopItemBuyStates();
        for (var item : shopItems) {
            if (item.getShopItem() instanceof ThemeItem themeItem) {
                themeItems.add(themeItem.getTheme());
            }
        }
        return themeItems;
    }

    @Transactional
    public List<AvatarItemDto> getAllBoughtAvatars(UUID subjectId) {
        Account account = accountRepository.findAccountBySubjectIdWithShopItemBuyStates(subjectId).orElseThrow(() -> new AccountNotFoundException("Could not find an account for subject id " + subjectId));

        List<AvatarItemDto> avatarItems = new ArrayList<>();
        List<ShopItemBuyState> shopItems = account.getShopItemBuyStates();

        for (ShopItemBuyState item : shopItems) {
            if (item.getShopItem() instanceof AvatarItem avatarItem) {
                var avatarDto = new AvatarItemDto(avatarItem.getShopItemId(), avatarItem.getName(), avatarItem.getPrice(), avatarItem.getShopCategory(), avatarItem.getAvatarItemUrl());
                avatarItems.add(avatarDto);
            }
        }

        return avatarItems;
    }

    public void setThemeOfUser(UUID subjectId, Theme theme) {
        Account account = accountRepository.findAccountBySubjectIdWithShopItemBuyStates(subjectId).orElseThrow(() -> new AccountNotFoundException("Could not find an account for subject id " + subjectId));
        account.setTheme(theme);
        accountRepository.save(account);
    }

    public Theme getThemeOfAccount(UUID subjectId) {
        Account account = accountRepository.findAccountBySubjectIdWithShopItemBuyStates(subjectId).orElseThrow(() -> new AccountNotFoundException("Could not find an account for subject id " + subjectId));
        return account.getTheme();
    }

    public List<RankedAccount> retrieveLeaderboardBySubjectId(UUID subjectId) {
        List<Account> topAccounts = accountRepository.findTopAccountsByExperiencePoints();
        List<RankedAccount> rankedAccounts = new ArrayList<>();
        for (int i = 0; i < topAccounts.size(); i++) {
            rankedAccounts.add(new RankedAccount(topAccounts.get(i), i+1));
        }
        if (topAccounts.stream().noneMatch(a -> a.getSubjectId().equals(subjectId))) {
            Optional<Account> optionalAccount = accountRepository.findAccountBySubjectId(subjectId);
            Optional<Integer> optionalRank = accountRepository.findRankBySubjectId(subjectId);
            if (optionalAccount.isPresent() && optionalRank.isPresent()) {
                rankedAccounts.add(new RankedAccount(optionalAccount.get(), optionalRank.get()));
            }
        }
        return rankedAccounts;
    }
}
