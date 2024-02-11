import {Alert, Box, Button, Dialog, DialogContent, DialogTitle, Typography} from "@mui/material";
import {ShopItem} from "../../model/shop/ShopItem.ts";
import {useShopItems} from "../../hooks/shop/useShopItems.tsx";
import {QueryClient} from "@tanstack/react-query";
import {useEquip} from "../../hooks/shop/useEquip.ts";
import AttachMoneyIcon from '@mui/icons-material/AttachMoney';
import {useTranslation} from "react-i18next";

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

export function ThemeConfirmationDialog({isOpen, onClose, shopItem, queryClient}: ConfirmationDialogModel) {

    const {t} = useTranslation();
    const {
        buyShopItem,
        isBuyingItem,
        isErrorBuyingItem,
        data,
        invalidateShopItems,
    } = useShopItems(queryClient);

    const {equipBoughtItem, isErrorEquipping, errorEquip} = useEquip();

    // This is a simple workaround because typescript requires explicit types
    const castedErrorEquip = errorEquip as ErrorMessage;

    const handleBuy = async () => {
        await buyShopItem(shopItem);
    };

    const handleOnClose = () => {
        invalidateShopItems();
        onClose();
    }

    const handleEquip = () => {
        equipBoughtItem(shopItem);

        // Themes need a reload, but a slight delay works best because the backend needs to register this changes first
        const delay = 500;
        const timeoutId = setTimeout(() => {
            window.location.reload();
        }, delay);

        return () => {
            clearTimeout(timeoutId);
        };

    }

    if (isErrorEquipping) {
        return (<Alert>{t('shop.text_error_equipping_item')}: {castedErrorEquip.response.data}</Alert>)
    }

    const imageUrl = import.meta.env['VITE_BACKEND_URL'] + shopItem.imageUrl;

    return (
        <Dialog open={isOpen} onClose={handleOnClose}>
            {isErrorBuyingItem ? (
                <DialogTitle color="red">{t('shop.text_not_enough_points')}</DialogTitle>
            ) : (
                <>
                    {!data ? (
                        <>
                            <DialogTitle variant="h5"> {t('shop.text_confirmation_question')}</DialogTitle>
                            <DialogContent>
                                <Typography variant="h5">{shopItem.name}</Typography>
                                <Typography variant="h5"><AttachMoneyIcon fontSize="medium"/>{shopItem.price}</Typography>
                                <Box display="flex" justifyContent="center" padding="10px">
                                    <img src={imageUrl} height="220px" alt="Item" style={{ display: 'block', margin: 'auto' }} />
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