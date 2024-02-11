import Diversity3Icon from '@mui/icons-material/Diversity3';
import {Badge, Box} from "@mui/material";
import {useState} from "react";
import {FriendListDialogComponent} from "./FriendListDialogComponent.tsx";
import {useFriendRequests} from "../../hooks/friends/useFriendRequests.ts";
import {useInvites} from "../../hooks/invites/useInvites.ts";

export function OpenFriendListButtonComponent() {
    const [isDialogOpen, setIsDialogOpen] = useState(false)
    const friendRequestsResponse = useFriendRequests();
    const invitesResponse = useInvites();
    return (
        <Box sx={{position: 'relative'}}>
            <FriendListDialogComponent
                isOpen={isDialogOpen}
                onClose={() => setIsDialogOpen(false)}
                friendRequestsResponse={friendRequestsResponse}
                invitesResponse={invitesResponse}
            />
            <Badge
                badgeContent={(friendRequestsResponse.friendRequests != undefined && invitesResponse.invites != undefined) ? friendRequestsResponse.friendRequests.length + invitesResponse.invites.length : 0}
                color='error'>
                <Diversity3Icon onClick={() => setIsDialogOpen(true)}/>
            </Badge>
        </Box>);
}