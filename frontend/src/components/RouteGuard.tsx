import {ReactElement, useContext} from "react";
import SecurityContext from "../context/securityContext/SecurityContext.ts";
import {LoginRequestComponent} from "./LoginRequestComponent.tsx";

export interface RouteGuardProps {
    component: ReactElement
}

const RouteGuard = ({component}: RouteGuardProps) => {
    const {isAuthenticated} = useContext(SecurityContext)
    if (isAuthenticated()) {
        return component
    } else {
        return <LoginRequestComponent/>
    }
}

export default RouteGuard