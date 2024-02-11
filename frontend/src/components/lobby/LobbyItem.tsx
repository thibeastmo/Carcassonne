import {useNavigate, useParams} from "react-router-dom";
import {useLobbyItems} from "../../hooks/useLobbyItems.tsx";
import Loader from "../Loader.tsx";
import {Alert, Box, Button} from "@mui/material";
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import {useTranslation} from "react-i18next";

export function LobbyItem() {
    const {id} = useParams();
    const {isLoading, isError, lobby, startGameFromLobby} = useLobbyItems(id!)
    const {t} = useTranslation();
    const navigate = useNavigate();

    const backendPrefix = import.meta.env['VITE_BACKEND_URL'];

    if (isLoading) {
        return <Loader>{t('loading.loading')}</Loader>;
    }

    if (isError || !lobby) {
        return (
            <Alert severity="error" variant="filled">
                {t('lobby_item.text_alert_not_enough_players_to_start')}
            </Alert>
        );
    }

    if (lobby.gameId != null) {
        navigate('/game/' + lobby.gameId);
    }

    let remainingSlots = lobby.maxPlayers - lobby.lobbyParticipants.length
    let remainingSlotArray = []

    const handleStartGame = () => {
        if (lobby.lobbyParticipants.length >= 2) {
            startGameFromLobby();
        } else {
            alert(t('lobby_item.text_alert_not_enough_players_to_start'));
        }
    }

    for (let i = 0; i < remainingSlots; i++) {
        remainingSlotArray.push(<>
            <Box>
                <AccountCircleIcon sx={{fontSize: 160}}/>
                <div>{t('lobby_item.text_empty_slot')}</div>
            </Box>
        </>)
    }

    return (
        <>
            <Box sx={{display: 'flex', justifyContent: 'center', alignItems: 'center'}}>
                {lobby.lobbyParticipants.map((lobbyParticipant) => (
                    <Box key={lobbyParticipant.accountId} sx={{
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                        textAlign: 'center'
                    }}>
                        {lobbyParticipant.avatar ? (
                            <img src={backendPrefix + lobbyParticipant.avatar.url} alt="User Avatar"
                                 style={{height: "150px", width: "150px"}}/>
                        ) : (
                            <AccountCircleIcon sx={{fontSize: 160}}/>
                        )}
                        <div>{lobbyParticipant.nickname}</div>
                    </Box>
                ))}
                <>{remainingSlotArray}</>
            </Box>
            {lobby.host && (<Button onClick={() => {handleStartGame()}}>{t('lobby_item.text_start_game')}</Button>
            )}
        </>
    );
}