package be.kdg.backend_game.message;

import java.util.UUID;

public record AccountMessage(
        UUID accountId,
        String nickName,
        int loyaltyPoints,
        int experiencePoints
) {
}
