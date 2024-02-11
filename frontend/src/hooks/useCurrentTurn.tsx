import {useGameId} from "./useGameId.ts";
import {useMutation, useQueryClient} from "@tanstack/react-query";
import {nextTurn} from "../services/GameDataService.tsx";

export function useCurrentTurn() {
    const queryClient = useQueryClient();
    const gameId = useGameId();

    const{
        mutate: nextTurnQuery,
        isLoading: isNextTurnLoading,
        isError: isNextTurnError
    } = useMutation(() => nextTurn(gameId),{
        onSuccess: () => {
            //todo: this is a hack to invalidate the legal tile placements query, should happen on placement
           queryClient.invalidateQueries(['legal-tile-placements']);
           queryClient.invalidateQueries(['player', 'gameId'])
        }
    });


    return {
        nextTurnQuery,
        isNextTurnLoading,
        isNextTurnError,
    }
}