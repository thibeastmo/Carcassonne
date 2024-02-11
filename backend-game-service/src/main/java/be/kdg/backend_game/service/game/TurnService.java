package be.kdg.backend_game.service.game;

import be.kdg.backend_game.domain.game.*;
import be.kdg.backend_game.repository.GameRepository;
import be.kdg.backend_game.repository.TurnRepository;
import be.kdg.backend_game.service.exception.GameNotFoundException;
import be.kdg.backend_game.service.exception.TileNotFoundException;
import be.kdg.backend_game.service.game.dto.TileDto;
import be.kdg.backend_game.service.game.tile_calculations.ScoreService;
import be.kdg.backend_game.service.statistics.UserStatisticsService;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
public class TurnService {
    private static final Logger logger = Logger.getLogger(TurnService.class.getPackageName());
    private final TurnRepository turnRepository;
    private final GameRepository gameRepository;
    private final PlayerService playerService;
    private final TileService tileService;
    private final GameRulesService gameRulesService;
    private final UserStatisticsService userStatisticsService;
    private final ScoreService scoreService;

    public TurnService(TurnRepository turnRepository, GameRepository gameRepository, PlayerService playerService, TileService tileService, GameRulesService gameRulesService, UserStatisticsService userStatisticsService, ScoreService scoreService) {
        this.turnRepository = turnRepository;
        this.gameRepository = gameRepository;
        this.playerService = playerService;
        this.tileService = tileService;
        this.gameRulesService = gameRulesService;
        this.userStatisticsService = userStatisticsService;
        this.scoreService = scoreService;
    }

    @Transactional
    public Optional<Turn> retrieveCurrentTurnByGame(UUID gameId) {
        var optionalTurn = turnRepository.findLatestTurnByGameId(gameId);
        if (optionalTurn.isEmpty()) return Optional.empty();
        var turn = optionalTurn.get();
        var maxTurnDurationInMinutes = turn.getGame().getGameTypeEnum().getTurnDuration();

        //check if the turn is over already and if the turn is over, check how many turns further the game should be. Once you know that, create the new turn
        var currentTime = getCurrentTime();
        var beginTurn = turn.getBeginTurn();
        var zone = getZone();
        long maxTurnDurationInMilliseconds = (long) maxTurnDurationInMinutes * 60 * 1000;
        long endTurn = beginTurn.atZone(zone).toInstant().toEpochMilli() + maxTurnDurationInMilliseconds;
        if (endTurn > currentTime.atZone(zone).toInstant().toEpochMilli()) {
            //return the current turn
            return Optional.of(turn);
        }

        //create a new turn because current turn is already over
        try {
            return createTurnWithSkips(turn, zone, maxTurnDurationInMilliseconds);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Problem occurred during retrieveCurrentTurnByGame: " + e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<Turn> createTurnWithSkips(Turn lastTurn, ZoneId zone, long maxTurnDurationInMilliseconds) throws Exception {
        try {
            long beginTurnInMilliseconds = lastTurn.getBeginTurn().atZone(zone).toInstant().toEpochMilli();
            long timeDiffCurrentAndBeginTurn = getCurrentTime().atZone(zone).toInstant().toEpochMilli() - beginTurnInMilliseconds;
            int amountOfSkips = (int) Math.floor((double) timeDiffCurrentAndBeginTurn / maxTurnDurationInMilliseconds);
            var game = lastTurn.getGame();
            tileService.discardTile(lastTurn.getTile());
            Player nextPlayer = lastTurn.getPlayer();
            int index = 0;
            Turn turn = lastTurn;
            while (index < amountOfSkips) {
                long beginNewTurn = (long) (index+1) * maxTurnDurationInMilliseconds + beginTurnInMilliseconds;
                try {
                    //discard tile of last turn
                    logger.log(Level.INFO, "Creating a new turn after time ran out of current turn with id: " + turn.getTurnId());

                    turn = new Turn();
                    turn.setBeginTurn(getTimeByMilliseconds(beginNewTurn));
                    nextPlayer = getPlayerOfANextTurn(game.getPlayers(), amountOfSkips, nextPlayer.getPlayerNumber());
                    turn.setPlayer(nextPlayer);
                    turn.setGame(game);

                    var optionalTile = getTileForTurn(game);
                    if (optionalTile.isEmpty()) return Optional.of(lastTurn);
                    var drawnTile = optionalTile.get();
                    if (index + 1 < amountOfSkips) {
                        var optionalDiscardedTile = tileService.discardTile(drawnTile);
                        logger.log(Level.INFO, "Tile discarded for the turn that ran out of time with tile id: " + drawnTile.getTileId());
                        if (optionalDiscardedTile.isEmpty()) return Optional.of(lastTurn);
                        drawnTile = optionalDiscardedTile.get();
                    }
                    turn.setTile(drawnTile);
                    logger.log(Level.INFO, "Creating new turn with player (" + nextPlayer.getPlayerNumber() + ") and tile (" + drawnTile.getTileId() + ") that has " + (drawnTile.isDiscarded() ? "not" : "") + " been discarded.");
                    turn = turnRepository.save(turn);
                    logger.log(Level.INFO, "Turn has successfully been created!");
                } catch (DataIntegrityViolationException e) {
                    logger.log(Level.WARNING, "Data integrity violation while creating a turn in createTurnWithSkips(): " + e.getMessage());
                    throw new DataIntegrityViolationException(e.getMessage());
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error caused while creating a turn: " + e.getMessage());
                    throw new Exception(e.getMessage());
                }
                index++;
            }
            return Optional.of(turn);
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Something went wrong while creating one ore many turns that are skipped: " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    private Optional<Tile> getTileForTurn(Game game) {
        Optional<Tile> tile;
        boolean tileDiscarded;
        do {
            tile = tileService.drawTile(game.getGameId());
            if (tile.isEmpty() || game.isGameOver()) {
                logger.log(Level.INFO, "No tile could be drawn so the game has ended.");
                game.setGameOver(true);
                scoreService.addFinalScore(game.getGameId());
                game = gameRepository.save(game);
                userStatisticsService.updateStatisticsAndLoyaltyPointsByGame(game.getGameId());
                return Optional.empty();
            }
            if (!gameRulesService.checkIfTilePlaceable(game.getGameId(), tile.get())) {
                tileService.discardTile(tile.get());
                tileDiscarded = true;
            } else tileDiscarded = false;
        } while (tileDiscarded);
        return tile;
    }

    private ZoneId getZone() {
        return ZoneId.of("Europe/London");
    }

    private LocalDateTime getCurrentTime() {
        return getCurrentTime(Instant.now());
    }

    private LocalDateTime getTimeByMilliseconds(long milliseconds) {
        return getCurrentTime(Instant.ofEpochMilli(milliseconds));
    }

    private LocalDateTime getCurrentTime(Instant instant) {
        ZonedDateTime local = instant.atZone(getZone());
        return local.toLocalDateTime();
    }

    @Transactional
    public Optional<Turn> createTurn(UUID gameId) {
        Game game = gameRepository.findGameByGameIdWithAccount(gameId).orElseThrow(() -> new GameNotFoundException("No game with id " + gameId + " could be found"));
        var turnOptional = turnRepository.findLatestTurnByGameId(gameId);
        if (turnOptional.isPresent()) {
            // TODO: put these checks in one check
            var lastTurn = turnOptional.get();
            var lastPlacedTile = lastTurn.getTile();
            scoreService.calculateScoreOfPlacedRoadOrCrossroad(lastPlacedTile);
            scoreService.checkTile(lastPlacedTile.getTileId());
        }

        try {
            var turn = new Turn();
            turn.setBeginTurn(getCurrentTime());
            var optionalPlayer = getPlayerOfNextTurn(gameId);
            if (optionalPlayer.isEmpty()) return Optional.empty();
            turn.setPlayer(optionalPlayer.get());
            turn.setGame(game);

            var optionalTile = getTileForTurn(game);
            if (optionalTile.isEmpty()) {
                return turnOptional;
            } else {
                if (turnOptional.isPresent()) {
                    var tile = optionalTile.get();
                    var latestTurn = turnOptional.get();
                    if (tile.getTileId() == latestTurn.getTile().getTileId()) return turnOptional;
                }
            }
            turn.setTile(optionalTile.get());

            turn.setTile(optionalTile.get());
            turn = turnRepository.save(turn);
            return Optional.of(turn);
        } catch (DataIntegrityViolationException e) {
            logger.log(Level.WARNING, "Data integrity violation while creating a turn in createTurn(): " + e.getMessage());
            throw new DataIntegrityViolationException(e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error caused while creating a turn: " + e.getMessage());
        }
        return Optional.empty();
    }

    private Optional<Player> getPlayerOfNextTurn(UUID gameId) {
        var players = playerService.retrievePlayersByGame(gameId);
        var optionalTurn = retrieveCurrentTurnByGame(gameId);
        if (optionalTurn.isEmpty()) {
            if (players.isEmpty()) return Optional.empty();
            return Optional.of(players.get(0)); //first player
        }
        var turn = optionalTurn.get();
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getPlayerNumber() == turn.getPlayer().getPlayerNumber()) {
                if (i + 1 < players.size()) return Optional.of(players.get(i + 1));
                return Optional.of(players.get(0));
            }
        }
        return Optional.empty();
    }

    private Player getPlayerOfANextTurn(List<Player> players, int amountOfSkips, int playerNumberOfLastTurn) {
        int index = 0;
        while (players.get(index).getPlayerNumber() != playerNumberOfLastTurn) {
            index++;
            if (index >= players.size()) index = 0;
        }
        while (amountOfSkips > 0) {
            index++;
            if (index >= players.size()) index = 0;
            amountOfSkips--;
        }
        var player = players.get(index);
        if (player.getPlayerNumber() == playerNumberOfLastTurn) {
            logger.log(Level.WARNING, "Last player number is the same number as the next player: (" + playerNumberOfLastTurn + " == " + player.getPlayerNumber() + ")");
        }
        else {
            logger.log(Level.INFO, "Last player number is not the same as the next player: (" + playerNumberOfLastTurn + " != " + player.getPlayerNumber() + ")");
        }
        return player;
    }

    public Tile retrieveCurrentTileByGame(UUID gameId) {
        var optionalTurn = turnRepository.findLatestTurnByGameId(gameId);
        return optionalTurn.map(Turn::getTile).orElse(null);
    }

    public UUID retrieveCurrentTileIdByGame(UUID gameId) {
        return turnRepository.findLatestTurnTileIdByGameId(gameId).orElseThrow(() -> new TileNotFoundException("Tile for current turn not found"));
    }

    public List<Game> getGamesWhereIsPlayerTurn(UUID accountId) {
        return turnRepository.findGamesWhereIsPlayerTurn(accountId);
    }

    public TileDto retrieveCurrentTileDtoByGameId(UUID gameId) {
        Tile tile = retrieveCurrentTileByGame(gameId);
        if (tile == null) return null;
        return new TileDto(tile);
    }
}
