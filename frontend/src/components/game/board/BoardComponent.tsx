import {Box} from "@mui/material";
import {TileComponent} from "./TileComponent.tsx";
import {TileModel} from "../../../model/game/board/TileModel.ts";
import {useEffect, useState} from "react";
import {TilePlaceholderComponent} from "./TilePlaceholderComponent.tsx";
import {AxiosResponse} from "axios";
import {TilePlacement} from "../../../model/TilePlacement.ts";
import {UseMutateFunction} from "@tanstack/react-query";
import {PlayerDataModel} from "../../../model/game/PlayerDataModel.ts";
import {UsedPlayerSerfs} from "../../../model/game/UsedPlayerSerfs.ts";
import {PlayerInGameModel} from "../../../model/game/PlayerInGameModel.ts";
import {UsedSerfs} from "../../../model/game/overlay/UsedSerfs.ts";

export const tileSize = 180;

export interface BoardProps {
    playerDataModel: PlayerDataModel;
    orientation: number;
    hasPlacedTile: boolean;
    placedTiles: TileModel[];
    legalTilePlacements: TileModel[];
    placeTileOnBoard: UseMutateFunction<AxiosResponse<any, any>, unknown, TilePlacement>;
}

function initializeUsedPlayerSerfs(playerDataModel: PlayerDataModel) {
    if (playerDataModel === undefined) return;
    if (playerDataModel.playerData === undefined) return;
    return playerDataModel.playerData.map((player: PlayerInGameModel) => {
        // Extracting relevant properties from the PlayerInGameModel
        const {serfsUsed} = player;

        // Filtering out undefined serfsUsed arrays
        const validSerfsUsed = serfsUsed ? serfsUsed.filter(Boolean) : [];

        // Mapping over the validSerfsUsed array and transforming it into UsedSerfs array
        const usedSerfs: UsedSerfs[] = validSerfsUsed.map((serf: UsedSerfs) => ({
            zoneId: serf.zoneId,
            x: serf.x,
            y: serf.y
        }));

        // Creating UsedPlayerSerfs object
        const usedPlayerSerfs: UsedPlayerSerfs = {
            playerId: player.playerNumber,
            serfColor: player.color,
            usedSerfs: usedSerfs,
        };

        return usedPlayerSerfs;
    });
}

export function BoardComponent({
                                   playerDataModel,
                                   orientation,
                                   hasPlacedTile,
                                   placedTiles,
                                   legalTilePlacements,
                                   placeTileOnBoard
                               }: BoardProps) {
    const storedZoomLevel = localStorage.getItem('zoomLevel');
    const initialZoomLevel = storedZoomLevel ? parseFloat(storedZoomLevel) : 1;
    const [zoomLevel, setZoomLevel] = useState<number>(initialZoomLevel);
    const boundingBox = getBoundingBoxCoordinates(placedTiles);
    const dimensions = calculateDimensions(boundingBox);
    const tiles = setLocations(placedTiles, dimensions.center);
    const storedPanPosition = localStorage.getItem('panPosition');
    const initialPanPosition = storedPanPosition ? JSON.parse(storedPanPosition) : {x: 0, y: 0};
    const [dragStart, setDragStart] = useState<{ x: number; y: number } | null>(null);
    const [panPosition, setPanPosition] = useState<{ x: number; y: number }>(initialPanPosition);

    useEffect(() => {
        localStorage.setItem('panPosition', JSON.stringify(panPosition));
    }, [panPosition]);
    useEffect(() => {
        localStorage.setItem('zoomLevel', zoomLevel.toString());
    }, [zoomLevel]);
    const handleMouseDown = (event: { clientX: any; clientY: any; }) => {
        setDragStart({x: event.clientX, y: event.clientY});
    };
    const handleMouseMove = (event: { clientX: number; clientY: number; }) => {
        if (dragStart) {
            const deltaX = event.clientX - dragStart.x;
            const deltaY = event.clientY - dragStart.y;

            setPanPosition((prevPanPosition) => ({
                x: clamp(prevPanPosition.x + deltaX, -(dimensions.width), dimensions.width),
                y: clamp(prevPanPosition.y + deltaY, -(dimensions.height), dimensions.height),
            }));

            setDragStart({x: event.clientX, y: event.clientY});
        }
    };
    const handleMouseUp = () => {
        setDragStart(null);
    };
    const handleWheel = (event: {
        deltaY: number;
        currentTarget: { getBoundingClientRect: () => any; };
        clientX: number;
        clientY: number;
    }) => {
        const newZoomLevel = clamp(zoomLevel - event.deltaY * 0.001, 0.5, 1);
        setZoomLevel(newZoomLevel);

        const boundingRect = event.currentTarget.getBoundingClientRect();
        const mouseX = event.clientX - boundingRect.left;
        const mouseY = event.clientY - boundingRect.top;

        const newPanX = panPosition.x - (mouseX * (newZoomLevel - zoomLevel));
        const newPanY = panPosition.y - (mouseY * (newZoomLevel - zoomLevel));

        setPanPosition({x: newPanX, y: newPanY});
    };
    const clamp = (value: number, min: number, max: number) => {
        return Math.min(Math.max(value, min), max);
    };
    const usedPlayerSerfs = initializeUsedPlayerSerfs(playerDataModel);
    const legalPlacementsWithCurrentOrientation = legalTilePlacements.filter((placement: TileModel) => placement.orientation === orientation)
    let tilesPlaceholders = null;
    if (legalPlacementsWithCurrentOrientation !== undefined && legalPlacementsWithCurrentOrientation.length > 0) {
        tilesPlaceholders = setLocations(legalPlacementsWithCurrentOrientation, dimensions.center);
    }

    return (
        <div
            className="game-board-container"
            onMouseDown={handleMouseDown}
            onMouseMove={handleMouseMove}
            onMouseUp={handleMouseUp}
            onWheel={handleWheel}
            style={{
                position: 'relative',
                height: '90vh',
                width: '100vw',
                overflow: 'hidden'
            }}
        >
            <Box
                style={{
                    transform: `translate(${panPosition.x}px, ${panPosition.y}px) scale(${zoomLevel})`,
                    transformOrigin: 'top left',
                }}
            >
                {tiles.map((tile: TileModel, index: number) => (
                    <TileComponent key={index} tileModel={tile} usedPlayerSerfs={usedPlayerSerfs}/>
                ))}
                {!hasPlacedTile && tilesPlaceholders !== null && tilesPlaceholders.length > 0 &&
                    tilesPlaceholders.map((tilePlaceholder: TileModel, index: number) => (
                        <TilePlaceholderComponent key={index} tileModel={tilePlaceholder}
                                                  placeTileOnBoard={placeTileOnBoard}/>
                    ))
                }
            </Box></div>
    );
}

function getBoundingBoxCoordinates(tiles: TileModel[]): {
    minX: number;
    minY: number;
    maxX: number;
    maxY: number;
} {
    if (tiles.length === 0) {
        throw new Error('Array is empty. Cannot calculate bounding box coordinates.');
    }

    let minX = tiles[0].coordinates.x;
    let minY = tiles[0].coordinates.y;
    let maxX = tiles[0].coordinates.x;
    let maxY = tiles[0].coordinates.y;

    tiles.forEach(tile => {
        minX = Math.min(minX, tile.coordinates.x);
        minY = Math.min(minY, tile.coordinates.y);
        maxX = Math.max(maxX, tile.coordinates.x);
        maxY = Math.max(maxY, tile.coordinates.y);
    });

    return {minX, minY, maxX, maxY};
}

function calculateDimensions(boundingBox: BoundingBox): {
    width: number;
    height: number,
    center: {
        x: number,
        y: number
    }
} {
    const extraMargin = Math.ceil(tileSize * 1.5); //margin zodat de uiterste tiles niet tegen de rand van de window liggen (dus het mogelij maakt om ernaast nog een kaart te leggen)
    const widthRightSide = boundingBox.maxX * tileSize + extraMargin;
    const widthLeftSide = Math.max(Math.abs(boundingBox.minY * tileSize), Math.ceil(window.innerWidth / 2)) + extraMargin;
    const totalWidth = widthRightSide + widthLeftSide + tileSize;

    const heightBottomSide = boundingBox.maxY * tileSize + extraMargin;
    const heightTopSide = Math.max(Math.abs(boundingBox.minY * tileSize), Math.ceil(window.innerHeight / 2)) + extraMargin;
    const totalHeight = heightBottomSide + heightTopSide + tileSize;

    return {width: totalWidth, height: totalHeight, center: {x: Math.abs(widthLeftSide), y: Math.abs(heightTopSide)}};
}

function setLocations(tiles: TileModel[], center: {
    x: number,
    y: number
}) {
    tiles.forEach(tile => {
        tile.location.top = center.y - tileSize * tile.coordinates.y;
        tile.location.left = center.x + tileSize * tile.coordinates.x;
    });

    return tiles;
}

interface BoundingBox {
    minX: number;
    minY: number;
    maxX: number;
    maxY: number;
}