import {
    Box,
    Dialog, DialogContent, DialogTitle, Typography
} from '@mui/material'
import {Notification} from "../../model/Notification.ts";
import {NotificationComponent} from "./NotificationComponent.tsx";
import {useTranslation} from "react-i18next";

export interface NotificationDialogProps {
    isOpen: boolean;
    onClose: () => void;
    notifications: Notification[];

}

export function NotificationDialogComponent({isOpen, onClose, notifications}: NotificationDialogProps) {
    const {t} = useTranslation();
    return (
        <Dialog open={isOpen} onClose={onClose} maxWidth="md" fullWidth>
            <DialogTitle>{t('game.games_waiting')}</DialogTitle>
            <DialogContent>
                {notifications.length > 0 ? (
                    notifications.map((notification: Notification) => (
                        <NotificationComponent
                            key={notification.gameId}
                            gameId={notification.gameId}
                            lobbyName={notification.lobbyName}
                            nicknames={notification.nicknames}
                            gameType={notification.gameType}
                            timePassed={notification.timePassed}
                            timeEnum={notification.timeEnum}
                            onClose={onClose}
                        />
                    ))
                ) : (
                    <Box textAlign="center" py={2}>
                        <Typography variant="subtitle1">{t('game.no_games_waiting')}</Typography>
                    </Box>
                )}
            </DialogContent>
        </Dialog>
    );
}