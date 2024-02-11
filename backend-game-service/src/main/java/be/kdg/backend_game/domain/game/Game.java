package be.kdg.backend_game.domain.game;

import be.kdg.backend_game.domain.GameTypeEnum;
import be.kdg.backend_game.domain.lobby.Lobby;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID gameId;
    private int maxTurnDurationTime;
    private GameTypeEnum gameTypeEnum;
    private boolean isGameOver;
    @OneToMany(orphanRemoval = false, mappedBy = "game", cascade = CascadeType.REMOVE)
    private List<Tile> tiles;
    @OneToMany(orphanRemoval = false, mappedBy = "game", cascade = CascadeType.REMOVE)
    private List<Player> players;
    @OneToOne(cascade = CascadeType.REMOVE)
    private Lobby lobby;
    @OneToMany(mappedBy = "game", cascade = CascadeType.REMOVE)
    private List<Turn> turns;

    public Game() {
    }
}
