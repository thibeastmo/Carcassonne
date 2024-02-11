package be.kdg.backend_game.domain.game;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.logging.Level;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Tile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID tileId;
    private String tileName;
    private int x;
    private int y;
    private boolean placed;
    private boolean isPlaceable;
    private boolean discarded;
    private boolean weapon;
    @ManyToOne
    @JoinColumn
    private Game game;
    private OrientationEnum orientation;
    @OneToOne(mappedBy = "tile")
    private Serf serf;

    private List<TileTypeEnum> tileZones;
    private String tileImage;

    public Tile(List<TileTypeEnum> tileZones, String tileImage, String tileName, boolean weapon) {
        this.tileZones = tileZones;
        this.tileImage = tileImage;
        this.tileName = tileName;
        this.weapon = weapon;
        this.isPlaceable = true;
        this.discarded = false;
        this.orientation = OrientationEnum.ROTATION_0;
    }

    public Tile() {
    }

    @Override
    public int hashCode() {
        return Objects.hash(tileId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile that = (Tile) o;
        return Objects.equals(this.tileId, that.tileId);
    }

    public void setSerfOnTileZone(Serf serf, int tileZoneId) {
        serf.setTile(this);
        serf.setTileZoneId(tileZoneId);
    }


    public HashMap<TileSideEnum, TileTypeEnum> rotateTile() {
        HashMap<TileSideEnum, TileTypeEnum> rotatedTile = new HashMap<>();
        switch (orientation) {
            case ROTATION_0 -> {
                rotatedTile.put(TileSideEnum.TOP, getTileZones().get(1));
                rotatedTile.put(TileSideEnum.RIGHT, getTileZones().get(5));
                rotatedTile.put(TileSideEnum.BOTTOM, getTileZones().get(7));
                rotatedTile.put(TileSideEnum.LEFT, getTileZones().get(3));
            }
            case ROTATION_90 -> {
                rotatedTile.put(TileSideEnum.TOP, getTileZones().get(3));
                rotatedTile.put(TileSideEnum.RIGHT, getTileZones().get(1));
                rotatedTile.put(TileSideEnum.BOTTOM, getTileZones().get(5));
                rotatedTile.put(TileSideEnum.LEFT, getTileZones().get(7));
            }
            case ROTATION_180 -> {
                rotatedTile.put(TileSideEnum.TOP, getTileZones().get(7));
                rotatedTile.put(TileSideEnum.RIGHT, getTileZones().get(3));
                rotatedTile.put(TileSideEnum.BOTTOM, getTileZones().get(1));
                rotatedTile.put(TileSideEnum.LEFT, getTileZones().get(5));
            }
            case ROTATION_270 -> {
                rotatedTile.put(TileSideEnum.TOP, getTileZones().get(5));
                rotatedTile.put(TileSideEnum.RIGHT, getTileZones().get(7));
                rotatedTile.put(TileSideEnum.BOTTOM, getTileZones().get(3));
                rotatedTile.put(TileSideEnum.LEFT, getTileZones().get(1));
            }
        }
        return rotatedTile;
    }
    public List<TileTypeEnum> getRotatedTileZones() {
        return switch (orientation) {
            case ROTATION_0 -> tileZones;
            case ROTATION_90 -> Arrays.asList(
                    tileZones.get(6),
                    tileZones.get(3),
                    tileZones.get(0),
                    tileZones.get(7),
                    tileZones.get(4),
                    tileZones.get(1),
                    tileZones.get(8),
                    tileZones.get(5),
                    tileZones.get(2)
            );
            case ROTATION_180 -> Arrays.asList(
                    tileZones.get(8),
                    tileZones.get(7),
                    tileZones.get(6),
                    tileZones.get(5),
                    tileZones.get(4),
                    tileZones.get(3),
                    tileZones.get(2),
                    tileZones.get(1),
                    tileZones.get(0)
            );
            case ROTATION_270 -> Arrays.asList(
                    tileZones.get(2),
                    tileZones.get(5),
                    tileZones.get(8),
                    tileZones.get(1),
                    tileZones.get(4),
                    tileZones.get(7),
                    tileZones.get(0),
                    tileZones.get(3),
                    tileZones.get(6)
            );
        };
    }

    public int getOriginalTilezoneId(int rotatedTileZoneId) {
        if (orientation == OrientationEnum.ROTATION_0 || rotatedTileZoneId == 4) return rotatedTileZoneId;
        List<Integer> rotation_90 = Arrays.asList(6, 3, 0, 7, 4, 1, 8, 5, 2);
        List<Integer> rotation_180 = Arrays.asList(8, 7, 6, 5, 4, 3, 2, 1, 0);
        List<Integer> rotation_270 = Arrays.asList(2, 5, 8, 1, 4, 7, 0, 3, 6);
        switch (orientation) {

            case ROTATION_90 -> {
                return rotation_90.get(rotatedTileZoneId);
            }
            case ROTATION_180 -> {
                return rotation_180.get(rotatedTileZoneId);
            }
            case ROTATION_270 -> {
                return rotation_270.get(rotatedTileZoneId);
            }
        }
        return -1;
    }

    public List<Integer> getRotatedTileZoneIds() {
        return switch (orientation) {
            case ROTATION_0 -> Arrays.asList(0,1,2,3,4,5,6,7,8);
            case ROTATION_270 -> Arrays.asList(
                    6,
                    3,
                    0,
                    7,
                    4,
                    1,
                    8,
                    5,
                    2
            );
            case ROTATION_180 -> Arrays.asList(
                    8,
                    7,
                    6,
                    5,
                    4,
                    3,
                    2,
                    1,
                    0
            );
            case ROTATION_90 -> Arrays.asList(
                    2,
                    5,
                    8,
                    1,
                    4,
                    7,
                    0,
                    3,
                    6
            );
        };
    }
}
