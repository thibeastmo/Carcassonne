package be.kdg.backend_game.repository;

import be.kdg.backend_game.domain.user_management.shop.ShopItem;
import be.kdg.backend_game.domain.user_management.shop.shop_items.AvatarItem;
import be.kdg.backend_game.domain.user_management.shop.shop_items.Theme;
import be.kdg.backend_game.domain.user_management.shop.shop_items.ThemeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;
import java.util.List;
import java.util.UUID;

@Repository
public interface ShopRepository extends JpaRepository<ShopItem, UUID> {

    @Query("SELECT a FROM AvatarItem a")
    List<AvatarItem> findAllAvatarItems();

    @Query("SELECT ti FROM ThemeItem ti")
    List<ThemeItem> findAllThemeItems();
}
