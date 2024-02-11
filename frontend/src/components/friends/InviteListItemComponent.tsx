import {Box, IconButton, Typography} from "@mui/material";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import HighlightOffIcon from "@mui/icons-material/HighlightOff";
import {Invite} from "../../model/Invite.ts";
import CheckCircleOutlineIcon from "@mui/icons-material/CheckCircleOutline";
import {t} from "i18next";

export interface InviteListItemProps {
    invite: Invite;
    onDelete: (inviteId: string) => void;
    onAccept: (inviteId: string) => void;
}

export function InviteListItemComponent({invite, onAccept, onDelete}: InviteListItemProps) {
    return (
        <Box
            border={1}
            borderRadius={4}
            p={2}
            display="flex"
            alignItems="center"
            justifyContent="space-between"
            textAlign="center"
            marginBottom={2}
        >
            <Box display="flex" alignItems="center">
                <Box marginRight={2}>
                    <AccountCircleIcon fontSize="large"/>
                </Box>
                <div>
                    <Typography variant="h6" component="div" marginBottom={1}>
                        {invite.nickname}
                    </Typography>
                    <Typography variant="body2" color="textSecondary">
                        {t('invite.game_type')}: {invite.gameTypeEnum}
                    </Typography>
                    <Typography variant="body2" color="textSecondary">
                        {t('invite.lobby_name')}: {invite.lobbyName}
                    </Typography>
                    <Typography variant="body2" color="textSecondary">
                        {t('invite.max_players')}: {invite.maxPlayers}
                    </Typography>
                </div>
            </Box>
            <IconButton onClick={() => onAccept(invite.inviteId)} color="success">
                <CheckCircleOutlineIcon color="success"/>
            </IconButton>
            <IconButton onClick={() => onDelete(invite.inviteId)} color="error">
                <HighlightOffIcon color="error"/>
            </IconButton>
        </Box>
    );
}