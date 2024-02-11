package be.kdg.backend_game.service.dto.shop;

import be.kdg.backend_game.domain.user_management.shop.ShopCategory;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class ShopItemDto {
    private UUID shopItemId;
    private String name;
    private int price;
    private ShopCategory shopCategory;

    public ShopItemDto(UUID shopItemId, String name, int price, ShopCategory shopCategory) {
        this.shopItemId = shopItemId;
        this.name = name;
        this.price = price;
        this.shopCategory = shopCategory;
    }
}
