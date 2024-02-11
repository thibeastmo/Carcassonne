import {useQuery} from "@tanstack/react-query";
import {getUserStatistics} from "../../services/UserStatisticsService.ts";

export function useUserStatistics() {
    const {
        isLoading,
        isError,
        data: userStatistics
    } = useQuery(['userStatistics'], () => getUserStatistics());

    return {
        isLoading,
        isError,
        userStatistics
    }
}