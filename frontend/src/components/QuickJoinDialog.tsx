import {useQuickjoin} from "../hooks/useQuickjoin.ts";
import {Box, Button, Dialog} from "@mui/material";
import {useState} from "react";
import {useNavigate} from "react-router-dom";
import Loader from "./Loader.tsx";
import {useTranslation} from "react-i18next";
import {CreateLobbyDialog} from "./lobby/CreateLobbyDialog.tsx";
import Typography from "@mui/material/Typography";


interface DialogModel {
    isOpen: boolean
    onClose: () => void
}


export function QuickJoinDialog({isOpen, onClose}: DialogModel) {
    const {isLoading, isError, foundLobby, setGameType} = useQuickjoin();
    const [selectedOption, setSelectedOption] = useState<string | null>(null);
    const navigate = useNavigate();
    const {t} = useTranslation();
    const [isDialogOpen, setIsDialogOpen] = useState(false);

    const handleOptionClick = async (option: string) => {
        setSelectedOption(option);
        setGameType(option);
    };

    const handleRetrySearch = async () => {
        setSelectedOption(null);
    };


    return (
        <Dialog open={isOpen} onClose={onClose} scroll="body" maxWidth="lg" fullWidth={true} PaperProps={{
            sx: {
                width: "50%",
                minHeight: "30%",
            }
        }}>
            <CreateLobbyDialog
                isOpen={isDialogOpen}
                onClose={() => setIsDialogOpen(false)}
            />

            <Box sx={{textAlign: "center", paddingTop: '5%'}}>
                {selectedOption === null ? (
                    <Box sx={{alignItems: "center"}}>
                        <Typography variant="h4">{t('lobby_quick_join.text_quickjoin_type_of_game')}</Typography>
                        <Button variant="contained" style={{margin: "10px", width:"400px", height: "50px"}}
                                onClick={() => handleOptionClick("SHORT")}>{t('lobby_quick_join.text_quickjoin_short_game')}</Button>
                        <Button variant="contained" style={{margin: "10px", width:"400px", height: "50px"}}
                                onClick={() => handleOptionClick("LONG")}>{t('lobby_quick_join.text_quickjoin_long_game')}</Button>
                    </Box>
                ) : (
                    <Box>
                        <p>
                            {isLoading ? (
                                <Loader>{t('lobby_quick_join.text_quickjoin_waiting')}</Loader>
                            ) : isError || !foundLobby ? (
                                <div>
                                    <h2>{t('lobby_quick_join.text_quickjoin_no_lobby_found')}</h2>
                                    <Button style={{margin: "10px"}} onClick={() => {
                                        setIsDialogOpen(true)
                                    }} variant="contained">
                                        {t('lobby_overview.text_create_game')}
                                    </Button>
                                    <Button onClick={handleRetrySearch} variant="contained">
                                        {t('lobby_quick_join.text_quickjoin_try_again')}
                                    </Button>
                                </div>
                            ) : foundLobby ? (<>
                                    `Data: ${foundLobby.lobbyId}`
                                    {navigate(`/lobby/${foundLobby?.lobbyId}`)}
                                </>
                            ) : (
                                t('lobby_quick_join.text_quickjoin_waiting')
                            )}
                        </p>
                    </Box>
                )}
            </Box>
        </Dialog>
    );
}