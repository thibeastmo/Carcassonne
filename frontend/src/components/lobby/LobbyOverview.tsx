import {useLobbies} from "../../hooks/useLobbies.ts";
import {
    Box,
    Button,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableRow,
    Typography
} from "@mui/material";
import {useState} from "react";
import './LobbyOverview.css'
import {useNavigate} from "react-router-dom";
import {CreateLobbyDialog} from "./CreateLobbyDialog.tsx";
import {useTranslation} from "react-i18next";
import {Quickjoin} from "../Quickjoin.tsx";

export function LobbyOverview() {
    const { lobbies, joinLobbyQuery, leaveLobbyQuery, games} = useLobbies();
    const [isDialogOpen, setIsDialogOpen] = useState(false);
    const {t} = useTranslation();

    const tableCellStyle = {
        border: "2px solid black"
    }

    const navigate = useNavigate();

    let openLobbies = [];
    let joinedLobbies = [];
    let activeGames = [];

    if (games){
        for (let i = 0; i < games.length; i++){
            let lobby = games[i];
                activeGames.push(<TableRow
                    key={lobby.lobbyId}
                    sx={{borderWidth: 2, borderColor: 'black', width: '100%'}}
                >
                    <TableCell style={tableCellStyle}>
                        <Typography>{lobby.lobbyName}</Typography>
                    </TableCell>
                    <TableCell style={tableCellStyle}>
                        <Typography>{lobby.lobbyParticipants.length}/{lobby.maxPlayers}</Typography>
                    </TableCell>
                    <TableCell style={tableCellStyle}>
                        <Typography>{lobby.gameTypeEnum}</Typography>
                    </TableCell>
                    <TableCell style={tableCellStyle}>
                        <Button variant="contained" onClick={() => {
                            navigate(`/game/${games[i].gameId}`);
                        }}>{t('lobby_overview.watch_game')}</Button>
                    </TableCell>
                </TableRow>)
        }
    }

    if (lobbies) {
        for (let i = 0; i < lobbies?.length; i++) {
            let lobby = lobbies[i];
            if (!lobbies[i].joined) {
                openLobbies.push(<TableRow
                    key={lobby.lobbyId}
                    sx={{borderWidth: 2, borderColor: 'black', width: '100%'}}
                >
                    <TableCell style={tableCellStyle}>
                        <Typography>{lobby.lobbyName}</Typography>
                    </TableCell>
                    <TableCell style={tableCellStyle}>
                        <Typography>{lobby.lobbyParticipants.length}/{lobby.maxPlayers}</Typography>
                    </TableCell>
                    <TableCell style={tableCellStyle}>
                        <Typography>{lobby.gameTypeEnum}</Typography>
                    </TableCell>
                    <TableCell style={tableCellStyle}>
                        <Button variant="contained" onClick={() => {
                            joinLobbyQuery(lobbies[i].lobbyId)
                                .then(() => {navigate(`/lobby/${lobbies[i].lobbyId}`)});
                        }}>{t('lobby_overview.join')}</Button>
                    </TableCell>
                </TableRow>)
            }
            else {
                joinedLobbies.push(<><TableRow
                    key={lobby.lobbyId}
                    sx={{borderWidth: 2, borderColor: 'black', width: '100%'}}
                >
                    <TableCell style={tableCellStyle}>
                        <Typography>{lobby.lobbyName}</Typography>
                    </TableCell>
                    <TableCell style={tableCellStyle}>
                        <Typography>{lobby.lobbyParticipants.length}/{lobby.maxPlayers}</Typography>
                    </TableCell>
                    <TableCell style={tableCellStyle}>
                        <Typography>{lobby.gameTypeEnum}</Typography>
                    </TableCell>
                    <TableCell style={tableCellStyle}>
                        <Button variant="contained" onClick={() => {
                            navigate(`/lobby/${lobbies[i].lobbyId}`)
                        }}>{t('lobby_overview.watch')}</Button>
                    </TableCell>
                    <TableCell style={tableCellStyle}>
                        <Button variant="contained" onClick={() => {
                            leaveLobbyQuery(lobbies[i].lobbyId)
                        }}>{t('lobby_overview.leave')}</Button>
                    </TableCell>
                </TableRow></>)
            }
        }
    }

    return (
        <Box display="flex" flexDirection="column" alignItems="center" justifyContent="center">
            <Box>
                <CreateLobbyDialog
                    isOpen={isDialogOpen}
                    onClose={() => setIsDialogOpen(false)}
                />
            </Box>
            <Box style={{marginBottom: "10px"}}>
                <Typography variant="h4">{t('lobby_overview.title')}</Typography>
            </Box>
            <Quickjoin></Quickjoin>
            <Box>
                <Typography variant="h6" className="title">{t('lobby_overview.text_lobbies_looking_for_players')}
                </Typography>
                <Box className="wooden-border-box" minHeight="100px" minWidth="800px">
                    <TableContainer>
                        <Table aria-label="simple table">
                            <TableBody>
                                {lobbies ? (
                                    openLobbies.map((lobbyElement) => lobbyElement)
                                ) : (
                                    <div>{t('lobby_overview.text_no_lobbies_available')}</div>
                                )}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Box>
            </Box>
            <Box>
                <Button style={{margin:"10px"}} onClick={() => {
                    setIsDialogOpen(true)
                }} variant="contained">
                    {t('lobby_overview.text_create_game')}
                </Button>
            </Box>

            <Box>
                <Typography variant="h6" className="title">{t('lobby_overview.text_joined_lobbies')}</Typography>
                <Box className="wooden-border-box" minHeight="100px" minWidth="800px">
                    <TableContainer>
                        <Table aria-label="simple table">
                            <TableBody>
                                {lobbies ? (
                                    joinedLobbies.map((lobbyElement) => lobbyElement)
                                ) : (
                                    <div>{t('lobby_overview.text_no_lobbies_available')}</div>
                                )}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Box>
            </Box>

            <Box>
                <Typography variant="h6" className="title">{t('lobby_overview.text_running_games')}</Typography>
                <Box className="wooden-border-box" minHeight="100px" minWidth="800px">
                    <TableContainer>
                        <Table aria-label="simple table">
                            <TableBody>
                                {lobbies ? (
                                    activeGames.map((lobbyElement) => lobbyElement)
                                ) : (
                                    <div>{t('lobby_overview.text_no_lobbies_available')}</div>
                                )}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Box>
            </Box>
        </Box>
    )
}

