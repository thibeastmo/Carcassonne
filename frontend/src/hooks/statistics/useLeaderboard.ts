import {useQuery} from "@tanstack/react-query";
import {getLeaderboard} from "../../services/AccountDataService.tsx";

export function useLeaderboard() {
    const {
        isLoading,
        isError,
        data: leaderboard
    } = useQuery(['leaderboard'], () => getLeaderboard());

    return {
        isLoading,
        isError,
        leaderboard
    }
}