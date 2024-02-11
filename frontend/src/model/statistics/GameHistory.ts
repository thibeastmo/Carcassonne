import {GameTypes} from "./../GameType.ts";
export interface GameHistory {
    gameHistoryId : string;
    gameId : string;
    avatarImage : string | null;
    gameType : GameTypes;
    nickname : string | null;
    rank : number;
    points : number;
    creationDate : number;
}