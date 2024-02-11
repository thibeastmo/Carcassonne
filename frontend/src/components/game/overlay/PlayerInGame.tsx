import {Avatar, Card, CardContent, CardHeader} from "@mui/material";
import {PlayerInGameProps} from "../../../model/game/overlay/PlayerInGameProps.ts";
import ManIcon from '@mui/icons-material/Man';
import EmojiEventsIcon from '@mui/icons-material/EmojiEvents';
import Typography from "@mui/material/Typography";
import "./PlayerInGame.css"

export function PlayerInGame({playerInGame}: PlayerInGameProps) {
    const maxSerfs = 7;

    const isTurn = playerInGame.isTurn != -1;
    return (
        <Card style={{
            width: '200px',
            margin: '10px',
            background: 'linear-gradient(45deg, #8B4513 0%, #b37d2d 50%, #8B4513 100%)',
            boxShadow: '0px 8px 16px rgba(0, 0, 0, .5)',
            ...(isTurn && {
                animation: 'pulse-border 2s infinite'
            }),
        }}>
            <CardHeader
                avatar={
                    <Avatar>
                        <img src={import.meta.env['VITE_BACKEND_URL'] + playerInGame.avatarUrl} alt={`Avatar of ${playerInGame.nickname}`}
                             style={{width: '100%', height: '100%', objectFit: 'cover'}}/>
                    </Avatar>
                }
                title={
                    <Typography variant="h6" style={{fontSize: '1.5rem', fontWeight: 'bold', color: '#fff'}}>
                        {truncateUsername(playerInGame.nickname, 11)}
                    </Typography>
                }
            />
            <CardContent>
                <div style={{display: 'flex', alignItems: 'center', justifyContent: 'space-between'}}>
                    <div>
                        <ManIcon fontSize={'large'} style={{color: playerInGame.color}}/>
                    </div>
                    <div>
                        <Typography variant="h6" style={{fontSize: '1.5rem', fontWeight: 'bold', color: '#fff'}}>
                            {playerInGame.serfsUsed === undefined ? maxSerfs : maxSerfs - playerInGame.serfsUsed?.length}
                        </Typography>
                    </div>
                    <div>
                        <EmojiEventsIcon fontSize={'large'}/>
                    </div>
                    <div>
                        <Typography variant="h6" style={{fontSize: '1.5rem', fontWeight: 'bold', color: '#fff'}}>
                            {playerInGame.score}
                        </Typography>
                    </div>
                </div>
            </CardContent>
        </Card>
    );
}

function truncateUsername(username: string, maxLength: number) {
    if (username.length >= maxLength) {
        return username.substring(0, maxLength - 3) + '...';
    }
    return username;
}