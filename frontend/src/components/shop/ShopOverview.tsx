import {useShopOverview} from "../../hooks/shop/useShopOverview.tsx";
import {ShopItem} from "../../model/shop/ShopItem.ts";
import {AvatarShopItem} from "./AvatarShopItem.tsx";
import {ThemeShopItem} from "./ThemeShopItem.tsx";
import Loader from "../Loader.tsx";
import {Box, Grid, Typography} from "@mui/material";
import {t} from "i18next";

export function ShopOverview() {
    const {
        isLoadingAvatars,
        isErrorLoadingAvatars,
        avatarShopItems,
        themeShopItems,
        isLoadingThemes,
        isErrorLoadingThemes,
        queryClient
    } = useShopOverview();

    if (isLoadingAvatars || !avatarShopItems || !themeShopItems || isLoadingThemes) {
        return <Loader>t('loading.loading').</Loader>
    }

    if (isErrorLoadingAvatars || isErrorLoadingThemes) {
        return <div>{t('loading.error')}</div>
    }

    return (
        <>
            <h2>{t('shop.shop')}</h2>
            <Typography variant="h2">{t('shop.avatars')}</Typography>
            <Box width="50%" margin="0 auto">
                <Grid container justifyContent="center" alignItems="center" spacing={1}>
                    {avatarShopItems.map((avatarShopItem: ShopItem) => (
                        <Grid item xs lg={3} key={avatarShopItem.shopItemId} display="flex" justifyContent="center" alignItems="center">
                            <AvatarShopItem queryClient={queryClient} avatarItem={avatarShopItem}/>
                        </Grid>
                    ))}
                </Grid>
            </Box>
            <Typography variant="h2">{t('shop.themes')}</Typography>
            {themeShopItems.map((themeShopItem: ShopItem) => (
                <Box sx={{display: 'inline-flex', flexDirection: 'row'}}>
                    <ThemeShopItem queryClient={queryClient} themeItem={themeShopItem}/>
                </Box>
            ))}
        </>
    )
}