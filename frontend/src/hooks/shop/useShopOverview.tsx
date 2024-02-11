import {useQuery, useQueryClient} from "@tanstack/react-query";
import {getAvatarItems, getThemeItems} from "../../services/ShopDataService.ts";


export function useShopOverview() {
    const queryClient = useQueryClient();

    const {
        isLoading : isLoadingAvatars,
        isError: isErrorLoadingAvatars,
        data: avatarShopItems
    } = useQuery(['avatarItems'], () => getAvatarItems());

    const {
        isLoading : isLoadingThemes,
        isError: isErrorLoadingThemes,
        data: themeShopItems
    } = useQuery(['themeItems'], () => getThemeItems());



    return {
        queryClient,
        isLoadingAvatars,
        isErrorLoadingAvatars,
        avatarShopItems,
        isLoadingThemes,
        isErrorLoadingThemes,
        themeShopItems
    }
}