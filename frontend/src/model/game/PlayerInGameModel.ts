import {UsedSerfs} from "./overlay/UsedSerfs.ts";

export interface PlayerInGameModel {
    playerNumber: number,
    nickname: string,
    avatarUrl: string,
    isTurn: number, //negative if false, positive with begin turn time in ms if true
    score: number,
    serfsUsed: UsedSerfs[] | undefined,
    color: string
}
export interface PlayerInGameWithoutDetails extends Omit<PlayerInGameModel, 'isTurn' | 'score' | 'serfsUsed'> {
}