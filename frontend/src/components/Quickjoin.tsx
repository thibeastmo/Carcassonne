import {Box, Button} from "@mui/material";
import {useState} from "react";
import {QuickJoinDialog} from "./QuickJoinDialog.tsx";
import {useTranslation} from "react-i18next";


export function Quickjoin() {
    const [isDialogOpen, setIsDialogOpen] = useState(false)
    const {t} = useTranslation();

    const handleQuickJoin = () => {
        setIsDialogOpen(true);
    }
    const handleOnClose = () => {
        setIsDialogOpen(false);
    }


    return (<>
            <Box>
                <QuickJoinDialog isOpen={isDialogOpen} onClose={handleOnClose}></QuickJoinDialog>
            </Box>
            <Button variant="contained" style={{margin:"10px"}} onClick={handleQuickJoin}>{t('lobby_quick_join.text_quickjoin_join_button')}</Button>
        </>
    )

}