import {useState} from "react";
import {Box, Fab, Tooltip} from "@mui/material";
import {GameRulesDialogComponent} from "./GameRulesDialogComponent.tsx";
import AutoStoriesIcon from '@mui/icons-material/AutoStories';
import {useTranslation} from "react-i18next";

export function GameRulesButtonComponent() {
    const [isDialogOpen, setIsDialogOpen] = useState(false);
    const {t} = useTranslation();
    return (
        <Box sx={{position: 'relative'}}>
            <GameRulesDialogComponent
                isOpen={isDialogOpen}
                onClose={() => setIsDialogOpen(false)}
            />
            <Tooltip title={t('instructions.button_tooltip')}>
                <Fab variant="extended"
                     onClick={() => setIsDialogOpen(true)}
                >
                    <AutoStoriesIcon/>
                </Fab>
            </Tooltip>
        </Box>);
}