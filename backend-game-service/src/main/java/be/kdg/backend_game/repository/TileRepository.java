package be.kdg.backend_game.repository;

import be.kdg.backend_game.domain.game.Tile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TileRepository extends JpaRepository<Tile, UUID> {
    List<Tile> findTilesByGame_GameId(UUID gameId);
    Optional<Tile> findFirstByPlacedAndDiscardedAndGame_GameId(boolean placed, boolean discarded, UUID game_gameId);
    List<Tile> findAllByPlacedAndGame_GameId(boolean placed, UUID game_gameId);
    @Query("SELECT t FROM Tile t LEFT JOIN FETCH t.serf s LEFT JOIN FETCH t.game g WHERE t.placed = :placed AND g.gameId = :gameId")
    List<Tile> findAllPlacedTilesByGameId(@Param("placed") boolean placed, @Param("gameId") UUID game_gameId);

    @Query("SELECT t FROM Tile t LEFT JOIN FETCH t.game g WHERE t.x = :x AND t.y = :y AND g.gameId = :gameId")
    Optional<Tile> findTilesByCoordinates(@Param("x") int x, @Param("y") int y, @Param("gameId") UUID gameId);

    @Query("SELECT t FROM Tile t LEFT JOIN FETCH t.game g WHERE t.tileName = :name AND g.gameId = :gameId")
    Optional<Tile> findTileByNameAndGameId(@Param("name") String name, @Param("gameId") UUID gameId);
}
