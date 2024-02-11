package be.kdg.backend_game.service.game.tile_calculations;

import be.kdg.backend_game.domain.game.*;

import java.util.*;

public class ScoreHelper {
    public static List<Integer> getNeighbouringZoneIndexes(int totalZones, int currentIndex) {
        int row = currentIndex / 3;
        int col = currentIndex % 3;

        List<Integer> indexes = new ArrayList<>();

        if (row > 0) indexes.add(currentIndex - 3);
        if (row < 2) indexes.add(currentIndex + 3);
        if (col > 0) indexes.add(currentIndex - 1);
        if (col < 2) indexes.add(currentIndex + 1);

        return indexes.stream()
                .filter(index -> index >= 0 && index < totalZones)
                .toList();
    }

    public static Set<Set<Integer>> findAllZoneIndexesOfType(List<TileTypeEnum> tileZones, TileTypeEnum type) {
        Set<Set<Integer>> indexSets = new HashSet<>();

        for (TileSideEnum side : TileSideEnum.values()) {
            int sideIndex = side.getOrdinalValue();

            if (tileZones.get(sideIndex) == type) {
                Set<Integer> indexSet = new HashSet<>();
                indexSet.add(sideIndex);
                findConnectedZonesOfType(tileZones, sideIndex, indexSet, type);
                indexSets.add(indexSet);
            }
        }

        return indexSets;
    }
    private static void findConnectedZonesOfType(List<TileTypeEnum> tileZones, int currentIndex, Set<Integer> connectedZones, TileTypeEnum type) {
        for (int neighborIndex : ScoreHelper.getNeighbouringZoneIndexes(tileZones.size(), currentIndex)) {
            if (tileZones.get(neighborIndex) == type && connectedZones.add(neighborIndex)) {
                findConnectedZonesOfType(tileZones, neighborIndex, connectedZones, type);
            }
        }
    }


    public static void addScoresToMap(Map<Integer, Integer> scoresPerPlayerNumber, int score, Map<Integer, Integer> serfCount) {
        List<Integer> winners = findWinners(serfCount);
        for (int playerNumber : winners) {
            if (scoresPerPlayerNumber.containsKey(playerNumber)) {
                int existingScore = scoresPerPlayerNumber.get(playerNumber);
                int newScore = existingScore + score;
                scoresPerPlayerNumber.put(playerNumber, newScore);
            } else {
                scoresPerPlayerNumber.put(playerNumber, score);
            }
        }
    }

    public static void addScoresToMap(Map<Integer, Integer> scoresPerPlayerNumber, int score, int playerNumber) {
        if (scoresPerPlayerNumber.containsKey(playerNumber)) {
            int existingScore = scoresPerPlayerNumber.get(playerNumber);
            int newScore = existingScore + score;
            scoresPerPlayerNumber.put(playerNumber, newScore);
        } else {
            scoresPerPlayerNumber.put(playerNumber, score);
        }
    }

    public static List<Integer> findWinners(Map<Integer, Integer> serfCount) {
        int maxCount = Integer.MIN_VALUE;
        List<Integer> winners = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : serfCount.entrySet()) {
            int count = entry.getValue();
            if (count > maxCount) {
                maxCount = count;
                winners.clear();
                winners.add(entry.getKey());
            } else if (count == maxCount) {
                winners.add(entry.getKey());
            }
        }

        return winners;
    }

    public static Map<Integer, Integer> countSerfsByPlayer(Set<Serf> serfs) {
        Map<Integer, Integer> playerSerfCount = new HashMap<>();

        for (Serf serf : serfs) {
            Player player = serf.getPlayer();
            playerSerfCount.put(player.getPlayerNumber(), playerSerfCount.getOrDefault(player.getPlayerNumber(), 0) + 1);
        }

        return playerSerfCount;
    }

    public static void calculateScoreSpecialCase(List<Tile> placedTiles, Map<Integer, Integer> scoresPerPlayerNumber, Tile tileToCheck, Serf serf, TileTypeEnum type) {
        int serfTileZoneId = serf.getRotatedTileZoneId(tileToCheck.getOrientation());
        if (serfTileZoneId == TileSideEnum.TOP.getOrdinalValue() || serfTileZoneId == TileSideEnum.RIGHT.getOrdinalValue() || serfTileZoneId == TileSideEnum.BOTTOM.getOrdinalValue() || serfTileZoneId == TileSideEnum.LEFT.getOrdinalValue()) {
            if (ScoreHelper.getNeighbour(tileToCheck, placedTiles, TileSideEnum.getByOrdinalValue(serfTileZoneId)).isEmpty()) {
                ScoreHelper.addScoresToMap(scoresPerPlayerNumber, 1, serf.getPlayer().getPlayerNumber());
            }
        }
    }

    public static Optional<Tile> getNeighbour(Tile tileToCheck, List<Tile> placedTiles, TileSideEnum direction) {
        int targetX = tileToCheck.getX();
        int targetY = tileToCheck.getY();

        return switch (direction) {
            case TOP ->
                    placedTiles.stream().filter(tile -> tile.getX() == targetX && tile.getY() == targetY + 1).findFirst();
            case RIGHT ->
                    placedTiles.stream().filter(tile -> tile.getX() == targetX + 1 && tile.getY() == targetY).findFirst();
            case BOTTOM ->
                    placedTiles.stream().filter(tile -> tile.getX() == targetX && tile.getY() == targetY - 1).findFirst();
            case LEFT ->
                    placedTiles.stream().filter(tile -> tile.getX() == targetX - 1 && tile.getY() == targetY).findFirst();
        };
    }

}
