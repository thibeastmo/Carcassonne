package be.kdg.backend_game.message;

import java.util.List;

public record AccountListMessage(List<AccountMessage> accounts) {
}
