package be.kdg.backend_game.repository;

import be.kdg.backend_game.domain.user_management.Account;
import be.kdg.backend_game.domain.user_management.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, UUID> {
    @Query("SELECT COUNT(f) > 0 FROM FriendRequest f " +
            "WHERE (f.requester = :account1 AND f.requested = :account2) " +
            "   OR (f.requested = :account2 AND f.requester = :account1)")
    boolean friendRequestExists(@Param("account1") Account account1, @Param("account2") Account account2);

    List<FriendRequest> findAllByRequested_AccountId(UUID accountId);
}
