package be.kdg.backend_game.domain.user_management.shop.shop_items;


import be.kdg.backend_game.domain.user_management.shop.ShopCategory;
import be.kdg.backend_game.domain.user_management.shop.ShopItem;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;

@Entity
@Getter
@Setter
public class AvatarItem extends ShopItem {
    private String avatarItemUrl;
    public AvatarItem() {
    }

    public AvatarItem(String name, int price, ShopCategory shopCategory, String avatarItemUrl) {
        super(name, price, shopCategory);
        this.avatarItemUrl = avatarItemUrl;
    }
}
