import {UsedPlayersSerfs} from "./overlay/UsedPlayersSerfs.ts";

export interface SerfsUsedModel {
    gameId: string,
    playersSerfs: UsedPlayersSerfs[]
}