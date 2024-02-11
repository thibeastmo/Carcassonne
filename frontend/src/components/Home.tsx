import {Box, Typography} from "@mui/material";
import {useState} from "react";
import "./Home.css"
import {QuickJoinDialog} from "./QuickJoinDialog.tsx";
import {t} from "i18next";

export function Home() {
    const [isDialogOpen, setIsDialogOpen] = useState(false)

    const handleOnClose = () => {
        setIsDialogOpen(false);
    }

    return (
        <>
            <Box sx={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                <img src="/src/assets/title-image.png"
                     alt="Title image"/>
            </Box>
            <Box>
                <QuickJoinDialog isOpen={isDialogOpen} onClose={handleOnClose}></QuickJoinDialog>
            </Box>


            <Typography variant="h2">{t('home.welcome')}</Typography>

            <p>{t('home.welcome_text')}</p>
        </>
    )
}