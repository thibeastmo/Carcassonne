import {ApplicationTheme} from "../model/ApplicationTheme.ts";
import {createContext} from "react";

export interface IApplicationThemeContext {
    applicationTheme : ApplicationTheme
    setApplicationTheme: (applicationTheme: ApplicationTheme ) => void;
}



export default createContext<IApplicationThemeContext>({
    applicationTheme:ApplicationTheme.NORMAL,
    setApplicationTheme: () => {}
})