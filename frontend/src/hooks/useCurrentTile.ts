import {useQuery} from "@tanstack/react-query";
import {getCurrentTile} from "../services/TileDataService.tsx";
import {useGameId} from "./useGameId.ts";

export function useCurrentTile() {
    const gameId = useGameId();
    const {
        isLoading,
        isError,
        data: tile
    } = useQuery(['tile', 'gameId', gameId], () => getCurrentTile(gameId),
        {
            refetchInterval: 3000
        });

    return {
        isLoading,
        isError,
        tile
    }
}