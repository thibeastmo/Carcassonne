import {Alert, Box, Button, Dialog, DialogContent} from "@mui/material";
import {ShopItem} from "../../model/shop/ShopItem.ts";
import {useEquip} from "../../hooks/shop/useEquip.ts";
import {t} from "i18next";

interface ConfirmationDialogModel {
    isOpen: boolean,
    onClose: () => void,
    shopItem: ShopItem,
}

interface ErrorMessage {
    response: {
        data: string;
    };
}

export function AvatarEquipConfirmationDialog({isOpen, onClose, shopItem}: ConfirmationDialogModel) {
    const {
        equipBoughtItem,
        isErrorEquipping,
        errorEquip
    } = useEquip();

    const castedErrorEquip = errorEquip as ErrorMessage;

    const handleEquip = () => {
        equipBoughtItem(shopItem);
        handleOnClose();
        window.location.reload();
    }
    const handleOnClose = () => {
        onClose();
    }
    if (isErrorEquipping) {
        return (<Alert>Something went wrong while equipping item: {castedErrorEquip.response.data}</Alert>)
    }

    return (
        <Dialog open={isOpen} onClose={handleOnClose}>
            <DialogContent>{t('shop.text_equip_item_question')}</DialogContent>
            <Box sx={{justifyContent: "center", padding: "10px"}}>
                <Button variant="contained" onClick={handleEquip}>{t('shop.text_use')}</Button>
                <Button variant="contained" onClick={handleOnClose}>{t('shop.text_close')}</Button>
            </Box>
        </Dialog>

    )
}

