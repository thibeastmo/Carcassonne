import {useUserStatistics} from "../../hooks/statistics/useUserStatistics.ts";
import {Box} from "@mui/material";
import {UserStatisticItemComponent} from "./UserStatisticItemComponent.tsx";
import {useTranslation} from "react-i18next";
import {CalculatedUserStatistics} from "../../model/statistics/CalculatedUserStatistics.ts";

export function UserStatisticsComponent() {
    const {t} = useTranslation();
    const {isLoading, isError, userStatistics} = useUserStatistics();
    let calculatedUserStatistics = new CalculatedUserStatistics(undefined);
    if (!isLoading && !isError) {
        calculatedUserStatistics = new CalculatedUserStatistics(userStatistics);
    }
    return (
        <Box id='userStatisticsGrid' style={{
            display: 'grid',
            gridTemplateColumns: '1fr 1fr 1fr 1fr 1fr 1fr',
            gridGap: '8px'
        }}>
            <Box>
                <UserStatisticItemComponent value={calculatedUserStatistics?.gamesWon} description={t('statistics.user_statistics.games_won')} />
            </Box>
            <Box>
                <UserStatisticItemComponent value={calculatedUserStatistics?.gamesPlayed} description={t('statistics.user_statistics.games_played')} />
            </Box>
            <Box>
                <UserStatisticItemComponent value={calculatedUserStatistics?.tilesPlaced} description={t('statistics.user_statistics.tiles_placed')} />
            </Box>
            <Box>
                <UserStatisticItemComponent value={calculatedUserStatistics?.serfsPlaced} description={t('statistics.user_statistics.serfs_placed')} />
            </Box>
            <Box>
                <UserStatisticItemComponent value={calculatedUserStatistics?.contestedLandWon} description={t('statistics.user_statistics.contested_land_won')} />
            </Box>
            <Box>
                <UserStatisticItemComponent value={calculatedUserStatistics?.contestedLandLost} description={t('statistics.user_statistics.contested_land_lost')} />
            </Box>
            <Box>
                <UserStatisticItemComponent value={calculatedUserStatistics?.totalScoreAchieved} description={t('statistics.user_statistics.total_score_achieved')} />
            </Box>
            <Box>
                <UserStatisticItemComponent value={calculatedUserStatistics?.gamesLost} description={t('statistics.user_statistics.games_lost')} />
            </Box>
            <Box>
                <UserStatisticItemComponent value={calculatedUserStatistics?.winRate} description={t('statistics.user_statistics.win_rate')} percentage={true} />
            </Box>
            <Box>
                <UserStatisticItemComponent value={calculatedUserStatistics?.contestedLandWinRate} description={t('statistics.user_statistics.contested_land_win_rate')} percentage={true} />
            </Box>
            <Box>
                <UserStatisticItemComponent value={calculatedUserStatistics?.avgSerfsPlacedPerGame} description={t('statistics.user_statistics.avg_serfs_placed_per_game')} />
            </Box>
            <Box>
                <UserStatisticItemComponent value={calculatedUserStatistics?.avgTilesPlacedPerGame} description={t('statistics.user_statistics.avg_tiles_placed_per_game')} />
            </Box>
            <Box>
                <UserStatisticItemComponent value={calculatedUserStatistics?.avgSerfsPlacedPerTile} description={t('statistics.user_statistics.avg_serfs_placed_per_tile')} />
            </Box>
            <Box>
                <UserStatisticItemComponent value={calculatedUserStatistics?.avgScorePerGame} description={t('statistics.user_statistics.avg_score_per_game')} />
            </Box>
            <Box>
                <UserStatisticItemComponent value={calculatedUserStatistics?.avgTilesPlacedPerContestedLandWon} description={t('statistics.user_statistics.avg_tiles_placed_per_contested_land_won')} />
            </Box>
            <Box>
                <UserStatisticItemComponent value={calculatedUserStatistics?.avgSerfsRequiredPerContestedLandWon} description={t('statistics.user_statistics.avg_serfs_required_per_contested_land_won')} />
            </Box>
            <Box>
                <UserStatisticItemComponent value={calculatedUserStatistics?.avgContestedLandsWonPerWin} description={t('statistics.user_statistics.avg_contested_lands_won_per_win')} />
            </Box>
        </Box>
    );
}