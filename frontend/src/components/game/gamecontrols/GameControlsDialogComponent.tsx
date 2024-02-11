import {useTranslation} from "react-i18next";
import {Dialog, DialogContent, Divider} from "@mui/material";
import Typography from "@mui/material/Typography";

export function GameControlsDialogComponent({isOpen, onClose}: DialogModel) {
    const {t} = useTranslation();
    const controlsContent = [
        {
            title: t('controls.buttons_title'),
            text: t('controls.buttons'),
            image: '/src/assets/controls/buttons.png',
        },
        {
            title: t('controls.players_title'),
            text: t('controls.players'),
            image: '/src/assets/controls/players.png',
        },
        {
            title: t('controls.time_title'),
            text: t('controls.time'),
            image: '/src/assets/controls/time.png',
        },
        {
            title: t('controls.tilecontrols_title'),
            text: t('controls.tilecontrols'),
            image: '/src/assets/controls/tilecontrols.png',
        },
        {
            title: t('controls.preview_title'),
            text: t('controls.preview'),
            image: '/src/assets/controls/preview.png',
        },
        {
            title: t('controls.placeserf_title'),
            text: t('controls.placeserf'),
            image: '/src/assets/controls/placeserf.png',
        },
    ];
    return (
        <Dialog open={isOpen} onClose={onClose} scroll="body" maxWidth="lg" PaperProps={{
            style: {
                background: 'linear-gradient(to bottom, #89cf86, #a0d593, #b7dd9f, #cef1ab, #e3fbb7)',
            }
        }}>
            <DialogContent>
                {controlsContent.map((section, index) => (
                    <div key={index}>
                        <Typography variant="h5">{section.title}</Typography>
                        <Typography>{section.text}</Typography>
                        <img src={section.image} alt={`Section ${index + 1}`} style={{ maxWidth: '100%' }} />
                        <Divider style={{ margin: '16px 0' }} />
                    </div>
                ))}
            </DialogContent>
        </Dialog>
    )
}