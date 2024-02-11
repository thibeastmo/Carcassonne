import {QueryClient, useMutation, useQuery} from "@tanstack/react-query";
import {getBoughtThemes, getChosenThemeOfUser, setApplicationThemeOfUser} from "../../services/AccountDataService.tsx";
import {ApplicationTheme} from "../../model/ApplicationTheme.ts";


export function useThemes() {
    let queryClient = new QueryClient();
    const {
        isLoading: isLoadingBoughtThemes,
        isError: isErrorLoadingBoughtThemes,
        data: boughtThemes
    } = useQuery(['boughtThemes'], () => getBoughtThemes());

    const {
        isLoading: isLoadingTheme,
        isError: isErrorLoadingTheme,
        data: chosenTheme,
        refetch : refetchMethod
    } = useQuery(['selectedTheme'], () => getChosenThemeOfUser(), {
    })

    const {
        mutate: setThemeOfUser,
    } = useMutation((applicationTheme: ApplicationTheme) => setApplicationThemeOfUser(applicationTheme), {
        onSuccess: () => {
            // Trigger the 'selectedTheme' query after the mutation succeeds
            queryClient.invalidateQueries(["selectedTheme"]);
            refetchMethod();
        }
    });

    return {
        isLoadingBoughtThemes,
        isErrorLoadingBoughtThemes,
        boughtThemes,
        setThemeOfUser,
        chosenTheme,
        isErrorLoadingTheme,
        isLoadingTheme
    }
}