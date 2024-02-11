import {Fab, Tooltip} from "@mui/material";
import PlayArrowIcon from "@mui/icons-material/PlayArrow";
import {useTranslation} from "react-i18next";

export interface ReplayButtonComponentProps {
    gameId: string
}

function setIsDialogOpen(gameId: string) {
    console.log('about to watch the replay of game with history id: ', gameId)
}

export function ReplayButtonComponent({gameId} : ReplayButtonComponentProps) {
    const {t} = useTranslation();
    return (
        <Tooltip title={t('statistics.game_history.watch_replay')} arrow>
            <Fab variant="extended"
                 onClick={() => setIsDialogOpen(gameId)}
            >
                <PlayArrowIcon />
            </Fab>
        </Tooltip>
    );
}