import {Box} from '@mui/material';
import React, {useContext} from 'react';
import ApplicationThemeContext, {IApplicationThemeContext} from "./context/ApplicationThemeContext.ts";
import {ApplicationTheme} from "./model/ApplicationTheme.ts";
import Snowfall from "react-snowfall";
import {useThemes} from "./hooks/shop/useThemes.ts";
import Loader from "./components/Loader.tsx";
import SecurityContext from "./context/securityContext/SecurityContext.ts";

export const Layout = ({children}: { children: React.ReactNode }) => {
    const {isErrorLoadingTheme, isLoadingTheme, chosenTheme} = useThemes();
    const {applicationTheme, setApplicationTheme} = useContext<IApplicationThemeContext>(ApplicationThemeContext);

    const {isAuthenticated } = useContext(SecurityContext);

    if (isAuthenticated()) {
        if (isLoadingTheme) {
            <Loader>Loading theme...</Loader>
        }
        if (isErrorLoadingTheme) {
            setApplicationTheme(ApplicationTheme.NORMAL);
        }

        if (chosenTheme) {
            if (chosenTheme.toString() === "NORMAL") {
                setApplicationTheme(ApplicationTheme.NORMAL);
            } else if (chosenTheme.toString() === "WINTER") {
                setApplicationTheme(ApplicationTheme.WINTER);
            }
        } else (setApplicationTheme(ApplicationTheme.NORMAL))
    }

    let bgColor = 'linear-gradient(to bottom, #89cf86, #a0d593, #b7dd9f, #cef1ab, #e3fbb7)';
    bgColor = applicationTheme === ApplicationTheme.WINTER ? "#48cae4" : bgColor;


    return (
        <>
            {applicationTheme === ApplicationTheme.WINTER ? (<Snowfall/>) : (<></>)}
            <Box sx={{
                background: bgColor, minHeight: '100vh', padding: '20px', zIndex:-9
            }}>
                {children}

            </Box>
        </>
    );
};