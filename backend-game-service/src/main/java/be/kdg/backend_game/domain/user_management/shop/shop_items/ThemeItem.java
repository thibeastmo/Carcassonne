package be.kdg.backend_game.domain.user_management.shop.shop_items;


import be.kdg.backend_game.domain.user_management.shop.ShopCategory;
import be.kdg.backend_game.domain.user_management.shop.ShopItem;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ThemeItem extends ShopItem {
    private Theme theme;
    private String imageUrl;

    public ThemeItem() {
    }

    public ThemeItem(String name, int price, ShopCategory shopCategory, Theme theme, String imageUrl) {
        super(name, price, shopCategory);
        this.theme = theme;
        this.imageUrl = imageUrl;
    }
}
