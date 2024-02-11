import {Box, IconButton, Typography} from "@mui/material";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import CheckCircleOutlineIcon from '@mui/icons-material/CheckCircleOutline';
import HighlightOffIcon from "@mui/icons-material/HighlightOff";
import {FriendRequest} from "../../model/FriendRequest.ts";

export interface FriendRequestListItemProps {
    friendRequest: FriendRequest;
    onDelete: (friendId: string) => void;
    onAccept: (friendId: string) => void;
}

export function FriendRequestListItemComponent({friendRequest, onAccept, onDelete}: FriendRequestListItemProps) {
    return (
        <Box
            border={1}
            borderRadius={4}
            p={1}
            display="flex"
            alignItems="center"
            justifyContent="space-between"
            textAlign="center"
            marginBottom={2}
        >
            <Box display="flex" alignItems="center">
                <Box marginRight={2}>
                    <AccountCircleIcon fontSize={"large"}/>
                </Box>
                <div>
                    <Typography variant="h6" component="div" marginBottom={1}>
                        {friendRequest.nickname}
                    </Typography>
                </div>
            </Box>
            <IconButton onClick={() => onAccept(friendRequest.friendRequestId)} color="success">
                <CheckCircleOutlineIcon color={"success"}/>
            </IconButton>
            <IconButton onClick={() => onDelete(friendRequest.friendRequestId)} color="error">
                <HighlightOffIcon color={"error"}/>
            </IconButton>
        </Box>
    );
}