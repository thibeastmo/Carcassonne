import {Box, Grid, Typography} from "@mui/material";
import {ShopItem} from "../../model/shop/ShopItem.ts";
import {useAvatars} from "../../hooks/profile/useAvatars.ts";
import Loader from "../Loader.tsx";
import {AvatarSingleItem} from "./AvatarSingleItem.tsx";
import {t} from "i18next";


export function AvatarOverview() {
    const {
        isLoadingBoughtAvatars,
        isErrorLoadingBoughtAvatars,
        boughtAvatars,
        queryClient
    } = useAvatars();
    if (isLoadingBoughtAvatars || !boughtAvatars) {
        return <Loader>{t('profile.loading')}</Loader>
    }

    if (isErrorLoadingBoughtAvatars) {
        return <div>{t('profile.error')}</div>
    }
    if (boughtAvatars.length === 0) {
        return (
            <>
                <Typography variant="h2">{t('profile.avatars_header')}</Typography>
                <Typography variant="h5">{t('profile.avatars_no_avatars')}</Typography>
            </>
        )
    }
    return (
        <>
            <Typography variant="h2">{t('profile.avatars_header')}</Typography>
            <Box width="50%" margin="0 auto">
                <Grid container justifyContent="center" alignItems="center" spacing={1}>
                    {boughtAvatars.map((avatarItem: ShopItem) => (
                        <Grid item xs lg={3} key={avatarItem.shopItemId} display="flex" justifyContent="center"
                              alignItems="center">
                            <AvatarSingleItem queryClient={queryClient} avatarItem={avatarItem}/>
                        </Grid>
                    ))}
                </Grid>
            </Box>
        </>
    )
}
