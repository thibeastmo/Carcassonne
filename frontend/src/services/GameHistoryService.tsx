import axios from "axios";

export const getGameHistories = async () => {
    const response = await axios.get(`/api/gamehistory/all`);
    return response.data;
}
export const getGameHistoryByGameId = async (gameId: string) => {
    const response = await axios.get(`/api/gamehistory?gameId=`+gameId);
    return response.data;
}