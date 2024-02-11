import axios from "axios";

export const getUserStatistics = async () => {
    const response = await axios.get(`/api/statistics`);
    return response.data;
}