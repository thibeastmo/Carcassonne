package be.kdg.backend_game.domain.game;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Serf {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID serfId;
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;
    @OneToOne
    private Tile tile;
    private int tileZoneId;
    private boolean isPlaced;
    public Serf() {
    }

    public Serf(Player player) {
        this.player = player;
    }

    public int getRotatedTileZoneId(OrientationEnum orientation) {
        if (tileZoneId == 4 || orientation == OrientationEnum.ROTATION_0) return tileZoneId;
        List<Integer> corners = Arrays.asList(2, 8, 6, 0);
        List<Integer> midSide = Arrays.asList(1, 5, 7, 3);
        if (corners.contains(tileZoneId)) {
            int index = (corners.indexOf(tileZoneId) + orientation.ordinal()) % corners.size();
            return corners.get(index);
        } else {
            int index = (midSide.indexOf(tileZoneId) + orientation.ordinal()) % midSide.size();
            return midSide.get(index);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(serfId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Serf serf = (Serf) o;
        return serf.serfId.equals(serf.serfId);
    }
}
