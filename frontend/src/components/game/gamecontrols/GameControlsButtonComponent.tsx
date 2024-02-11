import {useState} from "react";
import {useTranslation} from "react-i18next";
import {Box, Fab, Tooltip} from "@mui/material";
import SportsEsportsIcon from '@mui/icons-material/SportsEsports';
import {GameControlsDialogComponent} from "./GameControlsDialogComponent.tsx";

export function GameControlsButtonComponent() {
    const [isDialogOpen, setIsDialogOpen] = useState(false);
    const {t} = useTranslation();
    return (
        <Box style={{
            marginTop: "16px"
        }}
             sx={{position: 'relative'}}>
            <GameControlsDialogComponent
                isOpen={isDialogOpen}
                onClose={() => setIsDialogOpen(false)}
            />
            <Tooltip title={t('controls.button_tooltip')}>
                <Fab variant="extended"
                     onClick={() => setIsDialogOpen(true)}
                >
                    <SportsEsportsIcon/>
                </Fab>
            </Tooltip>
        </Box>);
}