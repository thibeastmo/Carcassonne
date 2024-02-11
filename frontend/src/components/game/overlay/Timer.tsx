import {Typography} from "@mui/material";
import {useEffect, useState} from "react";
export interface TimerProps {
    initialSecondsForTurnLeft: number;
    onTimeReachedZero: () => void;
}
export function Timer({ initialSecondsForTurnLeft, onTimeReachedZero }: TimerProps) {
    const [seconds, setSeconds] = useState(initialSecondsForTurnLeft);

    useEffect(() => {
        const interval = setInterval(() => {
            // Update the seconds state every second
            setSeconds((prevSeconds:number) => prevSeconds - 1);
        }, 1000);
        
        if (seconds === 0) {
            clearInterval(interval);
            onTimeReachedZero();
        }
        // This function will be called when the component unmounts
        return () => clearInterval(interval);
    }, [seconds]);
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = seconds % 60;
    let timerString = '';
    if (minutes > 0) {
        timerString = String(minutes).padStart(2, '0') + ':' + String(remainingSeconds).padStart(2, '0');
    } else {
        timerString = remainingSeconds + 's';
    }
    return (
        <Typography variant="h4">
            {timerString}
        </Typography>
    );
}