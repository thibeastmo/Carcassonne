import {Card, CardActionArea, CardContent, CardMedia, Typography} from "@mui/material";
import {ShopItem} from "../../model/shop/ShopItem.ts";
import {QueryClient} from "@tanstack/react-query";
import {t} from "i18next";
import {useContext, useState} from "react";
import {AvatarEquipConfirmationDialog} from "./AvatarEquipConfirmationDialog.tsx";
import SecurityContext from "../../context/securityContext/SecurityContext.ts";


interface AvatarItemProps {
    avatarItem: ShopItem,
    queryClient: QueryClient
}

export function AvatarSingleItem({avatarItem}: AvatarItemProps) {
    const imageUrl = import.meta.env['VITE_BACKEND_URL'] + avatarItem.imageUrl;
    const [isOpen, setIsOpen] = useState<boolean>(false);
    const {loggedInUser} = useContext(SecurityContext);

    const handleAvatarClick = () => {
        if (loggedInUser?.avatar?.url !== avatarItem.imageUrl){
            setIsOpen(true);
        }
    }

    return (
        <>
            <AvatarEquipConfirmationDialog key={avatarItem.shopItemId} shopItem={avatarItem} isOpen={isOpen}
                                           onClose={() => {
                                               setIsOpen(false)
                                           }}></AvatarEquipConfirmationDialog>
            <div style={{width: "200px"}} onClick={handleAvatarClick}>
                <Card>
                    <CardActionArea>
                        <CardMedia
                            component="img"
                            height="130px"
                            image={imageUrl}
                            alt={t('profile.avatar_alternative')}
                        />
                    </CardActionArea>
                    <CardContent>
                        <Typography gutterBottom variant="h5" component="div">
                            {avatarItem.name}
                        </Typography>
                        <Typography>
                            {loggedInUser?.avatar?.url === avatarItem.imageUrl ? t('profile.avatar_equipped') : ""}
                        </Typography>
                    </CardContent>
                </Card>
            </div>
        </>

    )
}