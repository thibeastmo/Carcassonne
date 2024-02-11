import {Box, Fab, Tooltip} from "@mui/material";
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';
import {useCurrentTurn} from "../../../hooks/useCurrentTurn.tsx";
import {calculateTimeRemainingInMs} from "../../../services/GameDataService.tsx";
import {useTranslation} from "react-i18next";
import {Dispatch, SetStateAction} from "react";

export interface NextTurnComponentProps {
    startTurnTime: number;
    maxTurnDurationInMilliseconds: number
    setHasPlacedTile: Dispatch<SetStateAction<boolean>>;
}
export default function NextTurnComponent({startTurnTime, maxTurnDurationInMilliseconds, setHasPlacedTile}: NextTurnComponentProps) {
    const {t} = useTranslation();
    const {nextTurnQuery} = useCurrentTurn();
    const cappedDifferenceInMilliseconds = calculateTimeRemainingInMs(startTurnTime, maxTurnDurationInMilliseconds);
    if (cappedDifferenceInMilliseconds < 0) return (<></>);
    return (
        <Box
            style={{
                marginTop: "16px"
            }}>
            <Tooltip title={t('in_game.next_turn_button_tooltip')}>
                <Fab variant={'extended'} onClick={handleClick}>
                    <ArrowForwardIosIcon/>
                </Fab></Tooltip>
        </Box>
    )

    function handleClick() {
        const tempCappedDifferenceInMilliseconds = calculateTimeRemainingInMs(startTurnTime, maxTurnDurationInMilliseconds);
        setHasPlacedTile(false);
        if (tempCappedDifferenceInMilliseconds > 0) {
            nextTurnQuery();
        }
    }
}