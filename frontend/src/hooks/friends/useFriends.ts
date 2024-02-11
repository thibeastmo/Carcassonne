import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {
    deleteFriendship, getFriends
} from "../../services/FriendshipDataService.ts";

export function useFriends() {
    const queryClient = useQueryClient()
    const {
        isLoading,
        isError,
        data: friends
    } = useQuery(['friends'], () => getFriends());
    const {
        mutate: deleteThisFriend,
        isLoading: isDeletingFriend,
        isError: isErrorDeletingFriend,
    } = useMutation((friendId: string) => deleteFriendship(friendId), {
        onSuccess: () => {
            queryClient.invalidateQueries(['friends'])
        },
    })
    return {
        isLoading,
        isError,
        friends,
        deleteThisFriend: deleteThisFriend,
        isDeletingFriend: isDeletingFriend,
        isErrorDeletingFriend: isErrorDeletingFriend
    }
}