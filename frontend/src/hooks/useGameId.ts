import {useNavigate, useParams} from "react-router-dom";
import {useEffect} from "react";

export function useGameId() {
    const navigate = useNavigate();
    let { id } = useParams();
    useEffect(() => {
        if (id === undefined) {
            navigate('/'); // Change the path to home page route
        }
    }, [id, navigate]);
    if (id === undefined) id = 'UNKNOWN';
    return id;
}