import {QueryClient, useMutation} from "@tanstack/react-query";
import {ShopItem} from "../../model/shop/ShopItem.ts";
import {buyItem} from "../../services/ShopDataService.ts";

export function useShopItems(queryClient : QueryClient) {
    const {
        mutate: buyShopItem,
        isLoading: isBuyingItem,
        isError: isErrorBuyingItem,
        error: error,
        data: data,
        status: status
    } = useMutation((item: ShopItem) => buyItem(item), {
    })



    function invalidateShopItems() {
        queryClient.invalidateQueries(["avatarItems"]);
        queryClient.invalidateQueries(["themeItems"]);
    }

    return {
        buyShopItem,
        isBuyingItem,
        isErrorBuyingItem,
        data,
        status,
        error,
        invalidateShopItems
    }
}