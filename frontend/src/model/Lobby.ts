import {LobbyParticipant} from "./LobbyParticipant.ts";


export type Lobby = {
    lobbyId: string,
    lobbyName: string,
    gameId: string | null,
    maxPlayers: number,
    gameTypeEnum: string,
    lobbyParticipants: LobbyParticipant[]
    joined: boolean
    host: boolean
}
