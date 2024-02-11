import {TileModel} from "./TileModel.ts";
import {UsedPlayerSerfs} from "../UsedPlayerSerfs.ts";

export interface TileModelProps {
    tileModel: TileModel;
    usedPlayerSerfs: UsedPlayerSerfs[] | undefined;
}