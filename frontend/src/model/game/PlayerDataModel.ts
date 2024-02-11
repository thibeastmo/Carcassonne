import {PlayerInGameModel, PlayerInGameWithoutDetails} from "./PlayerInGameModel.ts";

export interface PlayerDataModel {
    isLoading: boolean,
    isError: boolean,
    playerData: PlayerInGameModel[] | undefined
}
export interface PlayerDataWithoutDetails {
    isLoading: boolean;
    isError: boolean;
    playersConstantData: PlayerInGameWithoutDetails[] | undefined;
}
