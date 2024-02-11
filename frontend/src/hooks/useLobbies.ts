import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {createLobby, getActiveGames, getLobbies, joinLobby, leaveLobby} from "../services/LobbyDataService.ts";
import {Lobby} from "../model/Lobby.ts";
import {NewLobbyDto} from "../model/NewLobbyDto.ts";
import {useNavigate} from "react-router-dom";

export function useLobbies() {
    const queryClient = useQueryClient();
    const navigate = useNavigate();

    const {
        isLoading,
        isError,
        data: lobbies,
    } = useQuery({
        queryKey: ['lobbies'],
        queryFn: () => getLobbies(),
        refetchInterval: 5000,
    })

    const {
        data: games
    } = useQuery({
        queryKey: ['games'],
        queryFn: () => getActiveGames(),
    })
    const {
        mutate: addLobby,
        isLoading: isAddingLobby,
        isError: isErrorAddingLobby,
        data: createdLobby
    } = useMutation((lobby: NewLobbyDto) => createLobby(lobby), {
        onSuccess: (lobby: Lobby) => {
            navigate(`/lobby/${lobby.lobbyId}`);
        },
    })

    async function joinLobbyQuery(lobbyId: string) {
        await joinLobby(lobbyId);
        await queryClient.invalidateQueries(['lobbies'])
    }

    async function leaveLobbyQuery(lobbyId: string) {
        await leaveLobby(lobbyId)
        await queryClient.invalidateQueries(['lobbies'])
    }

    //const { isLoading, isError, data: lobbies } = useQuery(['lobbies'], getLobbies)

    return {
        isLoading,
        isError,
        lobbies,
        addLobby,
        isAddingLobby,
        isErrorAddingLobby,
        createdLobby,
        games,
        joinLobbyQuery,
        leaveLobbyQuery
    }
}