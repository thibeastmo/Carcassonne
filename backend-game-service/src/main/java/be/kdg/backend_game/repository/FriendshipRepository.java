package be.kdg.backend_game.repository;

import be.kdg.backend_game.domain.user_management.Account;
import be.kdg.backend_game.domain.user_management.Friendship;
import be.kdg.backend_game.domain.user_management.FriendshipId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipId> {
    @Query("SELECT COUNT(f) > 0 FROM Friendship f " +
            "WHERE (f.friendshipId.account1 = :account1 AND f.friendshipId.account2 = :account2) " +
            "   OR (f.friendshipId.account1 = :account2 AND f.friendshipId.account2 = :account1)")
    boolean friendshipExists(@Param("account1") Account account1, @Param("account2") Account account2);

    @Query("SELECT f FROM Friendship f " +
            "WHERE (f.friendshipId.account1 = :account1 AND f.friendshipId.account2 = :account2) " +
            "   OR (f.friendshipId.account1 = :account2 AND f.friendshipId.account2 = :account1)")
    Optional<Friendship> findByAccountAndFriend(@Param("account1") Account account1, @Param("account2") Account account2);

    @Query("SELECT f FROM Friendship f " +
            "WHERE f.friendshipId.account1 = :account OR f.friendshipId.account2 = :account ")
    List<Friendship> findAllByAccount(Account account);
}
