import {Friend} from "../../model/Friend.ts";
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import HighlightOffIcon from '@mui/icons-material/HighlightOff';
import {Box, Button, IconButton, Typography} from "@mui/material";
import {useSendInvite} from "../../hooks/invites/useSendInvite.ts";
import {useState} from "react";
import {useLocation} from "react-router-dom";
import {t} from "i18next";

export interface FriendListItemProps {
    friend: Friend;
    onDelete: (friendId: string) => void;
}

export function FriendListItemComponent({friend, onDelete}: FriendListItemProps) {
    const [inviteSent, setInviteSent] = useState(false);
    const {isErrorSendingInvite, isSendingInvite, sendInviteToFriend} = useSendInvite();
    const location = useLocation();
    const pathSegments = location.pathname.split('/');
    const hasLobbySegment = pathSegments.includes('lobby');
    const lobbyId = hasLobbySegment ? pathSegments.pop() || 'UNKNOWN' : 'UNKNOWN';

    const handleSendInvite = (lobbyId: string, accountId: string) => {
        sendInviteToFriend({lobbyId, accountId});
    };
    if (isSendingInvite){
        return (<div>{t('loading.sending')}</div>);
    }
    if (isErrorSendingInvite){
        return (<div>{t('loading.error')}</div>);
    }
    if (lobbyId != 'UNKNOWN') {
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
                            {friend.nickname}
                        </Typography>
                        <Typography color="textSecondary">{t('friend.score')}: {friend.experiencePoints}</Typography>
                    </div>
                </Box>
                <Button disabled={inviteSent} onClick={() => {
                    handleSendInvite(lobbyId, friend.accountId)
                    setInviteSent(true)
                }} variant={'outlined'}>{t('friend.invite')}</Button>
                <IconButton onClick={() => onDelete(friend.accountId)} color="error">
                    <HighlightOffIcon color={"error"}/>
                </IconButton>
            </Box>
        );
    }
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
                        {friend.nickname}
                    </Typography>
                    <Typography color="textSecondary">{t('friend.score')}: {friend.experiencePoints}</Typography>
                </div>
            </Box>
            <IconButton onClick={() => onDelete(friend.accountId)} color="error">
                <HighlightOffIcon color={"error"}/>
            </IconButton>
        </Box>
    );
}