import {useQuery} from "@tanstack/react-query";
import {quickJoinLobby} from "../services/LobbyDataService.ts";
import {useEffect, useState} from "react";

export function useQuickjoin() {
    const [gametype, setGameType] = useState<string | null>();

    const {data: foundLobby, isLoading, isError, refetch} = useQuery(['lobby', gametype], () => {
        return quickJoinLobby(gametype);
    }, {
        enabled: false,
        refetchOnWindowFocus: false,
        refetchInterval: data => (data === undefined ? false : 2000)
    });

    async function handleClick(gametype: string) {
        setGameType(gametype);
        const data = refetch();
        return data;
    }

    useEffect(() => {
        if (gametype != null) {
            refetch();
        }
    }, [gametype])

    return {
        foundLobby,
        isLoading,
        isError,
        handleClick,
        setGameType,
    }
}