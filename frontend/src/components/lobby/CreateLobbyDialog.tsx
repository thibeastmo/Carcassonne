import {
    Box,
    Button,
    Dialog,
    List,
    ListItem,
    ListItemText,
    TextField,
    Typography
} from "@mui/material";
import { useState} from "react";
import {useLobbies} from "../../hooks/useLobbies.ts";
import {t} from "i18next";


interface ItemDialogProps {
    isOpen: boolean
    onClose: () => void
}


export function CreateLobbyDialog({isOpen, onClose}: ItemDialogProps) {
    const [shortGame, setShortGame] = useState(true);
    const [gameTypeEnum, setGameTypeEnum] = useState("SHORT");
    const [maxPlayers, setMaxPlayers] = useState(2);
    const [lobbyName, setLobbyName] = useState("");

    const {addLobby} = useLobbies();

    const createLobby = () => {
        const newLobbyDto = {
            lobbyName,
            maxPlayers,
            gameTypeEnum: gameTypeEnum.toUpperCase(),
            ownerId: 'c47e8e47-92d7-4ee9-a77d-3f6a3d63e289'
        };
        addLobby(newLobbyDto);
    };

    const handleSubmit = () => {
        createLobby();
        handleResetForm();
    }

    const handleResetForm = () => {
        setGameTypeEnum("");
        setLobbyName("");
        setMaxPlayers(2);
    }
    const handleInputPlayers = (e: React.ChangeEvent<HTMLTextAreaElement | HTMLInputElement>) => {
        const inputValue = parseInt(e.target.value, 10); // Convert string to number
        if (!isNaN(inputValue) && inputValue >= 2 && inputValue <= 5) {
            setMaxPlayers(inputValue); // Update the state
        }
    };

    const handleGameTypeSelection = (type: string) => {
        if (type.toUpperCase() == 'SHORT') {
            setShortGame(true);
        } else setShortGame(false);
        setGameTypeEnum(type);
    };

    const gamemodeShortBoxStyle = {
        width: "600px",
        height: "300px",
        backgroundColor: shortGame ? '#35baf6' : 'transparent',
        borderRadius: 2,
        cursor: "pointer"
    };

    const gamemodeLongBoxStyle = {
        width: "600px",
        height: "300px",
        backgroundColor: !shortGame ? 'orange' : 'transparent',
        borderRadius: 2,
        cursor: "pointer"
    };


    return (
        <Dialog open={isOpen} onClose={onClose} maxWidth="md" fullWidth>
            <Box style={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                <Box>
                    <Typography variant="h4" fontFamily="Old English Text MT">{t('lobby_item.settings')}</Typography>
                </Box>
                <Box>
                    <TextField
                        required
                        id="outlined-required"
                        label={t('lobby_item.lobby_name')}
                        onBlur={(e) => setLobbyName(e.target.value)}
                    />
                    <TextField
                        id="outlined-number"
                        label={t('lobby_item.amount_of_players')}
                        type="number"
                        InputProps={{ inputProps: { min: "2", max: "5", step: "1"},defaultValue: "2" }}
                        InputLabelProps={{
                            shrink: true,
                        }}
                        onChange={(e) => handleInputPlayers(e)}
                    />
                </Box>
            </Box>
            <Box sx={{textAlign:"center"}}>
                <Box>
                    <Typography variant="h4" fontFamily="Old English Text MT">Gamemodes</Typography>
                </Box>
                <Box sx={{display: "flex"}}>
                    <Box sx={gamemodeShortBoxStyle} onClick={() => {
                        handleGameTypeSelection('short')
                    }}>
                        <Box>
                            <Typography variant="h6"> {t('lobby_item.short_game')}</Typography>
                        </Box>
                        <List>
                            <ListItem>
                                <ListItemText>
                                    {t('lobby_item.short_game_description')}
                                </ListItemText>
                            </ListItem>
                            <ListItem>
                                <ListItemText>
                                    {t('lobby_item.short_game_duration')}
                                </ListItemText>
                            </ListItem>
                            <ListItem>
                                <ListItemText>
                                    {t('lobby_item.short_game_turn_time')}
                                </ListItemText>
                            </ListItem>
                        </List>
                    </Box>
                    <Box sx={gamemodeLongBoxStyle} onClick={() => {
                        handleGameTypeSelection('long')
                    }}>
                        <Box>
                            <Typography variant="h6"> {t('lobby_item.long_game')}</Typography>
                        </Box>
                        <List>
                            <ListItem>
                                <ListItemText>
                                    {t('lobby_item.long_game_description')}
                                </ListItemText>
                            </ListItem>
                            <ListItem>
                                <ListItemText>
                                    {t('lobby_item.long_game_duration')}
                                </ListItemText>
                            </ListItem>
                            <ListItem>
                                <ListItemText>
                                    {t('lobby_item.long_game_turn_time')}
                                </ListItemText>
                            </ListItem>
                        </List>
                    </Box>
                </Box>
                <Button style={{margin:"10px"}} variant="contained" onClick={handleSubmit}> {t('lobby_item.create_lobby')}</Button>
            </Box>
        </Dialog>
    )
}