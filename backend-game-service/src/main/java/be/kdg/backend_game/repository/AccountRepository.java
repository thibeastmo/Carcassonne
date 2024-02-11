package be.kdg.backend_game.repository;

import be.kdg.backend_game.domain.user_management.Account;
import be.kdg.backend_game.domain.user_management.RankedAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    @Query("FROM Account a WHERE a.accountId = :accountId")
    Optional<Account> findAccountWithUserStatisticsByAccountId(@Param("accountId") UUID accountId);

    @Query("SELECT a FROM Account a WHERE a.accountId = :accountId")
    Optional<Account> findAccountByAccountId(@Param("accountId") UUID accountId);

    Optional<Account> findBysubjectId(UUID subjectId);

    @Query("SELECT a FROM Account a WHERE a.subjectId = :subjectId")
    Optional<Account> findAccountBySubjectId(UUID subjectId);

    @Query("SELECT a FROM Account a WHERE a.nickName = :usernameOrEmail OR a.email = :usernameOrEmail")
    Optional<Account> findAccountByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);

    @Query("SELECT a FROM Account a LEFT JOIN FETCH a.shopItemBuyStates WHERE a.subjectId = :subjectId")
    Optional<Account> findAccountBySubjectIdWithShopItemBuyStates(UUID subjectId);

    @Query("SELECT COUNT(a2) + 1 FROM Account a2 WHERE a2.experiencePoints > (SELECT a1.experiencePoints FROM Account a1 WHERE a1.subjectId = :subjectId)")
    Optional<Integer> findRankBySubjectId(@Param("subjectId") UUID subjectId);

    @Query("SELECT a FROM Account a " +
            "ORDER BY a.experiencePoints DESC " +
            "LIMIT 5")
    List<Account> findTopAccountsByExperiencePoints();
}
