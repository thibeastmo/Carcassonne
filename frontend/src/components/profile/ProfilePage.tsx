import {Button, Typography} from "@mui/material";
import {t} from "i18next";
import {AvatarOverview} from "./AvatarOverview.tsx";

export function ProfilePage() {
    const handleClickReferenceToKeycloak = () => {
        window.open(import.meta.env.VITE_KC_URL + "/realms/"+import.meta.env.VITE_KC_REALM+"/account/#/personal-info", '_blank');
    };
    return <div>

        <Typography variant="h2">{t('profile.header')}</Typography>
        <Button variant="contained" color="primary" onClick={handleClickReferenceToKeycloak} style={{}}>
            {t('profile.change_account_info_button')}
        </Button>
        <AvatarOverview/>
    </div>;


}