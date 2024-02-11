import {useGameId} from "./useGameId.ts";
import {useQuery} from "@tanstack/react-query";
import {getMaxTurnDuration} from "../services/GameDataService.tsx";

export function useMaxTurnDuration() {
    const gameId = useGameId();
    const {
        isLoading,
        isError,
        data: maxTurnDurationInMinutes
    } = useQuery(['turnDuration', 'gameId', gameId], () => getMaxTurnDuration(gameId),
        {
            refetchInterval: 5000
        });

    return {
        isLoading,
        isError,
        maxTurnDurationInMinutes
    }
}