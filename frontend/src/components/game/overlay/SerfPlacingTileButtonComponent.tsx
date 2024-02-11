import {useState} from "react";
import {Box, Fab, Tooltip} from "@mui/material";
import {SerfPlacingTileComponent} from "./SerfPlacingTileComponent.tsx";
import BoyIcon from "@mui/icons-material/Boy";
import {TileModel} from "../../../model/game/overlay/TileModel.ts";
import {calculateTimeRemainingInMs} from "../../../services/GameDataService.tsx";
import {useTranslation} from "react-i18next";

export interface CurrentTileModelProps {
    currentTile: TileModel;
    startTurnTime: number;
    maxTurnDurationInMilliseconds: number;
}

export function SerfPlacingTileButtonComponent({
                                                   currentTile,
                                                   startTurnTime,
                                                   maxTurnDurationInMilliseconds
                                               }: CurrentTileModelProps) {
    const {t} = useTranslation();
    const [isDialogOpen, setIsDialogOpen] = useState(false);
    const cappedDifferenceInMilliseconds = calculateTimeRemainingInMs(startTurnTime, maxTurnDurationInMilliseconds);
    if (currentTile == undefined || cappedDifferenceInMilliseconds < 0) return (<></>);
    return (
        <Box
            style={{
                marginTop: "16px"
            }}>
            <SerfPlacingTileComponent
                isOpen={isDialogOpen}
                onClose={() => setIsDialogOpen(false)}
                currentTile={currentTile}
                startTurnTime={startTurnTime}
                maxTurnDurationInMilliseconds={maxTurnDurationInMilliseconds}
            />
            <Tooltip title={t('in_game.place_serf_button_tooltip')}>
                <Fab variant="extended"
                     onClick={() => setIsDialogOpen(true)}
                >
                    <BoyIcon/>
                </Fab>
            </Tooltip>
        </Box>
    );
}