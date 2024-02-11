import {useTranslation} from "react-i18next";
import {useGameHistories} from "../../hooks/statistics/useGameHistories.ts";
import {GameHistoryListItemComponent} from "./GameHistoryListItemComponent.tsx";
import {GameHistory} from "../../model/statistics/GameHistory.ts";
import {Box, Typography} from "@mui/material";

export function GameHistoryComponent() {
    const { gameHistories, isError, isLoading } = useGameHistories();
    const {t} = useTranslation();

    return (
        <Box>
            <Typography variant="h2">{t('statistics.game_history.title')}</Typography>
        <Box>
            {
                isError || isLoading ?
                    isError ? <Typography variant="h3">An error occured</Typography> : <Typography variant="h3">Loading...</Typography>
                    :
                gameHistories.map((gameHistory : GameHistory) => (
                <GameHistoryListItemComponent key={gameHistory.gameHistoryId} gameHistory={gameHistory} />
            ))}
        </Box>
        </Box>
    );
}