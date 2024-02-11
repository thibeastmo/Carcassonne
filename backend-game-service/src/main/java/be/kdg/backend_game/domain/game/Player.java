package be.kdg.backend_game.domain.game;

import be.kdg.backend_game.domain.user_management.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int playerNumber;
    private Color color;
    @ManyToOne
    private Account account;
    @ManyToOne
    Game game;
    @OneToMany(orphanRemoval = false, mappedBy = "player", cascade = CascadeType.REMOVE)
    private List<Serf> serfs;
    @OneToMany(mappedBy = "player")
    private List<Turn> turns;
    private int points;
    private int contestedLandWon;
    private int contestedLandLost;

    public Player() {
        serfs = new ArrayList<>();
    }

    public Player(int playerNumber, Account account, Game game, List<Serf> serfs) {
        this.playerNumber = playerNumber;
        this.account = account;
        this.game = game;
        this.serfs = serfs;
    }

    public void addSerfToPlayer(Serf serf) {
        serfs.add(serf);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return playerNumber == player.playerNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerNumber);
    }

    public void addPoints(int pointsToAdd) {
        this.points += pointsToAdd;
    }

    public void addContestedLandWon() {
        this.contestedLandWon += 1;
    }

    public void addContestedLandLost() {
        this.contestedLandLost += 1;
    }
}
