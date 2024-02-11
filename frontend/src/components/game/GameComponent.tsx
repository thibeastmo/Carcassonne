import {BoardComponent} from "./board/BoardComponent.tsx";
import {OverlayUIComponent} from "./overlay/OverlayUIComponent.tsx";
import {useNavigate, useParams} from "react-router-dom";
import {useContext, useEffect, useState} from "react";
import {usePlacedTiles} from "../../hooks/usePlacedTiles.ts";
import {useLegalTilePlacements} from "../../hooks/useLegalTilePlacements.ts";
import {usePlayerData} from "../../hooks/usePlayerData.ts";
import {PlayerDataModel} from "../../model/game/PlayerDataModel.ts";
import {useGameHistories} from "../../hooks/statistics/useGameHistories.ts";
import {ResultsComponent} from "./results/ResultsComponent.tsx";
import {ApplicationTheme} from "../../model/ApplicationTheme.ts";
import ApplicationThemeContext from "../../context/ApplicationThemeContext.ts";
import {PlayerInGameModel} from "../../model/game/PlayerInGameModel.ts";
import {Account} from "../../model/Account.ts";
import {calculateTimeRemainingInMs} from "../../services/GameDataService.tsx";
import {useMaxTurnDuration} from "../../hooks/useMaxTurnDuration.ts";
import SecurityContext from "../../context/securityContext/SecurityContext.ts";
import {ProblemComponent} from "../ProblemComponent.tsx";
import {TilePlacement} from "../../model/TilePlacement.ts";
import {TurnTimeResult} from "../../model/game/TurnTimeResult.ts";
import {t} from "i18next";

export function GameComponent() {
    const navigate = useNavigate();
    const { id } = useParams();
    const [orientation, setOrientation] = useState(0);
    const[hasPlacedTile, setHasPlacedTile] = useState(false);
    useEffect(() => {
        if (id === undefined) {
            navigate('/'); // Change the path to home page route
        }
    }, [id, navigate]);
    if (id === undefined) return;
    const playerDataModel: PlayerDataModel = usePlayerData();
    const placedTilesResponse = usePlacedTiles();
    const legalTilePlacementResponse = useLegalTilePlacements();
    const { gameResults } = useGameHistories();
    const {isError, isLoading, maxTurnDurationInMinutes} = useMaxTurnDuration();
    const {loggedInUser} = useContext(SecurityContext);
    const maxTurnDurationInMilliseconds = Number(maxTurnDurationInMinutes) * 60 * 1000;
    if (loggedInUser === undefined || isError) return (
        <ProblemComponent />
    );
    const handleTurnTime = () => {
        const result = turnTimeLeftInMsByNickname(playerDataModel.playerData, loggedInUser, maxTurnDurationInMilliseconds);

        if ('errorCode' in result) {
            return {startTurnTime: -1, timeDifference: -1, serfsAvailable: -1} as TurnTimeResult;
        } else {
            return result;
        }
    }
    let result = handleTurnTime();
    if (placedTilesResponse.isLoading || legalTilePlacementResponse.isLoading || placedTilesResponse.isPlacingTile || isLoading){
        return <div>{t('loading.loading')}</div>
    }
    if (placedTilesResponse.isError ||legalTilePlacementResponse.isError || placedTilesResponse.isErrorPlacingTile || placedTilesResponse.tileModels == undefined){
        return <div>{t('loading.error')}</div>
    }

    const { applicationTheme } = useContext(ApplicationThemeContext);
    if (applicationTheme == ApplicationTheme.WINTER) {
            for (let i = 0; i < placedTilesResponse.tileModels.tilePlacements.length; i++) {
                placedTilesResponse.tileModels.tilePlacements[i].image_url =  placedTilesResponse.tileModels.tilePlacements[i].image_url.replace('default', 'winter');
        }
    }

    const handleTimeReachedZero = () => {
        console.log('Time reached zero!');
        result = handleTurnTime();
    };
    const handlePlaceTileOnBoard = (tilePlacement: TilePlacement) => {
        setHasPlacedTile(true);
        placedTilesResponse.placeTileOnBoard(tilePlacement);
        handleTurnTime();
    };
    return (
        <main>
            <BoardComponent playerDataModel={playerDataModel} orientation={orientation} hasPlacedTile={hasPlacedTile}  placedTiles={placedTilesResponse.tileModels.tilePlacements} legalTilePlacements={result.timeDifference > 0 ? legalTilePlacementResponse.tileModels : []} placeTileOnBoard={handlePlaceTileOnBoard} />
            <OverlayUIComponent serfsAvailable={result.serfsAvailable} startTurnTime={result.startTurnTime} setHasPlacedTile={setHasPlacedTile} maxTurnDurationInMilliseconds={maxTurnDurationInMilliseconds} onTimeReachedZero={handleTimeReachedZero} playerDataModel={playerDataModel} orientation={orientation} setOrientation={setOrientation} tilesPlacedInGame={placedTilesResponse.tileModels.tilePlacements.length} totalTilesInGame={placedTilesResponse.tileModels.totalTilesInGame}/>
            {gameResults !== undefined && gameResults.length > 0 ? <ResultsComponent gameResults={gameResults} gameId={id === undefined ? '' : id} /> : <></>}
        </main>
    )
}

function turnTimeLeftInMsByNickname(
    playerData: PlayerInGameModel[] | undefined,
    loggedInUser: Account,
    maxTurnDurationInMilliseconds: number
): { startTurnTime: number; timeDifference: number; serfsAvailable: number } | { errorCode: number } {
    if (playerData === undefined) return { errorCode: -1 };

    const player = playerData.find((item) => loggedInUser.nickname === item.nickname);
    if (player === undefined) return { errorCode: -2 };

    let amountOfUsedSerfs = 0;
    if (player.serfsUsed !== undefined) amountOfUsedSerfs = player.serfsUsed.length;

    if (player.isTurn < 0) return { errorCode: -4 };

    const cappedDifferenceInMilliseconds = calculateTimeRemainingInMs(
        player.isTurn,
        maxTurnDurationInMilliseconds
    );

    return {
        startTurnTime: player.isTurn,
        timeDifference: cappedDifferenceInMilliseconds,
        serfsAvailable: Number(7 - amountOfUsedSerfs),
    };
}
