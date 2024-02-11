import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {acceptInvite, deleteInvite} from "../../services/LobbyDataService.ts";
import {getInvites} from "../../services/AccountDataService.tsx";

export function useInvites() {
    const queryClient = useQueryClient()
    const {
        isLoading,
        isError,
        data: invites
    } = useQuery(['invites'], () => getInvites());
    const {
        mutate: deleteThisInvite,
        isLoading: isDeletingInvite,
        isError: isErrorDeletingInvite,
    } = useMutation((inviteId: string) => deleteInvite(inviteId), {
        onSuccess: () => {
            queryClient.invalidateQueries(['invites'])
        },
    })
    const {
        mutate: acceptThisInvite,
        isLoading: isAcceptingInvite,
        isError: isErrorAcceptingInvite,
    } = useMutation((inviteId: string) => acceptInvite(inviteId), {
        onSuccess: () => {
            queryClient.invalidateQueries(['invites'])
        },
    })
    return {
        isLoading,
        isError,
        invites,
        deleteThisInvite: deleteThisInvite,
        isDeletingInvite: isDeletingInvite,
        isErrorDeletingInvite: isErrorDeletingInvite,
        acceptThisInvite: acceptThisInvite,
        isAcceptingInvite: isAcceptingInvite,
        isErrorAcceptingInvite: isErrorAcceptingInvite
    }
}