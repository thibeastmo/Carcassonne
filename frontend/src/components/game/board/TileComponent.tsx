import {TileModelProps} from "../../../model/game/board/TileModelProps.ts";
import {tileSize} from "./BoardComponent.tsx";
import {Box} from "@mui/material";
import {TileZoneComponent} from "./TileZoneComponent.tsx";

export function TileComponent({tileModel, usedPlayerSerfs}: TileModelProps) {
    const rotationAngle = tileModel.orientation * 90;
    const tileZones = [...Array(9)];
    const imageUrl = import.meta.env['VITE_BACKEND_URL']+'/'+tileModel.image_url;

    return (
        <Box style={{
            backgroundImage: `url(${imageUrl})`,
            backgroundPosition: 'center',
            backgroundSize: 'cover',
            backgroundRepeat: 'no-repeat',
            position: 'absolute',
            top: tileModel.location.top,
            left: tileModel.location.left,
            width: `${tileSize}px`,
            height: `${tileSize}px`,
            transform: `rotate(${rotationAngle}deg)`,
            display: 'grid',
            gridTemplateColumns: `repeat(3, ${Math.floor((1 / 3) * 100)}%)`, // Equal width for each column
            gridTemplateRows: `repeat(3, ${Math.floor((1 / 3) * 100)}%)`, // Equal height for each row
        }}>
            {tileZones.map((_, index: number) => {
                // Find the matching UsedPlayerSerfs based on coordinates
                const matchingSerf = usedPlayerSerfs?.find((playerSerf) =>
                    playerSerf.usedSerfs.some(
                        (serf) => serf.x === tileModel.coordinates.x && serf.y === tileModel.coordinates.y && index === serf.zoneId
                    )
                );

                // Extract the serfName from the matchingSerf
                const serfColor = matchingSerf ? matchingSerf.serfColor : undefined;
                return <TileZoneComponent key={index} serfColor={serfColor} rotationAngle={rotationAngle} />;
            })}
        </Box>
    );
}