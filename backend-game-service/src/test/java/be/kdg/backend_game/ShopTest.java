package be.kdg.backend_game;


import be.kdg.backend_game.domain.user_management.Account;
import be.kdg.backend_game.domain.user_management.shop.ShopCategory;
import be.kdg.backend_game.domain.user_management.shop.ShopItem;
import be.kdg.backend_game.domain.user_management.shop.shop_items.AvatarItem;
import be.kdg.backend_game.domain.user_management.shop.shop_items.Theme;
import be.kdg.backend_game.domain.user_management.shop.shop_items.ThemeItem;
import be.kdg.backend_game.repository.AccountRepository;
import be.kdg.backend_game.repository.ShopRepository;
import be.kdg.backend_game.service.exception.AccountNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ShopTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ShopRepository shopRepository;


    ShopItem shopItemAvatar = null;
    ThemeItem shopItemTheme = null;
    Account account = null;
    @BeforeEach
    public void initBeforeEach() {
        Account newAccount = new Account(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "TestUser",
                "test@mail.com",
                1000,
                0
        );
        account = accountRepository.save(newAccount);
        shopItemAvatar = shopRepository.save(new AvatarItem("test avatar", 20, ShopCategory.AVATAR, "/test/item"));
        shopItemTheme = shopRepository.save(new ThemeItem("Test theme", 50, ShopCategory.THEME, Theme.WINTER, "/test/theme/item"));
    }

     @AfterEach
     public void finishTest() {
        shopRepository.delete(shopItemAvatar);
        accountRepository.delete(account);
     }

    @Test
    public void testBuyShopItemWithEnoughLoyaltyPointsShouldReturnSuccess() throws Exception {
        int originalLoyaltyPoints = account.getLoyaltyPoints();
        mockMvc.perform(post(String.format("/api/shop/buy-shop-item?shopItemId=%s", shopItemAvatar.getShopItemId()))
                .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                        .jwt(jwt -> jwt
                                .claim(StandardClaimNames.SUB, account.getSubjectId())
                                .claim(StandardClaimNames.GIVEN_NAME, "Test")
                                .claim(StandardClaimNames.FAMILY_NAME, "Icle")
                                .claim(StandardClaimNames.EMAIL, "testuser@gmail.com"))))
                .andExpect(status().isOk());
        var updatedAccount = accountRepository.findAccountBySubjectId(account.getSubjectId());
        if (updatedAccount.isEmpty()) fail("Could not retrieve account");
        assertEquals(originalLoyaltyPoints- shopItemAvatar.getPrice(), updatedAccount.get().getLoyaltyPoints());
    }

    @Test
    public void testBuyShopItemWithoutEnoughLoyaltyPointsShouldReturnError() throws Exception {
        account.setLoyaltyPoints(0);
        account = accountRepository.save(account);
        mockMvc.perform(post(String.format("/api/shop/buy-shop-item?shopItemId=%s", shopItemAvatar.getShopItemId()))
                        .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                .jwt(jwt -> jwt
                                        .claim(StandardClaimNames.SUB, account.getSubjectId())
                                        .claim(StandardClaimNames.GIVEN_NAME, "Test")
                                        .claim(StandardClaimNames.FAMILY_NAME, "User")
                                        .claim(StandardClaimNames.EMAIL, "testuser@gmail.com"))))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testBuyAvatarItemInShopAndEquipItShouldReturnSuccess() throws Exception {
        mockMvc.perform(post(String.format("/api/shop/buy-shop-item?shopItemId=%s", shopItemAvatar.getShopItemId()))
                        .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                .jwt(jwt -> jwt
                                        .claim(StandardClaimNames.SUB, account.getSubjectId())
                                        .claim(StandardClaimNames.GIVEN_NAME, "Test")
                                        .claim(StandardClaimNames.FAMILY_NAME, "User")
                                        .claim(StandardClaimNames.EMAIL, "testuser@gmail.com"))))
                .andExpect(status().isOk());

        mockMvc.perform(patch(String.format("/api/account/equip-shop-item?shopItemId=%s", shopItemAvatar.getShopItemId()))
                .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                        .jwt(jwt -> jwt
                                .claim(StandardClaimNames.SUB, account.getSubjectId())
                                .claim(StandardClaimNames.GIVEN_NAME, "Test")
                                .claim(StandardClaimNames.FAMILY_NAME, "User")
                                .claim(StandardClaimNames.EMAIL, "testuser@gmail.com"))))
                .andExpect(status().isOk());
    }

    @Test
    public void testEquipAvatarItemThatIsNotYetBoughtShouldReturnError() throws Exception {
        mockMvc.perform(patch(String.format("/api/account/equip-shop-item?shopItemId=%s", shopItemAvatar.getShopItemId()))
                        .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                .jwt(jwt -> jwt
                                        .claim(StandardClaimNames.SUB, account.getSubjectId())
                                        .claim(StandardClaimNames.GIVEN_NAME, "Test")
                                        .claim(StandardClaimNames.FAMILY_NAME, "User")
                                        .claim(StandardClaimNames.EMAIL, "testuser@gmail.com"))))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testBuyThemeAndEquipThemeShouldReturnSuccess() throws Exception {
        mockMvc.perform(post(String.format("/api/shop/buy-shop-item?shopItemId=%s", shopItemTheme.getShopItemId()))
                        .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                .jwt(jwt -> jwt
                                        .claim(StandardClaimNames.SUB, account.getSubjectId())
                                        .claim(StandardClaimNames.GIVEN_NAME, "Test")
                                        .claim(StandardClaimNames.FAMILY_NAME, "User")
                                        .claim(StandardClaimNames.EMAIL, "testuser@gmail.com"))))
                .andExpect(status().isOk());


        mockMvc.perform(patch(String.format("/api/account/set-theme?theme=%s", shopItemTheme.getTheme().toString()))
                        .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                .jwt(jwt -> jwt
                                        .claim(StandardClaimNames.SUB, account.getSubjectId())
                                        .claim(StandardClaimNames.GIVEN_NAME, "Test")
                                        .claim(StandardClaimNames.FAMILY_NAME, "User")
                                        .claim(StandardClaimNames.EMAIL, "testuser@gmail.com"))))
                .andExpect(status().isOk());

        account = accountRepository.findAccountBySubjectId(account.getSubjectId()).orElseThrow();
        assertEquals(account.getTheme(), shopItemTheme.getTheme());
    }

    @Test
    public void testOverviewShopItemsShouldReturnSuccess() throws Exception {
        mockMvc.perform(get("/api/shop/avatar-shop-items")
                        .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                .jwt(jwt -> jwt
                                        .claim(StandardClaimNames.SUB, account.getSubjectId())
                                        .claim(StandardClaimNames.GIVEN_NAME, "Test")
                                        .claim(StandardClaimNames.FAMILY_NAME, "User")
                                        .claim(StandardClaimNames.EMAIL, "testuser@gmail.com"))))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/shop/theme-shop-items")
                        .with(jwt().authorities(List.of(new SimpleGrantedAuthority("player")))
                                .jwt(jwt -> jwt
                                        .claim(StandardClaimNames.SUB, account.getSubjectId())
                                        .claim(StandardClaimNames.GIVEN_NAME, "Test")
                                        .claim(StandardClaimNames.FAMILY_NAME, "User")
                                        .claim(StandardClaimNames.EMAIL, "testuser@gmail.com"))))
                .andExpect(status().isOk());
    }
}
