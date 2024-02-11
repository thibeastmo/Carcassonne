package be.kdg.backend_game.domain.userManagement;

import be.kdg.backend_game.repository.AccountRepository;
import be.kdg.backend_game.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.UUID;

@SpringBootTest
public class LoyaltyPointsTest {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;
    @Test
    public void updateLoyaltyPointsCorrectly() {
        UUID accountId = UUID.fromString("60e2f67a-bf9f-4f2f-aca2-ba8d113bfa4b");
        if (!accountService.updateLoyaltyPoints(accountId, 5)) {
            fail("Could not update the loyalty points");
            return;
        }
        var optionalAccount = accountRepository.findAccountByAccountId(accountId);
        if (optionalAccount.isEmpty()) {
            fail("Could not find the loyalty points for account with accountId: " + accountId);
            return;
        }
        var account = optionalAccount.get();
        assertEquals(5, account.getLoyaltyPoints());
    }
}
