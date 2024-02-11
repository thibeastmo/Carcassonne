import {Box, Fab, Tooltip, Typography} from "@mui/material";
import {useTranslation} from "react-i18next";
import {useNavigate} from "react-router-dom";

export function ProblemComponent() {
    const {t} = useTranslation();
    const navigate = useNavigate();
    return (

        <Box style={{
            width: '100vw',
            height: '100vh',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center'
        }}>
            <Box style={{
                backgroundColor: 'lightgray',
                borderRadius: '16px'
            }}>
                <Typography>{t('error.not_found')}</Typography>
                <Tooltip title={t('error.home_button')} arrow>
                    <Fab variant="extended"
                         onClick={() => navigate('/')}
                    >
                        <Typography textAlign="center">{t('error.home_button')}</Typography>
                    </Fab>
                </Tooltip>
            </Box>
        </Box>
    );
}