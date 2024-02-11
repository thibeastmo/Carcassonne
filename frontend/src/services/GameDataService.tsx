import axios from "axios";

export async function nextTurn(gameId: string) {
    return axios.post('/api/game/nextTurn?gameId='+ gameId);
}
export const getMaxTurnDuration = async (gameId: string) => {
    const response = await axios.get(`api/game/maxTurnDuration?gameId=${gameId}`)
    return response.data?.maxTurnDurationInMinutes;
}

export function calculateTimeRemainingInMs(playerStartTurnTime: number, maxTurnDurationInMilliseconds: number) {
    const startTime = new Date(playerStartTurnTime);
    const currentTime = new Date();
    const differenceInMilliSeconds = Math.floor(currentTime.getTime() - startTime.getTime());
    if (maxTurnDurationInMilliseconds < 0) return -1;
    const left = maxTurnDurationInMilliseconds - differenceInMilliSeconds;
    return Math.floor( left / 1000);
}