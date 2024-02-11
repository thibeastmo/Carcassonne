package be.kdg.backend_game.service.shop;


import be.kdg.backend_game.domain.user_management.Account;
import be.kdg.backend_game.domain.user_management.AvatarImage;
import be.kdg.backend_game.domain.user_management.shop.ShopCategory;
import be.kdg.backend_game.domain.user_management.shop.ShopItem;
import be.kdg.backend_game.domain.user_management.shop.ShopItemBuyState;
import be.kdg.backend_game.domain.user_management.shop.shop_items.AvatarItem;
import be.kdg.backend_game.domain.user_management.shop.shop_items.Theme;
import be.kdg.backend_game.domain.user_management.shop.shop_items.ThemeItem;
import be.kdg.backend_game.repository.AccountRepository;
import be.kdg.backend_game.repository.ShopRepository;
import be.kdg.backend_game.service.dto.shop.AvatarItemDto;
import be.kdg.backend_game.service.dto.shop.ThemeItemDto;
import be.kdg.backend_game.service.exception.AccountNotFoundException;
import be.kdg.backend_game.service.exception.NotEnoughPointsException;
import be.kdg.backend_game.service.exception.ShopItemNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ShopService {
    private final ShopRepository shopRepository;
    private final AccountRepository accountRepository;


    public ShopService(ShopRepository shopRepository, AccountRepository accountRepository) {
        this.shopRepository = shopRepository;
        this.accountRepository = accountRepository;
        seedShopItems();
    }


    public List<AvatarItemDto> getAllPurchasableAvatarShopItems(UUID subjectId) {
        Account account = accountRepository.findAccountBySubjectIdWithShopItemBuyStates(subjectId)
                .orElseThrow(() -> new AccountNotFoundException("Could not find an account for subject id " + subjectId));

        List<AvatarItem> avatarItems = shopRepository.findAllAvatarItems();
        List<AvatarItemDto> avatarItemDtos = new ArrayList<>();
        for (var item : avatarItems) {
            if (account.getShopItemBuyStates()
                    .stream()
                    .noneMatch(shopItemBuyState -> {
                        return shopItemBuyState.getShopItem().getShopItemId().equals(item.getShopItemId());
                    }))
                avatarItemDtos.add(new AvatarItemDto(item));
        }
        return avatarItemDtos;
    }

    public List<ThemeItemDto> getAllPurchasableThemeShopItems(UUID subjectId) {
        Account account = accountRepository.findAccountBySubjectIdWithShopItemBuyStates(subjectId)
                .orElseThrow(() -> new AccountNotFoundException("Could not find an account for subject id " + subjectId));

        List<ThemeItem> themeItems = shopRepository.findAllThemeItems();
        List<ThemeItemDto> themeItemDtos = new ArrayList<>();
        for (var item : themeItems) {
            if (account.getShopItemBuyStates()
                    .stream()
                    .noneMatch(shopItemBuyState -> {
                        return shopItemBuyState.getShopItem().getShopItemId().equals(item.getShopItemId());
                    }))
                themeItemDtos.add(new ThemeItemDto(item));
        }
        return themeItemDtos;
    }




    public ShopItem buyItem(UUID subjectId, UUID shopItemId) {
        ShopItem shopItem = shopRepository.findById(shopItemId).orElseThrow(() -> new ShopItemNotFoundException("Could not find a shop item for item id " + shopItemId));
        Account account = accountRepository.findAccountBySubjectIdWithShopItemBuyStates(subjectId)
                .orElseThrow(() -> new AccountNotFoundException("Could not find an account for subject id " + subjectId));


        int loyaltyPoints = account.getLoyaltyPoints();
        int price = shopItem.getPrice();

        if (loyaltyPoints >= price) {
            ShopItemBuyState shopItemBuyState = new ShopItemBuyState(shopItem, account);
            account.addShopItemBuyState(shopItemBuyState);
            account.setLoyaltyPoints(loyaltyPoints - price);
            accountRepository.save(account);
            return shopItem;
        } else {
            throw new NotEnoughPointsException("Je hebt niet genoeg loyaliteitspunten!");
        }
    }

    @Transactional
    public void seedShopItems() {
        shopRepository.deleteAll();
        shopRepository.save(new AvatarItem("Avatar 1", 20, ShopCategory.AVATAR, "/images/avatars/1.png"));
        shopRepository.save(new AvatarItem("Avatar 2", 50, ShopCategory.AVATAR, "/images/avatars/2.png"));
        shopRepository.save(new AvatarItem("Avatar 3", 60, ShopCategory.AVATAR, "/images/avatars/3.png"));
        shopRepository.save(new AvatarItem("Avatar 4", 20, ShopCategory.AVATAR, "/images/avatars/4.png"));
        shopRepository.save(new AvatarItem("Avatar 5", 20, ShopCategory.AVATAR, "/images/avatars/5.png"));
        shopRepository.save(new AvatarItem("Avatar 6", 20, ShopCategory.AVATAR, "/images/avatars/6.png"));
        shopRepository.save(new AvatarItem("Avatar 7", 20, ShopCategory.AVATAR, "/images/avatars/7.png"));
        shopRepository.save(new AvatarItem("Avatar 8", 20, ShopCategory.AVATAR, "/images/avatars/8.png"));
        shopRepository.save(new AvatarItem("Avatar 9", 20, ShopCategory.AVATAR, "/images/avatars/9.png"));
        shopRepository.save(new AvatarItem("Avatar 10", 20, ShopCategory.AVATAR, "/images/avatars/10.png"));
        shopRepository.save(new AvatarItem("Avatar 11", 20, ShopCategory.AVATAR, "/images/avatars/11.png"));
        shopRepository.save(new AvatarItem("Avatar 12", 20, ShopCategory.AVATAR, "/images/avatars/12.png"));
        shopRepository.save(new AvatarItem("Avatar 13", 20, ShopCategory.AVATAR, "/images/avatars/13.png"));
        shopRepository.save(new AvatarItem("Avatar 14", 20, ShopCategory.AVATAR, "/images/avatars/14.png"));
        shopRepository.save(new AvatarItem("Avatar 15", 20, ShopCategory.AVATAR, "/images/avatars/15.png"));
        shopRepository.save(new AvatarItem("Avatar 16", 20, ShopCategory.AVATAR, "/images/avatars/16.png"));
        shopRepository.save(new AvatarItem("Avatar 17", 20, ShopCategory.AVATAR, "/images/avatars/17.png"));
        shopRepository.save(new AvatarItem("Avatar 18", 20, ShopCategory.AVATAR, "/images/avatars/18.png"));
        shopRepository.save(new AvatarItem("Avatar 19", 20, ShopCategory.AVATAR, "/images/avatars/19.png"));
        shopRepository.save(new AvatarItem("Avatar 20", 20, ShopCategory.AVATAR, "/images/avatars/20.png"));
        shopRepository.save(new AvatarItem("Avatar 21", 20, ShopCategory.AVATAR, "/images/avatars/21.png"));
        shopRepository.save(new AvatarItem("Avatar 22", 20, ShopCategory.AVATAR, "/images/avatars/22.png"));
        shopRepository.save(new AvatarItem("Avatar 23", 20, ShopCategory.AVATAR, "/images/avatars/23.png"));
        shopRepository.save(new AvatarItem("Avatar 24", 20, ShopCategory.AVATAR, "/images/avatars/24.png"));
        shopRepository.save(new AvatarItem("Avatar 25", 20, ShopCategory.AVATAR, "/images/avatars/25.png"));
        shopRepository.save(new AvatarItem("Avatar 26", 20, ShopCategory.AVATAR, "/images/avatars/26.png"));
        shopRepository.save(new AvatarItem("Avatar 27", 20, ShopCategory.AVATAR, "/images/avatars/27.png"));
        shopRepository.save(new AvatarItem("Avatar 28", 20, ShopCategory.AVATAR, "/images/avatars/28.png"));
        shopRepository.save(new AvatarItem("Avatar 29", 20, ShopCategory.AVATAR, "/images/avatars/29.png"));
        shopRepository.save(new AvatarItem("Avatar 30", 20, ShopCategory.AVATAR, "/images/avatars/30.png"));
        shopRepository.save(new AvatarItem("Avatar 31", 20, ShopCategory.AVATAR, "/images/avatars/31.png"));
        shopRepository.save(new AvatarItem("Avatar 32", 20, ShopCategory.AVATAR, "/images/avatars/32.png"));
        shopRepository.save(new AvatarItem("Avatar 33", 20, ShopCategory.AVATAR, "/images/avatars/33.png"));
        shopRepository.save(new AvatarItem("Avatar 34", 20, ShopCategory.AVATAR, "/images/avatars/34.png"));
        shopRepository.save(new AvatarItem("Avatar 35", 20, ShopCategory.AVATAR, "/images/avatars/35.png"));
        shopRepository.save(new AvatarItem("Avatar 36", 20, ShopCategory.AVATAR, "/images/avatars/36.png"));
        shopRepository.save(new AvatarItem("Avatar 37", 20, ShopCategory.AVATAR, "/images/avatars/37.png"));
        shopRepository.save(new AvatarItem("Avatar 38", 20, ShopCategory.AVATAR, "/images/avatars/38.png"));
        shopRepository.save(new AvatarItem("Avatar 39", 20, ShopCategory.AVATAR, "/images/avatars/39.png"));
        shopRepository.save(new AvatarItem("Avatar 40", 20, ShopCategory.AVATAR, "/images/avatars/40.png"));
        shopRepository.save(new AvatarItem("Avatar 41", 20, ShopCategory.AVATAR, "/images/avatars/41.png"));
        shopRepository.save(new AvatarItem("Avatar 42", 20, ShopCategory.AVATAR, "/images/avatars/42.png"));
        shopRepository.save(new AvatarItem("Avatar 43", 20, ShopCategory.AVATAR, "/images/avatars/43.png"));
        shopRepository.save(new AvatarItem("Avatar 44", 20, ShopCategory.AVATAR, "/images/avatars/44.png"));
        shopRepository.save(new AvatarItem("Avatar 45", 20, ShopCategory.AVATAR, "/images/avatars/45.png"));
        shopRepository.save(new AvatarItem("Avatar 46", 20, ShopCategory.AVATAR, "/images/avatars/46.png"));
        shopRepository.save(new AvatarItem("Avatar 47", 20, ShopCategory.AVATAR, "/images/avatars/47.png"));

        shopRepository.save(new ThemeItem("Winter theme", 90, ShopCategory.THEME, Theme.WINTER, "/images/theme/winter_theme.png"));
    }
}
