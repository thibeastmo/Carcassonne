import {getBoughtAvatars} from "../../services/AccountDataService.tsx";
import {useQuery, useQueryClient} from "@tanstack/react-query";


export function useAvatars() {
    const queryClient = useQueryClient();
    const {
        isLoading: isLoadingBoughtAvatars,
        isError: isErrorLoadingBoughtAvatars,
        data: boughtAvatars,
    } = useQuery(['boughtAvatars'], () => getBoughtAvatars());

    return {
        isLoadingBoughtAvatars,
        isErrorLoadingBoughtAvatars,
       boughtAvatars,
        queryClient
    }
}