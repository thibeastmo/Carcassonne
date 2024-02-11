import {Box, Typography} from "@mui/material";
import TurnedInIcon from '@mui/icons-material/TurnedIn';

export interface ResultRankIconProps {
    rank: number
}
export function ResultRankIcon({rank}: ResultRankIconProps) {
    const iconWidth = 60;
    return (
        <Box style={{
            width: iconWidth + 'px',
            position: 'absolute',
            top: (iconWidth/2.5)+'px',
            left: 0,
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
        }}>
            <TurnedInIcon style={{
                width: '100%',
                height: 'auto',
                position: 'absolute',
                color: rank === 1 ? 'gold' : rank === 2 ? 'silver' : rank === 3 ? '#CD7F32' : 'brown',
            }} />
            <Typography variant="h4" style={{
                color: 'white',
                textAlign: 'center',
                position: 'absolute',
            }} >{rank}</Typography>
        </Box>
    );
}