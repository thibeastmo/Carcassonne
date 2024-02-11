import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {getPlacedTiles, placeTile} from "../services/TileDataService.tsx";
import {TilePlacement} from "../model/TilePlacement.ts";
import {useGameId} from "./useGameId.ts";

export function usePlacedTiles() {
    const gameId = useGameId();
    const queryClient = useQueryClient()
    const {
        isLoading,
        isError,
        data: tileModels
    } = useQuery(
        ['placed-tiles', 'gameId', gameId],
        () => getPlacedTiles(gameId),
        {
            refetchInterval: 3000
        }
    );
    const {
        mutate: placeTileOnBoard,
        isLoading: isPlacingTile,
        isError: isErrorPlacingTile,
    } = useMutation((tilePlacement : TilePlacement) => placeTile(gameId,tilePlacement), {
        onSuccess: () => {
            queryClient.invalidateQueries(['placed-tiles'])
        },
    })
    return {
        isLoading,
        isError,
        tileModels,
        placeTileOnBoard,
        isPlacingTile,
        isErrorPlacingTile
    }
}