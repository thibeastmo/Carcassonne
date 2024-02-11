import {Box, Typography} from "@mui/material";
import {ResultRankIcon} from "./ResultRankIcon.tsx";

export interface PlayerResultComponentProps {
    avatarUrl: string | undefined,
    points: number,
    color: string | undefined,
    rank: number,
    nickname: string | null
}
export function PlayerResultComponent({avatarUrl, points, color, rank, nickname}: PlayerResultComponentProps) {
    const backgroundImageUrl = 'url('+import.meta.env['VITE_BACKEND_URL'] + '/images/icons/ribbon_'+color+'.png)';
    if (avatarUrl === undefined) {
        avatarUrl = import.meta.env['VITE_BACKEND_URL'] + '/images/avatars/1.png';
    }
    if (nickname === null) {
        nickname = 'No nickname';
    }
    const size = rank === 1 ? 350 : 250;
    return (
        <Box style={{
            width: size+'px',
            marginRight: '3%',
        }}>
            <Box style={{
                color: 'white',
                position: 'relative',
            }}>
                <img src={avatarUrl} alt="avatar of player" style={{
                    width: size/1.2+'px',
                }} />
                <ResultRankIcon rank={rank} />
            </Box>
            <Box style={{
                backgroundImage: backgroundImageUrl,
                backgroundRepeat: 'no-repeat',
                backgroundSize: 'cover',
                backgroundPosition: 'center',
                height: (size/3)+'px',
                width: '100%',
                transform: 'translate(0,-50%)',
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
            }}>
                <Box style={{
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center',
                    color: 'white',
                }}>
                    <Typography variant="h4" style={{
                        transform: 'translate(0,-10%)',
                    }}>{points}</Typography>
                </Box>
            </Box>
            <Typography variant="h5" style={{
                transform: 'translate(0,'+(-size/5)+'px)',
            }}>{nickname}</Typography>
        </Box>
    );
}