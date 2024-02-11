import {Box} from "@mui/material";
import NotificationsNoneIcon from "@mui/icons-material/NotificationsNone";

export function NotificationEmptyButtonComponent() {
    return (
        <Box sx={{position: 'relative'}}>
            <NotificationsNoneIcon/>
        </Box>
    );
}