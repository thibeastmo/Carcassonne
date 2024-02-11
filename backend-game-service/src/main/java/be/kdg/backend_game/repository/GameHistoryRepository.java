package be.kdg.backend_game.repository;

import be.kdg.backend_game.domain.user_management.history.GameHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GameHistoryRepository extends JpaRepository<GameHistory, UUID> {
    Optional<GameHistory> findFirstGameHistoryByAccount_AccountIdOrderByCreationDate(UUID account_account_id);
    List<GameHistory> findGameHistoriesByAccount_SubjectId(UUID account_subjectsId);
    @Query("SELECT gh FROM GameHistory gh " +
            "JOIN gh.game g " +
            "JOIN g.players p " +
            "WHERE g.gameId = :gameId " +
            "ORDER BY p.playerNumber ASC")
    List<GameHistory> findGameHistoryByGameIdOrderByPlayerNumberAsc(@Param("gameId") UUID gameId);
    @Modifying
    @Query("UPDATE GameHistory gh SET gh.game = null WHERE gh.game.gameId =:gameId")
    void updateDisconnectFromGame(UUID gameId);
}
