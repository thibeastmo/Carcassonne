package be.kdg.backend_game.repository;

import be.kdg.backend_game.domain.GameTypeEnum;
import be.kdg.backend_game.domain.lobby.Lobby;
import be.kdg.backend_game.domain.lobby.LobbyParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LobbyRepository extends JpaRepository<Lobby, UUID> {
    @Query("SELECT l FROM Lobby l " +
            "LEFT JOIN FETCH l.lobbyParticipants lp " +
            "LEFT JOIN FETCH lp.account a " +
            "LEFT JOIN FETCH a.avatar " +
            "LEFT JOIN FETCH l.owner " +
            "WHERE l.lobbyId = :lobbyId")
    Optional<Lobby> findLobbyWithLobbyParticipantsByLobbyId(@Param("lobbyId") UUID lobbyId);

    @Query("SELECT l FROM Lobby l JOIN FETCH l.lobbyParticipants WHERE l.closed = false")
    List<Lobby> findAllOpenLobbies();

    @Modifying
    @Query("UPDATE Lobby l SET l.closed = false WHERE l.lobbyId = :lobbyId")
    void updateClosedStatusById(UUID lobbyId);

    @Query("SELECT lp FROM LobbyParticipant lp WHERE lp.lobby.lobbyId = :lobbyId")
    List<LobbyParticipant> findLobbyParticipantsByLobby_LobbyId(@Param("lobbyId") UUID lobbyId);

    @Query("SELECT l FROM Lobby l LEFT JOIN FETCH l.lobbyParticipants WHERE l.game IS NOT NULL AND l.game.isGameOver = false")
    List<Lobby> findAllLobbiesWithActiveGames();

    //TODO: figure out if both are necessary or if one can replace the other
    @Query("SELECT l.lobbyParticipants FROM Lobby l WHERE l.lobbyId = :lobbyId")
    Optional<List<LobbyParticipant>> findLobbyParticipantsByLobbyId(@Param("lobbyId") UUID lobbyId);

    @Query("SELECT DISTINCT l FROM Lobby l LEFT JOIN FETCH l.lobbyParticipants lp LEFT JOIN FETCH lp.account WHERE l.closed = false AND l.gameTypeEnum = :gameTypeEnum")
    List<Lobby> findOpenLobbyByGameTypeEnum(@Param("gameTypeEnum") GameTypeEnum gameTypeEnum);
}
