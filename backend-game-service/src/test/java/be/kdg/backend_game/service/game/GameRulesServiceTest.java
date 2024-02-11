package be.kdg.backend_game.service.game;

import be.kdg.backend_game.domain.game.*;
import be.kdg.backend_game.service.game.tile_calculations.MonasteryScoreService;
import be.kdg.backend_game.service.game.tile_calculations.ScoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class GameRulesServiceTest {
    @Autowired
    private GameRulesService gameRulesService;


    @Test
    void checkingForMonasteryInNewIs0() {
        List<Tile> placedTiles = TileSeedEnum.seedTiles();
        var serfList = MonasteryScoreService.monasteryFinishedCheck(placedTiles);
        assertEquals(serfList.size(), 0);
    }

    @Test
    void checkingForSingleMonasteryWhenSeeded() {
        List<Tile> tiles = TileSeedEnum.seedTiles();
        Player player = new Player();
        player.setPlayerNumber(1);
        Serf serf = new Serf(player);

        Tile monasteryTile = tiles.get(37);
        Tile surroundingTile1 = tiles.get(49);
        Tile surroundingTile2 = tiles.get(55);
        Tile surroundingTile3 = tiles.get(55);
        Tile surroundingTile4 = tiles.get(56);
        Tile surroundingTile5 = tiles.get(57);
        Tile surroundingTile6 = tiles.get(58);
        Tile surroundingTile7 = tiles.get(59);
        Tile surroundingTile8 = tiles.get(60);
        //Check if the tile that is seeded is the right one
        assertEquals(monasteryTile.getTileZones().get(4), TileTypeEnum.MONASTERY);
        //setting monastery tile
        //todo: add serf to tile
        monasteryTile.setPlaced(true);
        monasteryTile.setX(0);
        monasteryTile.setY(1);
        monasteryTile.setSerf(serf);
        //check if tile is as expected
        assertEquals(surroundingTile1.getTileZones(), List.of(TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,
                TileTypeEnum.FARMLAND, TileTypeEnum.MONASTERY, TileTypeEnum.FARMLAND,
                TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND));
        surroundingTile1.setPlaced(true);
        surroundingTile1.setX(0);
        surroundingTile1.setY(2);

        assertEquals(surroundingTile2.getTileZones(), List.of(TileTypeEnum.FARMLAND, TileTypeEnum.CITY, TileTypeEnum.CITY,
                TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.CITY,
                TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND));
        surroundingTile2.setPlaced(true);
        surroundingTile2.setX(1);
        surroundingTile2.setY(1);

        surroundingTile3.setPlaced(true);
        surroundingTile3.setX(-1);
        surroundingTile3.setY(1);

        surroundingTile4.setPlaced(true);
        surroundingTile4.setX(1);
        surroundingTile4.setY(0);

        surroundingTile5.setPlaced(true);
        surroundingTile5.setX(1);
        surroundingTile5.setY(1);

        surroundingTile6.setPlaced(true);
        surroundingTile6.setX(-1);
        surroundingTile6.setY(0);

        surroundingTile7.setPlaced(true);
        surroundingTile7.setX(1);
        surroundingTile7.setY(2);

        surroundingTile8.setPlaced(true);
        surroundingTile8.setX(-1);
        surroundingTile8.setY(2);


        var serfList = MonasteryScoreService.monasteryFinishedCheck(tiles);
        assertEquals(1, serfList.size());
    }

    @Test
    void checkingForTwoMonasteryWhenSeeded() {
        List<Tile> tiles = TileSeedEnum.seedTiles();
        Player player = new Player();
        player.setPlayerNumber(1);
        Serf serf1 = new Serf(player);
        Serf serf2 = new Serf(player);

        Tile monasteryTile = tiles.get(37);
        Tile monasteryTile2 = tiles.get(49);
        Tile surroundingTile2 = tiles.get(55);
        Tile surroundingTile3 = tiles.get(55);
        Tile surroundingTile4 = tiles.get(56);
        Tile surroundingTile5 = tiles.get(57);
        Tile surroundingTile6 = tiles.get(58);
        Tile surroundingTile7 = tiles.get(59);
        Tile surroundingTile8 = tiles.get(60);
        Tile surroundingTile9 = tiles.get(61);
        Tile surroundingTile10 = tiles.get(62);
        Tile surroundingTile11 = tiles.get(63);
        //Check if the tile that is seeded is the right one
        assertEquals(monasteryTile.getTileZones().get(4), TileTypeEnum.MONASTERY);
        assertEquals(monasteryTile2.getTileZones().get(4), TileTypeEnum.MONASTERY);
        //setting monastery tile
        //todo: add serf to tile
        monasteryTile.setPlaced(true);
        monasteryTile.setX(0);
        monasteryTile.setY(1);
        monasteryTile.setSerf(serf1);
        //check if tile is as expected
        assertEquals(monasteryTile2.getTileZones(), List.of(TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,
                TileTypeEnum.FARMLAND, TileTypeEnum.MONASTERY, TileTypeEnum.FARMLAND,
                TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND));
        monasteryTile2.setPlaced(true);
        monasteryTile2.setX(0);
        monasteryTile2.setY(2);
        monasteryTile2.setSerf(serf2);

        assertEquals(surroundingTile2.getTileZones(), List.of(TileTypeEnum.FARMLAND, TileTypeEnum.CITY, TileTypeEnum.CITY,
                TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.CITY,
                TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND));
        surroundingTile2.setPlaced(true);
        surroundingTile2.setX(1);
        surroundingTile2.setY(1);

        surroundingTile3.setPlaced(true);
        surroundingTile3.setX(-1);
        surroundingTile3.setY(1);

        surroundingTile4.setPlaced(true);
        surroundingTile4.setX(1);
        surroundingTile4.setY(0);

        surroundingTile5.setPlaced(true);
        surroundingTile5.setX(1);
        surroundingTile5.setY(1);

        surroundingTile6.setPlaced(true);
        surroundingTile6.setX(-1);
        surroundingTile6.setY(0);

        surroundingTile7.setPlaced(true);
        surroundingTile7.setX(1);
        surroundingTile7.setY(2);

        surroundingTile8.setPlaced(true);
        surroundingTile8.setX(-1);
        surroundingTile8.setY(2);

        surroundingTile9.setPlaced(true);
        surroundingTile9.setX(-1);
        surroundingTile9.setY(3);

        surroundingTile10.setPlaced(true);
        surroundingTile10.setX(0);
        surroundingTile10.setY(3);

        surroundingTile11.setPlaced(true);
        surroundingTile11.setX(1);
        surroundingTile11.setY(3);


        var serfList = MonasteryScoreService.monasteryFinishedCheck(tiles);
        assertEquals(2, serfList.size());
    }
}
