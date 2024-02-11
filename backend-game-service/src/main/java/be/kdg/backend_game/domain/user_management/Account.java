package be.kdg.backend_game.domain.user_management;

import be.kdg.backend_game.domain.user_management.shop.ShopItemBuyState;
import be.kdg.backend_game.domain.lobby.LobbyParticipant;
import be.kdg.backend_game.domain.user_management.shop.shop_items.Theme;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Account {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID accountId;
    private UUID subjectId;
    private String nickName;
    private String email;
    private int loyaltyPoints;
    private int experiencePoints;
    private int userLevel;

    // Keep orphanRemoval on in case user equips a new image
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private AvatarImage avatar;
    @OneToOne(orphanRemoval = false, mappedBy = "account")
    private UserStatistics userStatistics;
    @OneToMany(orphanRemoval = false, mappedBy = "account", cascade = CascadeType.ALL)
    private List<ShopItemBuyState> shopItemBuyStates;
    private Theme theme;
    @OneToMany(orphanRemoval = true, mappedBy = "account", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<LobbyParticipant> lobbiesParticipating;
    private LocalDateTime lastLoginDate;

    public Account() {
        this.lobbiesParticipating = new ArrayList<>();
        this.shopItemBuyStates = new ArrayList<>();
        this.theme = Theme.NORMAL;
        this.userLevel = 1;
    }

    public void addShopItemBuyState(ShopItemBuyState shopItemBuyState) {
        shopItemBuyState.setAccount(this);
        this.shopItemBuyStates.add(shopItemBuyState);
    }

    public Account(UUID accountId, UUID subjectId, String nickName, String email, int loyaltyPoints, int experiencePoints) {
        this.accountId = accountId;
        this.subjectId = subjectId;
        this.nickName = nickName;
        this.email = email;
        this.loyaltyPoints = loyaltyPoints;
        setExperiencePoints(experiencePoints);
        this.shopItemBuyStates = new ArrayList<>();
        this.lobbiesParticipating = new ArrayList<>();
        this.theme = Theme.NORMAL;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", subjectId=" + subjectId +
                ", nickname='" + nickName + '\'' +
                ", loyaltyPoints=" + loyaltyPoints +
                ", experiencePoints=" + experiencePoints +
                '}';
    }

    public void setExperiencePoints(int experiencePoints) {
        this.experiencePoints = experiencePoints;
        this.setUserLevel(UserLevel.getLevelByExperiencePoints(this.getExperiencePoints()));
    }
}
