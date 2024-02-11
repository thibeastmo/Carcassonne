package be.kdg.backend_game.domain.game;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum TileSeedEnum {
    TILE1START("images/tiles/default/1_start.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.STREET, TileTypeEnum.STREET, TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,}),
    TILE2("images/tiles/default/2.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.STREET, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,}),
    TILE3("images/tiles/default/3.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.STREET, TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,}),
    TILE4("images/tiles/default/4.png", new TileTypeEnum[]{TileTypeEnum.CITY, TileTypeEnum.STREET, TileTypeEnum.FARMLAND, TileTypeEnum.CITY, TileTypeEnum.CROSSROADS, TileTypeEnum.STREET, TileTypeEnum.CITY, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,}),
    TILE5("images/tiles/default/5.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.STREET, TileTypeEnum.FARMLAND, TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,}),
    TILE6("images/tiles/default/6.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,}),
    TILE7("images/tiles/default/7.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.STREET, TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,}),
    TILE8("images/tiles/default/8.png", new TileTypeEnum[]{TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.FARMLAND, TileTypeEnum.CITY, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,}),
    TILE9("images/tiles/default/9.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.STREET, TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,}),
    TILE10("images/tiles/default/10.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.FARMLAND, TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.FARMLAND, TileTypeEnum.CITY, TileTypeEnum.CITY,}),
    TILE11("images/tiles/default/11.png", new TileTypeEnum[]{TileTypeEnum.CITY, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.CITY, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.CITY, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,}),
    TILE12("images/tiles/default/12.png", new TileTypeEnum[]{TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,}),
    TILE13("images/tiles/default/13.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.STREET, TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,}),
    TILE14("images/tiles/default/14.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.STREET, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,}),
    TILE15("images/tiles/default/15.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.STREET, TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,}),
    TILE16("images/tiles/default/16.png", new TileTypeEnum[]{TileTypeEnum.CITY, TileTypeEnum.STREET, TileTypeEnum.FARMLAND, TileTypeEnum.CITY, TileTypeEnum.CROSSROADS, TileTypeEnum.STREET, TileTypeEnum.CITY, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,}),
    TILE17("images/tiles/default/17.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.STREET, TileTypeEnum.FARMLAND, TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,}),
    TILE18("images/tiles/default/18.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,}),
    TILE19("images/tiles/default/19.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.CITY, TileTypeEnum.STREET, TileTypeEnum.STREET, TileTypeEnum.CITY, TileTypeEnum.FARMLAND, TileTypeEnum.CITY, TileTypeEnum.CITY,}),
    TILE20("images/tiles/default/20.png", new TileTypeEnum[]{TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.FARMLAND, TileTypeEnum.CITY, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,}),
    TILE21("images/tiles/default/21.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.STREET, TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,}),
    TILE22("images/tiles/default/22.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.FARMLAND, TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.FARMLAND, TileTypeEnum.CITY, TileTypeEnum.CITY,}),
    TILE23("images/tiles/default/23.png", new TileTypeEnum[]{TileTypeEnum.CITY, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.CITY, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.CITY, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,}),
    TILE24("images/tiles/default/24.png", new TileTypeEnum[]{TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,}),
    TILE28("images/tiles/default/28.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.STREET, TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,}),
    TILE29("images/tiles/default/29.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.STREET, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,}),
    TILE30("images/tiles/default/30.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.STREET,
            TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,}),
    TILE31("images/tiles/default/31.png", new TileTypeEnum[]{TileTypeEnum.CITY, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,
            TileTypeEnum.CITY, TileTypeEnum.CROSSROADS, TileTypeEnum.STREET,
            TileTypeEnum.CITY, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,}),
    TILE32("images/tiles/default/32.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,
            TileTypeEnum.STREET, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,
            TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,}),
    TILE33("images/tiles/default/33.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,}),
    TILE34("images/tiles/default/34.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.CITY,
            TileTypeEnum.STREET, TileTypeEnum.STREET, TileTypeEnum.CITY,
            TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,}),
    TILE35("images/tiles/default/35.png", new TileTypeEnum[]{TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,
            TileTypeEnum.CITY, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,
            TileTypeEnum.CITY, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,}),
    TILE36("images/tiles/default/36.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.STREET,
            TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,}),
    TILE37("images/tiles/default/37.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.CITY, TileTypeEnum.CITY,
            TileTypeEnum.FARMLAND, TileTypeEnum.CITY, TileTypeEnum.CITY,
            TileTypeEnum.FARMLAND, TileTypeEnum.CITY, TileTypeEnum.CITY,}),
    TILE38("images/tiles/default/38.png", new TileTypeEnum[]{TileTypeEnum.CITY, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,
            TileTypeEnum.CITY, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,
            TileTypeEnum.CITY, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,}),
    TILE39("images/tiles/default/39.png", new TileTypeEnum[]{TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,
            TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,
            TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,}),
    TILE40("images/tiles/default/40.png", true, new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.CITY, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.CITY, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.CITY, TileTypeEnum.FARMLAND,}),
    TILE41("images/tiles/default/41.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.MONASTERY, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,}),
    TILE42("images/tiles/default/42.png", new TileTypeEnum[]{TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITYSPLIT,
            TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.CITY,
            TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.CITY,}),
    TILE43("images/tiles/default/43.png", true, new TileTypeEnum[]{TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,
            TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,
            TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,}),
    TILE44("images/tiles/default/44.png", true, new TileTypeEnum[]{TileTypeEnum.CITY, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,
            TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.FARMLAND,
            TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,}),
    TILE45("images/tiles/default/45.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.MONASTERY, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,}),
    TILE46("images/tiles/default/46.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.CROSSROADS, TileTypeEnum.STREET,
            TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,}),
    TILE47("images/tiles/default/47.png", new TileTypeEnum[]{TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,
            TileTypeEnum.STREET, TileTypeEnum.STREET, TileTypeEnum.STREET,
            TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,}),
    TILE48("images/tiles/default/48.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,
            TileTypeEnum.STREET, TileTypeEnum.CROSSROADS, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,}),
    TILE49("images/tiles/default/49.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,}),
    TILE50("images/tiles/default/50.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.MONASTERY, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,}),
    TILE51("images/tiles/default/51.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,}),
    TILE52("images/tiles/default/52.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,}),
    TILE53("images/tiles/default/53.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.MONASTERY, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,}),
    TILE54("images/tiles/default/54.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,}),
    TILE55("images/tiles/default/55.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.CROSSROADS, TileTypeEnum.STREET,
            TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,}),
    TILE56("images/tiles/default/56.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,
            TileTypeEnum.STREET, TileTypeEnum.STREET, TileTypeEnum.STREET,
            TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,}),
    TILE57("images/tiles/default/57.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,
            TileTypeEnum.STREET, TileTypeEnum.CROSSROADS, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,}),
    TILE58("images/tiles/default/58.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.MONASTERY, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,}),
    TILE59("images/tiles/default/59.png", true, new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.CITY, TileTypeEnum.CITY,
            TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.CITY,
            TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,}),
    TILE60("images/tiles/default/60.png", true, new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,
            TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,
            TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,}),
    TILE61("images/tiles/default/61.png", new TileTypeEnum[]{TileTypeEnum.CITY, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,
            TileTypeEnum.CITY, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,
            TileTypeEnum.CITYSPLIT, TileTypeEnum.CITY, TileTypeEnum.CITY,}),
    TILE62("images/tiles/default/62.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.MONASTERY, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,}),
    TILE63("images/tiles/default/63.png", true, new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.CITY, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.CITY, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.CITY, TileTypeEnum.FARMLAND,}),
    TILE64("images/tiles/default/64.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,
            TileTypeEnum.STREET, TileTypeEnum.STREET, TileTypeEnum.STREET,
            TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,}),
    TILE65("images/tiles/default/65.png", true, new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.CITY,
            TileTypeEnum.STREET, TileTypeEnum.STREET, TileTypeEnum.CITY,
            TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,}),
    TILE66("images/tiles/default/66.png", true, new TileTypeEnum[]{TileTypeEnum.CITY, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,
            TileTypeEnum.CITY, TileTypeEnum.STREET, TileTypeEnum.STREET,
            TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,}),
    TILE67("images/tiles/default/67.png", new TileTypeEnum[]{TileTypeEnum.CITY, TileTypeEnum.FARMLAND, TileTypeEnum.CITY,
            TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,
            TileTypeEnum.CITY, TileTypeEnum.FARMLAND, TileTypeEnum.CITY,}),
    TILE68("images/tiles/default/68.png", true, new TileTypeEnum[]{TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,
            TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,
            TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,}),
    TILE69("images/tiles/default/69.png", true, new TileTypeEnum[]{TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,
            TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,
            TileTypeEnum.CITY, TileTypeEnum.FARMLAND, TileTypeEnum.CITY,}),
    TILE70("images/tiles/default/70.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.CITY,
            TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.CITY,
            TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.CITY,}),
    TILE71("images/tiles/default/71.png", new TileTypeEnum[]{TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,
            TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,
            TileTypeEnum.CITY, TileTypeEnum.STREET, TileTypeEnum.CITY,}),
    TILE72("images/tiles/default/72.png", new TileTypeEnum[]{TileTypeEnum.CITY, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,
            TileTypeEnum.CITY, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,
            TileTypeEnum.CITY, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,}),
    TILE73("images/tiles/default/73.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,
            TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.STREET,
            TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,}),
    TILE74("images/tiles/default/74.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,
            TileTypeEnum.STREET, TileTypeEnum.CROSSROADS, TileTypeEnum.STREET,
            TileTypeEnum.FARMLAND, TileTypeEnum.STREET, TileTypeEnum.FARMLAND,}),
    TILE75("images/tiles/default/75.png", new TileTypeEnum[]{TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND, TileTypeEnum.FARMLAND,
            TileTypeEnum.STREET, TileTypeEnum.STREET, TileTypeEnum.STREET,
            TileTypeEnum.CITY, TileTypeEnum.CITY, TileTypeEnum.CITY,}),
    ;
    final TileTypeEnum[] tileTypeEnum;
    final boolean weapon;
    final String imageUrl;

    TileSeedEnum(String imageUrl, TileTypeEnum[] tileTypeEnum) {
        this.tileTypeEnum = tileTypeEnum;
        this.imageUrl = imageUrl;
        this.weapon = false;
    }

    TileSeedEnum(String imageUrl, boolean weapon, TileTypeEnum[] tileTypeEnum) {
        this.tileTypeEnum = tileTypeEnum;
        this.imageUrl = imageUrl;
        this.weapon = weapon;
    }

    private static List<TileSeedEnum> seedEnumListTiles() {
        List<TileSeedEnum> tileSeedEnumList = new ArrayList<>(Arrays.asList(TileSeedEnum.values()));
        return tileSeedEnumList;
    }

    public static List<Tile> seedTiles() {
        List<TileSeedEnum> tileSeedEnumList = seedEnumListTiles();
        List<Tile> tilesList = new ArrayList<>();
        for (TileSeedEnum tileSeedEnum : tileSeedEnumList) {
            tilesList.add(new Tile(Arrays.stream(tileSeedEnum.getTileTypeEnum()).toList(), tileSeedEnum.getImageUrl(), tileSeedEnum.name(), tileSeedEnum.isWeapon()));
        }
        return tilesList;
    }

}
