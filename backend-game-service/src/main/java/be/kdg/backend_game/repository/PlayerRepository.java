package be.kdg.backend_game.repository;

import be.kdg.backend_game.domain.game.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
    //TODO: check if anything is broken here after merge (should be fine tho!)
    @Query("SELECT p FROM Player p " +
            "LEFT JOIN FETCH p.account a " +
            "LEFT JOIN FETCH a.avatar " +
            "WHERE p.game.gameId = :gameId " +
            "ORDER BY p.playerNumber ASC")
    Optional<List<Player>> findPlayerByGame_GameIdOrderByPlayerNumber(@Param("gameId") UUID gameId);

    Optional<Player> findByAccount_AccountId(UUID accountId);

    @Query("SELECT p FROM Player p " +
            "LEFT JOIN FETCH p.account a " +
            "WHERE p.playerNumber = :playerNumber")
    Optional<Player> findPlayerWithAccountByPlayerNumber(int playerNumber);

    @Query("SELECT p FROM Player p " +
            "LEFT JOIN FETCH p.account a " +
            "LEFT JOIN FETCH a.avatar  " +
            "WHERE p.game.gameId = :gameId")
    List<Player> findPlayersWithAccountAndAvatarWithGameId(UUID gameId);
}
