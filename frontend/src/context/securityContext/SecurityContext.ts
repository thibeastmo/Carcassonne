import { createContext } from 'react'
import {Account} from "../../model/Account.ts";

export interface ISecurityContext {
    isAuthenticated: () => boolean
    loggedInUser: Account | undefined
    login: () => void
    logout: () => void
}

export default createContext<ISecurityContext>({
    isAuthenticated: () => false,
    loggedInUser: undefined,
    login: () => {},
    logout: () => {},
})
