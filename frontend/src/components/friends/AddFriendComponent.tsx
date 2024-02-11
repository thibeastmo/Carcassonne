import {z} from "zod";
import {FriendRequestWithUsernameOrEmail} from "../../model/FriendRequestWithUsernameOrEmail.ts";
import {Controller, useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {useSendFriendRequestWithUsernameOrEmail} from "../../hooks/friends/useSendFriendRequestWithUsernameOrEmail.ts";
import {Box, Button, TextField} from "@mui/material";
import {t} from "i18next";

const friendRequestSchema: z.ZodType<FriendRequestWithUsernameOrEmail> = z.object({
    usernameOrEmail: z.string()
})

export function AddFriendComponent() {
    const {
        isErrorSendingFriendRequest,
        isSendingFriendRequest,
        sendFriendRequest
    } = useSendFriendRequestWithUsernameOrEmail();
    const {
        handleSubmit,
        reset,
        control,
        formState: {errors},
    } = useForm<FriendRequestWithUsernameOrEmail>({
        resolver: zodResolver(friendRequestSchema),
        defaultValues: {
            usernameOrEmail: ''
        },
    })
    if (isSendingFriendRequest){
        return <div>{t('loading.sending')}</div>
    }
    if (isErrorSendingFriendRequest){
        return <div>{t('loading.error')}.</div>
    }

    return (
        <form
            onSubmit={handleSubmit((data) => {
                sendFriendRequest(data.usernameOrEmail)
                reset()
            })}
        >
            <Box sx={{display: 'flex', flexDirection: 'column', gap: '0.5rem'}}>
                <Controller
                    name="usernameOrEmail"
                    control={control}
                    render={({field}) => (
                        <TextField
                            {...field}
                            label={t('friend.username_or_email')}
                            error={!!errors.usernameOrEmail}
                            helperText={errors.usernameOrEmail?.message}
                        />
                    )}
                />
                <Button type="submit" variant="contained">
                    {t('friend.send')}
                </Button>
            </Box>
        </form>
    )
}