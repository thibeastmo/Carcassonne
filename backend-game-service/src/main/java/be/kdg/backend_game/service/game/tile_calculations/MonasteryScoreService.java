package be.kdg.backend_game.service.game.tile_calculations;

import be.kdg.backend_game.domain.game.Serf;
import be.kdg.backend_game.domain.game.Tile;
import be.kdg.backend_game.domain.game.TileTypeEnum;
import be.kdg.backend_game.repository.PlayerRepository;
import be.kdg.backend_game.service.game.SerfService;
import be.kdg.backend_game.service.game.TileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MonasteryScoreService {


    private static final Logger logger = LoggerFactory.getLogger(MonasteryScoreService.class.toString());
    private final TileService tileService;
    private final SerfService serfService;
    private final PlayerRepository playerRepository;

    public MonasteryScoreService(TileService tileService, SerfService serfService, PlayerRepository playerRepository) {
        this.tileService = tileService;
        this.serfService = serfService;
        this.playerRepository = playerRepository;
    }

    public List<Serf> resolveMonasteryCompletion(UUID gameId) {
        var placedTiles = tileService.retrievePlacedTiles(gameId);
        var monasteryFinishedCheck = monasteryFinishedCheck(placedTiles);
        for (Serf serf : monasteryFinishedCheck) {
            logger.info("Monastery finished");
            int pointsToAdd = 9;
            serf.getPlayer().addPoints(pointsToAdd);
            serf.getPlayer().addContestedLandWon();
            serfService.returnSerf(serf);
            playerRepository.save(serf.getPlayer());
        }
        return monasteryFinishedCheck;
    }

    public static List<Serf> monasteryFinishedCheck(List<Tile> placedTiles) {
        List<Serf> serfsOnCompletedMonasteries = new ArrayList<>();
        for (Tile tile : placedTiles) {
            var isMonasteryMap = isMonasteryAndHasSerf(tile);
            if (isMonasteryMap.containsKey(Boolean.TRUE)) {
                boolean isSurrounded = true;
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if (dx == 0 && dy == 0) {
                            // is monastery
                            continue;
                        }
                        int neighborX = tile.getX() + dx;
                        int neighborY = tile.getY() + dy;
                        boolean neighborExists = false;

                        for (Tile t : placedTiles) {
                            if (t.getX() == neighborX && t.getY() == neighborY) {
                                neighborExists = true;
                                break;
                            }
                        }

                        if (!neighborExists) {
                            isSurrounded = false;
                            break;
                        }
                    }
                    if (!isSurrounded) {
                        break;
                    }
                }
                if (isSurrounded) {
                    Serf serf = isMonasteryMap.get(true);
                    if (serf != null) {
                        serfsOnCompletedMonasteries.add(serf);
                    }
                }
            }
        }
        return serfsOnCompletedMonasteries;
    }

    private static Map<Boolean, Serf> isMonasteryAndHasSerf(Tile tile) {
        Map<Boolean, Serf> serfAndMonasteryMap = new HashMap<>();
        if (tile.getTileZones().contains(TileTypeEnum.MONASTERY)) {
            if (tile.getSerf() == null) {
                serfAndMonasteryMap.put(true, null);
            } else {
                serfAndMonasteryMap.put(true, tile.getSerf());
            }

        } else {
            serfAndMonasteryMap.put(false, null);

        }
        return serfAndMonasteryMap;
    }

    public int countNeighbours(Tile tileToCheck, List<Tile> placedTiles) {
        int[][] coordinatesToCheck = new int[][]{
                {tileToCheck.getX(), tileToCheck.getY() + 1},
                {tileToCheck.getX() + 1, tileToCheck.getY() + 1},
                {tileToCheck.getX() + 1, tileToCheck.getY()},
                {tileToCheck.getX() + 1, tileToCheck.getY() - 1},
                {tileToCheck.getX(), tileToCheck.getY() - 1},
                {tileToCheck.getX() - 1, tileToCheck.getY() - 1},
                {tileToCheck.getX() - 1, tileToCheck.getY()},
                {tileToCheck.getX() - 1, tileToCheck.getY() + 1}
        };
        int neighbourCount = 0;
        for (var coords : coordinatesToCheck) {
            if (placedTiles.stream().anyMatch(tile -> tile.getX() == coords[0] && tile.getY() == coords[1]))
                neighbourCount++;
        }
        return neighbourCount;
    }

    public void addScoresToMap(Map<Integer, Integer> scoresPerPlayerNumber, int score, int playerNumber) {
        if (scoresPerPlayerNumber.containsKey(playerNumber)) {
            int existingScore = scoresPerPlayerNumber.get(playerNumber);
            int newScore = existingScore + score;
            scoresPerPlayerNumber.put(playerNumber, newScore);
        } else {
            scoresPerPlayerNumber.put(playerNumber, score);
        }
        logger.info("Adding " + score + " point(s) to player " + playerNumber);
    }



}
