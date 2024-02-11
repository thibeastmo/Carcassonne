import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import {useNavigate} from 'react-router-dom'
import {useContext, useState, MouseEvent} from "react";
import SecurityContext from "../context/securityContext/SecurityContext.ts";
import {Avatar, Box, Container, Hidden, IconButton, Menu, MenuItem, Tooltip} from "@mui/material";
import MenuIcon from '@mui/icons-material/Menu';
import ApplicationThemeContext, {IApplicationThemeContext} from "../context/ApplicationThemeContext.ts";
import {ApplicationTheme} from "../model/ApplicationTheme.ts";
import Diversity3Icon from '@mui/icons-material/Diversity3';
import {NotificationButtonComponent} from "./notification/NotificationButtonComponent.tsx";
import {NotificationEmptyButtonComponent} from "./notification/NotificationEmptyButtonComponent.tsx";
import {LoyaltyPointsComponent} from "./statistics/LoyaltyPointsComponent.tsx";
import {OpenFriendListButtonComponent} from "./friends/OpenFriendListButtonComponent.tsx";
import {QuickJoinDialog} from "./QuickJoinDialog.tsx";
import {t} from "i18next";

export default function Navigation() {
    const [isDialogOpen, setIsDialogOpen] = useState(false)
    const {isAuthenticated, logout, loggedInUser, login} = useContext(SecurityContext);
    const [anchorElNav, setAnchorElNav] = useState<null | HTMLElement>(null);
    const [anchorElUser, setAnchorElUser] = useState<null | HTMLElement>(null);
    const navigate = useNavigate();

    const {applicationTheme} = useContext<IApplicationThemeContext>(ApplicationThemeContext);

    const handleQuickJoin = () => {
        setIsDialogOpen(true);
    }
    const handleOnClose = () => {
        setIsDialogOpen(false);
    }


    if (loggedInUser) {
        console.log(loggedInUser.avatar);
    }
    const handleOpenNavMenu = (event: MouseEvent<HTMLElement>) => {
        setAnchorElNav(event.currentTarget);
    };
    const handleOpenUserMenu = (event: MouseEvent<HTMLElement>) => {
        setAnchorElUser(event.currentTarget);
    };

    const handleCloseNavMenu = () => {
        setAnchorElNav(null);
    };

    const handleCloseUserMenu = () => {
        setAnchorElUser(null);
    };

    // ADD MENU ITEMS HERE
    const menuItems = [
        (
            <MenuItem key="lobbies" href="/lobbyOverview" onClick={() => {
                handleCloseNavMenu();
                navigate('/lobbyOverview');
            }}>
                <Typography textAlign="center">{t('navigation.lobbies')}</Typography>
            </MenuItem>
        ),
        (
            <MenuItem key="play" onClick={() => {
                handleCloseNavMenu();
                if (isAuthenticated()) {
                    handleQuickJoin();
                }
            }}>
                <Typography textAlign="center">{t('navigation.play')}</Typography>
            </MenuItem>
        ),
        (
            <MenuItem key="history" onClick={() => {
                handleCloseNavMenu();
                navigate('/gameHistory');
            }}>
                <Typography textAlign="center">{t('navigation.history')}</Typography>
            </MenuItem>
        ),
        (
            <MenuItem key="userStatistics" onClick={() => {
                handleCloseNavMenu();
                navigate('/userStatistics');
            }}>
                <Typography textAlign="center">{t('navigation.statistics')}</Typography>
            </MenuItem>
        ),
        (
            <MenuItem key="leaderboard" onClick={() => {
                handleCloseNavMenu();
                navigate('/leaderboard');
            }}>
                <Typography textAlign="center">{t('navigation.leaderboard')}</Typography>
            </MenuItem>
        ),
        (
            <MenuItem key="shop" onClick={() => {
                handleCloseNavMenu();
                navigate('/shop')
            }}>
                <Typography textAlign="center">{t('navigation.shop')}</Typography>
            </MenuItem>
        ),
        (
            <MenuItem key="friends" onClick={handleCloseNavMenu}>
                {isAuthenticated() ? <OpenFriendListButtonComponent/> : <><Diversity3Icon/></>}
            </MenuItem>
        ),
        (
            <MenuItem key="notifications" onClick={handleCloseNavMenu}>
                {isAuthenticated() ? <NotificationButtonComponent/> : <NotificationEmptyButtonComponent/>}
            </MenuItem>
        ),
        // ADD MENU ITEMS HERE
    ];

    // ADD USER SPECIFIC ITEMS HERE
    const userSettings = [
        (<MenuItem onClick={() =>
            navigate('/profile')
        }>
            {t('navigation.profile')}
        </MenuItem>),
        (
            <MenuItem key="settings" onClick={() => {
                navigate(`/settings`);
            }}>
                {t('navigation.settings')}
            </MenuItem>
        ),
        (
            <MenuItem key="logout" onClick={() => {
                logout();
            }}>
                {t('navigation.logout')}
            </MenuItem>
        ),
        // ADD MENU ITEMS HERE
    ]

    const getDynamicStyle = (applicationTheme: ApplicationTheme) => {
        if (applicationTheme === ApplicationTheme.WINTER) {
            return {
                backgroundColor: '#DBF1FD',
                color: '#000',
            };
        }
    };
    const dynamicStyle = getDynamicStyle(applicationTheme);

    const handleHomeClick = () => {
        navigate(`/`);
    }

    return (
        <>
            <QuickJoinDialog isOpen={isDialogOpen} onClose={handleOnClose}></QuickJoinDialog>
            <AppBar position="static" sx={dynamicStyle}>
                <Container maxWidth="xl">
                    <Toolbar disableGutters>
                        <Hidden xsDown mdDown>
                            <img style={{height: "40px", width: "40px", margin: "10px"}}
                                 src="/src/assets/carcassone_logo_small.png" alt="carcassone logo"/>
                        </Hidden>
                        <Typography
                            variant="h6"
                            noWrap
                            onClick={handleHomeClick}
                            sx={{
                                mr: 2,
                                display: {xs: 'none', md: 'flex'},
                                fontFamily: 'monospace',
                                fontWeight: 700,
                                letterSpacing: '.08rem',
                                color: 'inherit',
                                textDecoration: 'none',
                                cursor: 'pointer'
                            }}
                        >
                            CARCASSONNE
                        </Typography>

                        <Box sx={{flexGrow: 1, display: {xs: 'flex', md: 'none'}}}>
                            <IconButton
                                size="large"
                                aria-label="account of current user"
                                aria-controls="menu-appbar"
                                aria-haspopup="true"
                                onClick={handleOpenNavMenu}
                                color="inherit"
                            >
                                <MenuIcon/>
                            </IconButton>
                            <Menu
                                id="menu-appbar"
                                anchorEl={anchorElNav}
                                anchorOrigin={{
                                    vertical: 'bottom',
                                    horizontal: 'left',
                                }}
                                keepMounted
                                transformOrigin={{
                                    vertical: 'top',
                                    horizontal: 'left',
                                }}
                                open={Boolean(anchorElNav)}
                                onClose={handleCloseNavMenu}
                                sx={{
                                    display: {xs: 'block', md: 'none'},
                                }}
                            >
                                {menuItems.map(item => item)}
                            </Menu>
                        </Box>
                        {/* PAGES GO HERE */}
                        <Box sx={{flexGrow: 1, display: {xs: 'none', md: 'flex'}}}>
                            {menuItems.map(item => item)}
                        </Box>

                        {/* CURRENCY GOES HERE */}
                        {isAuthenticated() ? <LoyaltyPointsComponent/> : <></>}

                        <Typography
                            variant="h5"
                            noWrap
                            onClick={handleHomeClick}
                            sx={{
                                mr: 2,
                                display: {xs: 'flex', md: 'none'},
                                flexGrow: 1,
                                fontFamily: 'monospace',
                                fontWeight: 700,
                                letterSpacing: '.08rem',
                                color: 'inherit',
                                textDecoration: 'none',
                                cursor: 'pointer'
                            }}
                        >
                            Carcassonne
                        </Typography>

                        {isAuthenticated() ? (
                            <Box sx={{flexGrow: 0}}>
                                <Tooltip title="Open settings">
                                    <IconButton onClick={handleOpenUserMenu} sx={{p: 0}}>
                                        {loggedInUser?.avatar ? (
                                            <Avatar alt="Remy Sharp"
                                                    src={import.meta.env['VITE_BACKEND_URL'] + loggedInUser?.avatar.url}/>
                                        ) : (<Avatar alt="Remy Sharp" src="/static/images/avatar/2.jpg"/>
                                        )}
                                    </IconButton>
                                </Tooltip>
                                <Menu
                                    sx={{mt: '45px'}}
                                    id="menu-appbar"
                                    anchorEl={anchorElUser}
                                    anchorOrigin={{
                                        vertical: 'top',
                                        horizontal: 'right',
                                    }}
                                    keepMounted
                                    transformOrigin={{
                                        vertical: 'top',
                                        horizontal: 'right',
                                    }}
                                    open={Boolean(anchorElUser)}
                                    onClose={handleCloseUserMenu}
                                >
                                    {userSettings.map(item => item)}
                                </Menu>
                            </Box>
                        ) : (<MenuItem onClick={login}>{t('navigation.login')}</MenuItem>)}
                    </Toolbar>
                </Container>
            </AppBar>
        </>
    );
}