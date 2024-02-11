import {useFriends} from "../../hooks/friends/useFriends.ts";
import {Friend} from "../../model/Friend.ts";
import {FriendListItemComponent} from "./FriendListItemComponent.tsx";
import {t} from "i18next";

export function FriendListComponent(){
    const {deleteThisFriend, friends, isDeletingFriend, isError, isErrorDeletingFriend, isLoading} = useFriends();
    if (isLoading || isDeletingFriend){
        return <div>{t('loading.loading')}</div>
    }
    if (isError || isErrorDeletingFriend){
        return <div>{t('loading.error')}</div>
    }
    const handleDelete = (friendId : string) => {
        deleteThisFriend(friendId);
    };
    return (
        <div>
            {friends.map((friend : Friend) => (
                <FriendListItemComponent key={friend.accountId} friend={friend} onDelete={handleDelete} />
            ))}
        </div>
    );
}