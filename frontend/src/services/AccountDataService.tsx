import axios from "axios";
import {ShopItem} from "../model/shop/ShopItem.ts";
import {ApplicationTheme} from "../model/ApplicationTheme.ts";
import {LoyaltyPoints} from "../model/statistics/LoyaltyPoints.ts";
import {Leaderboard} from "../model/statistics/Leaderboard.ts";

const BACKEND_URL = import.meta.env.VITE_BACKEND_URL
export function createBackendAccount() {
    return axios.post( `${BACKEND_URL}/api/account`)
        .then(response => {
            console.log('Login successful into backend:', response.data);
            return response.data;
        })
        .catch(error => {
            console.error('Error logging into backend:', error);
            throw error;
        });
}

export const getInvites = async () => {
    const response = await axios.get(`/api/account/get-invites`);
    return response.data;
}

export const equipShopItem = async (item : ShopItem) => {
    const response = await axios.patch(`/api/account/equip-shop-item?shopItemId=${item.shopItemId}`);
    return response.data;
}

export const getBoughtThemes = async () => {
    const response = await axios.get<ApplicationTheme[]>("/api/account/bought-themes")
    return response.data;
}
export const getBoughtAvatars = async () => {
    const response = await axios.get<ShopItem[]>("/api/account/bought-avatars")
    return response.data;
}

export const setApplicationThemeOfUser = async (applicationTheme : ApplicationTheme) => {
    const response = await axios.patch<ApplicationTheme>(`/api/account/set-theme?theme=${applicationTheme.toUpperCase()}`)
    return response.data
}

export const getChosenThemeOfUser = async () => {
    const response = await axios.get<ApplicationTheme>("/api/account/selected-theme")
    return response.data;
}
export const getNotifications = async () => {
    const response = await axios.get(`/api/account/notifications`);
    return response.data;
}
export const getLoyaltyPoints = async () => {
    const response = await axios.get<LoyaltyPoints>(`/api/account/loyaltypoints`);
    return response.data;
}
export const getLeaderboard = async () => {
    const response = await axios.get<Leaderboard>(`/api/account/leaderboard`);
    return response.data;
}