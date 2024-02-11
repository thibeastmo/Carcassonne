import {useQuery} from "@tanstack/react-query";
import {getNotifications} from "../services/AccountDataService.tsx";

export function useNotifications() {
    const {
        isLoading,
        isError,
        data: notifications
    } = useQuery(['notifications'], () => getNotifications());

    return {
        isLoading,
        isError,
        notifications
    }
}