package be.kdg.backend_game.service.game.tile_calculations;

import be.kdg.backend_game.domain.game.*;
import be.kdg.backend_game.service.game.PlayerService;
import be.kdg.backend_game.service.game.SerfService;
import be.kdg.backend_game.service.game.TileService;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoadScoreService {

    private static final Logger logger = LoggerFactory.getLogger(RoadScoreService.class.toString());
    private final TileService tileService;
    private final SerfService serfService;
    private final PlayerService playerService;

    public RoadScoreService(TileService tileService, SerfService serfService, PlayerService playerService) {
        this.tileService = tileService;
        this.serfService = serfService;
        this.playerService = playerService;
    }

    public Map<Player, Integer> calculateScoreOfPlacedRoadOrCrossroad(Tile tile) {
        // This map contains the player and the score that needs to be added for every player (score should always be the same)
        Map<Player, Integer> winnersMap = new HashMap<>();

        if (tile.getTileZones().stream().anyMatch(tileTypeEnum -> tileTypeEnum.equals(TileTypeEnum.STREET))) {
            if (tile.getTileZones().stream().noneMatch(tileTypeEnum -> tileTypeEnum.equals(TileTypeEnum.CROSSROADS))) {
                RoadResult roadResult = checkRoadFinishedStart(tile);
                if (roadResult.isFinished() && !(roadResult.playerResults.isEmpty())) {
                    calculateScoresOfRoads(roadResult, winnersMap);
                }
            } else {
                // When a tile has crossroads, do a different check
                List<RoadResult> roadResults = checkCrossroadsSidesFinishedStart(tile);
                for (var roadResult : roadResults) {
                    if (roadResult.isFinished && !(roadResult.playerResults.isEmpty())) {
                        calculateScoresOfRoads(roadResult, winnersMap);
                    }
                }
            }
        }
        return winnersMap;
    }


    private void calculateScoresOfRoads(RoadResult roadResult, Map<Player, Integer> winnersMap) {
        var playerResults = roadResult.getPlayerResults();
        int max = Collections.max(playerResults.values());
        var winnersList = playerResults.entrySet().stream()
                .filter(entry -> entry.getValue() == max)
                .map(Map.Entry::getKey)
                .toList();
        winnersList.forEach(player ->
                winnersMap.put(player, roadResult.getFoundTiles().size())
        );
        Map<Integer, Integer> winnersMapConverted = new HashMap<>();
        winnersMap.forEach((player, value) -> {
            int playerNumber = player.getPlayerNumber();
            winnersMapConverted.put(playerNumber, value);
        });

        Map<Player, Integer> loserMap = new HashMap<>();
        playerResults.forEach((player, score) -> {
            if (!winnersList.contains(player)) {
                loserMap.put(player, roadResult.getFoundTiles().size());
            }
        });

        Map<Integer, Integer> loserMapConverted = new HashMap<>();
        loserMap.forEach((player, value) -> {
            int playerNumber = player.getPlayerNumber();
            loserMapConverted.put(playerNumber, value);
        });
        playerService.updatePlayerScores(winnersMapConverted);
        playerService.updatePlayerClaims(winnersMapConverted, loserMapConverted);
        roadResult.playerResults.forEach((player, integer) -> logger.info("Player: " + player + "Score: " + integer));
        returnRelevantSerfs(roadResult);
    }

    public List<RoadResult> checkCrossroadsSidesFinishedStart(Tile starterCrossroadTile) {
        var sides = starterCrossroadTile.rotateTile();
        List<Tile> placedTiles = tileService.retrievePlacedTiles(starterCrossroadTile.getGame().getGameId());
        int x = starterCrossroadTile.getX();
        int y = starterCrossroadTile.getY();

        List<RoadResult> totalResult = new ArrayList<>();

        var tileBottom = placedTiles.stream().filter(t -> t.getX() == x && t.getY() == y - 1).findFirst();
        var tileTop = placedTiles.stream().filter(t -> t.getX() == x && t.getY() == y + 1).findFirst();
        var tileLeft = placedTiles.stream().filter(t -> t.getX() == x - 1 && t.getY() == y).findFirst();
        var tileRight = placedTiles.stream().filter(t -> t.getX() == x + 1 && t.getY() == y).findFirst();

        if (sides.get(TileSideEnum.BOTTOM) == TileTypeEnum.STREET) {
            RoadResult roadResult = new RoadResult();
            roadResult.setFinished(false);
            if (tileBottom.isPresent()) {
                var tile = tileBottom.get();
                TileTypeEnum bottomSide = tile.rotateTile().get(TileSideEnum.TOP);
                boolean bottomFinished = checkSide(placedTiles, tile.getX(), tile.getY(), bottomSide, TileSideEnum.TOP, roadResult);
                roadResult.setFinished(bottomFinished);
                findSerfOnCrossroads(starterCrossroadTile, roadResult, TileSideEnum.BOTTOM);
                roadResult.addFoundStreet(starterCrossroadTile);
                totalResult.add(roadResult);
            }
        }
        if (sides.get(TileSideEnum.TOP) == TileTypeEnum.STREET) {
            RoadResult roadResult = new RoadResult();
            roadResult.setFinished(false);
            if (tileTop.isPresent()) {
                var tile = tileTop.get();
                var topSide = tile.rotateTile().get(TileSideEnum.BOTTOM);
                boolean topFinished = checkSide(placedTiles, tile.getX(), tile.getY(), topSide, TileSideEnum.BOTTOM, roadResult);
                roadResult.setFinished(topFinished);
                findSerfOnCrossroads(starterCrossroadTile, roadResult, TileSideEnum.TOP);
                roadResult.addFoundStreet(starterCrossroadTile);
                totalResult.add(roadResult);
            }
        }
        if (sides.get(TileSideEnum.LEFT) == TileTypeEnum.STREET) {
            RoadResult roadResult = new RoadResult();
            roadResult.setFinished(false);
            if (tileLeft.isPresent()) {
                var tile = tileLeft.get();
                var leftSide = tile.rotateTile().get(TileSideEnum.RIGHT);
                boolean leftFinished = checkSide(placedTiles, tile.getX(), tile.getY(), leftSide, TileSideEnum.RIGHT, roadResult);
                roadResult.setFinished(leftFinished);
                findSerfOnCrossroads(starterCrossroadTile, roadResult, TileSideEnum.LEFT);
                roadResult.addFoundStreet(starterCrossroadTile);
                totalResult.add(roadResult);
            }
        }
        if (sides.get(TileSideEnum.RIGHT) == TileTypeEnum.STREET) {
            RoadResult roadResult = new RoadResult();
            roadResult.setFinished(false);
            if (tileRight.isPresent()) {
                var tile = tileRight.get();
                var rightSide = tile.rotateTile().get(TileSideEnum.LEFT);
                boolean rightFinished = checkSide(placedTiles, tile.getX(), tile.getY(), rightSide, TileSideEnum.LEFT, roadResult);
                roadResult.setFinished(rightFinished);
                findSerfOnCrossroads(starterCrossroadTile, roadResult, TileSideEnum.RIGHT);
                roadResult.addFoundStreet(starterCrossroadTile);
                totalResult.add(roadResult);
            }
        }
        return totalResult;
    }

    public RoadResult checkRoadFinishedStart(Tile starterTile) {
        var sides = starterTile.rotateTile();
        List<Tile> placedTiles = tileService.retrievePlacedTiles(starterTile.getGame().getGameId());
        int x = starterTile.getX();
        int y = starterTile.getY();
        RoadResult roadResult = new RoadResult();
        roadResult.setTileCount(1);

        findSerfOnStreetTile(starterTile, roadResult);
        roadResult.addFoundStreet(starterTile);

        boolean bottomFinished = checkSide(placedTiles, x, y - 1, sides.get(TileSideEnum.BOTTOM), TileSideEnum.TOP, roadResult);
        boolean topFinished = checkSide(placedTiles, x, y + 1, sides.get(TileSideEnum.TOP), TileSideEnum.BOTTOM, roadResult);
        boolean leftFinished = checkSide(placedTiles, x - 1, y, sides.get(TileSideEnum.LEFT), TileSideEnum.RIGHT, roadResult);
        boolean rightFinished = checkSide(placedTiles, x + 1, y, sides.get(TileSideEnum.RIGHT), TileSideEnum.LEFT, roadResult);

        roadResult.setFinished(bottomFinished && topFinished && leftFinished && rightFinished);
        return roadResult;
    }

    private boolean checkSide(List<Tile> placedTiles, int x, int y, TileTypeEnum sideType, TileSideEnum oppositeSide, RoadResult roadResult) {
        var tile = placedTiles.stream().filter(t -> t.getX() == x && t.getY() == y).findFirst();
        if (sideType.equals(TileTypeEnum.STREET)) {
            return tile.map(value -> checkIfOrientationOfRoadIsFinished(placedTiles, value, oppositeSide, roadResult)).orElse(false);
        } else {
            return true;
        }
    }

    private boolean checkIfOrientationOfRoadIsFinished(List<Tile> placedTiles, Tile tileToCheck, TileSideEnum skipTileSide, RoadResult roadResult) {
        var sides = tileToCheck.rotateTile();

        boolean bottomFinished;
        boolean topFinished;
        boolean leftFinished;
        boolean rightFinished;

        int x = tileToCheck.getX();
        int y = tileToCheck.getY();

        roadResult.addFoundStreet(tileToCheck);

        roadResult.tileCount += 1;
        if (tileToCheck.getTileZones().stream().anyMatch(tileTypeEnum -> tileTypeEnum.equals(TileTypeEnum.CROSSROADS))) {
            findSerfOnCrossroads(tileToCheck, roadResult, skipTileSide);
            return true;
        } else {
            findSerfOnStreetTile(tileToCheck, roadResult);
        }

        if (sides.values().stream().filter(tileTypeEnum -> tileTypeEnum.equals(TileTypeEnum.STREET)).count() == 1) {
            return true;
        }

        if (skipTileSide != TileSideEnum.BOTTOM && sides.get(TileSideEnum.BOTTOM).equals(TileTypeEnum.STREET)) {
            Optional<Tile> bottomTile = placedTiles.stream().filter(tile ->
                    tile.getX() == x && tile.getY() == y - 1
            ).findFirst();
            if (bottomTile.isPresent()) {
                bottomFinished = bottomTile.filter(tile -> checkIfOrientationOfRoadIsFinished(placedTiles, tile, TileSideEnum.TOP, roadResult)).isPresent();
            } else bottomFinished = false;
        } else bottomFinished = true;

        if (skipTileSide != TileSideEnum.LEFT && sides.get(TileSideEnum.LEFT).equals(TileTypeEnum.STREET)) {
            var leftTile = placedTiles.stream().filter(tile ->
                    tile.getX() == x - 1 && tile.getY() == y
            ).findFirst();
            if (leftTile.isPresent()) {
                leftFinished = leftTile.filter(tile -> checkIfOrientationOfRoadIsFinished(placedTiles, tile, TileSideEnum.RIGHT, roadResult)).isPresent();
            } else leftFinished = false;
        } else leftFinished = true;

        if (skipTileSide != TileSideEnum.RIGHT && sides.get(TileSideEnum.RIGHT).equals(TileTypeEnum.STREET)) {
            var rightTile = placedTiles.stream().filter(tile ->
                    tile.getX() == x + 1 && tile.getY() == y
            ).findFirst();
            if (rightTile.isPresent()) {
                rightFinished = rightTile.filter(tile -> checkIfOrientationOfRoadIsFinished(placedTiles, tile, TileSideEnum.LEFT, roadResult)).isPresent();
            } else rightFinished = false;
        } else rightFinished = true;

        if (skipTileSide != TileSideEnum.TOP && sides.get(TileSideEnum.TOP).equals(TileTypeEnum.STREET)) {
            var topTile = placedTiles.stream().filter(tile ->
                    tile.getX() == x && tile.getY() == y + 1
            ).findFirst();
            if (topTile.isPresent()) {
                topFinished = topTile.filter(tile -> checkIfOrientationOfRoadIsFinished(placedTiles, tile, TileSideEnum.BOTTOM, roadResult)).isPresent();
            } else topFinished = false;
        } else topFinished = true;

        return topFinished && bottomFinished && rightFinished && leftFinished;
    }

    private void findSerfOnStreetTile(Tile tileToCheck, RoadResult roadResult) {
        Serf serf = tileToCheck.getSerf();
        List<TileTypeEnum> tileZones = tileToCheck.getTileZones();
        if (serf != null) {
            TileTypeEnum tileZone = tileZones.get(serf.getTileZoneId());
            if (tileZone == TileTypeEnum.STREET) {
                addSerfToResult(serf.getPlayer(), roadResult, serf);
            }
        }
    }


    private void findSerfOnCrossroads(Tile tileToCheck, RoadResult roadResult, TileSideEnum tileSideToCheck) {
        Serf serf = tileToCheck.getSerf();
        var rotatedTileZoneIds = tileToCheck.getRotatedTileZoneIds();
        if (serf != null) {
//             var rotatedIndex = serf.getRotatedTileZoneId(tileToCheck.getOrientation());
            var rotatedIndex = rotatedTileZoneIds.get(serf.getTileZoneId());
            if (tileSideToCheck == TileSideEnum.TOP && rotatedIndex == 1) {
                addSerfToResult(serf.getPlayer(), roadResult, serf);
            } else if (tileSideToCheck == TileSideEnum.BOTTOM && rotatedIndex == 7) {
                addSerfToResult(serf.getPlayer(), roadResult, serf);
            } else if (tileSideToCheck == TileSideEnum.LEFT && rotatedIndex == 3) {
                addSerfToResult(serf.getPlayer(), roadResult, serf);
            } else if (tileSideToCheck == TileSideEnum.RIGHT && rotatedIndex == 5) {
                addSerfToResult(serf.getPlayer(), roadResult, serf);
            }
        }
    }
    public void addStreetScores(List<Tile> placedTiles, Map<Integer, Integer> scoresPerPlayerNumber, RoadResult roadResult) {
        List<Tile> crossRoads = roadResult.getFoundTiles().stream().filter(tile -> tile.getTileZones().contains(TileTypeEnum.CROSSROADS) && tile.getSerf() != null && tile.getTileZones().get(tile.getSerf().getTileZoneId()) == TileTypeEnum.STREET).toList();
        if (!crossRoads.isEmpty()) {
            for (Tile crossRoad : crossRoads) {
                ScoreHelper.calculateScoreSpecialCase(placedTiles, scoresPerPlayerNumber, crossRoad, crossRoad.getSerf(), TileTypeEnum.CROSSROADS);
            }
        }
        int score = roadResult.getFoundTiles().size();
        var serfCount = ScoreHelper.countSerfsByPlayer(roadResult.getFoundSerfs());
        logger.info("Score for STREET:");
        ScoreHelper.addScoresToMap(scoresPerPlayerNumber, score, serfCount);
    }

    private void addSerfToResult(Player player, RoadResult roadResult, Serf serf) {
        int count = roadResult.playerResults.getOrDefault(player, 0);
        roadResult.foundSerfs.add(serf);
        logger.info("Adding a serf to the result, serf has zone index: " + serf.getTileZoneId());
        roadResult.playerResults.put(player, count + 1);
    }

    private void returnRelevantSerfs(RoadResult cr) {
        cr.getFoundSerfs().forEach(serfService::returnSerf);
    }


    public Collection<Integer> getAvailableStreetZones(Tile tile) {
        Set<Integer> availableStreetZones = new HashSet<>();
        if (tile.getTileZones().contains(TileTypeEnum.CROSSROADS)) {
            List<RoadResult> results = checkCrossroadsSidesFinishedStart(tile);
            var indexes = ScoreHelper.findAllZoneIndexesOfType(tile.getRotatedTileZones(), TileTypeEnum.STREET);
            for (var zoneSet : indexes) {
                for (int rotatedZoneId : zoneSet) {
                    availableStreetZones.add(tile.getOriginalTilezoneId(rotatedZoneId));
                }
            }
            for (RoadResult result : results) {
                if (result.foundSerfs.isEmpty()) continue;
                for (var zoneSet : indexes) {
                    boolean foundValidSide = false;
                    if (zoneSet.contains(TileSideEnum.TOP.getOrdinalValue()) && ScoreHelper.getNeighbour(tile, result.getFoundTiles().stream().toList(), TileSideEnum.TOP).isPresent()) {
                        foundValidSide = true;
                    } else if (zoneSet.contains(TileSideEnum.RIGHT.getOrdinalValue()) && ScoreHelper.getNeighbour(tile, result.getFoundTiles().stream().toList(), TileSideEnum.RIGHT).isPresent()) {
                        foundValidSide = true;
                    } else if (zoneSet.contains(TileSideEnum.BOTTOM.getOrdinalValue()) && ScoreHelper.getNeighbour(tile, result.getFoundTiles().stream().toList(), TileSideEnum.BOTTOM).isPresent()) {
                        foundValidSide = true;
                    } else if (zoneSet.contains(TileSideEnum.LEFT.getOrdinalValue()) && ScoreHelper.getNeighbour(tile, result.getFoundTiles().stream().toList(), TileSideEnum.LEFT).isPresent()) {
                        foundValidSide = true;
                    }

                    if (foundValidSide) {
                        for (int rotatedZoneId : zoneSet) {
                            availableStreetZones.remove(tile.getOriginalTilezoneId(rotatedZoneId));
                        }
                    }
                }
            }
        } else {
            RoadResult result = checkRoadFinishedStart(tile);
            if (result.foundSerfs.isEmpty()) {
                var indexes = ScoreHelper.findAllZoneIndexesOfType(tile.getTileZones(), TileTypeEnum.STREET);
                availableStreetZones.addAll(indexes.stream().findFirst().orElse(Set.of()));
            }
        }

        return availableStreetZones;
    }

    @Getter
    @Setter
    public static class RoadResult {
        private boolean isFinished;
        // The key is a player and the value is the amount of serfs
        private HashMap<Player, Integer> playerResults;
        private int tileCount;
        private Set<Serf> foundSerfs;
        private Set<Tile> foundTiles;


        public RoadResult() {
            foundSerfs = new HashSet<>();
            playerResults = new HashMap<>();
            foundTiles = new HashSet<>();
        }

        public void addFoundStreet(Tile tile) {
            foundTiles.add(tile);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RoadScoreService.RoadResult that = (RoadScoreService.RoadResult) o;
            return isFinished == that.isFinished &&
                    tileCount == that.tileCount &&
                    Objects.equals(playerResults, that.playerResults) &&
                    Objects.equals(foundSerfs, that.foundSerfs) &&
                    Objects.equals(foundTiles, that.foundTiles);
        }

        @Override
        public int hashCode() {
            return Objects.hash(isFinished, playerResults, tileCount, foundSerfs, foundTiles);
        }
    }
}
