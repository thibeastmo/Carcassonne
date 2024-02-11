import axios from "axios";
import {PlayersScoresModel} from "../model/game/overlay/PlayersScoresModel.ts";
import {SerfsUsedModel} from "../model/game/SerfsUsedModel.ts";
import {CurrentTurnModel} from "../model/game/overlay/CurrentTurnModel.ts";
import {PlayerInGameModel} from "../model/game/PlayerInGameModel.ts";
import {PlayerDataWithoutDetails} from "../model/game/PlayerDataModel.ts";

export async function getPlayerdata(gameId: string): Promise<PlayerInGameModel[]> {
    const scores = await getScores(gameId);
    const serfsUsed = await getSerfsUsed(gameId);
    const playersConstantData = await getConstantPlayerData(gameId);
    const currentTurn = await getCurrentTurn(gameId);
    const playerData: PlayerInGameModel[] = [];

    scores.playerPoints.forEach(pScore => {
        const playerConstantData = playersConstantData.playersConstantData?.find(player => player.playerNumber === pScore.playerNumber);
        const pAvatar = playerConstantData?.avatarUrl;
        const pSerfsUsed = serfsUsed.playersSerfs.find(serfs => serfs.playerNumber === pScore.playerNumber)?.playerSerfs;
        const pBeginTurn = pScore.playerNumber === currentTurn.playerNumber ? currentTurn.beginTurn : -1;
        const pNickname = playerConstantData?.nickname;
        const pColorString = playerConstantData?.color;
        if (pAvatar === undefined) return;
        if (pBeginTurn === undefined) return;
        if (pNickname === undefined) return;
        if (pColorString === undefined) return;

        const playerInGameModel: PlayerInGameModel = {
            playerNumber: pScore.playerNumber,
            nickname: pNickname,
            avatarUrl: pAvatar,
            isTurn: pBeginTurn,
            score: pScore.points,
            serfsUsed: pSerfsUsed,
            color: pColorString
        }
        playerData.push(playerInGameModel)
    });
    return playerData;
}

async function getScores(gameId: string) {
    const result = await axios.get<PlayersScoresModel>('/api/player/scores?gameId=' + gameId);
    return result.data;
}

async function getSerfsUsed(gameId: string) {
    const result = await axios.get<SerfsUsedModel>('/api/serf/used?gameId=' + gameId);
    return result.data;
}

async function getConstantPlayerData(gameId: string) {
    const result = await axios.get<PlayerDataWithoutDetails>('/api/player/playerData?gameId=' + gameId);
    return result.data;
}

async function getCurrentTurn(gameId: string) {
    const result = await axios.get<CurrentTurnModel>('/api/game/currentTurn?gameId=' + gameId);
    return result.data;
}