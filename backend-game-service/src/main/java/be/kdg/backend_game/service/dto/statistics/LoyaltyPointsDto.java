package be.kdg.backend_game.service.dto.statistics;

import lombok.Data;

@Data
public class LoyaltyPointsDto {
    private int amountOfLoyaltyPoints;

    public LoyaltyPointsDto(int loyaltyPoints) {
        amountOfLoyaltyPoints = loyaltyPoints;
    }
}
