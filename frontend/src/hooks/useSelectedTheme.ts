import {QueryClient, useQuery} from "@tanstack/react-query";
import {getChosenThemeOfUser} from "../services/AccountDataService.tsx";


export function useSelectedTheme() {
    let queryClient = new QueryClient();
    const {
        isLoading: isLoadingTheme,
        isError: isErrorLoadingTheme,
        data: chosenTheme
    } = useQuery(['selectedTheme'], () => getChosenThemeOfUser())

    function invalidateTheme() {
        queryClient.invalidateQueries(["selectedTheme"]);
    }

    return {
        isErrorLoadingTheme,
        isLoadingTheme,
        chosenTheme,
        invalidateTheme,
    }
}