package be.kdg.backend_game.domain.user_management.shop;

import be.kdg.backend_game.domain.user_management.Account;
import be.kdg.backend_game.domain.user_management.shop.shop_items.ShopItemBuyStateId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ShopItemBuyState {
    @EmbeddedId
    private ShopItemBuyStateId shopItemBuyStateId;
    public ShopItemBuyState() {
        this.shopItemBuyStateId = new ShopItemBuyStateId();
    }

    public ShopItemBuyState(ShopItem shopItem, Account account) {
        this.shopItem = shopItem;
        this.account = account;
        this.shopItemBuyStateId = new ShopItemBuyStateId();
        this.shopItemBuyStateId.setShopItem(shopItem);
        this.shopItemBuyStateId.setAccount(account);
    }

    @ManyToOne
    @MapsId("shopItemId")
    private ShopItem shopItem;
    @ManyToOne
    @MapsId("accountId")
    private Account account;
}
