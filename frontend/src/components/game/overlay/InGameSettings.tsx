import {
    Dialog,
    DialogContent, DialogContentText,
    DialogTitle, Typography,
} from '@mui/material';
import {t} from "i18next";

export function InGameSettings({isOpen, onClose}: DialogModel) {
    return (
        <Dialog open={isOpen} onClose={onClose} scroll="body" maxWidth="lg" PaperProps={{
            style: {
                backgroundImage: "url('../../public/old-rough-paper-seamless-texture.jpg')"
            }
        }}>
            <DialogTitle style={{textAlign: 'center'}}><img src="../../../assets/title-image.png"
                                                            alt="Title image"/></DialogTitle>
            <DialogContent>
                <DialogContentText>
                    <Typography style={{color: 'black'}} variant="body1">
                        {t('settings_ingame.placeholder')}
                    </Typography>
                </DialogContentText>
            </DialogContent>
        </Dialog>
    )
}