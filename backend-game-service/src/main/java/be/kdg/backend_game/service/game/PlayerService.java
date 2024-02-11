package be.kdg.backend_game.service.game;

import be.kdg.backend_game.domain.game.Player;
import be.kdg.backend_game.repository.AccountRepository;
import be.kdg.backend_game.repository.PlayerRepository;
import be.kdg.backend_game.repository.TurnRepository;
import be.kdg.backend_game.service.JwtExtractorHelper;
import be.kdg.backend_game.service.game.dto.PlayersDataDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PlayerService {
    private static final Logger logger = Logger.getLogger(PlayerService.class.getPackageName());
    private final PlayerRepository playerRepository;
    private final AccountRepository accountRepository;
    private final TurnRepository turnRepository;

    public PlayerService(PlayerRepository playerRepository,
                         AccountRepository accountRepository,
                         TurnRepository turnRepository) {
        this.playerRepository = playerRepository;
        this.accountRepository = accountRepository;
        this.turnRepository = turnRepository;
    }


    /**
     * @return list of players by gameId that is ordered ascending based on the playerNumber.
     */
    public List<Player> retrievePlayersByGame(UUID gameId) {
        Optional<List<Player>> playersOptional = playerRepository.findPlayerByGame_GameIdOrderByPlayerNumber(gameId);
        return playersOptional.get();
    }

    public Optional<Player> retrievePlayerByPlayerNumber(int playerNumber) {
        return playerRepository.findById(playerNumber);
    }

    public Optional<Player> retrievePlayerWithAccountByPlayerNumber(int playerNumber) {
        return playerRepository.findPlayerWithAccountByPlayerNumber(playerNumber);
    }

    public Optional<Boolean> isPlayerCurrentlyInTurn(Jwt token, UUID gameId) {
        var player = retrievePlayerBySubjectId(token, gameId);
        var turn = turnRepository.findLatestTurnByGameId(gameId);
        if (player.isPresent() && turn.isPresent()) {
            return Optional.of(player.get().getPlayerNumber() == turn.get().getPlayer().getPlayerNumber());
        } else {
            logger.warning("Player or turn not found");
            return Optional.empty();
        }
    }

    public void updatePlayerScores(Map<Integer, Integer> scoreMap) {
        try {
            logger.log(Level.INFO, "Updating player scores");
            for (Map.Entry<Integer, Integer> entry : scoreMap.entrySet()) {
                int playerNumber = entry.getKey();
                int scoreToAdd = entry.getValue();

                Player player = playerRepository.getReferenceById(playerNumber);
                player.setPoints(player.getPoints() + scoreToAdd);
                playerRepository.save(player);
            }
        } catch (DataIntegrityViolationException e) {
            logger.log(Level.WARNING, "Data integrity violation while updating player score: " + e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "ERROR: " + e.getMessage());
        }
    }

    public void updatePlayerClaims(Map<Integer, Integer> totalClaimsWon, Map<Integer, Integer> totalClaimsLost) {
        totalClaimsWon.forEach((playerNumber, claimsWon) -> {
            Optional<Player> optionalPlayer = retrievePlayerByPlayerNumber(playerNumber);
            if (optionalPlayer.isPresent()) {
                Player player = optionalPlayer.get();
                player.setContestedLandWon(player.getContestedLandWon() + claimsWon);
                playerRepository.save(player);
            }
        });

        totalClaimsLost.forEach((playerNumber, claimsLost) -> {
            Optional<Player> optionalPlayer = retrievePlayerByPlayerNumber(playerNumber);
            if (optionalPlayer.isPresent()) {
                Player player = optionalPlayer.get();
                player.setContestedLandLost(player.getContestedLandLost() + claimsLost);
                playerRepository.save(player);
            }
        });
    }


    private Optional<Player> retrievePlayerBySubjectId(Jwt token, UUID gameUuid) {
        var subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
        var account = accountRepository.findAccountBySubjectId(subjectId);
        if (account.isPresent()) {
            //todo: check if player is in game
            return playerRepository.findByAccount_AccountId(account.get().getAccountId());
        } else {
            logger.warning("Account not found");
            return Optional.empty();
        }
    }

    public PlayersDataDto retrieveplayersData(UUID gameId) {
        List<Player> players = playerRepository.findPlayersWithAccountAndAvatarWithGameId(gameId);
        if (players.isEmpty()) return null;
        return new PlayersDataDto(players);
    }
}
