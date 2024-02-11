import {UsedSerfs} from "./overlay/UsedSerfs.ts";

export interface UsedPlayerSerfs {
    playerId: number,
    serfColor: string,
    usedSerfs: UsedSerfs[],
}