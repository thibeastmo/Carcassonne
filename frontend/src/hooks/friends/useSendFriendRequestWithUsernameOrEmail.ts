import {useMutation, useQueryClient} from "@tanstack/react-query";
import {
    sendFriendRequestWithUsernameOrEmail
} from "../../services/FriendshipDataService.ts";

export function useSendFriendRequestWithUsernameOrEmail() {
    const queryClient = useQueryClient()
    const {
        mutate: sendFriendRequest,
        isLoading: isSendingFriendRequest,
        isError: isErrorSendingFriendRequest,
    } = useMutation((usernameOrEmail: string) => sendFriendRequestWithUsernameOrEmail(usernameOrEmail), {
        onSuccess: () => {
            queryClient.invalidateQueries(['friends'])
        },
    })
    return {
        isSendingFriendRequest,
        isErrorSendingFriendRequest,
        sendFriendRequest
    }
}