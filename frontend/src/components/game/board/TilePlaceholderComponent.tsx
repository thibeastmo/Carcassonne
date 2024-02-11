import {tileSize} from "./BoardComponent.tsx";
import {useContext, useState} from "react";
import {TileModel} from "../../../model/game/board/TileModel.ts";
import {UseMutateFunction} from "@tanstack/react-query";
import {TilePlacement} from "../../../model/TilePlacement.ts";
import {AxiosResponse} from "axios";
import ApplicationThemeContext from "../../../context/ApplicationThemeContext.ts";
import {ApplicationTheme} from "../../../model/ApplicationTheme.ts";

export interface TilePlaceholderProps {
    tileModel: TileModel
    placeTileOnBoard :  UseMutateFunction<AxiosResponse<any, any>, unknown, TilePlacement, unknown>;
}
export function TilePlaceholderComponent({ tileModel, placeTileOnBoard }: TilePlaceholderProps) {
    const rotationAngle = tileModel.orientation * 90;
    const [isHovered, setIsHovered] = useState(false);
    const handleTileClick = () => {

        placeTileOnBoard({
            xValue: tileModel.coordinates.x,
            yValue: tileModel.coordinates.y,
            orientation: tileModel.orientation,
        });
    };

    let tileImage = tileModel.image_url;
    const {applicationTheme} = useContext(ApplicationThemeContext);
    tileImage = applicationTheme === ApplicationTheme.WINTER ?
        tileImage.replace('default', 'winter') :
        tileImage;

    return (
        <>
            <img
                src={import.meta.env['VITE_BACKEND_URL'] + '/' + tileImage}
                alt={`Coordinate of location: x=${tileModel.coordinates.x}, y=${tileModel.coordinates.y}`}
                style={{
                    position: 'absolute',
                    top: tileModel.location.top,
                    left: tileModel.location.left,
                    width: `${tileSize}px`,
                    height: 'auto',
                    transform: `rotate(${rotationAngle}deg)`,
                    opacity: isHovered ? 1 : 0.25,
                }}
                onMouseEnter={() => setIsHovered(true)}
                onMouseLeave={() => setIsHovered(false)}
                onClick={() => handleTileClick()}
            />
        </>
    );
}