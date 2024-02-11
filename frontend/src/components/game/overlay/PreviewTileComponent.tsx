import {Box} from "@mui/material";

export function PreviewTileComponent() {
    return (
        <Box style={{
            marginRight: '10px',
            backgroundColor: 'red',
                }}>
            <img src={import.meta.env['VITE_BACKEND_URL'] + '/images'+'/tiles/31.png'} alt="Preview of holding tile"/>
        </Box>
    )
}