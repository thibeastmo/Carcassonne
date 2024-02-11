package be.kdg.backend_game.repository;

import be.kdg.backend_game.domain.user_management.Account;
import be.kdg.backend_game.domain.lobby.Invite;
import be.kdg.backend_game.domain.lobby.Lobby;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InviteRepository extends JpaRepository<Invite, UUID> {
    List<Invite> findAllByInvitee_AccountId(UUID invitee_accountId);

    boolean existsByLobbyAndInvitee(Lobby lobby, Account invitee);

    void deleteAllByLobby(Lobby lobby);
}
