package be.kdg.backend_game.service.game;

import be.kdg.backend_game.domain.game.*;
import be.kdg.backend_game.repository.AccountRepository;
import be.kdg.backend_game.repository.PlayerRepository;
import be.kdg.backend_game.repository.TurnRepository;
import be.kdg.backend_game.service.JwtExtractorHelper;
import be.kdg.backend_game.service.game.dto.TilePlacementDto;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.*;

@Transactional
@Service
public class GameRulesService {
    private static final Logger logger = LoggerFactory.getLogger(GameRulesService.class.toString());
    private final TileService tileService;
    private final AccountRepository accountRepository;
    private final TurnRepository turnRepository;

    public GameRulesService(TileService tileService, AccountRepository accountRepository, TurnRepository turnRepository) {
        this.tileService = tileService;
        this.accountRepository = accountRepository;
        this.turnRepository = turnRepository;
    }

    public boolean checkIfTilePlaceable(UUID gameId, Tile tile) {
        return retrieveLegalTilePlacementsForAllSides(gameId, tile.getTileId()).isPresent();
    }

    public boolean checkIfValidTilePlacement(Tile tile, int xValue, int yValue, OrientationEnum orientation) {
        var legalTilePlacements = retrieveLegalTilePlacementsForAllSides(tile.getGame().getGameId(), tile.getTileId());
        return legalTilePlacements.map(tilePlacements -> tilePlacements.contains(new TilePlacement(xValue, yValue, orientation))).orElse(false);
    }

    public Optional<List<TilePlacementDto>> retrieveLegalTilePlacementsDto(UUID gameId, Jwt token) {
        var subjectId = JwtExtractorHelper.convertjwtToSubjectId(token);
        var reqAccount = accountRepository.findAccountBySubjectId(subjectId);
        if (reqAccount.isEmpty()) {
            logger.error("Player " + subjectId + " not found");
            return Optional.empty();
        }


        var turn = turnRepository.findLatestTurnByGameId(gameId);
        if (turn.isEmpty()) {
            logger.error("Turn not found");
            return Optional.empty();
        }

        List<TilePlacementDto> tilePlacementDtos = new ArrayList<>();

        if (turn.get().getPlayer().getAccount().getAccountId().equals(reqAccount.get().getAccountId())) {
            logger.info("Player " + reqAccount.get().getAccountId() + " is allowed to place a tile");

            var tileToPlace = turn.get().getTile();
            if (tileToPlace == null) {
                logger.error("Tile not found");
                return Optional.empty();
            }

            Optional<List<TilePlacement>> legalPlacements = retrieveLegalTilePlacementsForAllSides(gameId, tileToPlace.getTileId());
            if (legalPlacements.isEmpty()) {
                logger.info("No legal placements found");
                return Optional.of(tilePlacementDtos);
            }

            for (TilePlacement placement : legalPlacements.get()) {
                tilePlacementDtos.add(new TilePlacementDto(tileToPlace.getTileImage(), placement.getOrientation().ordinal(), placement.getXValue(), placement.getYValue()));
            }
        } else {
            logger.info("Player " + reqAccount.get().getAccountId() + " is not allowed to place a tile");
        }
        return Optional.of(tilePlacementDtos);

    }

    // Keep this method as public, it is needed to run unit tests correctly
    public Optional<List<TilePlacement>> retrieveLegalTilePlacementsForAllSides(UUID gameId, UUID tileId) {
        var placedTiles = tileService.retrievePlacedTiles(gameId);
        Optional<Tile> optionalTileToPlace = tileService.retrieveTile(tileId);
        if (optionalTileToPlace.isEmpty()) return Optional.empty();
        var tileToPlace = optionalTileToPlace.get();
        List<TilePlacement> legalTilePlacements = new ArrayList<>();
        List<TilePlacement> placedTilePlacements = new ArrayList<>();
        for (Tile placedTile : placedTiles) {
            placedTilePlacements.add(new TilePlacement(placedTile.getX(), placedTile.getY(), placedTile.getOrientation()));
        }

        for (Tile placedTile : placedTiles) {
            List<TilePlacement> placementsToCheck = new ArrayList<>();
            placementsToCheck.add(new TilePlacement(placedTile.getX(), placedTile.getY() + 1, OrientationEnum.ROTATION_0));
            placementsToCheck.add(new TilePlacement(placedTile.getX() + 1, placedTile.getY(), OrientationEnum.ROTATION_0));
            placementsToCheck.add(new TilePlacement(placedTile.getX(), placedTile.getY() - 1, OrientationEnum.ROTATION_0));
            placementsToCheck.add(new TilePlacement(placedTile.getX() - 1, placedTile.getY(), OrientationEnum.ROTATION_0));
            for (TilePlacement placementToCheck : placementsToCheck) {
                if (!placedTilePlacements.contains(placementToCheck)) {
                    HashMap<TileSideEnum, TileTypeEnum> requiredSides = getRequiredSides(placedTiles, placementToCheck);
                    List<TilePlacement> legalTilePlacementsForThisTile = checkForLegalTilePlacements(requiredSides, tileToPlace, placementToCheck);
                    legalTilePlacements.addAll(legalTilePlacementsForThisTile);
                }
            }
        }
        if (legalTilePlacements.isEmpty()) return Optional.empty();
        return Optional.of(legalTilePlacements);
    }

    private List<TilePlacement> checkForLegalTilePlacements(HashMap<TileSideEnum, TileTypeEnum> requiredSides, Tile tileToPlace, TilePlacement possiblePlacement) {
        List<TilePlacement> legalPlacements = new ArrayList<>();
        for (OrientationEnum orientation : OrientationEnum.values()) {
            HashMap<TileSideEnum, TileTypeEnum> tileSides = rotateTile(tileToPlace, orientation);
            if ((requiredSides.get(TileSideEnum.TOP) == null || requiredSides.get(TileSideEnum.TOP) == tileSides.get(TileSideEnum.TOP)) &&
                    (requiredSides.get(TileSideEnum.RIGHT) == null || requiredSides.get(TileSideEnum.RIGHT) == tileSides.get(TileSideEnum.RIGHT)) &&
                    (requiredSides.get(TileSideEnum.BOTTOM) == null || requiredSides.get(TileSideEnum.BOTTOM) == tileSides.get(TileSideEnum.BOTTOM)) &&
                    (requiredSides.get(TileSideEnum.LEFT) == null || requiredSides.get(TileSideEnum.LEFT) == tileSides.get(TileSideEnum.LEFT))) {
                legalPlacements.add(new TilePlacement(possiblePlacement.getXValue(), possiblePlacement.getYValue(), orientation));
            }
        }
        return legalPlacements;
    }

    private HashMap<TileSideEnum, TileTypeEnum> rotateTile(Tile tile, OrientationEnum orientation) {
        HashMap<TileSideEnum, TileTypeEnum> rotatedTile = new HashMap<>();
        switch (orientation) {
            case ROTATION_0 -> {
                rotatedTile.put(TileSideEnum.TOP, tile.getTileZones().get(1));
                rotatedTile.put(TileSideEnum.RIGHT, tile.getTileZones().get(5));
                rotatedTile.put(TileSideEnum.BOTTOM, tile.getTileZones().get(7));
                rotatedTile.put(TileSideEnum.LEFT, tile.getTileZones().get(3));
            }
            case ROTATION_90 -> {
                rotatedTile.put(TileSideEnum.TOP, tile.getTileZones().get(3));
                rotatedTile.put(TileSideEnum.RIGHT, tile.getTileZones().get(1));
                rotatedTile.put(TileSideEnum.BOTTOM, tile.getTileZones().get(5));
                rotatedTile.put(TileSideEnum.LEFT, tile.getTileZones().get(7));
            }
            case ROTATION_180 -> {
                rotatedTile.put(TileSideEnum.TOP, tile.getTileZones().get(7));
                rotatedTile.put(TileSideEnum.RIGHT, tile.getTileZones().get(3));
                rotatedTile.put(TileSideEnum.BOTTOM, tile.getTileZones().get(1));
                rotatedTile.put(TileSideEnum.LEFT, tile.getTileZones().get(5));
            }
            case ROTATION_270 -> {
                rotatedTile.put(TileSideEnum.TOP, tile.getTileZones().get(5));
                rotatedTile.put(TileSideEnum.RIGHT, tile.getTileZones().get(7));
                rotatedTile.put(TileSideEnum.BOTTOM, tile.getTileZones().get(3));
                rotatedTile.put(TileSideEnum.LEFT, tile.getTileZones().get(1));
            }
        }
        return rotatedTile;
    }


    private HashMap<TileSideEnum, TileTypeEnum> getRequiredSides(List<Tile> placedTiles, TilePlacement possibleTilePlacement) {
        HashMap<TileSideEnum, TileTypeEnum> requiredSides = new HashMap<>();
        var topNeighbour = placedTiles.stream().filter(tile -> tile.getX() == possibleTilePlacement.getXValue() && tile.getY() == possibleTilePlacement.getYValue() + 1).findFirst();
        var rightNeighbour = placedTiles.stream().filter(tile -> tile.getX() == possibleTilePlacement.getXValue() + 1 && tile.getY() == possibleTilePlacement.getYValue()).findFirst();
        var bottomNeighbour = placedTiles.stream().filter(tile -> tile.getX() == possibleTilePlacement.getXValue() && tile.getY() == possibleTilePlacement.getYValue() - 1).findFirst();
        var leftNeighbour = placedTiles.stream().filter(tile -> tile.getX() == possibleTilePlacement.getXValue() - 1 && tile.getY() == possibleTilePlacement.getYValue()).findFirst();
        if (topNeighbour.isPresent()) {
            var sidesTopNeighbour = rotateTile(topNeighbour.get(), topNeighbour.get().getOrientation());
            requiredSides.put(TileSideEnum.TOP, sidesTopNeighbour.get(TileSideEnum.BOTTOM));
        } else requiredSides.put(TileSideEnum.TOP, null);
        if (rightNeighbour.isPresent()) {
            var sidesRightNeighbour = rotateTile(rightNeighbour.get(), rightNeighbour.get().getOrientation());
            requiredSides.put(TileSideEnum.RIGHT, sidesRightNeighbour.get(TileSideEnum.LEFT));
        } else requiredSides.put(TileSideEnum.RIGHT, null);
        if (bottomNeighbour.isPresent()) {
            var sidesBottomNeighbour = rotateTile(bottomNeighbour.get(), bottomNeighbour.get().getOrientation());
            requiredSides.put(TileSideEnum.BOTTOM, sidesBottomNeighbour.get(TileSideEnum.TOP));
        } else requiredSides.put(TileSideEnum.BOTTOM, null);
        if (leftNeighbour.isPresent()) {
            var sidesLeftNeighbour = rotateTile(leftNeighbour.get(), leftNeighbour.get().getOrientation());
            requiredSides.put(TileSideEnum.LEFT, sidesLeftNeighbour.get(TileSideEnum.RIGHT));
        } else requiredSides.put(TileSideEnum.LEFT, null);
        return requiredSides;
    }

}
