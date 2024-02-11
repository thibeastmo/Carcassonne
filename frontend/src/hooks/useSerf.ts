import {useMutation, useQueryClient} from "@tanstack/react-query";
import {placeSerf} from "../services/SerfService.tsx";
import {useGameId} from "./useGameId.ts";

export function useSerf(tileZone: number) {
    const queryClient = useQueryClient()
    const gameId = useGameId();
    const {
        mutate: placeSerfOnTile,
        isLoading: isPlacingSerf,
        isError: isErrorPlacingSerf,
    } = useMutation(() => placeSerf(gameId, tileZone), {
        onSuccess: () => {
            queryClient.invalidateQueries(['placed-tiles', 'gameId'])
            queryClient.invalidateQueries(['player','gameId'])
        },
    })
    return {
        placeSerfOnTile,
        isPlacingSerf,
        isErrorPlacingSerf
    }
}