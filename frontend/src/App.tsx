import './App.css'
import axios from "axios";
import {QueryClient, QueryClientProvider} from "@tanstack/react-query";
// import {HelloWorldComponent} from "./components/HelloWorldComponent.tsx";
import SecurityContextProvider from "./context/securityContext/SecurityContextProvider.tsx";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import RouteGuard from "./components/RouteGuard.tsx";
import {createTheme, ThemeProvider} from '@mui/material'
import './i18n';
import {LobbyOverview} from "./components/lobby/LobbyOverview.tsx";
import {LobbyItem} from "./components/lobby/LobbyItem.tsx";
import {Home} from "./components/Home.tsx";
import Navigation from "./components/Navigation.tsx";
import {GameComponent} from "./components/game/GameComponent.tsx";
import {GameHistoryComponent} from "./components/statistics/GameHistoryComponent.tsx";
import {UserStatisticsComponent} from "./components/statistics/UserStatisticsComponent.tsx";
import {ProfilePage} from "./components/profile/ProfilePage.tsx";
import {Settings} from "./components/Settings.tsx";
import {Layout} from './Layout.tsx';
import {useState} from "react";
import {ShopOverview} from './components/shop/ShopOverview.tsx';
import ApplicationThemeContextProvider from './context/ApplicationThemeContextProvider.tsx';
import {LeaderboardOverview} from "./components/statistics/Leaderboard/LeaderboardOverview.tsx";

axios.defaults.baseURL = import.meta.env['VITE_BACKEND_URL'];
const queryClient = new QueryClient();

const defaultTheme = createTheme({
    typography: {
        fontFamily: 'Caudex, sans-serif',
    },
    palette: {
        background: {
            default: '#fff'
        }
    },
});

function App() {
    const [currentTheme, setCurrentTheme] = useState(defaultTheme);

    const setTheme = (themeString: string) => {
        if (themeString.toUpperCase() == "WINTER") {
            const winterTheme = createTheme({
                typography: {
                    fontFamily: 'sans-serif',
                },
                palette: {
                    background: {
                        default: '#fff'
                    }
                }
            });
            setCurrentTheme(winterTheme);
        } else if (themeString.toUpperCase() == "NORMAL") {
            setCurrentTheme(defaultTheme);
        }
    };

    return (
        <QueryClientProvider client={queryClient}>
            <SecurityContextProvider>
                <ThemeProvider theme={currentTheme}>
                    <ApplicationThemeContextProvider>
                        <BrowserRouter>
                            <Navigation/>
                            <Layout>
                                <Routes>
                                    <Route path="/" element={<Home/>}/>
                                    <Route path="/userStatistics"
                                           element={<RouteGuard component={<UserStatisticsComponent/>}/>}/>
                                    <Route path="/gameHistory"
                                           element={<RouteGuard component={<GameHistoryComponent/>}/>}/>
                                    <Route path="/lobbyOverview" element={<RouteGuard component={<LobbyOverview/>}/>}/>
                                    <Route path="/lobby/:id" element={<RouteGuard component={<LobbyItem/>}/>}/>
                                    {/*<Route path="/" element={<RouteGuard component={<HelloWorldPage/>}/>}/>*/}
                                    <Route path="/game/:id" element={<RouteGuard component={<GameComponent/>}/>}/>
                                    <Route path="/settings" element={<RouteGuard
                                        component={<Settings setThemeForProvider={setTheme}/>}/>}/>
                                    <Route path="/shop" element={<RouteGuard component={<ShopOverview/>}/>}/>
                                    <Route path="/leaderboard" element={<RouteGuard component={<LeaderboardOverview/>}/>}/>
                                    <Route path="/profile" element={<RouteGuard component={<ProfilePage/>}/>}/>
                                </Routes>
                            </Layout>
                        </BrowserRouter>
                        {/*<div className="App">*/}
                        {/*    <HelloWorldComponent></HelloWorldComponent>*/}
                        {/*</div>*/}
                    </ApplicationThemeContextProvider>
                </ThemeProvider>
            </SecurityContextProvider>
        </QueryClientProvider>
    )
}

export default App
