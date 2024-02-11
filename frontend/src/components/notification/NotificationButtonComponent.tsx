import NotificationsNoneIcon from '@mui/icons-material/NotificationsNone';
import {Badge, Box} from "@mui/material";
import {useState} from "react";
import {useNotifications} from "../../hooks/useNotifications.ts";
import {NotificationDialogComponent} from "./NotificationDialogComponent.tsx";
import {Notification} from "../../model/Notification.ts";
import {NotificationEmptyButtonComponent} from "./NotificationEmptyButtonComponent.tsx";
import {useLocation} from "react-router-dom";

export function NotificationButtonComponent() {
    const {isError, isLoading, notifications} = useNotifications();
    const location = useLocation();
    const gameId = location.pathname.split('/').pop() || 'UNKNOWN';
    const [isDialogOpen, setIsDialogOpen] = useState(false)
    if (isError || isLoading) {
        return (<NotificationEmptyButtonComponent />);
    }
    const filteredNotifications = notifications.notificationDtos.filter((notification : Notification) => notification.gameId !== gameId);
    return (
        <Box sx={{position: 'relative'}}>
            <NotificationDialogComponent
                isOpen={isDialogOpen}
                onClose={() => setIsDialogOpen(false)}
                notifications={filteredNotifications}
            />
            <Badge
                badgeContent={filteredNotifications.length}
                color='error'>
                <NotificationsNoneIcon onClick={() => setIsDialogOpen(true)}/>
            </Badge>
        </Box>);
}