package be.kdg.backend_game.repository;

import be.kdg.backend_game.domain.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface GameRepository extends JpaRepository<Game, UUID> {
    @Query("UPDATE Lobby l SET l.closed = false WHERE l.lobbyId = :lobbyId")
    void updateClosedStatusById(UUID lobbyId);

    Optional<Game> findGameByLobby_LobbyId(UUID lobbyId);
    @Query("SELECT g FROM Game g " +
            "LEFT JOIN FETCH g.players p " +
            "LEFT JOIN FETCH p.account a " +
            "LEFT JOIN FETCH a.avatar " +
            "WHERE g.gameId = :gameId")
    Optional<Game> findGameByGameIdWithAccount(@Param("gameId") UUID gameId);

    @Query("SELECT g FROM Game g LEFT JOIN FETCH g.players WHERE g.gameId = :gameId")
    Optional<Game> findByIdWithPlayers(UUID gameId);
}
