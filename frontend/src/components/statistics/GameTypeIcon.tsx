import DoubleArrowIcon from '@mui/icons-material/DoubleArrow';
import KeyboardArrowRightIcon from '@mui/icons-material/KeyboardArrowRight';
import {Box} from "@mui/material";
import {GameTypes} from "../../model/GameType.ts";

interface GameTypeIconProps {
        gameType: GameTypes
}
export function GameTypeIcon({gameType}: GameTypeIconProps) {
    let iconElement;
    let backgroundColor = 'black';
    switch (gameType) {
        case GameTypes.FAST:
            iconElement = <KeyboardArrowRightIcon style={{
                width: '100%',
                height: '100%',
            }} />;
            backgroundColor = 'rgb(100, 242, 55)';
            break;
        case GameTypes.SHORT:
            iconElement = <DoubleArrowIcon style={{
                width: '100%',
                height: '100%',
            }}/>;
            backgroundColor = 'orange';
            break;
        default:
            iconElement = <KeyboardArrowRightIcon style={{
                width: '100%',
                height: '100%',
            }} />;
            break;
    }
    const iconWidth = 30;
    return (
        <Box style={{
            border: '2px transparant solid',
            borderRadius: '50%',
            width: iconWidth + 'px',
            height: iconWidth + 'px',
            position: 'absolute',
            transform: 'translate(10%)',
            bottom: 'calc('+(-iconWidth/5)+'px)',
            backgroundColor: backgroundColor,
            color: 'white'
        }}>
            {iconElement}
        </Box>
    );
}