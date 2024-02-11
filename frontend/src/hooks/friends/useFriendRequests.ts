import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {acceptFriendRequest, deleteFriendRequest, getFriendRequests} from "../../services/FriendshipDataService.ts";

export function useFriendRequests() {
    const queryClient = useQueryClient()
    const {
        isLoading,
        isError,
        data: friendRequests
    } = useQuery(['friend-requests'], () => getFriendRequests());
    const {
        mutate: acceptThisFriendRequest,
        isLoading: isAcceptingFriendRequest,
        isError: isErrorAcceptingFriendRequest,
    } = useMutation((friendRequestId: string) => acceptFriendRequest(friendRequestId), {
        onSuccess: () => {
            queryClient.invalidateQueries(['friend-requests'])
        },
    })
    const {
        mutate: deleteThisFriendRequest,
        isLoading: isDeletingFriendRequest,
        isError: isErrorDeletingFriendRequest,
    } = useMutation((friendRequestId: string) => deleteFriendRequest(friendRequestId), {
        onSuccess: () => {
            queryClient.invalidateQueries(['friend-requests'])
        },
    })
    return {
        isLoading,
        isError,
        friendRequests,
        acceptThisFriendRequest,
        isAcceptingFriendRequest,
        isErrorAcceptingFriendRequest,
        deleteThisFriendRequest,
        isDeletingFriendRequest,
        isErrorDeletingFriendRequest
    }
}