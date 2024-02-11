import {FriendRequest} from "../../model/FriendRequest.ts";
import {FriendRequestListItemComponent} from "./FriendRequestListItemComponent.tsx";
import {t} from "i18next";

export interface FriendRequestListProps {
    friendRequestsResponse: any;
}

export function FriendRequestListComponent({friendRequestsResponse}: FriendRequestListProps) {

    const {
        acceptThisFriendRequest,
        deleteThisFriendRequest,
        friendRequests,
        isAcceptingFriendRequest,
        isDeletingFriendRequest,
        isError,
        isErrorAcceptingFriendRequest,
        isErrorDeletingFriendRequest,
        isLoading
    } = friendRequestsResponse;
    if (isLoading || isAcceptingFriendRequest || isDeletingFriendRequest) {
        return <div>{t('loading.loading')}</div>
    }
    if (isError || isErrorAcceptingFriendRequest || isErrorDeletingFriendRequest) {
        return <div>{t('loading.error')}</div>
    }
    const handleDelete = (friendRequestId: string) => {
        deleteThisFriendRequest(friendRequestId);
    };
    const handleAccept = (friendRequestId: string) => {
        acceptThisFriendRequest(friendRequestId);
    };
    return (
        <div>
            {friendRequests.map((friendRequest: FriendRequest) => (
                <FriendRequestListItemComponent key={friendRequest.friendRequestId} friendRequest={friendRequest}
                                                onAccept={handleAccept} onDelete={handleDelete}/>
            ))}
        </div>
    );
}