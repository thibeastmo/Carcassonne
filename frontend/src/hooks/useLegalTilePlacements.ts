import {useQuery} from "@tanstack/react-query";
import {getLegalTilePlacements} from "../services/LegalTilePlacementsData.tsx";
import {useGameId} from "./useGameId.ts";

export function useLegalTilePlacements() {
    const gameId = useGameId();
    const {
        isLoading,
        isError,
        data: tileModels,
        refetch,
    } = useQuery(['legal-tile-placements','gameId', gameId], () => getLegalTilePlacements(gameId),
        {
            refetchInterval: 5000
        });

    return {
        isLoading,
        isError,
        tileModels,
        refetch
    }
}