package be.kdg.backend_game.repository;

import be.kdg.backend_game.domain.game.Serf;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SerfRepository extends JpaRepository<Serf, UUID> {
    List<Serf> findSerfByPlayer_PlayerNumber(int playerNumber);
    List<Serf> findSerfByPlayer_Game_GameIdAndTileIsNotNullAndTile_TileIdIsNotNull(UUID player_game_gameId);
    Optional<Serf> findFirstSerfByPlayer_PlayerNumberAndIsPlacedFalse(int player_playerNumber);
    List<Serf> findSerfByTile_TileId(UUID tile_tileId);
}
