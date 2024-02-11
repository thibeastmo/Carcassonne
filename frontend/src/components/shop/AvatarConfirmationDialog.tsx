import {Alert, Box, Button, Dialog, DialogContent, DialogTitle, Typography} from "@mui/material";
import {ShopItem} from "../../model/shop/ShopItem.ts";
import {useShopItems} from "../../hooks/shop/useShopItems.tsx";
import {QueryClient} from "@tanstack/react-query";
import {useEquip} from "../../hooks/shop/useEquip.ts";
import AttachMoneyIcon from '@mui/icons-material/AttachMoney';
import {useTranslation} from "react-i18next";
import {useLoyaltyPoints} from "../../hooks/statistics/useLoyaltyPoints.ts";

interface ConfirmationDialogModel {
    isOpen: boolean,
    onClose: () => void,
    shopItem: ShopItem,
    queryClient: QueryClient,
}

interface ErrorMessage {
    response: {
        data: string;
    };
}

export function AvatarConfirmationDialog({isOpen, onClose, shopItem, queryClient}: ConfirmationDialogModel) {
    const {t} = useTranslation();

    const {
        buyShopItem,
        isBuyingItem,
        isErrorBuyingItem,
        data,
        invalidateShopItems,
        error
    } = useShopItems(queryClient);

    const {equipBoughtItem, isErrorEquipping, errorEquip} = useEquip();
    const {refetchMethod} = useLoyaltyPoints();

    // This is a simple workaround because typescript requires explicit types
    const castedError = error as ErrorMessage;
    const castedErrorEquip = errorEquip as ErrorMessage;


    const handleBuy = async () => {
        buyShopItem(shopItem);
    };

    const handleOnClose = () => {
        refetchMethod();
        invalidateShopItems();
        onClose();
    }

    const handleEquip = () => {
        equipBoughtItem(shopItem);
        handleOnClose();
    }

    if (isErrorEquipping) {
        return (<Alert>Something went wrong while equipping item: {castedErrorEquip.response.data}</Alert>)
    }

    const imageUrl = import.meta.env['VITE_BACKEND_URL'] + shopItem.imageUrl;

    return (
        <Dialog open={isOpen} onClose={handleOnClose}>
            {isErrorBuyingItem ? (
                <DialogTitle color="red">{castedError.response.data}</DialogTitle>
            ) : (
                <>
                    {!data ? (
                        <>
                            <DialogTitle variant="h5"> {t('shop.text_confirmation_question')}</DialogTitle>
                            <DialogContent>
                                <Typography variant="h5">{shopItem.name}</Typography>
                                <Typography variant="h5"><AttachMoneyIcon fontSize="medium"/>{shopItem.price}</Typography>
                                <Box justifyContent="center">
                                <img src={imageUrl} height="220px"/>
                                </Box>
                                <Button
                                    variant="contained"
                                    onClick={handleBuy}
                                    disabled={isBuyingItem || data !== undefined}
                                    fullWidth
                                >
                                    {isBuyingItem ? t('shop.text_purchasing') : data !== undefined ? t('shop.text_purchased') :  t('shop.text_purchase')}
                                </Button>
                            </DialogContent>
                        </>
                    ) : (
                        <>
                            <DialogTitle>{t('shop.text_purchase_successful')}</DialogTitle>
                            <DialogContent>{t('shop.text_equip_item_question')}</DialogContent>
                            <Box sx={{justifyContent: "center", padding:"10px"}}>
                                <Button variant="contained" onClick={handleEquip}>{t('shop.text_use')}</Button>
                                <Button variant="contained" onClick={handleOnClose}>{t('shop.text_close')}</Button>
                            </Box>
                        </>
                    )}
                </>
            )}
        </Dialog>
    );
}