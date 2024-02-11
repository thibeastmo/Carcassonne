import {Box, Typography} from "@mui/material";

export interface LeaderboardListItemProps {
    rank: string;
    nickname: string;
    experiencePoints: string;
}

export function LeaderboardListItemComponent({rank, nickname, experiencePoints}: LeaderboardListItemProps) {
    return (
        <Box>
            <Box
                style={{
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center',
                }}>
                <Box
                    style={{
                        display: 'grid',
                        gridTemplateColumns: '1fr 2fr 1fr',
                        backgroundColor: rank === '1' ? 'gold' : rank === '2' ? 'silver' : rank === '3' ? '#CD7F32' : '#cccccc50',
                        width: '50vw',
                        borderRadius: '16px',
                        marginBottom: '8px',
                        color: 'black'
                    }}>
                    <Typography variant="h3">{rank}.</Typography>
                    <Box style={{
                        textAlign: 'center'
                    }}>
                        <Typography
                            variant="h4">{nickname !== null ? nickname : 'NO NICKNAME'}</Typography>
                    </Box>
                    <Typography variant="h3">{experiencePoints}</Typography>
                </Box>
            </Box>
        </Box>
    );
}