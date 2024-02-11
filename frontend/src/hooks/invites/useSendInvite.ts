import {useMutation, useQueryClient} from "@tanstack/react-query";
import {sendInvite} from "../../services/LobbyDataService.ts";

export function useSendInvite() {
    const queryClient = useQueryClient()
    const {
        mutate: sendInviteToFriend,
        isLoading: isSendingInvite,
        isError: isErrorSendingInvite,
    } = useMutation((data: { lobbyId: string; accountId: string }) => sendInvite(data.lobbyId, data.accountId), {
        onSuccess: () => {
            queryClient.invalidateQueries(['invite'])
        },
    })
    return {
        isErrorSendingInvite,
        isSendingInvite,
        sendInviteToFriend
    }
}