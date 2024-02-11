import {useQuery} from "@tanstack/react-query";
import {getHelloWorld} from "../services/HelloWorldDataService.tsx";

export function useHelloWorld() {
    const {isLoading, isError, data: helloWorld} = useQuery(["hello-world"], getHelloWorld);

    return {
        isLoading,
        isError,
        helloWorld
    }
}