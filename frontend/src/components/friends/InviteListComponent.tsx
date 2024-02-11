import {Invite} from "../../model/Invite.ts";
import {InviteListItemComponent} from "./InviteListItemComponent.tsx";
import {t} from "i18next";
export interface InviteListProps {
    invitesResponse: any;
}
export function InviteListComponent({invitesResponse} : InviteListProps){
    const {
        acceptThisInvite,
        deleteThisInvite,
        invites,
        isAcceptingInvite,
        isDeletingInvite,
        isError,
        isErrorAcceptingInvite,
        isErrorDeletingInvite,
        isLoading
    } = invitesResponse;
    if (isLoading || isAcceptingInvite || isDeletingInvite){
        return <div>{t('loading.loading')}</div>
    }
    if (isError || isErrorAcceptingInvite || isErrorDeletingInvite){
        return <div>{t('loading.error')}</div>
    }
    const handleDelete = (inviteId : string) => {
        deleteThisInvite(inviteId);
    };
    const handleAccept = (inviteId : string) => {
        acceptThisInvite(inviteId);
    };
    return (
        <div>
            {invites.map((invite : Invite) => (
                <InviteListItemComponent key={invite.inviteId} invite={invite} onAccept={handleAccept} onDelete={handleDelete} />
            ))}
        </div>
    );
}