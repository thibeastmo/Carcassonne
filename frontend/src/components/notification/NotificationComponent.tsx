import {Box, Typography} from "@mui/material";
import {Link} from "react-router-dom";
import {useTranslation} from "react-i18next";

export interface NotificationProps {
    gameId: string;
    nicknames: string[];
    lobbyName: string;
    gameType: string;
    timeEnum: string;
    timePassed: number;
    onClose: () => void;
}

export function NotificationComponent({
                                          gameId,
                                          nicknames,
                                          lobbyName,
                                          gameType,
                                          timeEnum,
                                          timePassed,
                                          onClose
                                      }: NotificationProps) {
    const {t} = useTranslation();
    const formatTimeMessage = () => {
        if (timePassed === 0) {
            return t('time.just_now');
        }
        if (timePassed > 1) {
            const timeUnit = timeEnum === "MINUTE" ? t('time.minutes') : timeEnum === "HOUR" ? t('time.hours') : t('time.days');
            return `${timePassed} ${timeUnit} ${t('time.ago')}`;
        }
        if (timePassed === 1) {
            const timeUnit = timeEnum === "MINUTE" ? t('time.minute') : timeEnum === "HOUR" ? t('time.hour') : t('time.day');
            return `${timePassed} ${timeUnit} ${t('time.ago')}`;
        }
    };
    return (
        <Link onClick={onClose} to={`/game/${gameId}`} style={{textDecoration: 'none'}}>
            <Box
                border={1}
                borderRadius={4}
                padding={2}
                display="flex"
                alignItems="center"
                justifyContent="space-between"
                textAlign="center"
                marginBottom={2}
                sx={{
                    '&:hover': {
                        backgroundColor: '#f5f5f5',
                    },
                }}
            >
                <Box display="flex" alignItems="center">
                    <Typography variant="h5">{t('game.game')}: {lobbyName}</Typography>
                </Box>
                <Box>
                    <Typography variant="subtitle1">
                        {t('game.players')}: {nicknames.join(', ')} - {gameType} - {formatTimeMessage()}
                    </Typography>
                </Box>
            </Box>
        </Link>
    );
}