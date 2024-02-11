package be.kdg.backend_game.service.game;

import be.kdg.backend_game.domain.game.OrientationEnum;
import be.kdg.backend_game.domain.game.Tile;
import be.kdg.backend_game.repository.TileRepository;
import be.kdg.backend_game.service.exception.TileNotFoundException;
import be.kdg.backend_game.service.game.dto.PlacedTilesDto;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
public class TileService {
    private final TileRepository tileRepository;
    private static final Logger logger = Logger.getLogger(TileService.class.getPackageName());

    public TileService(TileRepository tileRepository) {
        this.tileRepository = tileRepository;
    }

    public Optional<Tile> drawTile(UUID gameId) {
        return tileRepository.findFirstByPlacedAndDiscardedAndGame_GameId(false, false, gameId);
    }

    public Tile placeTile(UUID tileId, int xValue, int yValue, OrientationEnum orientation) {
        var optionalTile = tileRepository.findById(tileId);
        if (optionalTile.isEmpty()) throw new TileNotFoundException();
        var tile = optionalTile.get();
        tile.setX(xValue);
        tile.setY(yValue);
        tile.setOrientation(orientation);
        tile.setPlaced(true);
        try {
            return tileRepository.save(tile);
        } catch (DataIntegrityViolationException e) {
            logger.log(Level.WARNING, "Data integrity violation while placing tile: " + e.getMessage());
            throw new DataIntegrityViolationException(e.getMessage());
        }
    }

    public List<Tile> retrievePlacedTiles(UUID gameId) {
        return tileRepository.findAllPlacedTilesByGameId(true, gameId);
    }

    public Optional<Tile> retrieveTile(UUID tileId) {
        return tileRepository.findById(tileId);
    }

    public void discardTile(UUID tileId) {
        var optionalTile = tileRepository.findById(tileId);
        if (optionalTile.isEmpty()) return;
        discardTile(optionalTile.get());
    }
    public Optional<Tile> discardTile(Tile tile) {
        tile.setDiscarded(true);
        try {
            return Optional.of(tileRepository.save(tile));
        } catch (DataIntegrityViolationException e) {
            logger.log(Level.WARNING, "Data integrity violation while discarding tile: " + e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<Tile> retrieveTileByCoordinates(int x, int y, UUID gameId) {
        return tileRepository.findTilesByCoordinates(x, y, gameId);
    }

    public Optional<Tile> retrieveTileByNameAndGameId(String name, UUID gameId) {
        return tileRepository.findTileByNameAndGameId(name, gameId);
    }

    public PlacedTilesDto retrievePlacedTilesWithTileCount(UUID gameId) {
        var tiles = tileRepository.findTilesByGame_GameId(gameId);
        if (tiles.isEmpty()) return null;
        return new PlacedTilesDto(tiles);
    }
}
