import {useMutation} from "@tanstack/react-query";
import {ShopItem} from "../../model/shop/ShopItem.ts";
import {equipShopItem} from "../../services/AccountDataService.tsx";


export function useEquip() {
    const {
        mutate: equipBoughtItem,
        isLoading: isEquipingItem,
        isError: isErrorEquipping,
        error: errorEquip,
    } = useMutation((item: ShopItem) => equipShopItem(item), {
    })


    return {
        isEquipingItem,
        equipBoughtItem,
        isErrorEquipping,
        errorEquip
    }
}
