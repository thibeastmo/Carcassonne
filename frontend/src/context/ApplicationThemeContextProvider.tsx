import {ReactElement, useState} from "react";
import {ApplicationTheme} from "../model/ApplicationTheme.ts";
import ApplicationThemeContext from "./ApplicationThemeContext.ts";

interface WithChildren {
    children: ReactElement | ReactElement[]
}

export default function ApplicationThemeContextProvider({ children }: WithChildren) {
    const [applicationTheme, setApplicationTheme ] = useState<ApplicationTheme>(ApplicationTheme.NORMAL)

    return (
        <ApplicationThemeContext.Provider value={{ applicationTheme, setApplicationTheme }}>
            {children}
        </ApplicationThemeContext.Provider>
    )
}
