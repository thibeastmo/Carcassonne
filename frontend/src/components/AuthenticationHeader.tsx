import {useContext} from "react";
import SecurityContext from "../context/securityContext/SecurityContext.ts";
import {Button, Stack, Typography} from "@mui/material";

export function AuthenticationHeader() {
    const { isAuthenticated, logout, loggedInUser } = useContext(SecurityContext)
    return (
        <Stack direction="row" alignItems="center" spacing={2} sx={{ mt: 3, ml: 3 }}>
            {isAuthenticated() && (
                <>
                    <Typography>Hello {loggedInUser?.nickname}</Typography>
                    <Button type="submit" variant="contained" sx={{ mt: 3, mb: 2 }} onClick={logout}>
                        Log out
                    </Button>
                </>
            )}
        </Stack>
    )
}