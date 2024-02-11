import {Box} from "@mui/material";
import {PlayerInGame} from "./PlayerInGame.tsx";
import {PlayerInGameModel} from "../../../model/game/PlayerInGameModel.ts";
import {PlayersInGameProps} from "../../../model/game/PlayersInGameProps.ts";

export function PlayersInGame({ players }: PlayersInGameProps) {
    return (
        <div>
            <Box style={{
                position: 'fixed',
                bottom: '8px',
                left: '16px',
                display: 'flex',
                height: '150px'
            }}>
                {players.map((player: PlayerInGameModel, index: number) => (
                    <PlayerInGame key={index} playerInGame={player}/>
                ))}
            </Box>
        </div>
    );
}