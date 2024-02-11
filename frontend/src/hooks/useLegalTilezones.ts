import {useGameId} from "./useGameId.ts";
import {useQuery} from "@tanstack/react-query";
import {getLegalTilezones} from "../services/SerfService.tsx";

export function useLegalTilezones() {
    const gameId = useGameId();
    const {
        isLoading,
        isError,
        data: tilezoneIds
    } = useQuery(['legal-tilezones','gameId', gameId], () => getLegalTilezones(gameId),
        {
            refetchInterval: 5000
        });

    return {
        isLoading,
        isError,
        tilezoneIds
    }
}