import axios from "axios";
import {TilePlacement} from "../model/TilePlacement.ts";
import {TileModels} from "../model/game/board/TileModels.ts";
import {TileModel} from "../model/game/overlay/TileModel.ts";

export const getCurrentTile = async (gameId: string) => {
    const response = await axios.get<TileModel>(`/api/tile/currentTile?gameId=${gameId}`)
    return response.data;
}
export const getPlacedTiles = async (gameId: string) => {
    const response = await axios.get<TileModels>(`/api/tile/placedTiles?gameId=${gameId}`)
    return response.data;
}

export const placeTile = (gameId : string, placementDto : TilePlacement) => {
    return axios.patch(`/api/tile/placeTile?gameId=${gameId}`, placementDto)
}