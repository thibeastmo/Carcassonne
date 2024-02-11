package be.kdg.backend_game.service.game.tile_calculations;

import be.kdg.backend_game.domain.game.*;
import be.kdg.backend_game.service.game.PlayerService;
import be.kdg.backend_game.service.game.SerfService;
import be.kdg.backend_game.service.game.TileService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CityScoreService {

    private static final Logger logger = LoggerFactory.getLogger(CityScoreService.class.toString());
    private final TileService tileService;
    private final SerfService serfService;
    private final PlayerService playerService;

    public CityScoreService(TileService tileService, SerfService serfService, PlayerService playerService) {
        this.tileService = tileService;
        this.serfService = serfService;
        this.playerService = playerService;
    }

    public int calculateCityScore(Set<Tile> cityTiles) {
        return cityTiles.stream()
                .mapToInt(tile -> tile.isWeapon() ? 4 : 2)
                .sum();
    }

    public void calculateScoreSpecialCase(List<Tile> placedTiles, Map<Integer, Integer> scoresPerPlayerNumber, Tile tileToCheck, Serf serf, TileTypeEnum type) {
        int serfTileZoneId = serf.getRotatedTileZoneId(tileToCheck.getOrientation());
        if (serfTileZoneId == TileSideEnum.TOP.getOrdinalValue() || serfTileZoneId == TileSideEnum.RIGHT.getOrdinalValue() || serfTileZoneId == TileSideEnum.BOTTOM.getOrdinalValue() || serfTileZoneId == TileSideEnum.LEFT.getOrdinalValue()) {
            if (ScoreHelper.getNeighbour(tileToCheck, placedTiles, TileSideEnum.getByOrdinalValue(serfTileZoneId)).isEmpty()) {
                logger.info("Score for " + type.name() + ":");
                ScoreHelper.addScoresToMap(scoresPerPlayerNumber, 1, serf.getPlayer().getPlayerNumber());
            }
        }
    }


    public Optional<Map<Integer, Integer>> checkTile(UUID tileId) {
        return tileService.retrieveTile(tileId)
                .map(tile -> {
                    List<Tile> placedTiles = tileService.retrievePlacedTiles(tile.getGame().getGameId());

                    if (tile.getTileZones().contains(TileTypeEnum.CITY)) {
                        List<CityResult> completedCities = checkCityStatus(tile, placedTiles).stream()
                                .filter(CityResult::isCityCompleted)
                                .peek(this::returnRelevantSerfs)
                                .toList();

                        Map<Integer, Integer> totalScores = new HashMap<>();
                        Map<Integer, Integer> totalClaimsWon = new HashMap<>();
                        Map<Integer, Integer> totalClaimsLost = new HashMap<>();

                        for (CityResult completedCity : completedCities) {
                            Map<Integer, Integer> scores = calculateScoreFinishedCity(completedCity);
                            updateScores(totalScores, scores);
                            updateClaims(totalClaimsWon, totalClaimsLost, completedCity);
                        }
                        if (!totalScores.isEmpty()) playerService.updatePlayerScores(totalScores);
                        if (!totalClaimsWon.isEmpty() || !totalClaimsLost.isEmpty())
                            playerService.updatePlayerClaims(totalClaimsWon, totalClaimsLost);

                        return totalScores;
                    }

                    return Collections.emptyMap();
                });
    }

    private void updateScores(Map<Integer, Integer> existingScores, Map<Integer, Integer> newScores) {
        for (Map.Entry<Integer, Integer> entry : newScores.entrySet()) {
            int playerNumber = entry.getKey();
            int scoreToAdd = entry.getValue();

            existingScores.merge(playerNumber, scoreToAdd, Integer::sum);
        }
    }

    private void updateClaims(Map<Integer, Integer> totalClaimsWon, Map<Integer, Integer> totalClaimsLost, CityResult completedCity) {
        var serfCount = ScoreHelper.countSerfsByPlayer(completedCity.getRelevantSerfs());
        List<Integer> winners = ScoreHelper.findWinners(serfCount);
        for (Integer winner : winners) {
            totalClaimsWon.merge(winner, 1, Integer::sum);
        }

        for (Serf serf : completedCity.getRelevantSerfs()) {
            Player player = serf.getPlayer();
            if (!winners.contains(player.getPlayerNumber())) {
                totalClaimsLost.merge(player.getPlayerNumber(), 1, Integer::sum);
            }
        }
    }

    public Map<Integer, Integer> calculateScoreFinishedCity(CityResult cr) {
        Map<Integer, Integer> winnerScores = new HashMap<>();
        int cityScore = calculateCityScore(cr.getConnectedCityTiles());
        Map<Integer, Integer> serfCount = ScoreHelper.countSerfsByPlayer(cr.getRelevantSerfs());
        List<Integer> winners = ScoreHelper.findWinners(serfCount);
        for (int winner : winners) {
            winnerScores.put(winner, cityScore);
        }
        return winnerScores;
    }


    public Set<CityResult> checkCityStatus(Tile tileToCheck, List<Tile> placedTiles) {
        Set<CityResult> cityResults = new HashSet<>();
        List<TileTypeEnum> tileZones = tileToCheck.getRotatedTileZones();
        Set<Set<Integer>> cityZoneIndexSets = ScoreHelper.findAllZoneIndexesOfType(tileZones, TileTypeEnum.CITY);

        for (Set<Integer> set : cityZoneIndexSets) {
            cityResults.add(buildCityResult(tileToCheck, placedTiles, set));
        }

        return cityResults;
    }


    public int calculateScoreUnfinishedCity(Set<Tile> connectedCityTiles) {
        int coatOfArmsCount = (int) connectedCityTiles.stream().filter(Tile::isWeapon).count();
        return coatOfArmsCount + connectedCityTiles.size();
    }

    private void returnRelevantSerfs(CityResult cr) {
        cr.getRelevantSerfs().forEach(serfService::returnSerf);
    }

    private void findConnectedZonesOfType(List<TileTypeEnum> tileZones, int currentIndex, Set<Integer> connectedZones, TileTypeEnum type) {
        for (int neighborIndex : ScoreHelper.getNeighbouringZoneIndexes(tileZones.size(), currentIndex)) {
            if (tileZones.get(neighborIndex) == type && connectedZones.add(neighborIndex)) {
                findConnectedZonesOfType(tileZones, neighborIndex, connectedZones, type);
            }
        }
    }

    private CityResult buildCityResult(Tile thisTile, List<Tile> placedTiles, Set<Integer> connectedCityZones) {
        CityResult cityResult = new CityResult();
        boolean isCityCompleted = true;

        cityResult.addConnectedCityTile(thisTile);
        addRelevantSerf(cityResult, connectedCityZones, thisTile);

        for (TileSideEnum side : TileSideEnum.values()) {
            if (connectedCityZones.contains(side.getOrdinalValue())) {
                Optional<Tile> optionalNeighbour = ScoreHelper.getNeighbour(thisTile, placedTiles, side);

                if (optionalNeighbour.isPresent()) {
                    buildCityResultSide(optionalNeighbour.get(), placedTiles, side.opposite(), cityResult);
                } else {
                    isCityCompleted = false;
                }
            }
        }

        cityResult.setCityCompleted(cityResult.isCityCompleted() && isCityCompleted);
        return cityResult;
    }


    private void buildCityResultSide(Tile thisTile, List<Tile> placedTiles, TileSideEnum sidePreviousTile, CityResult cityResult) {
        cityResult.addConnectedCityTile(thisTile);
        List<TileTypeEnum> tileZones = thisTile.getRotatedTileZones();
        int startIndex = sidePreviousTile.getOrdinalValue();
        Set<Integer> connectedCityZones = new HashSet<>();
        connectedCityZones.add(startIndex);
        findConnectedZonesOfType(tileZones, startIndex, connectedCityZones, TileTypeEnum.CITY);

        addRelevantSerf(cityResult, connectedCityZones, thisTile);

        boolean isCityCompleted = checkConnectedCityZones(connectedCityZones, sidePreviousTile, placedTiles, cityResult, thisTile);

        cityResult.setCityCompleted(cityResult.isCityCompleted() && isCityCompleted);
    }

    private boolean checkConnectedCityZones(Set<Integer> connectedCityZones, TileSideEnum sidePreviousTile, List<Tile> placedTiles, CityResult cityResult, Tile thisTile) {
        boolean isCityCompleted = true;
        for (int connectedCityZone : connectedCityZones) {
            if (connectedCityZone == sidePreviousTile.getOrdinalValue()) continue;
            if (connectedCityZone == TileSideEnum.TOP.getOrdinalValue() || connectedCityZone == TileSideEnum.RIGHT.getOrdinalValue() || connectedCityZone == TileSideEnum.BOTTOM.getOrdinalValue() || connectedCityZone == TileSideEnum.LEFT.getOrdinalValue()) {
                Optional<Tile> neighbouringTile = ScoreHelper.getNeighbour(thisTile, placedTiles, TileSideEnum.getByOrdinalValue(connectedCityZone));
                if (neighbouringTile.isPresent()) {
                    if (!cityResult.getConnectedCityTiles().contains(neighbouringTile.get())) {
                        buildCityResultSide(neighbouringTile.get(), placedTiles, TileSideEnum.getByOrdinalValue(connectedCityZone).opposite(), cityResult);
                    }
                } else {
                    isCityCompleted = false;
                }
            }
        }
        return isCityCompleted;
    }

    private void addRelevantSerf(CityResult cityResult, Set<Integer> connectedCityZones, Tile thisTile) {
        List<Serf> serfs = serfService.retrieveSerfsByTileId(thisTile.getTileId());
        if (!serfs.isEmpty()) {
            int tileZoneId = serfs.get(0).getRotatedTileZoneId(thisTile.getOrientation());
            if (connectedCityZones.contains(tileZoneId)) {
                cityResult.addRelevantSerf(serfs.get(0));
            }
        }
    }


    public Collection<Integer> getAvailableCityZones(Tile tile) {
        Set<Integer> availableCityZones = new HashSet<>();
        var placedTiles = tileService.retrievePlacedTiles(tile.getGame().getGameId());
        var cityZones = ScoreHelper.findAllZoneIndexesOfType(tile.getRotatedTileZones(), TileTypeEnum.CITY);
        var cityResults = checkCityStatus(tile, placedTiles);
        boolean cityResultsAllHaveOneTile = true;
        Set<Tile> allConnectedTiles = new HashSet<>();
        for (CityResult cityResult : cityResults) {
            cityResultsAllHaveOneTile &= cityResult.getConnectedCityTiles().size() == 1;
            allConnectedTiles.addAll(cityResult.getConnectedCityTiles());
        }
        if (cityResults.isEmpty()) return availableCityZones;
        for (CityResult cityResult : cityResults) {
            if (cityResult.getRelevantSerfs().isEmpty()) {
                for (var zoneSet : cityZones) {
                    boolean foundValidSide = false;
                    Tile neighbour = null;
                    boolean hasTopNeighbour = false;
                    boolean hasRightNeighbour = false;
                    boolean hasBottomNeighbour = false;
                    boolean hasLeftNeighbour = false;
                    if (zoneSet.contains(TileSideEnum.TOP.getOrdinalValue()) && ScoreHelper.getNeighbour(tile, allConnectedTiles.stream().toList(), TileSideEnum.TOP).isPresent()) {
                        foundValidSide = true;
                        neighbour = ScoreHelper.getNeighbour(tile, allConnectedTiles.stream().toList(), TileSideEnum.TOP).get();
                        hasTopNeighbour = true;
                    } else if (zoneSet.contains(TileSideEnum.RIGHT.getOrdinalValue()) && ScoreHelper.getNeighbour(tile, allConnectedTiles.stream().toList(), TileSideEnum.RIGHT).isPresent()) {
                        foundValidSide = true;
                        neighbour = ScoreHelper.getNeighbour(tile, allConnectedTiles.stream().toList(), TileSideEnum.RIGHT).get();
                        hasRightNeighbour = true;
                    } else if (zoneSet.contains(TileSideEnum.BOTTOM.getOrdinalValue()) && ScoreHelper.getNeighbour(tile, allConnectedTiles.stream().toList(), TileSideEnum.BOTTOM).isPresent()) {
                        foundValidSide = true;
                        hasBottomNeighbour = true;
                        neighbour = ScoreHelper.getNeighbour(tile, allConnectedTiles.stream().toList(), TileSideEnum.BOTTOM).get();
                    } else if (zoneSet.contains(TileSideEnum.LEFT.getOrdinalValue()) && ScoreHelper.getNeighbour(tile, allConnectedTiles.stream().toList(), TileSideEnum.LEFT).isPresent()) {
                        foundValidSide = true;
                        neighbour = ScoreHelper.getNeighbour(tile, allConnectedTiles.stream().toList(), TileSideEnum.LEFT).get();
                        hasLeftNeighbour = true;
                    }

                    if ((foundValidSide && cityResult.getConnectedCityTiles().contains(neighbour)) || cityResultsAllHaveOneTile) {
                        for (int rotatedZoneId : zoneSet) {
                            availableCityZones.add(tile.getOriginalTilezoneId(rotatedZoneId));
                        }
                    } else if (cityResult.getConnectedCityTiles().size() == 1 && ((zoneSet.contains(TileSideEnum.TOP.getOrdinalValue()) && !hasTopNeighbour) || (zoneSet.contains(TileSideEnum.RIGHT.getOrdinalValue()) && !hasRightNeighbour) || (zoneSet.contains(TileSideEnum.BOTTOM.getOrdinalValue()) && !hasBottomNeighbour) || (zoneSet.contains(TileSideEnum.LEFT.getOrdinalValue()) && !hasLeftNeighbour))) {
                        for (int rotatedZoneId : zoneSet) {
                            availableCityZones.add(tile.getOriginalTilezoneId(rotatedZoneId));
                        }
                    }
                }
            }
        }
        return availableCityZones;
    }








    @Data
    public static class CityResult {
        private Set<Tile> connectedCityTiles;
        private Set<Serf> relevantSerfs;
        private boolean isCityCompleted;

        public CityResult() {
            this.connectedCityTiles = new HashSet<>();
            this.relevantSerfs = new HashSet<>();
            this.isCityCompleted = true;
        }

        public void addConnectedCityTile(Tile tile) {
            connectedCityTiles.add(tile);
        }

        public void addRelevantSerf(Serf serf) {
            relevantSerfs.add(serf);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CityResult that = (CityResult) o;

            return Objects.equals(connectedCityTiles, that.connectedCityTiles) &&
                    Objects.equals(relevantSerfs, that.relevantSerfs) &&
                    isCityCompleted == that.isCityCompleted;
        }

        @Override
        public int hashCode() {
            return Objects.hash(connectedCityTiles, relevantSerfs, isCityCompleted);
        }
    }
}
