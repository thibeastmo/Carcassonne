import axios from "axios";
import {ShopItem} from "../model/shop/ShopItem.ts";

export async function getAvatarItems() {
const response = await axios.get<ShopItem[]>(`/api/shop/avatar-shop-items`)
    return response.data;
}

export async function getThemeItems() {
    const response = await axios.get<ShopItem[]>(`/api/shop/theme-shop-items`)
    return response.data
}

export const buyItem = async (item : ShopItem) => {
    const response = await axios.post<ShopItem>(`/api/shop/buy-shop-item?shopItemId=${item.shopItemId}`)
    return response.data;
}