package be.kdg.backend_game.service.statistics;

import be.kdg.backend_game.domain.game.Game;
import be.kdg.backend_game.domain.user_management.Account;
import be.kdg.backend_game.domain.user_management.UserLevel;
import be.kdg.backend_game.domain.user_management.UserStatistics;
import be.kdg.backend_game.domain.game.Player;
import be.kdg.backend_game.domain.game.Turn;
import be.kdg.backend_game.domain.user_management.history.GameHistory;
import be.kdg.backend_game.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserStatisticsService {
    private static final Logger logger = Logger.getLogger(UserStatisticsService.class.getPackageName());
    private final GameRepository gameRepository;
    private final UserStatisticsRepository userStatisticsRepository;
    private final AccountRepository accountRepository;
    private final GameHistoryRepository gameHistoryRepository;
    private final TurnRepository turnRepository;

    public UserStatisticsService(GameRepository gameRepository, UserStatisticsRepository userStatisticsRepository, AccountRepository accountRepository, GameHistoryRepository gameHistoryRepository, TurnRepository turnRepository) {
        this.gameRepository = gameRepository;
        this.userStatisticsRepository = userStatisticsRepository;
        this.accountRepository = accountRepository;
        this.gameHistoryRepository = gameHistoryRepository;
        this.turnRepository = turnRepository;
    }

    @Transactional
    public boolean updateStatisticsAndLoyaltyPointsByGame(UUID gameId) {
        var optionalGame = gameRepository.findGameByGameIdWithAccount(gameId);
        if (optionalGame.isEmpty()) {
            logger.log(Level.WARNING, "No game found with id: " + gameId);
            return false;
        }
        var game = optionalGame.get();
        var players = game.getPlayers();
        var turns = turnRepository.findTurnsByGame_GameId(gameId);
        boolean statisticsUpdated = updateUserStatistics(players, turns);
        if (statisticsUpdated) {
            logger.log(Level.INFO, "UserStatistics updated for game with id: " + gameId);
        } else {
            logger.log(Level.WARNING, "UserStatistics could not be updated for game with id: " + gameId);
        }
        boolean loyaltyPointsUpdated = updateLoyaltyPoints(players, turns);
        if (loyaltyPointsUpdated) {
            logger.log(Level.INFO, "LoyaltyPoints updated for game with id: " + gameId);
        } else {
            logger.log(Level.WARNING, "LoyaltyPoints could not be updated for game with id: " + gameId);
        }
        boolean gameHistoryCreated = createGameHistories(players, game);
        if (gameHistoryCreated) {
            logger.log(Level.INFO, "Game history per player created for game with id: " + gameId);
        } else {
            logger.log(Level.WARNING, "Game history per player could not be created for game with id: " + gameId);
        }
        return loyaltyPointsUpdated && statisticsUpdated && gameHistoryCreated;
    }

    private boolean createGameHistories(List<Player> players, Game game) {
        players.sort(Comparator.comparingInt(Player::getPoints));
        var gameHistories = new ArrayList<GameHistory>();
        for (var i = 0; i < players.size(); i++) {
            var player = players.get(i);
            gameHistories.add(new GameHistory((short) (i + 1), player.getPoints(), player.getColor(), player.getAccount(), game));
        }
        if (gameHistoryRepository.findGameHistoryByGameIdOrderByPlayerNumberAsc(game.getGameId()).isEmpty()) {
            logger.log(Level.INFO, "Creating game history because it's the end of a game: " + game.getGameId());
            gameHistoryRepository.saveAll(gameHistories);
        }
        else {
            logger.log(Level.INFO, "Not creating game history because it already exists: " + game.getGameId());
        }
        return true;
    }

    private boolean updateLoyaltyPoints(List<Player> players, List<Turn> turns) {
        Map<Player, Integer> amountOfTurnsActuallyPlayedPerPlayer = new HashMap<>();
        Map<Player, List<Turn>> playerTurns = initializeMappedTurnsForPlayers(turns);
        for (var playerTurn : playerTurns.entrySet()) {
            var amountOfTurnsActuallyPlayed = playerTurn.getValue().stream().filter(t -> t.getTile().isPlaced()).toList().size();
            amountOfTurnsActuallyPlayedPerPlayer.put(playerTurn.getKey(),amountOfTurnsActuallyPlayed);
        }
        List<Account> accounts = new ArrayList<>();
        for (var i = 0; i < players.size(); i++) {
            var player = players.get(i);
            var extraLoyaltyPoints = i == 0 ? player.getPoints() : (int)Math.ceil((double)player.getPoints() / 2);
            var turnsPlayed = amountOfTurnsActuallyPlayedPerPlayer.get(player);
            extraLoyaltyPoints += i == 0 ? turnsPlayed * 2 : turnsPlayed;
            var account = player.getAccount();
            account.setLoyaltyPoints(account.getLoyaltyPoints() + extraLoyaltyPoints);
            accounts.add(account);
        }
        accountRepository.saveAll(accounts);
        return true;
    }

    private boolean updateUserStatistics(List<Player> players, List<Turn> turns) {
        Map<Player, List<Turn>> playerTurns = initializeMappedTurnsForPlayers(turns);

        int winnerNumber = getPlayerNumberOfWinner(players);
        List<UserStatistics> userStatisticsList = new ArrayList<>();
        List<Account> accountList = new ArrayList<>();

        for (var player : players) {
            var account = player.getAccount();
            var optionalUserStatistics = userStatisticsRepository.findUserStatisticsByAccount_AccountId(account.getAccountId());
            if (optionalUserStatistics.isEmpty()) {
                logger.log(Level.WARNING, "No userstatistics found for player with id: " + account.getAccountId());
                logger.log(Level.INFO, "Creating new userstatistics for player with id: " + account.getAccountId());
                optionalUserStatistics = Optional.of(new UserStatistics(account));
            }
            var userStatistics = optionalUserStatistics.get();
            if (player.getPlayerNumber() == winnerNumber) {
                userStatistics.setGamesWon(userStatistics.getGamesWon() + 1);
                account.setExperiencePoints(account.getExperiencePoints() + player.getPoints());
            } else {
                account.setExperiencePoints(account.getExperiencePoints() + player.getPoints() / 2);
            }
            userStatistics.setGamesPlayed(userStatistics.getGamesPlayed() + 1);
            userStatistics.setTotalScoreAchieved(userStatistics.getTotalScoreAchieved() + player.getPoints());
            int amountOfNewlyPlacedTiles = 0;
            int amountOfNewlyPlacedSerfs = 0;
            for (var playerTurn : playerTurns.get(player)) {
                if (playerTurn.getTile().isPlaced()) amountOfNewlyPlacedTiles++;
                if (playerTurn.isPlacedSerf()) amountOfNewlyPlacedSerfs++;
            }
            userStatistics.setTilesPlaced(userStatistics.getTilesPlaced() + amountOfNewlyPlacedTiles);
            userStatistics.setSerfsPlaced(userStatistics.getSerfsPlaced() + amountOfNewlyPlacedSerfs);
            userStatistics.setContestedLandWon(userStatistics.getContestedLandWon() + player.getContestedLandWon());
            userStatistics.setContestedLandLost(userStatistics.getContestedLandLost() + player.getContestedLandLost());

            userStatisticsList.add(userStatistics);
            accountList.add(account);
        }
        userStatisticsRepository.saveAll(userStatisticsList);
        accountRepository.saveAll(accountList);
        return true;
    }

    private Map<Player, List<Turn>> initializeMappedTurnsForPlayers(List<Turn> turns) {
        Map<Player, List<Turn>> playerTurns = new HashMap<>();
        for (var turn : turns) {
            var player = turn.getPlayer();
            if (playerTurns.containsKey(player)) {
                var turnsOfPlayer = playerTurns.get(player);
                turnsOfPlayer.add(turn);
            }
            else {
                List<Turn> turnsList = new ArrayList<>();
                turnsList.add(turn);
                playerTurns.put(player, turnsList);
            }
        }
        return playerTurns;
    }

    private int getPlayerNumberOfWinner(List<Player> players) {
        if (players.isEmpty()) {
            logger.log(Level.WARNING, "Players list was empty when asking for winner");
            return -1;
        }
        return players.stream()
                .max(Comparator.comparingInt(Player::getPoints))
                .get()
                .getPlayerNumber();
    }

    public Optional<UserStatistics> retrieveUserStatisticsByAccountId(UUID accountId) {
        return userStatisticsRepository.findUserStatisticsByAccount_AccountId(accountId);
    }

    public Optional<UserStatistics> retrieveUserStatisticsBySubjectId(UUID subjectId) {
        return userStatisticsRepository.findUserStatisticsByAccount_SubjectId(subjectId);
    }
}
