package be.kdg.backend_game.domain.game;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TileSeedEnumTest {

    @Test
    void seedTilesIs72() {
        List<Tile> tileSeedEnums = TileSeedEnum.seedTiles();
        assertEquals(72, tileSeedEnums.size());
    }
}