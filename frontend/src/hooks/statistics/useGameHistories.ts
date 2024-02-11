import {useQuery} from "@tanstack/react-query";
import {getGameHistories, getGameHistoryByGameId} from "../../services/GameHistoryService.tsx";
import {useGameId} from "../useGameId.ts";

export function useGameHistories() {
    const gameId = useGameId();
    const {
        isLoading,
        isError,
        data: gameHistories,
    } = useQuery({
        queryKey: ['gamehistories'],
        queryFn: () => getGameHistories(),
    })
    const {
        data: gameResults,
    } = useQuery({
        queryKey: ['gamehistory'],
        queryFn: () => getGameHistoryByGameId(gameId),
    })

    return {
        isLoading,
        isError,
        gameHistories,
        gameResults,
    }
}