import {Box, Dialog, DialogContent, IconButton} from "@mui/material";
import {useContext, useEffect, useState} from "react";
import {TileZoneComponent} from "./TileZoneComponent.tsx";
import {useSerf} from "../../../hooks/useSerf.ts";
import {useLegalTilezones} from "../../../hooks/useLegalTilezones.ts";
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import CancelIcon from '@mui/icons-material/Cancel';
import {TileModel} from "../../../model/game/overlay/TileModel.ts";
import {TileZoneModel} from "../../../model/game/overlay/TileZoneModel.ts";
import ApplicationThemeContext from "../../../context/ApplicationThemeContext.ts";
import {ApplicationTheme} from "../../../model/ApplicationTheme.ts";
import {calculateTimeRemainingInMs} from "../../../services/GameDataService.tsx";

export interface SerfPlacingTileProps {
    isOpen: boolean;
    onClose: () => void;
    currentTile: TileModel;
    startTurnTime: number;
    maxTurnDurationInMilliseconds: number;
}

export function SerfPlacingTileComponent({isOpen, onClose, currentTile, startTurnTime, maxTurnDurationInMilliseconds}: SerfPlacingTileProps) {
    const {isError, isLoading, tilezoneIds} = useLegalTilezones();

    let imageName = currentTile.tileImage;

    let availableZones = tilezoneIds?.availableZones;
    if (isLoading || isError) {
        availableZones = null;
    }

    const {applicationTheme} = useContext(ApplicationThemeContext);
    imageName = applicationTheme === ApplicationTheme.WINTER ?
        imageName.replace('default', 'winter') :
        imageName;

    // Assuming 'imageName' is the relative path of the image
    const imageUrl = import.meta.env['VITE_BACKEND_URL'] + '/' + imageName;
    const rotationAngle = currentTile?.orientation * 90;

    const [backgroundHeight, setBackgroundHeight] = useState(0);
    const [placedTileZoneId, setPlacedTileZoneId] = useState(-1);
    const serfInfo = useSerf(placedTileZoneId);
    useEffect(() => {
        //scale tile placed image size
        setBackgroundHeight(window.innerWidth * 0.3);
    }, [imageUrl]);

    if (calculateTimeRemainingInMs(startTurnTime, maxTurnDurationInMilliseconds) < 0) return <></>
    return (
        <Dialog open={currentTile?.placed && isOpen} onClose={onClose} scroll="body" maxWidth="lg">
            <DialogContent
                style={{
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center',
                    background: 'linear-gradient(to bottom, #89cf86, #a0d593, #b7dd9f, #cef1ab, #e3fbb7)'
                }}
            >
                <Box style={{
                    transform: `rotate(${rotationAngle}deg)`,
                    display: 'grid',
                    gridTemplateColumns: `repeat(3, ${Math.floor((1 / 3) * 100)}%)`, // Equal width for each column
                    gridTemplateRows: `repeat(3, ${Math.floor((1 / 3) * 100)}%)`, // Equal height for each row
                    gap: '0px',
                    border: 'none',
                    backgroundImage: `url(${imageUrl})`, // Use the url() function
                    backgroundSize: 'cover', // Make the background cover the entire container
                    backgroundPosition: 'center', // Center the background image
                    height: `${backgroundHeight}px`,
                    width: `${backgroundHeight}px`,
                }}>
                    {currentTile?.tileZones.map((_tileZone: TileZoneModel, index: number) => (
                        <TileZoneComponent key={index} availableTileZones={availableZones} tileZoneId={index}
                                           placeSerfOnTileZone={setPlacedTileZoneId} placedTileZoneId={placedTileZoneId}
                                           orientation={currentTile.orientation}/>
                    ))}
                </Box>
                <IconButton onClick={() => {
                    if (placedTileZoneId >= 0) {
                        serfInfo.placeSerfOnTile();
                    }
                    onClose();
                }
                }
                >
                    <CheckCircleIcon fontSize={'large'} color={'success'}/>
                </IconButton>
                <IconButton onClick={() => {
                    onClose();
                }
                }
                >
                    <CancelIcon fontSize={'large'} color={'error'}/>
                </IconButton>
            </DialogContent>
        </Dialog>
    );
}