import {Box, Typography} from "@mui/material";
import {GameHistory} from "../../model/statistics/GameHistory.ts";
import {GameTypeIcon} from "./GameTypeIcon.tsx";
import {ReplayButtonComponent} from "./replay/ReplayButtonComponent.tsx";

export interface GameHistoryListItemProps {
    gameHistory: GameHistory;
}

export function GameHistoryListItemComponent({gameHistory}: GameHistoryListItemProps) {
    const datetime = new Date(gameHistory.creationDate);
    const formattedDatetime = datetime.toLocaleString();
    const backgroundImage = 'url(\'' + (gameHistory.avatarImage !== null ? gameHistory.avatarImage : (import.meta.env['VITE_BACKEND_URL'] + '/images/avatars/1.png'))+ '\')';
    return (
        <Box
        style={{
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
        }}>
        <Box
        style={{
            display: 'grid',
            gridTemplateColumns: '1fr 2fr 1fr',
            backgroundColor: gameHistory.rank === 1 ? 'green' : 'red',
            width: '50vw',
            borderRadius: '16px',
            marginBottom: '8px',
        }}>
            <Box style={{
                position: 'relative',
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
            }}>
                <Box style={{
                    width: '100px',
                    height: '100%',
                    backgroundSize: 'contain',
                    backgroundRepeat: 'no-repeat',
                    backgroundImage: backgroundImage,
                }} >
                    <GameTypeIcon gameType={gameHistory.gameType} />
                </Box>
            </Box>
            <Box style={{
                display: 'grid',
                gridTemplateColumns: '1fr',
            }}>
                <Typography variant="h4">{gameHistory.nickname !== null ? gameHistory.nickname : 'NO NICKNAME'}</Typography>
                <Typography variant="h5">{formattedDatetime}</Typography>
            </Box>
            <Box style={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
            }}>
                <ReplayButtonComponent gameId={gameHistory.gameId}/>
            </Box>
        </Box>
        </Box>
    );
}