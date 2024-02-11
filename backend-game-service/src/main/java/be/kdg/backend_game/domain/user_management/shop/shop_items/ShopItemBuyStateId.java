package be.kdg.backend_game.domain.user_management.shop.shop_items;

import be.kdg.backend_game.domain.user_management.Account;
import be.kdg.backend_game.domain.user_management.shop.ShopItem;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class ShopItemBuyStateId implements Serializable {
    public ShopItemBuyStateId() {
    }

    public ShopItemBuyStateId(ShopItem shopItem, Account account) {
        this.shopItem = shopItem;
        this.account = account;
    }

    @ManyToOne
    private ShopItem shopItem;

    @ManyToOne
    private Account account;

}
