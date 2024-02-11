import {useTranslation} from "react-i18next";
import {Box, Typography} from "@mui/material";
import {useLeaderboard} from "../../../hooks/statistics/useLeaderboard.ts";
import {RankedAccount} from "../../../model/statistics/RankedAccount.ts";
import {LeaderboardListItemComponent} from "./LeaderboardListItemComponent.tsx";

export function LeaderboardOverview() {
    const { leaderboard, isError, isLoading } = useLeaderboard();
    const {t} = useTranslation();
    return (
        <Box>
            <Typography variant="h2">{t('leaderboard.title')}</Typography>
            <Box style={{
                marginTop: '16px'
            }}>
                <LeaderboardListItemComponent key={-1} rank={t('leaderboard.columns.rank')} nickname={t('leaderboard.columns.nickname')} experiencePoints={t('leaderboard.columns.experiencePoints')} />
                {
                    isError || isLoading ?
                        isError ? <Typography variant="h3">{t('loading.error')}</Typography> : <Typography variant="h3">{t('loading.loading')}</Typography>
                        :
                        leaderboard === undefined || leaderboard.ranks === undefined ?
                            <Typography variant="h3">{t('leaderboard.empty')}</Typography>
                            :
                        leaderboard.ranks.map((rankedAccount : RankedAccount) => (
                            <LeaderboardListItemComponent key={rankedAccount.rank} rank={rankedAccount.rank.toString()} experiencePoints={rankedAccount.experiencePoints.toString()} nickname={rankedAccount.nickname} />
                        ))}
            </Box>
        </Box>
    );
}