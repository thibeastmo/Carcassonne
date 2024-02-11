import {Box, Typography} from "@mui/material";
import {Timer} from "./Timer.tsx";
import {useTranslation} from "react-i18next";

export interface TopMiddleOverlayComponentProps {
    turnTimeLeft: number;
    onTimeReachedZero: () => void;
}
export function TopMiddleOverlayComponent({turnTimeLeft, onTimeReachedZero}: TopMiddleOverlayComponentProps) {
    const {t} = useTranslation();
    if (turnTimeLeft === undefined) return <></>

    return (
        <Box style={{
            position: 'fixed',
            top: '12vh',
            left: '50%',
            transform: 'translate(-50%, -50%)',
            display: 'flex',
        }}>
            {
                turnTimeLeft > 0 ?
                    <Timer initialSecondsForTurnLeft={turnTimeLeft} onTimeReachedZero={onTimeReachedZero} />
                    : <Typography>{t('in_game.waitingForOpponent')}</Typography>
            }
        </Box>
    );
}