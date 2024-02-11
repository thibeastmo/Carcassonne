package be.kdg.backend_game.domain.lobby;

import be.kdg.backend_game.domain.GameTypeEnum;
import be.kdg.backend_game.domain.user_management.Account;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import be.kdg.backend_game.domain.game.Game;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Lobby implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID lobbyId;
    private String lobbyName;
    private int maxPlayers;
    private boolean closed;
    private GameTypeEnum gameTypeEnum;
    @ManyToOne
    private Account owner;
    private LocalDateTime startDate;
    @OneToMany(orphanRemoval = true, mappedBy = "lobby", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<LobbyParticipant> lobbyParticipants;
    @OneToOne
    private Game game;

    public Lobby() {
    }

    public Lobby(String lobbyName, int maxPlayers, boolean closed, GameTypeEnum gameTypeEnum, Account owner, LocalDateTime startDate) {
        this.lobbyName = lobbyName;
        this.maxPlayers = maxPlayers;
        this.closed = closed;
        this.gameTypeEnum = gameTypeEnum;
        this.owner = owner;
        this.startDate = startDate;
        this.lobbyParticipants = new ArrayList<>();
    }

    public void addLobbyParticipant(LobbyParticipant participant) {
        participant.setLobby(this);
        lobbyParticipants.add(participant);
    }

    @Override
    public String toString() {
        return "Lobby{" +
                "lobbyId=" + lobbyId +
                ", maxPlayers=" + maxPlayers +
                ", closed=" + closed +
                ", gameTypeEnum=" + gameTypeEnum +
                '}';
    }
}
