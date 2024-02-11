import {Box, Fab} from "@mui/material";
import {InGameSettings} from "./InGameSettings.tsx";
import {HamburgerButton} from "../../HamburgerButton.tsx";
import {useState} from "react";

export function SettingsButtonComponent() {
    const [isDialogOpen, setIsDialogOpen] = useState(false)
    return (
        <Box
            style={{
            }} >
            <InGameSettings
                isOpen={isDialogOpen}
                onClose={() => setIsDialogOpen(false)}
            />
            <Fab variant="extended"
                 onClick={() => setIsDialogOpen(true)}
            >
                <HamburgerButton />
            </Fab>
        </Box>
    );
}