import {Card, Box, IconButton} from "@mui/material";
import {Dispatch, SetStateAction} from "react";
import TileComponent from "./TileComponent.tsx";
import RotateLeftIcon from "@mui/icons-material/RotateLeft";
import RotateRightIcon from "@mui/icons-material/RotateRight";
import Typography from "@mui/material/Typography";
import {TileModel} from "../model/game/overlay/TileModel.ts";
import {calculateTimeRemainingInMs} from "../services/GameDataService.tsx";

export interface TileControlsProps {
    orientation: number;
    setOrientation: Dispatch<SetStateAction<number>>;
    currentTile: TileModel;
    maxTurnDurationInMilliseconds: number;
    startTurnTime: number;
    totalTilesInGame: number;
    tilesPlacedInGame: number;
}

export default function TileControlsComponent({
                                                  orientation,
                                                  setOrientation,
                                                  currentTile,
                                                  maxTurnDurationInMilliseconds,
                                                  startTurnTime,
                                                  totalTilesInGame,
                                                  tilesPlacedInGame
                                              }: TileControlsProps) {
    const handleRotateLeft = () => {
        setOrientation((prevOrientation) => (prevOrientation + 3) % 4);
    };

    const handleRotateRight = () => {
        setOrientation((prevOrientation) => (prevOrientation + 1) % 4);
    };

    const turnTimeLeftInMs = calculateTimeRemainingInMs(startTurnTime, maxTurnDurationInMilliseconds);
    return (
        <Card sx={{width: 250, height: 250, boxShadow: '0px 8px 16px rgba(0, 0, 0, .3)'}}
              style={{background: 'linear-gradient(to bottom, #54582f, #86895d)'}}>
            <Box
                display="flex"
                flexDirection="column"
                justifyContent="center"
                alignItems="center"
                height="100%"
            >
                <TileComponent orientation={orientation} currentTile={currentTile}/>
                <Box
                    display="flex"
                    justifyContent={turnTimeLeftInMs > 0 ? 'space-between' : 'center'}
                    marginTop={2}
                    width="100%"
                >
                    {turnTimeLeftInMs > 0 ?
                        <IconButton onClick={handleRotateLeft} style={{color: 'white'}}>
                                <RotateLeftIcon fontSize={'large'}/>
                        </IconButton>
                        : <></>}
                    <Typography variant={'h6'} color={'white'}>
                        {tilesPlacedInGame}/{totalTilesInGame}
                    </Typography>
                    {turnTimeLeftInMs > 0 ?
                        <IconButton onClick={handleRotateRight}>
                            <RotateRightIcon fontSize={'large'} style={{color: 'white'}}/>
                        </IconButton>
                        : <></>}
                </Box>
            </Box>
        </Card>
    );
}
