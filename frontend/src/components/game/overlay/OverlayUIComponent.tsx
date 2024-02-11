import {PlayersInGame} from "./PlayersInGame.tsx";
import {TopMiddleOverlayComponent} from "./TopMiddleOverlayComponent.tsx";
import {TopRightOverlayComponent} from "./TopRightOverlayComponent.tsx";
import {Dispatch, SetStateAction} from "react";
import {ProblemComponent} from "../../ProblemComponent.tsx";
import {calculateTimeRemainingInMs} from "../../../services/GameDataService.tsx";
import {PlayerDataModel} from "../../../model/game/PlayerDataModel.ts";

export interface OverlayUIComponentProps {
    playerDataModel: PlayerDataModel;
    orientation: number;
    setOrientation: Dispatch<SetStateAction<number>>;
    totalTilesInGame: number;
    tilesPlacedInGame: number;
    startTurnTime: number;
    maxTurnDurationInMilliseconds: number;
    serfsAvailable: number;
    onTimeReachedZero: () => void;
    setHasPlacedTile: Dispatch<SetStateAction<boolean>>;
}

export function OverlayUIComponent({
                                       playerDataModel,
                                       orientation,
                                       setOrientation,
                                       totalTilesInGame,
                                       tilesPlacedInGame,
                                       startTurnTime,
                                       maxTurnDurationInMilliseconds,
                                       serfsAvailable,
                                       onTimeReachedZero,
                                       setHasPlacedTile
                                   }: OverlayUIComponentProps) {
    const timeDifference = calculateTimeRemainingInMs(startTurnTime, maxTurnDurationInMilliseconds);
    if (playerDataModel.playerData !== undefined) {
        return (
            <>
                <TopMiddleOverlayComponent turnTimeLeft={timeDifference} onTimeReachedZero={onTimeReachedZero}/>
                <TopRightOverlayComponent maxTurnDurationInMilliseconds={maxTurnDurationInMilliseconds}
                                          startTurnTime={startTurnTime}
                                          orientation={orientation}
                                          setOrientation={setOrientation}
                                          totalTilesInGame={totalTilesInGame}
                                          tilesPlacedInGame={tilesPlacedInGame}
                                          serfsAvailable={serfsAvailable}
                                          setHasPlacedTile={setHasPlacedTile}/>

                <PlayersInGame players={playerDataModel.playerData}/>
            </>
        )
    } else if (playerDataModel.isError || playerDataModel.isLoading) {

        return (
            <>
                <TopMiddleOverlayComponent turnTimeLeft={timeDifference} onTimeReachedZero={onTimeReachedZero}/>
                <TopRightOverlayComponent maxTurnDurationInMilliseconds={maxTurnDurationInMilliseconds}
                                          startTurnTime={startTurnTime}
                                          orientation={orientation}
                                          setOrientation={setOrientation}
                                          totalTilesInGame={totalTilesInGame}
                                          tilesPlacedInGame={tilesPlacedInGame}
                                          serfsAvailable={serfsAvailable}
                                          setHasPlacedTile={setHasPlacedTile}
                />
            </>
        )
    }

    return (<ProblemComponent/>);
}
