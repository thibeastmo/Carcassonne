import {Box, Card, CardActionArea, CardContent, CardMedia, Typography} from "@mui/material";
import SellIcon from "@mui/icons-material/Sell";
import {ShopItem} from "../../model/shop/ShopItem.ts";
import {QueryClient} from "@tanstack/react-query";
import {useState} from "react";
import {ThemeConfirmationDialog} from "../shop/ThemeConfirmationDialog.tsx"

interface ThemeShopItemProps {
    themeItem: ShopItem,
    queryClient: QueryClient
}


export function ThemeShopItem({themeItem, queryClient}: ThemeShopItemProps) {
    const imageUrl = import.meta.env['VITE_BACKEND_URL'] + themeItem.imageUrl;
    const [isOpen, setIsOpen] = useState<boolean>(false);

    const handleThemeItemClick = () => {
        setIsOpen(true);
    }

    const handleOnClose = () => {
        setIsOpen(false);
    }

    return (
        <Box>
            <ThemeConfirmationDialog key={themeItem.shopItemId} queryClient={queryClient}
                                     shopItem={themeItem} isOpen={isOpen} onClose={() => {
                handleOnClose()
            }}></ThemeConfirmationDialog>
            <Card onClick={handleThemeItemClick} sx={{width: "300px"}}>
                <CardActionArea>
                    <CardMedia
                        component="img"
                        height="150px"
                        image={imageUrl}
                        alt="theme image"
                    />
                </CardActionArea>
                <CardContent>
                    <Typography gutterBottom variant="h5" component="div">
                        {themeItem.name}
                    </Typography>
                    <Typography>
                        <SellIcon/>
                        {themeItem.price}
                    </Typography>
                </CardContent>
            </Card>
        </Box>
    )
}