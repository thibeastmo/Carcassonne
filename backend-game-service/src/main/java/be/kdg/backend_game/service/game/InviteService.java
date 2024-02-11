package be.kdg.backend_game.service.game;

import be.kdg.backend_game.domain.user_management.Account;
import be.kdg.backend_game.domain.lobby.Invite;
import be.kdg.backend_game.domain.lobby.Lobby;
import be.kdg.backend_game.repository.AccountRepository;
import be.kdg.backend_game.repository.InviteRepository;
import be.kdg.backend_game.service.dto.lobby.InviteDto;
import be.kdg.backend_game.service.exception.AccountNotFoundException;
import be.kdg.backend_game.service.exception.InvalidInviteException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InviteService {
    private final InviteRepository inviteRepository;
    private final AccountRepository accountRepository;

    public InviteService(InviteRepository inviteRepository, AccountRepository accountRepository) {
        this.inviteRepository = inviteRepository;
        this.accountRepository = accountRepository;
    }

    public InviteDto createInvite(Account invitee, Lobby lobby, Account inviter) {
        boolean inviteExists = inviteRepository.existsByLobbyAndInvitee(lobby, invitee);
        if (inviteExists) {
            throw new InvalidInviteException("Invite already exists for the specified lobby and account.");
        }
        Invite invite = inviteRepository.save(new Invite(lobby,invitee, inviter));
        return new InviteDto(invite);
    }

    public Optional<Invite> retrieveInviteByInviteId(UUID inviteId) {
        return inviteRepository.findById(inviteId);
    }

    public List<InviteDto> retrieveInvitesWithSubjectId(UUID subjectId) {
        Account account = accountRepository.findAccountBySubjectId(subjectId)
            .orElseThrow(() -> new AccountNotFoundException("Account not found for id " + subjectId.toString()));
        List<Invite> invites = inviteRepository.findAllByInvitee_AccountId(account.getAccountId());
        List<InviteDto> inviteDtos = new ArrayList<>();
        for (Invite invite: invites) {
            inviteDtos.add(new InviteDto(invite));
        }
        return inviteDtos;
    }

    public void removeInvite(Invite invite) {
        inviteRepository.delete(invite);
    }

    public void removeInvitesWithForLobby(Lobby lobby) {
        inviteRepository.deleteAllByLobby(lobby);
    }
}
