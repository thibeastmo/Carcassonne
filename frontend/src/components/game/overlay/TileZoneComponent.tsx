import {TileZoneComponentProps} from "../../../model/game/overlay/TileZoneComponentProps.ts";
import {Box} from "@mui/material";
import {useState} from "react";

export function TileZoneComponent({
                                      availableTileZones,
                                      tileZoneId,
                                      placeSerfOnTileZone,
                                      placedTileZoneId,
                                      orientation
                                  }: TileZoneComponentProps) {
    const [isHovered, setIsHovered] = useState(false);
    const isPossible: boolean = checkIfTileZoneForSerfIsPossible(tileZoneId, availableTileZones);
    const imgSrc = import.meta.env['VITE_BACKEND_URL'] + '/images/icons/' + (isPossible ? 'serf_grey' : 'cross') + '.png';
    const counterRotation = (4 - orientation) % 4 * 90;
    return (
        <Box style={{
            border: 'none',
            textAlign: 'center',
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
        }}>
            <img src={imgSrc}
                 alt={isPossible ? 'serf icon, placement is possible' : 'cross icon, placement not possible'} style={{
                height: '80%',
                width: 'auto',
                opacity: isHovered || placedTileZoneId === tileZoneId ? 1 : 0,
                transform: `rotate(${counterRotation}deg)`
            }}
                 onMouseEnter={() => setIsHovered(true)}
                 onMouseLeave={() => setIsHovered(false)}
                 onClick={() => placeSerfOnTileZone(tileZoneId)}
            />
        </Box>
    );
}

function checkIfTileZoneForSerfIsPossible(tileZoneId: number, availableTileZones: number[] | undefined): boolean {
    if (availableTileZones == undefined) return false;
    return availableTileZones.includes(tileZoneId);
}