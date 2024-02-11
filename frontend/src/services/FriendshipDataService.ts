import axios from "axios";

export const getFriendRequests = async () => {
    const response = await axios.get(`api/account/get-friend-requests`)
    return response.data;
}
export const getFriends = async () => {
    const response = await axios.get(`api/account/get-friends`)
    return response.data;
}
export const sendFriendRequestWithUsernameOrEmail = async (usernameOrEmail : string) => {
    const response = await axios.post(`api/account/send-friend-request-with-username-or-email?usernameOrEmail=${usernameOrEmail}`)
    return response.data;
}
export const acceptFriendRequest = async (friendRequestId : string) => {
    const response = await axios.post(`api/account/accept-friend-request?friendRequestId=${friendRequestId}`)
    return response.data;
}
export const deleteFriendRequest = async (friendRequestId : string) => {
    const response = await axios.delete(`api/account/delete-friend-request?friendRequestId=${friendRequestId}`)
    return response.data;
}
export const deleteFriendship = async (friendId : string) => {
    const response = await axios.delete(`api/account/delete-friendship?friendId=${friendId}`)
    return response.data;
}
