package be.kdg.backend_game.repository;

import be.kdg.backend_game.domain.lobby.LobbyParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LobbyParticipantRepository extends JpaRepository<LobbyParticipant, UUID> {
    List<LobbyParticipant> findLobbyParticipantsByLobby_LobbyId(UUID lobbyId);
}
