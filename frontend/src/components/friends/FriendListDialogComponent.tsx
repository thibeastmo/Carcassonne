import {
    Box,
    Dialog, DialogContent, DialogTitle, Tab
} from '@mui/material'
import TabContext from '@mui/lab/TabContext';
import TabList from '@mui/lab/TabList';
import TabPanel from '@mui/lab/TabPanel';
import {SyntheticEvent, useState} from "react";
import {FriendListComponent} from "./FriendListComponent.tsx";
import {AddFriendComponent} from "./AddFriendComponent.tsx";
import {FriendRequestListComponent} from "./FriendRequestListComponent.tsx";
import {InviteListComponent} from "./InviteListComponent.tsx";
import {t} from "i18next";

export interface FriendListDialogProps {
    isOpen: boolean;
    onClose: () => void;
    friendRequestsResponse: any;
    invitesResponse: any;
}

export function FriendListDialogComponent({isOpen, onClose, friendRequestsResponse, invitesResponse}: FriendListDialogProps) {
    const [value, setValue] = useState('1');
    const handleChange = (_event: SyntheticEvent, newValue: string) => {
        setValue(newValue);
    };
    return (
        <Dialog open={isOpen} onClose={onClose} scroll="body" maxWidth="lg">
            <TabContext value={value}>
                <DialogTitle>
                    <Box sx={{borderBottom: 1, borderColor: 'divider'}}>
                        <TabList onChange={handleChange} aria-label="Vrienden tabs">
                            <Tab label={t('friend.friends')} value="1"/>
                            <Tab label={t('friend.friend_requests')} value="2"/>
                            <Tab label={t('friend.add')}value="3"/>
                            <Tab label={t('friend.invites')} value="4"/>
                        </TabList>
                    </Box>
                </DialogTitle>
                <DialogContent>
                    <TabPanel value="1">
                        <FriendListComponent/>
                    </TabPanel>
                    <TabPanel value="2"><FriendRequestListComponent friendRequestsResponse={friendRequestsResponse}/></TabPanel>
                    <TabPanel value="3"><AddFriendComponent/></TabPanel>
                    <TabPanel value="4"><InviteListComponent invitesResponse={invitesResponse}/></TabPanel>
                </DialogContent>
            </TabContext>
        </Dialog>
    );
}