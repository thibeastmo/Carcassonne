import axios from "axios";

export async function placeSerf(gameId: string, tileZoneId: number) {
    const response = await axios.patch(`/api/serf/place?gameId=${gameId}&tileZoneId=${tileZoneId}`)
    return response.data;
}
export async function getLegalTilezones(gameId: string) {
    const response = await axios.get(`/api/serf/get-legal-tilezones?gameId=${gameId}`)
    return response.data;
}