import {Box, MenuItem, Select, SelectChangeEvent, Typography} from "@mui/material";
import {useThemes} from "../hooks/shop/useThemes.ts";
import {ApplicationTheme} from "../model/ApplicationTheme.ts";
import {t} from "i18next";

interface SettingsProps {
    setThemeForProvider: (themeString: string) => void
}

export function Settings({setThemeForProvider}: SettingsProps) {
    const {isLoadingBoughtThemes, isErrorLoadingBoughtThemes, boughtThemes, setThemeOfUser} = useThemes();

    if (isLoadingBoughtThemes || !boughtThemes) {
        return (<>Loading...</>);
    }

    if (isErrorLoadingBoughtThemes) {
        alert("Something went wrong!");
    }

    const handleChange = async (event: SelectChangeEvent) => {
        let themeString = event.target.value as string;
        if (themeString.toUpperCase() == "WINTER") {
            console.log("Setting theme to wintertheme");
            setThemeForProvider("WINTER");
            await setThemeOfUser(ApplicationTheme.WINTER);
        } else if (themeString.toUpperCase() == "NORMAL") {
            console.log("Setting theme to normal theme");
            setThemeForProvider("NORMAL");
            await setThemeOfUser(ApplicationTheme.NORMAL);
        }
    };


    return (
        <Box>
            <Typography variant="h2">{t('settings.title')}</Typography>
            <Box>
                <Select variant={"filled"} onChange={handleChange}>
                    <MenuItem key="normal" value={ApplicationTheme.NORMAL}>
                        NORMAL
                    </MenuItem>
                    {boughtThemes.map((theme: ApplicationTheme, index: number) => (
                        <MenuItem key={index} value={theme}>
                            {theme.toUpperCase()}
                        </MenuItem>
                    ))}
                </Select>
            </Box>
        </Box>
    );
}