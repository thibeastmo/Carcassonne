package be.kdg.backend_game.service.dto.shop;

import be.kdg.backend_game.domain.user_management.shop.ShopCategory;
import be.kdg.backend_game.domain.user_management.shop.shop_items.AvatarItem;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AvatarItemDto {
    private UUID shopItemId;
    private String name;
    private int price;
    private ShopCategory shopCategory;
    private String imageUrl;

    public AvatarItemDto(AvatarItem avatarItem) {
        this.shopItemId = avatarItem.getShopItemId();
        this.name = avatarItem.getName();
        this.price = avatarItem.getPrice();
        this.shopCategory = avatarItem.getShopCategory();
        this.imageUrl = avatarItem.getAvatarItemUrl();
    }

    public AvatarItemDto(UUID shopItemId, String name, int price, ShopCategory shopCategory, String imageUrl) {
        this.shopItemId = shopItemId;
        this.name = name;
        this.price = price;
        this.shopCategory = shopCategory;
        this.imageUrl = imageUrl;
    }
}
