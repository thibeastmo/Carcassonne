import {Typography} from "@mui/material";

export interface  UserStatisticItemComponentProps {
    value: number,
    description: string,
    percentage?: boolean
}
export function UserStatisticItemComponent({value, description, percentage = false}: UserStatisticItemComponentProps) {
    const formattedValue = isNaN(value) || !isFinite(value) ? 0 : value;

    // If the value is a whole number, remove decimal places
    const stringValue =  formattedValue % 1 === 0 ? formattedValue.toFixed(0) : formattedValue.toFixed(2);
    return (
        <>
            <Typography variant="h4">{(value < 1 ? percentage ? 0 : '-' : stringValue)+(percentage ? '%' : '')}</Typography>
            <Typography variant="h5">{description}</Typography>
        </>
    );
}