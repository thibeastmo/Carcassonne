package be.kdg.backend_game.service.game;

import be.kdg.backend_game.domain.game.Player;
import be.kdg.backend_game.domain.game.Serf;
import be.kdg.backend_game.domain.game.Tile;
import be.kdg.backend_game.domain.game.Turn;
import be.kdg.backend_game.repository.SerfRepository;
import be.kdg.backend_game.repository.TileRepository;
import be.kdg.backend_game.service.exception.NoSerfsAvailableException;
import jakarta.transaction.Transactional;
import be.kdg.backend_game.repository.TurnRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
public class SerfService {
    private static final Logger logger = Logger.getLogger(SerfService.class.getPackageName());
    private final SerfRepository serfRepository;
    private final TurnRepository turnRepository;
    private final TileRepository tileRepository;


    public SerfService(SerfRepository serfRepository, TurnRepository turnRepository, TileRepository tileRepository) {
        this.serfRepository = serfRepository;
        this.turnRepository = turnRepository;
        this.tileRepository = tileRepository;
    }

    public List<Serf> getUsedSerfsByGameId(UUID gameId) {
        return serfRepository.findSerfByPlayer_Game_GameIdAndTileIsNotNullAndTile_TileIdIsNotNull(gameId);
    }

    //TODO: fix this one, return type changed from boolean to Serf
    public Serf setTileForSerf(int tileZoneId, Tile tile, Player player) throws Exception {
        try {
            var optionalSerf = retrieveAvailableSerfByPlayerId(player.getPlayerNumber());
            if (optionalSerf.isEmpty())
                throw new NoSerfsAvailableException("There were no serfs available for the given player");
            var serf = optionalSerf.get();
            serf.setTile(tile);
            serf.setTileZoneId(tileZoneId);
            tile.setSerf(serf);
            tileRepository.save(tile);
            return serfRepository.save(serf);
        } catch (DataIntegrityViolationException e) {
            logger.log(Level.WARNING, "Data integrity violation while set a serf on a tile: " + e.getMessage());
            throw new DataIntegrityViolationException(e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error caused while setting a serf on a tile: " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    public Serf setTileForSerf(int tileZoneId, Tile tile, Turn turn) throws Exception {
        try {
            var serf = retrieveAvailableSerfByPlayerId(turn.getPlayer().getPlayerNumber()).orElseThrow(() -> new NoSerfsAvailableException("This player has no available serfs"));
            serf.setTile(tile);
            serf.setTileZoneId(tileZoneId);
            turn.setPlacedSerf(true);
            serf.setPlaced(true);
            turnRepository.save(turn);
            return serfRepository.save(serf);
        } catch (DataIntegrityViolationException e) {
            logger.log(Level.WARNING, "Data integrity violation while set a serf on a tile: " + e.getMessage());
            throw new DataIntegrityViolationException(e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error caused while setting a serf on a tile: " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }


    public Optional<Serf> retrieveSerf(UUID serfId) {
        return serfRepository.findById(serfId);
    }

    private Optional<Serf> retrieveAvailableSerfByPlayerId(int playerNumber) {
        return serfRepository.findFirstSerfByPlayer_PlayerNumberAndIsPlacedFalse(playerNumber);
    }

    public List<Serf> retrieveSerfsByTileId(UUID tileId) {
        return serfRepository.findSerfByTile_TileId(tileId);
    }

    public void returnSerf(Serf serf) {
        Tile tile = serf.getTile();
        if (tile != null) {
            serf.setTile(null);
            tile.setSerf(null);
            serf.setTileZoneId(0);
            serf.setPlaced(false);
            try {
                serfRepository.save(serf);
                tileRepository.save(tile);
            } catch (DataIntegrityViolationException e) {
                logger.log(Level.WARNING, "Data integrity violation while returning serf to player: " + e.getMessage());
            }
        }
    }

    public boolean hasCurrentTurnPlayerAtLeastOneSerf(UUID gameId) {
        var optionalTurn = turnRepository.findLatestTurnByGameId(gameId);
        if (optionalTurn.isEmpty()) {
            logger.log(Level.WARNING, "No turn found for game (" + gameId + ") when checking hasCurrentTurnPlayerAtLeastOneSerf");
            return false;
        }
        var turn = optionalTurn.get();
        return serfRepository.findFirstSerfByPlayer_PlayerNumberAndIsPlacedFalse(turn.getPlayer().getPlayerNumber()).isPresent();
    }
}
