import {Box} from "@mui/material";
import SvgSerf from "../serf/SvgSerf.tsx";

export interface TileZoneComponentProps {
    serfColor: string | undefined,
    rotationAngle: number,
}
export function TileZoneComponent({serfColor, rotationAngle}: TileZoneComponentProps) {
    if (serfColor === undefined) {
        return (
            <Box></Box>
        );
    }
    return (
        <Box style={{
            border: 'none',
            textAlign: 'center',
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            transform: `rotate(${-rotationAngle}deg)`
        }}>
            <SvgSerf color={serfColor} viewBox={'-60 -70 400 400'}/>
        </Box>
    );
}