package be.kdg.backend_game.controller;

import be.kdg.backend_game.domain.user_management.shop.ShopCategory;
import be.kdg.backend_game.service.AccountService;
import be.kdg.backend_game.service.JwtExtractorHelper;
import be.kdg.backend_game.service.exception.AccountNotFoundException;
import be.kdg.backend_game.service.exception.NotEnoughPointsException;
import be.kdg.backend_game.service.shop.ShopService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/shop")
public class ShopController {

    private final ShopService shopService;
    private final AccountService accountService;

    public ShopController(ShopService shopService, AccountService accountService){
        this.shopService = shopService;
        this.accountService = accountService;
    }


    @GetMapping("/avatar-shop-items")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> avatarShopItems(@AuthenticationPrincipal Jwt token){
        var subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
        try {
            var allShopItems = shopService.getAllPurchasableAvatarShopItems(subjectId);
            return ResponseEntity.ok().body(allShopItems);
        } catch (AccountNotFoundException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/theme-shop-items")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> themeShopItems(@AuthenticationPrincipal Jwt token){
        var subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
        try {
            var allShopItems = shopService.getAllPurchasableThemeShopItems(subjectId);
            return ResponseEntity.ok().body(allShopItems);
        } catch (AccountNotFoundException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/buy-shop-item")
    @PreAuthorize("hasAuthority('player')")
    public ResponseEntity<?> buyShopItem(@AuthenticationPrincipal Jwt token, @RequestParam UUID shopItemId) {
        var subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
        try {
            shopService.buyItem(subjectId, shopItemId);
            return ResponseEntity.ok("Success!");
        } catch (AccountNotFoundException | NotEnoughPointsException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
