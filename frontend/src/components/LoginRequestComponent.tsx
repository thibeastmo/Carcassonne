import {useContext} from "react";
import SecurityContext from "../context/securityContext/SecurityContext.ts";
import {Box, Button, Typography} from "@mui/material";
import {t} from "i18next";

export function LoginRequestComponent() {
    const {login} = useContext(SecurityContext);
    return (
        <Box sx={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
            <img src="/src/assets/title-image.png"
                 alt="Title image"/>
            <Typography sx={{margin: '1em'}}>{t('login.text_login_message')}</Typography>
            <Button sx={{margin: '2em'}} type="submit" variant="contained" onClick={login}>{t('login.text_login')}</Button>
        </Box>
    )
}