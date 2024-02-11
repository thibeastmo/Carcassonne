import {Box, MenuItem} from "@mui/material";
import Typography from "@mui/material/Typography";
import {useLoyaltyPoints} from "../../hooks/statistics/useLoyaltyPoints.ts";
import {useNavigate} from "react-router-dom";

export function LoyaltyPointsComponent() {
    const {isLoading, isError, loyaltyPoints} = useLoyaltyPoints()
    const navigate = useNavigate();
    return (
        <Box sx={{flexGrow: 1, display: {xs: 'none', md: 'flex'}}}>
            <MenuItem key="loyaltyPoints" onClick={() => {
                navigate('/shop');
            }}>
                <Typography textAlign="center">LP: {isError || isLoading ? 0 : loyaltyPoints?.amountOfLoyaltyPoints}</Typography>
            </MenuItem>
        </Box>
    );
}