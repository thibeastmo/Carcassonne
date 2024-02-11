import axios from "axios";

export const getLegalTilePlacements = async (gameId: string) => {
    const response = await axios.get(`api/turn/getLegalPlacements?gameId=${gameId}`)
    return response.data;
}