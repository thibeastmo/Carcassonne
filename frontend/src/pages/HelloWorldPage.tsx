import {HelloWorldComponent} from "../components/HelloWorldComponent.tsx";
import {Link} from "react-router-dom";

export function HelloWorldPage() {
    const id = '55cc808a-0f9f-4a28-9b16-d4f18dc647bb';
    return <div>
        <Link to={`/game/${id}`}>Game with id</Link>
        <HelloWorldComponent/>
    </div>;
}