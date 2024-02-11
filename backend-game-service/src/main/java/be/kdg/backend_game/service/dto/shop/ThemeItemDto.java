package be.kdg.backend_game.service.dto.shop;

import be.kdg.backend_game.domain.user_management.shop.ShopCategory;
import be.kdg.backend_game.domain.user_management.shop.shop_items.AvatarItem;
import be.kdg.backend_game.domain.user_management.shop.shop_items.ThemeItem;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ThemeItemDto {
    private UUID shopItemId;
    private String name;
    private int price;
    private ShopCategory shopCategory;
    private String imageUrl;

    public ThemeItemDto(ThemeItem themeItem) {
        this.shopItemId = themeItem.getShopItemId();
        this.name = themeItem.getName();
        this.price = themeItem.getPrice();
        this.shopCategory = themeItem.getShopCategory();
        this.imageUrl = themeItem.getImageUrl();
    }

    public ThemeItemDto(UUID shopItemId, String name, int price, ShopCategory shopCategory, String imageUrl) {
        this.shopItemId = shopItemId;
        this.name = name;
        this.price = price;
        this.shopCategory = shopCategory;
        this.imageUrl = imageUrl;
    }
}


