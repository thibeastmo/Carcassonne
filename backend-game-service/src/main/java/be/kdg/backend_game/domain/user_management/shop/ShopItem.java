package be.kdg.backend_game.domain.user_management.shop;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
public abstract class ShopItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID shopItemId;
    private String name;
    private int price;
    private ShopCategory shopCategory;
    @OneToMany(orphanRemoval = true, mappedBy = "shopItem", cascade = CascadeType.ALL)
    private List<ShopItemBuyState> shopItemBuyState;

    public ShopItem() {
        this.shopItemBuyState = new ArrayList<>();
    }

    public ShopItem(String name, int price, ShopCategory shopCategory) {
        this.name = name;
        this.price = price;
        this.shopCategory = shopCategory;
        this.shopItemBuyState = new ArrayList<>();
    }
}
