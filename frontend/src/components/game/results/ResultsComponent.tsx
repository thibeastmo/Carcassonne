import {Box, Fab, Tooltip, Typography} from "@mui/material";
import {useTranslation} from "react-i18next";
import {PlayerResultComponent} from "./PlayerResultComponent.tsx";
import {GameResult} from "../../../model/game/results/GameResult.ts";
import {ReplayButtonComponent} from "../../statistics/replay/ReplayButtonComponent.tsx";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";

export interface ResultsComponentProps {
    gameResults: GameResult[],
    gameId: string
}

export function ResultsComponent({gameResults, gameId}: ResultsComponentProps) {
    const {t} = useTranslation();
    const navigate = useNavigate();

    useEffect(() => {
        // Add a class to the body when the component mounts
        document.body.classList.add('disable-overflow');

        // Remove the class when the component unmounts
        return () => {
            document.body.classList.remove('disable-overflow');
        };
    }, []);

    const sortedResults = [...gameResults].sort((a, b) => b.points - a.points);
    return (
        <Box style={{
            position: 'fixed',
            backgroundColor: '#61616150',
            width: '100vw',
            height: '100vh',
            backdropFilter: 'blur(5px)',
            top: 0,
            bottom: 0,
            overflow: 'none',
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
        }}>
            <Box style={{
                width: '100%',
                height: '100%',
                display: 'grid',
                gridTemplateColumns: '1fr',
            }}>
                <Box>
                    <Typography variant="h2">{t('in_game.results.title')}</Typography>
                    <Typography variant="h4">{t('in_game.results.subtitle')}</Typography>
                </Box>
                <Box style={{
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center',
                }}>
                    <Box style={{
                        flexDirection: 'row', // Arrange items horizontally
                        overflowX: 'auto', // Enable horizontal scrolling if the content overflows
                        display: 'flex',
                        alignItems: 'center',
                    }}>
                        {sortedResults.map((gameResult: GameResult, index: number) => (
                            <PlayerResultComponent key={'playerResult' + index} points={gameResult.points}
                                                   rank={index + 1} color={gameResult.color}
                                                   avatarUrl={gameResult.avatarUrl}
                                                   nickname={gameResult.nickname}
                            />
                        ))}
                    </Box>
                </Box>
                <Box style={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center'
                }}>
                    <Box style={{
                        display: 'grid',
                        gridGap: '16px',
                        gridTemplateColumns: '1fr 1fr',
                        width: '40%',
                    }}>
                        <ReplayButtonComponent gameId={gameId}/>
                        <Tooltip title={t('in_game.results.home_tooltip')} arrow>
                            <Fab variant="extended"
                                 onClick={() => navigate('/')}
                            >
                                <Typography textAlign="center">{t('in_game.results.home')}</Typography>
                            </Fab>
                        </Tooltip>
                    </Box>
                </Box>
            </Box>
        </Box>
    );
}