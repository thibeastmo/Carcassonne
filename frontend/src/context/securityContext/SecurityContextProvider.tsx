import {ReactNode, useEffect, useState} from 'react'
import SecurityContext from './SecurityContext.ts'
import {addAccessTokenToAuthHeader, removeAccessTokenFromAuthHeader} from '../../services/AuthenticationService.tsx'
import {isExpired} from 'react-jwt'
import Keycloak from 'keycloak-js'
import {createBackendAccount} from "../../services/AccountDataService.tsx";
import {Account} from "../../model/Account.ts";
import {Avatar} from "../../model/Avatar.ts";

interface IWithChildren {
    children: ReactNode
}

const keycloakConfig = {
    url: import.meta.env.VITE_KC_URL,
    realm: import.meta.env.VITE_KC_REALM,
    clientId: import.meta.env.VITE_KC_CLIENT_ID,
}

//console.log("SecurityContextProvider entry")
const keycloak: Keycloak = new Keycloak(keycloakConfig)

export default function SecurityContextProvider({children}: IWithChildren) {
    const [loggedInUser, setLoggedInUser] = useState<Account | undefined>()
    useEffect(() => {
        //console.log("SecurityContextProvider: useEffect")
            keycloak.init({onLoad: 'check-sso'})

    }, []);

    keycloak.onAuthSuccess = () => {
        addAccessTokenToAuthHeader(keycloak.token);
        mapToAccount(createBackendAccount().then(value => {setLoggedInUser(value)}));
    }

    const mapToAccount = (data: any): Account => {
        return {
            subjectId: data.subjectId,
            nickname: data.nickname,
            avatar: data.avatar as Avatar
        };
    };

    keycloak.onAuthLogout = () => {
        removeAccessTokenFromAuthHeader()
    }

    keycloak.onAuthError = () => {
        removeAccessTokenFromAuthHeader()
    }

    keycloak.onTokenExpired = () => {
        console.log("Token expired!")
        keycloak.updateToken(-1).then(function () {
            addAccessTokenToAuthHeader(keycloak.token)
            console.log(loggedInUser);
            mapToAccount(createBackendAccount().then(value => {setLoggedInUser(value)}));
            //setLoggedInUser(keycloak.idTokenParsed?.name)
        })
    }

    function login() {
        keycloak.login()
    }

    function logout() {
        const logoutOptions = {redirectUri: import.meta.env.VITE_REACT_APP_URL}
        keycloak.logout(logoutOptions)
    }

    function isAuthenticated() {
        if (keycloak.token) return !isExpired(keycloak.token)
        else return false
    }

    return (
        <SecurityContext.Provider
            value={{
                isAuthenticated,
                loggedInUser,
                login,
                logout,
            }}>
            {children}
        </SecurityContext.Provider>
    )
}
