package be.kdg.backend_game.service.game.tile_calculations;

import be.kdg.backend_game.domain.game.*;
import be.kdg.backend_game.repository.AccountRepository;
import be.kdg.backend_game.repository.PlayerRepository;
import be.kdg.backend_game.repository.TurnRepository;
import be.kdg.backend_game.service.game.PlayerService;
import be.kdg.backend_game.service.game.SerfService;
import be.kdg.backend_game.service.game.TileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ScoreService {
    private static final Logger logger = LoggerFactory.getLogger(ScoreService.class.toString());
    private final TileService tileService;
    private final PlayerService playerService;
    private final PlayerRepository playerRepository;
    private final CityScoreService cityScoreService;
    private final RoadScoreService roadScoreService;
    private final MonasteryScoreService monasteryScoreService;

    public ScoreService(TileService tileService, PlayerService playerService, PlayerRepository playerRepository, CityScoreService cityScoreService, RoadScoreService roadScoreService, MonasteryScoreService monasteryScoreService) {
        this.tileService = tileService;
        this.playerService = playerService;
        this.playerRepository = playerRepository;
        this.cityScoreService = cityScoreService;
        this.roadScoreService = roadScoreService;
        this.monasteryScoreService = monasteryScoreService;
    }

    public Optional<Map<Integer, Integer>> checkTile(UUID tileId) {
        return cityScoreService.checkTile(tileId);
    }

    public Map<Player, Integer> calculateScoreOfPlacedRoadOrCrossroad(Tile tile) {
        return roadScoreService.calculateScoreOfPlacedRoadOrCrossroad(tile);
    }

    public List<Serf> resolveMonasteryCompletion(UUID gameId) {
        return monasteryScoreService.resolveMonasteryCompletion(gameId);
    }

    public void addFinalScore(UUID gameId) {
        Map<Integer, Integer> scorePerPlayerNumber = calculateScoreForEndOfGame(gameId);
        List<Player> players = playerService.retrievePlayersByGame(gameId);
        for (Player player : players) {
            if (scorePerPlayerNumber.containsKey(player.getPlayerNumber())) {
                player.setPoints(player.getPoints() + scorePerPlayerNumber.get(player.getPlayerNumber()));
            }
        }
        try {
            playerRepository.saveAll(players);
        } catch (DataIntegrityViolationException e) {
            logger.error("Could not add final scores to players: " + e);
        }
    }

    private Map<Integer, Integer> calculateScoreForEndOfGame(UUID gameId) {
        List<Tile> placedTiles = tileService.retrievePlacedTiles(gameId);
        Map<Integer, Integer> scoresPerPlayerNumber = new HashMap<>();
        Set<Tile> checkedCities = new HashSet<>();
        Set<Tile> checkedStreets = new HashSet<>();
        for (Tile tileToCheck : placedTiles) {
            logger.info("Checking tile " + tileToCheck.getTileName());
            if (tileToCheck.getTileZones().contains(TileTypeEnum.CITY) && !checkedCities.contains(tileToCheck)) {
                var cityZones = ScoreHelper.findAllZoneIndexesOfType(tileToCheck.getTileZones(), TileTypeEnum.CITY);
                if (cityZones.size() > 1) {
                    checkedCities.add(tileToCheck);
                    if (tileToCheck.getSerf() != null) {
                        var serf = tileToCheck.getSerf();
                        if (tileToCheck.getTileZones().get(serf.getTileZoneId()) == TileTypeEnum.CITY) {
                            ScoreHelper.calculateScoreSpecialCase(placedTiles, scoresPerPlayerNumber, tileToCheck, serf, TileTypeEnum.CITYSPLIT);
                        }
                    }
                } else {
                    var cityResults = cityScoreService.checkCityStatus(tileToCheck, placedTiles);
                    for (var cityResult : cityResults) {
                        if (!cityResult.isCityCompleted() && !cityResult.getRelevantSerfs().isEmpty()) {
                            int score = cityScoreService.calculateScoreUnfinishedCity(cityResult.getConnectedCityTiles());
                            var serfCount = ScoreHelper.countSerfsByPlayer(cityResult.getRelevantSerfs());
                            logger.info("Score for CITY:");
                            ScoreHelper.addScoresToMap(scoresPerPlayerNumber, score, serfCount);
                        }
                        var splitCities = cityResult.getConnectedCityTiles().stream().filter(tile -> !checkedCities.contains(tile) && ScoreHelper.findAllZoneIndexesOfType(tile.getTileZones(), TileTypeEnum.CITY).size() > 1 && tile.getSerf() != null).toList();
                        if (!splitCities.isEmpty()) {
                            for (Tile splitCity : splitCities) {
                                checkedCities.add(splitCity);
                                var serf = splitCity.getSerf();
                                if (splitCity.getTileZones().get(serf.getTileZoneId()) != TileTypeEnum.CITY) continue;
                                cityScoreService.calculateScoreSpecialCase(placedTiles, scoresPerPlayerNumber, splitCity, serf, TileTypeEnum.CITYSPLIT);
                            }
                        }
                        checkedCities.addAll(cityResult.getConnectedCityTiles());
                    }
                }
            }
            if (tileToCheck.getTileZones().contains(TileTypeEnum.STREET) && !checkedStreets.contains(tileToCheck)) {
                if (tileToCheck.getTileZones().contains(TileTypeEnum.CROSSROADS)) {
                    List<RoadScoreService.RoadResult> roadResults = roadScoreService.checkCrossroadsSidesFinishedStart(tileToCheck);
                    roadResults.forEach(roadResult -> checkedStreets.addAll(roadResult.getFoundTiles()));
                    roadResults = roadResults.stream().filter(roadResult -> !roadResult.isFinished() && !roadResult.getFoundSerfs().isEmpty()).toList();
                    if (!roadResults.isEmpty()) {
                        for (RoadScoreService.RoadResult roadResult : roadResults) {
                            roadScoreService.addStreetScores(placedTiles, scoresPerPlayerNumber, roadResult);
                        }
                    }
                } else {
                    RoadScoreService.RoadResult roadResult = roadScoreService.checkRoadFinishedStart(tileToCheck);
                    checkedStreets.addAll(roadResult.getFoundTiles());
                    if (!roadResult.isFinished() && !roadResult.getFoundSerfs().isEmpty()) {
                        roadScoreService.addStreetScores(placedTiles, scoresPerPlayerNumber, roadResult);
                    }
                }
            }
            if (tileToCheck.getTileZones().contains(TileTypeEnum.MONASTERY)) {
                Serf serf = tileToCheck.getSerf();
                if (serf != null && serf.getTileZoneId() == 4) {
                    int score = monasteryScoreService.countNeighbours(tileToCheck, placedTiles) + 1;
                    int playerNumber = serf.getPlayer().getPlayerNumber();
                    logger.info("Score for MONASTERY:");
                    monasteryScoreService.addScoresToMap(scoresPerPlayerNumber, score, playerNumber);
                }
            }
        }
        return scoresPerPlayerNumber;
    }




}
