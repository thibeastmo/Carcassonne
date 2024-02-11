import {Avatar} from "./Avatar.ts";

export type LobbyParticipant = {
    accountId: string,
    nickname: string,
    loyaltyPoints: number,
    experiencePoints: number,
    avatar: Avatar
}