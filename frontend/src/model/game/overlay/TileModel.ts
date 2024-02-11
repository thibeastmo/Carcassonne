import {TileZoneModel} from "./TileZoneModel.ts";

export interface TileModel {
    tileId: string,
    tileName: string,
    tileZones: TileZoneModel[],
    tileImage: string,
    placed: boolean,
    orientation: number,
}