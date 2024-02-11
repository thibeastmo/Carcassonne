import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';

export function HamburgerButton() {

    return (
        <IconButton color="inherit" edge="start" aria-label="menu">
            <MenuIcon />
        </IconButton>
    )
}