import {UserStatistics} from "./UserStatistics.ts";

export class CalculatedUserStatistics implements UserStatistics {
    gamesWon: number;
    gamesPlayed: number;
    tilesPlaced: number;
    serfsPlaced: number;
    contestedLandWon: number;
    contestedLandLost: number;
    totalScoreAchieved: number;
    gamesLost: number;
    winRate: number;
    contestedLandWinRate: number;
    avgSerfsPlacedPerGame: number;
    avgTilesPlacedPerGame: number;
    avgSerfsPlacedPerTile: number; //to see how often the player places a serf when the player places a tile
    avgScorePerGame: number;
    avgTilesPlacedPerContestedLandWon: number; //calculates the "tile value" for the player. It calculates how often the player wins a land per tile placed. This is handy for those who want to know if the player relies on small feats or big feats.
    avgSerfsRequiredPerContestedLandWon: number; //calculates how many serfs are used to win land (= gain points)
    avgContestedLandsWonPerWin: number;

    constructor(userStats: UserStatistics | undefined) {
        if (userStats === undefined) {
            this.gamesWon = 0;
            this.gamesPlayed = 0;
            this.tilesPlaced = 0;
            this.serfsPlaced = 0;
            this.contestedLandWon = 0;
            this.contestedLandLost = 0;
            this.totalScoreAchieved = 0;
            this.gamesLost = 0;
            this.winRate = 0;
            this.contestedLandWinRate = 0;
            this.avgSerfsPlacedPerGame = 0;
            this.avgTilesPlacedPerGame = 0;
            this.avgSerfsPlacedPerTile = 0;
            this.avgScorePerGame = 0;
            this.avgTilesPlacedPerContestedLandWon = 0;
            this.avgSerfsRequiredPerContestedLandWon = 0;
            this.avgContestedLandsWonPerWin = 0;
            return;
        }
        this.gamesWon = (userStats.gamesWon);
        this.gamesPlayed = (userStats.gamesPlayed);
        this.tilesPlaced = (userStats.tilesPlaced);
        this.serfsPlaced = (userStats.serfsPlaced);
        this.contestedLandWon = (userStats.contestedLandWon);
        this.contestedLandLost = (userStats.contestedLandLost);
        this.totalScoreAchieved = (userStats.totalScoreAchieved);
        this.gamesLost = (userStats.gamesPlayed - userStats.gamesWon);
        this.winRate = ((userStats.gamesWon) / userStats.gamesPlayed * 100);
        this.contestedLandWinRate = ((userStats.contestedLandWon / (userStats.contestedLandWon + userStats.contestedLandLost)) * 100);
        this.avgSerfsPlacedPerGame = (userStats.serfsPlaced / userStats.gamesPlayed);
        this.avgTilesPlacedPerGame = (userStats.tilesPlaced / userStats.gamesPlayed);
        this.avgSerfsPlacedPerTile = (userStats.serfsPlaced / userStats.tilesPlaced);
        this.avgScorePerGame = (userStats.totalScoreAchieved / userStats.gamesPlayed);
        this.avgTilesPlacedPerContestedLandWon = (userStats.tilesPlaced / userStats.contestedLandWon);
        this.avgSerfsRequiredPerContestedLandWon = (userStats.serfsPlaced / userStats.contestedLandWon);
        this.avgContestedLandsWonPerWin = (userStats.contestedLandWon / userStats.gamesPlayed);
    }
}