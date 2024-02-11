package be.kdg.backend_game.service.game;


import be.kdg.backend_game.domain.TimeEnum;
import be.kdg.backend_game.domain.game.*;
import be.kdg.backend_game.domain.lobby.LobbyParticipant;
import be.kdg.backend_game.domain.user_management.Account;
import be.kdg.backend_game.repository.*;
import be.kdg.backend_game.service.dto.NotificationDto;
import be.kdg.backend_game.service.dto.NotificationsDto;
import be.kdg.backend_game.service.exception.AccountNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
public class GameService {
    public static final short TOTAL_SERFS_COUNT = 7;
    private static final Logger logger = Logger.getLogger(GameService.class.getPackageName());
    private final GameRepository gameRepository;
    private final LobbyRepository lobbyRepository;
    private final PlayerRepository playerRepository;
    private final SerfRepository serfRepository;
    private final TileRepository tileRepository;
    private final TurnService turnService;
    private final GameHistoryService gameHistoryService;
    private final AccountRepository accountRepository;

    public GameService(GameRepository gameRepository, LobbyRepository lobbyRepository, PlayerRepository playerRepository, SerfRepository serfRepository, TileRepository tileRepository, TurnService turnService, GameHistoryService gameHistoryService, AccountRepository accountRepository) {
        this.gameRepository = gameRepository;
        this.lobbyRepository = lobbyRepository;
        this.playerRepository = playerRepository;
        this.serfRepository = serfRepository;
        this.tileRepository = tileRepository;
        this.turnService = turnService;
        this.gameHistoryService = gameHistoryService;
        this.accountRepository = accountRepository;
    }

    public Optional<Game> retrieveGameWithPlayers(UUID gameId) {
        return gameRepository.findByIdWithPlayers(gameId);
    }

    public Optional<Game> createGame(UUID lobbyId) {
        var optionalLobby = lobbyRepository.findById(lobbyId);
        if (optionalLobby.isEmpty()) return Optional.empty();
        var lobby = optionalLobby.get();
        try {
            //close lobby
            lobby.setClosed(true);
            lobbyRepository.save(lobby);

            var game = new Game();
            game.setGameTypeEnum(lobby.getGameTypeEnum());
            game.setMaxTurnDurationTime(lobby.getGameTypeEnum().getTurnDuration());
            game.setLobby(lobby);
            game = gameRepository.save(game);
            List<Color> availableColors = new ArrayList<>();
            Collections.addAll(availableColors, Color.values());
            Collections.shuffle(availableColors);
            //set players
            List<Player> playersList = new ArrayList<>();
            for (LobbyParticipant lp : lobby.getLobbyParticipants()) {
                var newPlayer = new Player();
                newPlayer.setGame(game);
                newPlayer.setAccount(lp.getLobbyParticipantId().getAccount());
                assignColor(newPlayer, availableColors);
                playersList.add(newPlayer);
            }
            // Shuffle the playersList to randomize the order
            Collections.shuffle(playersList);
            //save players
            playersList = playerRepository.saveAll(playersList);
            //set serfs
            seedSerfsForPlayers(playersList);


            //set tiles
            if (!seedTiles(game)) return Optional.empty();
            //set turn
            if (turnService.createTurn(game.getGameId()).isEmpty()) return Optional.empty();


            return Optional.of(game);
        } catch (DataIntegrityViolationException e) {
            logger.log(Level.WARNING, "Data integrity violation while creating a game: " + e.getMessage());
            throw new DataIntegrityViolationException(e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error caused while creating a game: " + e.getMessage());
            throw new DataIntegrityViolationException(e.getMessage());
        }
    }

    private void seedSerfsForPlayers(List<Player> players) {
        List<Serf> serfs = new ArrayList<>();
        for (Player player : players) {
            for (var i = 0; i < TOTAL_SERFS_COUNT; i++) {
                var serf = new Serf(player);
                serfs.add(serf);
            }
        }
        serfRepository.saveAll(serfs);
    }

    public Optional<Game> retrieveGameWithLobbyId(UUID lobbyId) {
        return gameRepository.findGameByLobby_LobbyId(lobbyId);
    }

    public List<Serf> retrieveSerfsByPlayer(int playerNumber) {
        return serfRepository.findSerfByPlayer_PlayerNumber(playerNumber);
    }

    public List<Tile> retrieveTilesByGame(UUID gameId) {
        return tileRepository.findTilesByGame_GameId(gameId);
    }

    private boolean seedTiles(Game game) {
        List<Tile> tiles = TileSeedEnum.seedTiles();
        Collections.shuffle(tiles);
        for (var tile : tiles) {
            tile.setGame(game);
            if (tile.getTileName().equals("TILE1START")) tile.setPlaced(true);
        }
        try {
            tileRepository.saveAll(tiles);
            return true;
        } catch (DataIntegrityViolationException e) {
            logger.log(Level.WARNING, "Data integrity violation while seeding tiles: " + e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error caused while seeding tiles: " + e.getMessage());
        }
        return false;
    }

    //TODO: fix delete game, maybe useful for tests
    public void deleteGame(UUID gameId) {
        var game = gameRepository.findById(gameId).orElseThrow();
        if (game.isGameOver()) {
            gameHistoryService.disconnectFromGame(gameId);
        }
        gameRepository.delete(game);
    }

    public NotificationsDto getNotifications(UUID subjectId) {
        List<NotificationDto> notificationDtos = new ArrayList<>();
        Account account = accountRepository.findAccountBySubjectId(subjectId)
                .orElseThrow(() -> new AccountNotFoundException("Could not find an account for account id " + subjectId));

        List<Game> gamesWhereIsPlayerTurn = turnService.getGamesWhereIsPlayerTurn(account.getAccountId());
        if (gamesWhereIsPlayerTurn.isEmpty()) return new NotificationsDto(0, Collections.emptyList());
        for (Game game : gamesWhereIsPlayerTurn) {
            List<String> nicknames = new ArrayList<>();
            for (Player player : game.getPlayers()) {
                nicknames.add(player.getAccount().getNickName());
            }
            var beginTurn = game.getTurns().stream()
                    .max(Comparator.comparing(Turn::getBeginTurn))
                    .orElseThrow(NoSuchElementException::new).getBeginTurn();
            var timePassed = formatRelativeTime(beginTurn);
            notificationDtos.add(new NotificationDto(game.getLobby().getLobbyName(), game.getGameId(), game.getGameTypeEnum(), timePassed.getAmount(), timePassed.getTimeEnum(), nicknames));
        }
        return new NotificationsDto(notificationDtos.size(), notificationDtos);
    }

    private PassedTime formatRelativeTime(LocalDateTime timestamp) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(timestamp, now);

        if (duration.toHours() < 1) {
            long minutes = duration.toMinutes();
            return new PassedTime((int) minutes, TimeEnum.MINUTE);
        } else if (duration.toDays() < 1) {
            long hours = duration.toHours();
            return new PassedTime((int) hours, TimeEnum.HOUR);
        } else {
            long days = duration.toDays();
            return new PassedTime((int) days, TimeEnum.DAY);
        }
    }
    private void assignColor(Player player, List<Color> availableColors) {
        if (!availableColors.isEmpty()) {
            Color assignedColor = availableColors.remove(0);
            player.setColor(assignedColor);
        }
    }

    public Integer retrieveMaxTurnDuration(UUID gameId) {
        var game = gameRepository.findById(gameId).orElseThrow();
        return game.getGameTypeEnum().getTurnDuration();
    }

    @Getter
    @AllArgsConstructor
    private static class PassedTime {
        private int amount;
        private TimeEnum timeEnum;
    }
}
