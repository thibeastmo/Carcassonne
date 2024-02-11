import {
    Button, ButtonProps,
    Dialog,
    DialogActions,
    DialogContent, DialogContentText,
    DialogTitle, styled, Typography,
} from '@mui/material'
import {useTranslation} from "react-i18next";

const ColorButton = styled(Button)<ButtonProps>(({theme}) => ({
    color: theme.palette.getContrastText('#d8c59d'),
    backgroundColor: '#d8c59d',
    '&:hover': {
        backgroundColor: '#918461',
    },
}));

export function GameRulesDialogComponent({isOpen, onClose}: DialogModel) {
    const {t} = useTranslation();
    return (
        <Dialog open={isOpen} onClose={onClose} scroll="body" maxWidth="lg" PaperProps={{
            style: {
                background: 'linear-gradient(to bottom, #89cf86, #a0d593, #b7dd9f, #cef1ab, #e3fbb7)',
            }
        }}>
                <DialogTitle style={{textAlign: 'center'}}><img src="/src/assets/title-image.png"
                                                                alt="Title image"/></DialogTitle>
                <DialogContent>
                    <Typography variant="h5">
                        {t('instructions.title_goal')}
                    </Typography>
                    <DialogContentText>
                        <Typography style={{color: 'black'}} variant="body1">
                            {t('instructions.text_goal')}
                        </Typography>
                    </DialogContentText>
                    <Typography variant="h5">{t('instructions.title_start')}</Typography>
                    <DialogContentText>
                        <Typography style={{color: 'black'}} variant="body1">
                            {t('instructions.text_start')}
                        </Typography>
                    </DialogContentText>

                    <Typography variant="h5">{t('instructions.title_progress')}</Typography>

                    <DialogContentText>
                        <Typography style={{color: 'black'}} variant="body1">
                            {t('instructions.text_progress_1')}
                        </Typography>
                        <Typography style={{color: 'black'}} variant="body1">
                            {t('instructions.text_progress_2')}
                        </Typography>
                        <Typography style={{color: 'black'}} variant="body1">
                            {t('instructions.text_progress_3')}
                        </Typography>
                        <Typography style={{color: 'black'}} variant="body1">
                            {t('instructions.text_progress_4')}
                        </Typography>
                        <Typography style={{color: 'black'}} variant="body1">
                            {t('instructions.text_progress_5')}
                        </Typography>
                    </DialogContentText>
                    <Typography variant="h6">{t('instructions.title_place_tiles')}</Typography>
                    <DialogContentText>
                        <Typography style={{color: 'black'}} variant="body1">
                            {t('instructions.text_place_tiles')}
                        </Typography>
                    </DialogContentText>
                    <Typography variant="h6">{t('instructions.title_place_serfs')}</Typography>
                    <DialogContentText>
                        <Typography style={{color: 'black'}} variant="body1">
                            {t('instructions.text_place_serfs')}
                        </Typography>
                    </DialogContentText>
                    <Typography variant="h6">{t('instructions.title_counting')}</Typography>
                    <DialogContentText>
                        <Typography style={{color: 'black'}} variant="body1">
                            {t('instructions.text_counting')}
                        </Typography>
                    </DialogContentText>
                    <Typography variant="h6">{t('instructions.title_return_serfs')}</Typography>
                    <DialogContentText>
                        <Typography style={{color: 'black'}} variant="body1">
                            {t('instructions.text_return_serfs')}
                        </Typography>
                    </DialogContentText>
                    <Typography variant="h6">{t('instructions.title_fields')}</Typography>
                    <DialogContentText>
                        <Typography style={{color: 'black'}} variant="body1">
                            {t('instructions.text_fields')}
                        </Typography>
                    </DialogContentText>
                    <Typography variant="h5">{t('instructions.title_endgame')}</Typography>
                    <DialogContentText>
                        <Typography style={{color: 'black'}} variant="body1">
                            {t('instructions.text_endgame')}
                        </Typography>
                    </DialogContentText>
                    <Typography variant="h5">{t('instructions.title_counting_end')}</Typography>
                    <Typography variant="h6">{t('instructions.title_counting_structures')}</Typography>
                    <DialogContentText>
                        <Typography style={{color: 'black'}} variant="body1">
                            {t('instructions.text_counting_structures')}
                        </Typography>
                    </DialogContentText>
                    <Typography variant="h6">{t('instructions.title_counting_farmers')}</Typography>
                    <DialogContentText>
                        <Typography style={{color: 'black'}} variant="body1">
                            {t('instructions.text_counting_farmers')}
                        </Typography>
                    </DialogContentText>
                </DialogContent>
                <DialogActions style={{paddingRight: '1.5em', paddingBottom: '1.5em'}}>
                    <ColorButton variant="contained" onClick={onClose}>
                        {t('common.close')}
                    </ColorButton>
                </DialogActions>
        </Dialog>
    )
}
