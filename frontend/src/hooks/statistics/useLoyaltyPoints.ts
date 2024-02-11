import {useQuery} from "@tanstack/react-query";
import {getLoyaltyPoints} from "../../services/AccountDataService.tsx";

export function useLoyaltyPoints() {
    const {
        isLoading,
        isError,
        data: loyaltyPoints,
        refetch: refetchMethod,
    } = useQuery(['loyaltyPoints'], () => getLoyaltyPoints());


    return {
        isLoading,
        isError,
        loyaltyPoints,
        refetchMethod
    }
}