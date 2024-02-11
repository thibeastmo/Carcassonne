import {Card, CardMedia} from "@mui/material";
import {TileModel} from "../model/game/overlay/TileModel.ts";

export interface TileProps {
    orientation: number;
    currentTile: TileModel | undefined;
}

export default function TileComponent({orientation, currentTile}: TileProps) {
    const rotationAngle = orientation * 90;
    if (currentTile == undefined){
        return (
            <Card sx={{width: 180, height: 180}}>
                <CardMedia
                    sx={{width: 180, height: 180, transform: `rotate(${rotationAngle}deg)`}}
                    image={import.meta.env['VITE_BACKEND_URL'] + "/" + "images/tiles/default/back-tile.png"}
                    title="Back of tile"
                />
            </Card>
        );
    }
    let tileImage = currentTile.tileImage;
    return (
        <Card sx={{width: 180, height: 180}}>
            <CardMedia
                sx={{width: 180, height: 180, transform: `rotate(${rotationAngle}deg)`}}
                image={import.meta.env['VITE_BACKEND_URL'] + "/" + tileImage}
                title={currentTile.tileName}
            />
        </Card>
    );
}