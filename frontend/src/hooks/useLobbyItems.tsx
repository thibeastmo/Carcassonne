import {useQuery} from "@tanstack/react-query";
import {getLobby, startGame} from "../services/LobbyDataService.ts";


export function useLobbyItems(lobbyId : string) {
    function startGameFromLobby() {
        startGame(lobbyId);
    }

    const {
        isLoading,
        isError,
        data: lobby,
    } = useQuery({
        queryKey: ['lobby', lobbyId],
        queryFn: () => getLobby(lobbyId),
        refetchInterval:1000
    })

    return {
        lobby,
        isLoading,
        isError,
        startGameFromLobby
    }
}