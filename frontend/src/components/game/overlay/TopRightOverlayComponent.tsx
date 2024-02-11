import {Box} from "@mui/material";
// import {SettingsButtonComponent} from "./SettingsButtonComponent.tsx";
import TileControlsComponent from "../../TileControlsComponent.tsx";
import {Dispatch, SetStateAction, useContext} from "react";
import {SerfPlacingTileButtonComponent} from "./SerfPlacingTileButtonComponent.tsx";
import {useCurrentTile} from "../../../hooks/useCurrentTile.ts";
import NextTurnComponent from "./NextTurnComponent.tsx";
import {GameRulesButtonComponent} from "../gamerules/GameRulesButtonComponent.tsx";
import {ApplicationTheme} from "../../../model/ApplicationTheme.ts";
import ApplicationThemeContext from "../../../context/ApplicationThemeContext.ts";
import {calculateTimeRemainingInMs} from "../../../services/GameDataService.tsx";
import {GameControlsButtonComponent} from "../gamecontrols/GameControlsButtonComponent.tsx";

export interface TopRightOverlayProps {
    orientation: number;
    setOrientation: Dispatch<SetStateAction<number>>;
    totalTilesInGame: number;
    tilesPlacedInGame: number;
    maxTurnDurationInMilliseconds: number;
    startTurnTime: number
    serfsAvailable: number;
    setHasPlacedTile: Dispatch<SetStateAction<boolean>>;
}

export function TopRightOverlayComponent({
                                             orientation,
                                             setOrientation,
                                             totalTilesInGame,
                                             tilesPlacedInGame,
                                             maxTurnDurationInMilliseconds,
                                             startTurnTime,
                                             serfsAvailable,
                                             setHasPlacedTile
                                         }: TopRightOverlayProps) {

    const {isError: isErrorCurrentTile, isLoading: isLoadingCurrentTile, tile} = useCurrentTile();
    if (isErrorCurrentTile) {
        console.error("Error loading from backend")
    }
    if (isErrorCurrentTile) {
        console.log("Error loading from backend")
    }
    if (tile != undefined && !isLoadingCurrentTile) {
        const {applicationTheme} = useContext(ApplicationThemeContext);
        tile.tileImage = applicationTheme === ApplicationTheme.WINTER ?
            tile.tileImage.replace('default', 'winter') :
            tile.tileImage;
        const cappedDifferenceInMilliseconds = calculateTimeRemainingInMs(startTurnTime, maxTurnDurationInMilliseconds);
        return (
            <Box style={{
                position: 'fixed',
                top: '12vh',
                right: '16px',
                display: 'flex',
            }}>
                <TileControlsComponent orientation={orientation}
                                       setOrientation={setOrientation}
                                       currentTile={tile}
                                       maxTurnDurationInMilliseconds={maxTurnDurationInMilliseconds}
                                       startTurnTime={startTurnTime}
                                       tilesPlacedInGame={tilesPlacedInGame}
                                       totalTilesInGame={totalTilesInGame}/>
                <Box style={{
                    display: 'inline-block',
                    marginLeft: '16px'
                }}>
                    {/*<SettingsButtonComponent/>*/}
                    <GameRulesButtonComponent/>
                    <GameControlsButtonComponent/>
                    {
                        cappedDifferenceInMilliseconds > 0 ? (
                            <>
                                {serfsAvailable > 0 ? (
                                        <SerfPlacingTileButtonComponent
                                            startTurnTime={startTurnTime}
                                            maxTurnDurationInMilliseconds={maxTurnDurationInMilliseconds}
                                            currentTile={tile}
                                        />
                                ) : (
                                    <></>
                                )}
                                <NextTurnComponent
                                    setHasPlacedTile={setHasPlacedTile}
                                    startTurnTime={startTurnTime}
                                    maxTurnDurationInMilliseconds={maxTurnDurationInMilliseconds}
                                />
                            </>
                        ) : (
                            <></>
                        )
                    }
                </Box>
            </Box>
        );
    }
}