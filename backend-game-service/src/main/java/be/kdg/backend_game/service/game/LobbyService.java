package be.kdg.backend_game.service.game;


import be.kdg.backend_game.domain.GameTypeEnum;
import be.kdg.backend_game.domain.user_management.Account;
import be.kdg.backend_game.domain.game.Game;
import be.kdg.backend_game.domain.lobby.Invite;
import be.kdg.backend_game.domain.lobby.Lobby;
import be.kdg.backend_game.domain.lobby.LobbyParticipant;
import be.kdg.backend_game.domain.user_management.UserLevel;
import be.kdg.backend_game.repository.AccountRepository;
import be.kdg.backend_game.repository.LobbyRepository;
import be.kdg.backend_game.service.dto.lobby.InviteDto;
import be.kdg.backend_game.service.dto.lobby.LobbyInfoDto;
import be.kdg.backend_game.service.dto.lobby.NewLobbyDto;
import be.kdg.backend_game.service.exception.TileNotFoundException;
import be.kdg.backend_game.service.exception.AccountNotFoundException;
import be.kdg.backend_game.service.exception.InvalidInviteException;
import be.kdg.backend_game.service.exception.LobbyNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@Transactional
public class LobbyService {
    private static final Logger logger = Logger.getLogger(be.kdg.backend_game.service.game.LobbyService.class.getPackageName());
    private final LobbyRepository lobbyRepository;
    private final AccountRepository accountRepository;
    private final GameService gameService;
    private final InviteService inviteService;


    public LobbyService(LobbyRepository lobbyRepository, AccountRepository accountRepository, GameService gameService, InviteService inviteService) {
        this.lobbyRepository = lobbyRepository;
        this.accountRepository = accountRepository;
        this.gameService = gameService;
        this.inviteService = inviteService;
    }

    public LobbyInfoDto createLobby(NewLobbyDto newLobbyDto, UUID subjectId) {
        Account account = accountRepository.findAccountBySubjectId(subjectId)
                .orElseThrow(() -> new TileNotFoundException("Account not found for id " + subjectId.toString()));

        Lobby lobby = new Lobby(String.format(newLobbyDto.getLobbyName(), account.getNickName()), newLobbyDto.getMaxPlayers(), false, newLobbyDto.getGameTypeEnum(), account, LocalDateTime.now());
        lobby.setGameTypeEnum(newLobbyDto.getGameTypeEnum());
        LobbyParticipant lobbyParticipant = new LobbyParticipant(lobby, account);
        lobby.addLobbyParticipant(lobbyParticipant);
        lobby = lobbyRepository.save(lobby);

        LobbyInfoDto lobbyDto = new LobbyInfoDto();
        lobbyDto.setLobby(lobby);
        lobbyDto.setHost(subjectId.equals(lobby.getOwner().getSubjectId()));
        return lobbyDto;
    }

    public List<LobbyInfoDto> getAllOpenLobbies(UUID subjectId) {
        List<Lobby> lobbies = lobbyRepository.findAllOpenLobbies();
        List<LobbyInfoDto> lobbyInfoDtos = new ArrayList<>();
        for (var lobby : lobbies) {
            if (lobby.getLobbyParticipants().stream().anyMatch(lp -> {
                return lp.getAccount().getSubjectId().equals(subjectId);
            }) || lobby.getLobbyParticipants().size() < lobby.getMaxPlayers()) {
                LobbyInfoDto lobbyInfoDto = new LobbyInfoDto();
                lobbyInfoDto.setLobby(lobby);

                lobbyInfoDto.setJoined(lobby.getLobbyParticipants().stream().anyMatch(lobbyParticipant -> {
                    return lobbyParticipant.getLobbyParticipantId().getAccount().getSubjectId().equals(subjectId);
                }));
                lobbyInfoDtos.add(lobbyInfoDto);
                lobbyInfoDto.setHost(subjectId.equals(lobby.getOwner().getSubjectId()));
            }
        }
        return lobbyInfoDtos;
    }

    public List<LobbyInfoDto> getActiveGamesOfPlayer(UUID subjectId) {
        List<Lobby> lobbiesWithGames = lobbyRepository.findAllLobbiesWithActiveGames();
        List<LobbyInfoDto> lobbyInfoDtos = new ArrayList<>();
        for (var lobby : lobbiesWithGames) {
            if (lobby.getLobbyParticipants().stream().anyMatch(lp -> {
                return lp.getAccount().getSubjectId().equals(subjectId);
            })) {
                var lobbyInfoDto = new LobbyInfoDto();
                lobbyInfoDto.setLobby(lobby);
                lobbyInfoDto.setGameId(lobby.getGame().getGameId());
                lobbyInfoDtos.add(lobbyInfoDto);
            }
        }
        return lobbyInfoDtos;
    }

    public LobbyInfoDto getLobbyInfoDto(UUID lobbyId, UUID subjectId) {
        Lobby lobby = lobbyRepository.findLobbyWithLobbyParticipantsByLobbyId(lobbyId)
                .orElseThrow(() -> new LobbyNotFoundException("Could not find lobby for lobby id " + lobbyId));

        var lobbyDto = new LobbyInfoDto();
        lobbyDto.setLobby(lobby);
        lobbyDto.setHost(subjectId.equals(lobby.getOwner().getSubjectId()));
        if (lobby.getGame() != null) {
            lobbyDto.setGameId(lobby.getGame().getGameId());
        }
        lobbyDto.setJoined(lobby.getLobbyParticipants().stream().anyMatch(lobbyParticipant -> {
            return lobbyParticipant.getLobbyParticipantId().getAccount().getSubjectId().equals(subjectId);
        }));
        return lobbyDto;
    }

    public void joinLobby(UUID lobbyId, UUID subjectId) {
        Lobby lobby = lobbyRepository.findLobbyWithLobbyParticipantsByLobbyId(lobbyId)
                .orElseThrow(() -> new LobbyNotFoundException("Could not find lobby for lobby id " + lobbyId));

        Account account = accountRepository.findAccountBySubjectId(subjectId)
                .orElseThrow(() -> new AccountNotFoundException("Could not find an account for account id " + subjectId));

        if (lobby.getLobbyParticipants().size() == lobby.getMaxPlayers()) {
            throw new LobbyNotFoundException("Lobby is full");
        }

        LobbyParticipant lobbyParticipant = new LobbyParticipant(lobby, account);
        lobby.addLobbyParticipant(lobbyParticipant);
        lobbyRepository.save(lobby);
    }

    public Optional<Lobby> retrieveLobby(UUID lobbyId) {
        return lobbyRepository.findById(lobbyId);
    }

    // return game id
    //TODO: return type changed after merge, check if this causes bugs elsewhere
    public Game startGameFromLobby(UUID lobbyId) {
        Lobby lobby = lobbyRepository.findLobbyWithLobbyParticipantsByLobbyId(lobbyId)
                .orElseThrow(() -> new LobbyNotFoundException("Could not find lobby for lobby id " + lobbyId));
        inviteService.removeInvitesWithForLobby(lobby);
        var gameOptional = gameService.createGame(lobbyId);
        lobby.setGame(gameOptional.get());
        lobbyRepository.save(lobby);
        return gameOptional.get();
    }

    public boolean closeLobby(UUID lobbyId) {
        try {
            lobbyRepository.updateClosedStatusById(lobbyId);
        } catch (DataIntegrityViolationException e) {
            logger.log(Level.WARNING, "Data integrity violation while closing a lobby: " + e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error caused while closing a lobby: " + e.getMessage());
        }
        return false;
    }

    public List<LobbyParticipant> retrieveLobbyParticipantsByLobbyId(UUID lobbyId) {
        return lobbyRepository.findLobbyParticipantsByLobby_LobbyId(lobbyId);
    }

    public void leaveLobby(UUID lobbyId, UUID subjectId) {
        Lobby lobby = lobbyRepository.findLobbyWithLobbyParticipantsByLobbyId(lobbyId)
                .orElseThrow(() -> new LobbyNotFoundException("Could not find lobby for lobby id " + lobbyId));

        Account account = accountRepository.findAccountBySubjectId(subjectId)
                .orElseThrow(() -> new AccountNotFoundException("Could not find an account for account id " + subjectId));

        if (lobby.isClosed()) {
            throw new LobbyNotFoundException("A lobby was found, but is not open. Make sure that the lobby is not closed");
        }

        lobby.getLobbyParticipants().removeIf(lobbyParticipant -> {
            return lobbyParticipant.getAccount().getAccountId().equals(account.getAccountId());
        });

        if (lobby.getLobbyParticipants().isEmpty()) {
            lobbyRepository.delete(lobby);
        } else lobbyRepository.save(lobby);
    }

    public Optional<LobbyInfoDto> quickJoinAvailableLobby(UUID subjectId, GameTypeEnum gameTypeEnum) {
        Account account = accountRepository.findAccountBySubjectId(subjectId)
                .orElseThrow(() -> new AccountNotFoundException("Could not find an account for account id " + subjectId));

        Optional<LobbyInfoDto> lobbyInfoDto = Optional.empty();
        Optional<Lobby> availableLobby = Optional.empty();

        List<Lobby> lobbies = lobbyRepository.findOpenLobbyByGameTypeEnum(gameTypeEnum);

        List<Lobby> allAvailableLobbies = lobbies.stream()
                .filter((lobby) -> {
                    return lobby.getLobbyParticipants().size() < lobby.getMaxPlayers() && !lobby.isClosed() && lobby.getLobbyParticipants().stream().noneMatch(lp -> {
                        return lp.getAccount().getSubjectId().equals(subjectId);
                    });
                }).toList();

        Optional<Lobby> bestFittingLobby = allAvailableLobbies.stream().findFirst();
        int lowestSCore = 1000;
        int userLevelOfRequestingUser = account.getUserLevel();
        for (var lobby : allAvailableLobbies) {
            int score = 0;
            for (var lobbyParticipant : lobby.getLobbyParticipants()) {
                int scoreOfLobbyParticipant = Math.abs(userLevelOfRequestingUser - lobbyParticipant.getAccount().getUserLevel());
                score += scoreOfLobbyParticipant;
            }
            if (score < lowestSCore) {
                bestFittingLobby = Optional.of(lobby);
                lowestSCore = score;
            }
        }

        if (bestFittingLobby.isPresent()) {
            LobbyParticipant lobbyParticipant = new LobbyParticipant(bestFittingLobby.get(), account);
            bestFittingLobby.get().addLobbyParticipant(lobbyParticipant);
            lobbyInfoDto = Optional.of(new LobbyInfoDto());
            lobbyInfoDto.get().setLobby(bestFittingLobby.get());
        }

        return lobbyInfoDto;
    }

    public InviteDto createInvite(UUID lobbyId, UUID accountId, UUID subjectId) {
        Account inviter = accountRepository.findAccountBySubjectId(subjectId)
                .orElseThrow(() -> new AccountNotFoundException("Could not find an account for subject id " + subjectId));
        Account invitee = accountRepository.findAccountByAccountId(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Could not find an account for account id " + accountId));
        if (inviter.getAccountId().equals(invitee.getAccountId())) throw new InvalidInviteException("Inviter and invitee are the same account");
        Lobby lobby = lobbyRepository.findLobbyWithLobbyParticipantsByLobbyId(lobbyId)
                .orElseThrow(() -> new LobbyNotFoundException("Could not find lobby for lobby id " + lobbyId));
        if (lobby.getLobbyParticipants().stream().noneMatch(participant -> participant.getAccount().equals(inviter))) {
            throw new InvalidInviteException("Inviter is not in the lobby.");
        }
        if (lobby.getLobbyParticipants().stream().anyMatch(participant -> participant.getAccount().equals(invitee))) {
            throw new InvalidInviteException("Invitee is already in the lobby.");
        }
        return inviteService.createInvite(invitee, lobby, inviter);
    }

    public void acceptInvite(UUID inviteId, UUID subjectId) {
        Account account = accountRepository.findAccountBySubjectId(subjectId)
                .orElseThrow(() -> new AccountNotFoundException("Could not find an account for subject id " + subjectId));
        Invite invite = inviteService.retrieveInviteByInviteId(inviteId)
                .orElseThrow(() -> new InvalidInviteException("Could not find an invite for invite id " + inviteId));
        Lobby lobby = lobbyRepository.findLobbyWithLobbyParticipantsByLobbyId(invite.getLobby().getLobbyId())
                .orElseThrow(() -> new LobbyNotFoundException("Could not find lobby for lobby id " + invite.getLobby().getLobbyId()));
        if (!invite.getInvitee().getAccountId().equals(account.getAccountId())){
            throw new InvalidInviteException("Invitee id does not match account with subject id " + subjectId);
        }
        if (lobby.getLobbyParticipants().stream().anyMatch(participant -> participant.getAccount().getAccountId().equals(account.getAccountId()))) {
            throw new InvalidInviteException("Invitee is already in the lobby.");
        }
        if (lobby.getLobbyParticipants().size() == lobby.getMaxPlayers()) {
            throw new LobbyNotFoundException("Lobby is full");
        }
        LobbyParticipant lobbyParticipant = new LobbyParticipant(lobby, account);
        lobby.addLobbyParticipant(lobbyParticipant);
        lobbyRepository.save(lobby);
        inviteService.removeInvite(invite);
    }

    public void removeInvite(UUID inviteId, UUID subjectId) {
        Account account = accountRepository.findAccountBySubjectId(subjectId)
                .orElseThrow(() -> new AccountNotFoundException("Could not find an account for subject id " + subjectId));
        Invite invite = inviteService.retrieveInviteByInviteId(inviteId)
                .orElseThrow(() -> new InvalidInviteException("Could not find an invite for invite id " + inviteId));
        if (!invite.getInvitee().getAccountId().equals(account.getAccountId())){
            throw new InvalidInviteException("Invitee id does not match account with subject id " + subjectId);
        }
        inviteService.removeInvite(invite);
    }
}
