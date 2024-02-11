import {TileModel} from "./TileModel.ts";

export interface CurrentTileModel {
    isError: boolean,
    isLoading: boolean,
    tile: TileModel
}