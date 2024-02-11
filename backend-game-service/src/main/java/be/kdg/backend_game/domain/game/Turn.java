package be.kdg.backend_game.domain.game;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Turn {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID turnId;
    @ManyToOne
    private Player player;
    @ManyToOne
    private Game game;
    @OneToOne
    private Tile tile;
    private LocalDateTime beginTurn;
    private boolean placedSerf;
}
