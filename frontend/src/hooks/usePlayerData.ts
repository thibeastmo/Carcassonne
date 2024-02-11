import {useQuery} from "@tanstack/react-query";
import {getPlayerdata} from "../services/PlayerDataService.tsx";
import {useGameId} from "./useGameId.ts";
export function usePlayerData() {
    const gameId = useGameId();
    const {
        isLoading,
        isError,
        data: playerData
    } = useQuery(['player', 'gameId', gameId], () => getPlayerdata(gameId),
        {
            refetchInterval: 5000,
        });
    return {
        isLoading,
        isError,
        playerData
    }
}