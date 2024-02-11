import {useHelloWorld} from "../hooks/useHelloWorld.tsx";

export function HelloWorldComponent() {
    const {isLoading, isError, helloWorld} = useHelloWorld();

    if (isLoading) {
        return <div>Loading...</div>;
    }

    if (isError) {
        return <div>Something went wrong</div>;
    }
    console.log(helloWorld.helloWorld)

    return (
        <div>{helloWorld.helloWorld}</div>
    )
}