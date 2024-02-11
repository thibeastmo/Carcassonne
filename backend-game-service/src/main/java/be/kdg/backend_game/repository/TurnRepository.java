package be.kdg.backend_game.repository;

import be.kdg.backend_game.domain.game.Game;
import be.kdg.backend_game.domain.game.Turn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface TurnRepository extends JpaRepository<Turn, UUID> {
    @Query("SELECT t FROM Turn t " +
            "JOIN FETCH t.player p " +
            "JOIN FETCH t.tile ti " +
            "WHERE t.game.gameId = :gameId " +
            "ORDER BY t.beginTurn DESC " +
            "LIMIT 1")
    Optional<Turn> findLatestTurnByGameId(UUID gameId);

    @Query("SELECT t.tile.tileId FROM Turn t " +
            "WHERE t.game.gameId = :gameId " +
            "ORDER BY t.beginTurn DESC " +
            "LIMIT 1")
    Optional<UUID> findLatestTurnTileIdByGameId(UUID gameId);
    List<Turn> findTurnsByGame_GameId(UUID game_gameId);
    @Query("SELECT t.game " +
            "FROM Turn t " +
            "WHERE t.player.account.accountId = :accountId " +
            "AND t.beginTurn IN (" +
            "    SELECT MAX(turn.beginTurn) " +
            "    FROM Turn turn " +
            "    WHERE turn.game = t.game AND turn.game.isGameOver = false" +
            ")")
    List<Game> findGamesWhereIsPlayerTurn(UUID accountId);
}
