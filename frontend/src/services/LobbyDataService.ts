import axios from "axios";
import {Lobby} from "../model/Lobby.ts";
import {NewLobbyDto} from "../model/NewLobbyDto.ts";


export const quickJoinLobby = async (gametype: string | null | undefined) => {
    let lobby = await axios.post<Lobby>('api/lobby/quick-join-lobby?gameTypeEnum=' + gametype);
    console.log(lobby.data);
    return lobby.data;
};

export const getLobbies = async () => {
    const lobbies = await axios.get<Lobby[]>('/api/lobby/get-all-open-lobbies')
    return lobbies.data;
}

export const startGame = async (id: string) => {
    const game = await axios.post('/api/lobby/start-game?lobbyId=' + id)
    return game;
}

export const getActiveGames = async () => {
    const listOfGames = await axios.get<Lobby[]>('/api/lobby/get-active-games')
    return listOfGames.data;
}

export const getLobby = async (id: string) => {
    const lobby = await axios.get<Lobby>('/api/lobby/get-lobby?lobbyId=' + id)
    console.log("Got lobby: " + lobby.data)
    return lobby.data;
}

export const createLobby = async (newLobbyDto: NewLobbyDto) => {
    const lobbies = await axios.post('/api/lobby/create-lobby', newLobbyDto);
    return lobbies.data;
};

export const joinLobby = async (lobbyId: string) => {
    const lobby = await axios.post('/api/lobby/join-lobby?lobbyId=' + lobbyId);
    return lobby;
}

export const leaveLobby = async (lobbyId: string) => {
    const lobby = await axios.post('/api/lobby/leave-lobby?lobbyId=' + lobbyId);
    return lobby;
}
export const sendInvite = async (lobbyId : string, accountId: string) => {
    const response = await axios.post(`/api/lobby/send-invite?lobbyId=${lobbyId}&accountId=${accountId}`);
    return response.data;
}
export const acceptInvite = async (inviteId : string) => {
    const response = await axios.post(`/api/lobby/accept-invite?inviteId=${inviteId}`);
    return response.data;
}

export const deleteInvite = async (inviteId : string) => {
    const response = await axios.delete(`/api/lobby/delete-invite?inviteId=${inviteId}`);
    return response.data;
}



export const emulateFetch = async () => {
    console.log("Fetching from the internet ...");
    return await new Promise((resolve) => {
        setTimeout(() => {
            resolve({data: "ok"});
            console.log("... done");
        }, 500);
    })
}