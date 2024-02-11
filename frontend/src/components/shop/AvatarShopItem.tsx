import {Card, CardActionArea, CardContent, CardMedia, Typography} from "@mui/material";
import {ShopItem} from "../../model/shop/ShopItem.ts";
import SellIcon from '@mui/icons-material/Sell';
import {AvatarConfirmationDialog} from "./AvatarConfirmationDialog.tsx";
import {useState} from "react";
import {QueryClient} from "@tanstack/react-query";


interface AvatarShopItemProps {
    avatarItem: ShopItem,
    queryClient : QueryClient
}

export function AvatarShopItem({avatarItem, queryClient}: AvatarShopItemProps) {
    const imageUrl = import.meta.env['VITE_BACKEND_URL'] + avatarItem.imageUrl;
    const [isOpen, setIsOpen] = useState<boolean>(false);

    const handleAvatarClick = () => {
        setIsOpen(true);
    }


    return (
        <div style={{width:"200px"}}>
            <AvatarConfirmationDialog key={avatarItem.shopItemId} queryClient={queryClient} shopItem={avatarItem} isOpen={isOpen} onClose={() => {setIsOpen(false)}}></AvatarConfirmationDialog>
            <Card onClick={handleAvatarClick}>
                <CardActionArea>
                    <CardMedia
                        component="img"
                        height="130px"
                        image={imageUrl}
                        alt="avatar image"
                    />
                </CardActionArea>
                <CardContent>
                    <Typography gutterBottom variant="h5" component="div">
                        {avatarItem.name}
                    </Typography>
                    <Typography>
                        <SellIcon/>
                        {avatarItem.price}
                    </Typography>
                </CardContent>
            </Card>
        </div>
    )
}