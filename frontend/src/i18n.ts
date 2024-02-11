import i18n from 'i18next';
import {initReactI18next} from 'react-i18next';
import LanguageDetector from 'i18next-browser-languagedetector';

i18n
    // detect user language
    // learn more: https://github.com/i18next/i18next-browser-languageDetector
    .use(LanguageDetector)
    // pass the i18n instance to react-i18next.
    .use(initReactI18next)
    // init i18next
    // for all options read: https://www.i18next.com/overview/configuration-options
    .init({
        debug: true,
        fallbackLng: 'en',
        interpolation: {
            escapeValue: false, // not needed for react as it escapes by default
        },
        resources: {
            nl: {
                translation: {
                    home: {
                        welcome: 'Welkom bij carcassonne!',
                        welcome_text: 'Stap in een betoverende wereld vol strategie, avontuur en concurrentie met Carcassonne! Dit bordspel\n' +
                            '                neemt je mee op een reis door een middeleeuws landschap, waar elke zet je dichter bij de overwinning\n' +
                            '                brengt.\n' +
                            '                Leg de basis voor een uniek landschap terwijl je tegels combineert en een prachtig spelbord vormt. Elk\n' +
                            '                stuk dat je plaatst, onthult steden, wegen, kloosters en velden die wachten om ontdekt te worden. Maar\n' +
                            '                pas op, elke beslissing kan het spel veranderen!\n' +
                            '                Roep je strategisch inzicht op om gebieden te veroveren door slimme keuzes te maken en meeples op de\n' +
                            '                beste plekken te plaatsen. Of het nu gaat om het bouwen van een imposante stad, het leggen van een\n' +
                            '                cruciale weg of het voltooien van een sierlijk klooster, elke zet brengt je dichter bij het claimen van\n' +
                            '                de meeste punten en de overwinning.\n' +
                            '                Carcassonne biedt een perfecte mix van uitdagende tactieken, verrassende wendingen en een spannende\n' +
                            '                competitie. Geschikt voor spelers van alle niveaus, dit spel staat garant voor eindeloos plezier en een\n' +
                            '                onvergetelijke tijd met vrienden en familie. Waar wacht je nog op? Betreed de middeleeuwse wereld van\n' +
                            '                Carcassonne en beleef het spelavontuur van je leven!'
                    },
                    navigation: {
                        lobbies: 'Lobbies',
                        play: 'Play',
                        history: 'History',
                        statistics: 'Statistics',
                        shop: 'Shop',
                        profile: 'Profiel',
                        settings: 'Settings',
                        logout: 'Logout',
                        login: 'Login',
                        leaderboard: 'Leaderboard'
                    },
                    settings: {
                        title: 'Settings',
                    },
                    settings_ingame: {
                        placeholder: 'Hier komen de settings'
                    },
                    loading: {
                        loading: 'laden...',
                        error: 'Er is iets misgegaan',
                        sending: 'Versturen...',
                    },
                    game: {
                        game: 'Spel',
                        players: 'Spelers',
                        games_waiting: 'Deze spellen wachten op jou',
                        no_games_waiting: 'Er zijn geen spellen die wachten op jou'
                    },
                    friend: {
                        username_or_email: 'Gebruikersnaam of email',
                        send: 'Verstuur',
                        friends: 'Vrienden',
                        friend_requests: 'Vriendschapsverzoeken',
                        add: 'Toevoegen',
                        invites: 'Uitnodigingen',
                        invite: 'invite',
                        score: 'score',

                    },
                    invite: {
                        game_type: 'game type',
                        lobby_name: 'lobby naam',
                        max_players: 'max players',
                    },

                    time: {
                        just_now: 'zonet',
                        minutes: 'minuten',
                        minute: 'minuut',
                        hour: 'uur',
                        hours: 'uren',
                        day: 'dag',
                        days: 'dagen',
                        ago: 'geleden'
                    },
                    common: {
                        close: 'sluiten'
                    },
                    tile_controls: {
                        draw: 'Trek kaart'
                    },
                    controls: {
                        button_tooltip: 'Controles',
                        buttons: 'Met deze knoppen kan je de spelregels en controles openen, een horige plaatsen of je beurt beëindigen.',
                        players: 'Hier kan je de spelers in het spel zien. Je ziet hun avatar en gebruikersnaam, je kan zien hoeveel punten ze dit spel al gehaald hebben en hoeveel horigen ze nog kunnen plaatsen. De kleur van het horige icoontje geeft aan met welke kleur de speler speelt. Je kan hier ook zien welke speler aan de beurt is, wanneer een speler aan de beurt is zal hun kaartje groter en kleiner worden.',
                        time: 'Naargelang welk speltype je gekozen hebt, heb je meer of minder tijd om je beurt af te ronden. De tijd is aan de top van je scherm zichtbaar. Let op, als je tijd om is kan je niet meer spelen.',
                        tilecontrols: 'Deze box geeft weer welke tegel je deze beurt mag plaatsen. Het geeft je ook de mogelijkheid om de tegel te draaien zodat je deze beter zou kunnen plaatsen. Ook wordt er weergegeven hoeveel tegels er al gelegd zijn en hoeveel er nog over zijn.',
                        preview: 'Je kan een tegel plaatsen door op één van deze doorzichtige tegels te klikken. Deze veranderen naargelang je de tegel draait, dus probeer eerst een paar draaiingen om te zien hoe je je tegel best kan plaatsen. Na het plaatsen van een tegel kan je een horige plaatsen op deze tegel.',
                        placeserf: 'Er zijn 9 zones op een tegel waarop je een horige kan plaatsen. Bij het plaatsen van een horige moet je rekening houden met de structuren op de tegel en met de tegels die al geplaatst zijn. Een structuur waar al een horige op staat kan niet nog een horige krijgen. Een horige plaats je door op een geldige zone te klikken en vervolgens op het vinkje te klikken. Indien je wenst te annuleren kan je op het kruisje klikken.',
                        buttons_title: 'Knoppen',
                        players_title: 'Spelers',
                        time_title: 'Tijd',
                        tilecontrols_title: 'Tegels bedienen',
                        preview_title: 'Tegels plaatsen',
                        placeserf_title: 'Horigen plaatsen',
                    },
                    instructions: {
                        button_tooltip: 'Spelregels',
                        title_goal: 'Het doel van het spel',
                        text_goal: 'Het is de bedoeling om na afloop zoveel mogelijk punten te hebben. De spelers leggen iedere beurt landtegels neer. Er ontstaan wegen, steden, weiden en kloosters, waarop de spelers hun horigen mogen zetten. Zo vergaren zij punten. Daar men zowel tijdens het spel als aan de hand van de eindtelling punten verdient, staat de winnaar pas na afloop vast.',
                        title_start: 'Het begin van het spel',
                        text_start: 'Het spel begint met een starttegel op het midden van het speelveld. Er zijn nog 71 andere landtegels die één voor één getrokken moeten worden en geplaatst worden door de spelers. Iedere speler beschikt aan de start over 7 horigen.',
                        title_progress: 'Het verloop van het spel',
                        text_progress_1: 'Men speelt met de klok mee. Wie aan de beurt is, voert de hierna genoemde acties in de aangegeven volgorde uit:',
                        text_progress_2: '1. De speler moet een nieuwe landtegel trekken en aanleggen.',
                        text_progress_3: '2. De speler mag daarna als hij dat wenst één van zijn horigen uit zijn voorraad op de juist geplaatste landtegel zetten.',
                        text_progress_4: '3. Als door het plaatsen van een landtegel wegen, steden of kloosters afgebouwd worden, worden deze nu geteld.',
                        text_progress_5: 'Daarna is de volgende speler aan de beurt.',
                        title_place_tiles: '1. Het leggen van landtegels',
                        text_place_tiles: 'De speler die aan de beurt is krijgt een landtegel uit de stapel. Deze is zichtbaar voor alle spelers. Indien de tegel niet geplaatst kan worden, zal deze uit het spel worden verwijderd en krijgt de  speler aan wiens beurt het is een nieuwe landtegel. Nieuwe tegels moeten met minstens één zijde aan een of meer eerder geplaatste landtegels gelegd worden. Weiden, wegen en stadsdelen mogen niet onderbroken worden',
                        title_place_serfs: '2. Het plaatsen van horigen',
                        text_place_serfs: 'Zodra de speler zijn landtegel heeft geplaatst, mag hij een horige in het spel brengen. Er mag maar 1 horige geplaatst worden per beurt, deze mag ook enkel op de net gelegde landtegel geplaatst worden. De speler kan beslissen op welk deel hij zijn horige legt. Als in of op een door een landtegel vergrote stad, weide of weg al een horige stond, mag er geen horige aan toegevoegd worden. Het speelt daarbij geen rol, hoe ver weg de andere horige staat. Als in de loop van het spel van een speler de horigen uitgeput raken, kan hij uitsluitend nog landtegels aanleggen. Maar geen paniek: er komen uit het spel regelmatig ook weer horigen in ieders voorraad terug. Nu is de beurt van de speler voorbij en is de speler links van hem aan de beurt. Uitzondering: als door het leggen van een landtegel een stad, weg of klooster afgebouwd wordt, volgt direct een telling.',
                        title_counting: '3. Afgebouwde wegen, steden en kloosters worden geteld',
                        text_counting: 'Een weg is af, asl deze een doorlopende verbinding vormt tussen twee punten, die elk een splitsing, een stadspoort of een klooster met een weg zijn (er zijn ook kloosters zonder weg). Tussen beide punten mag een willekeurig aantal verbindingsstukken liggen. Voor een afgebouwde weg krijgt de speler, die het met een struikrover bezet, zoveel punten als de weg (uitgedrukt in landtegels) lang is. Een stad is af, als deze een sluitende stadsmuur heeft, en zich binnen de muren geen gaten meer bevinden. Een stad mag uit een onbeperkt aantal landtegels bestaan. Voor een afgebouwde stad krijgt de speler, die er een ridder bezit, 2 punten voor elke tegel, waaruit deze stad bestaat. Elk wapen geeft bovendien een bonus van 2 punten. Wat gebeurt er, als meerdere horigen in een afgebouwde stad of op een afgebouwde weg staan? Door het slim plaatsen van landtegels kan men het voor elkaar krijgen, dat er meerdere struikrovers op een af te bouwen weg staan, of dat meerdere ridders zich in een af te bouwen stad bevinden. De speler met de meeste horigen op de weg of in de stad krijgt als deze af is de punten. Bij een gelijke stand krijgen alle spelers met de meeste horigen het volle aantal punten. Een klooster is af als het gebouw geheel door landschapskaarten is omgeven (4 zijden en 4 hoeken). De speler, die een monnik in een afgebouwd klooster heeft, krijgt direct 9 punten.',
                        title_return_serfs: 'De terugkeer van horigen naar hun meesters',
                        text_return_serfs: 'Nadat een weg, stad of klooster gewaardeerd is, en alleen dan, keren de zich daar bevindende horigen terug naar hun meester. De speler mag ze vanaf zijn volgende beurt weer in een rol naar keuze inzetten. Als je je horige plaatst en je kan meteen tot waardering overgaan, krijg je je horige meteen ook terug.',
                        title_fields: 'Weiden',
                        text_fields: 'Meerdere aan elkaar grenzende weidestukken worden als weiden aangeduid. Weiden worden niet geteld: ze dienen alleen om boeren te huisvesten. Boeren krijgen pas aan het einde van het spel punten. Daarom blijven boeren het hele spel in hun weiden staan en keren ze tussentijds niet naar hun meesters terug! Weiden worden door wegen, steden en de rand van het bord van andere weiden gescheiden.',
                        title_endgame: 'Het einde van het spel',
                        text_endgame: 'Aan het einde van de beurt, waarin de laatste landtegel gelegd wordt, is het spel afgelopen. Nu volgt de laatste telling.',
                        title_counting_end: 'De eindtelling',
                        title_counting_structures: 'Telling van onafgebouwde wegen, steden en kloosters',
                        text_counting_structures: 'Bij de eindtelling worden eerst alle onafgebouwde wegen, steden en kloosters geteld. Voor elke onafgebouwde weg, stad en klooster krijgen de meesters van de zich daar bevindende horigen 1 punt voor elke tegel waaruit het bouwsel bestaat. Ook een wapen in een stadsdeel telt maar als 1 punt.',
                        title_counting_farmers: 'Telling van boeren',
                        text_counting_farmers: 'Nu krijgen de spelers punten voor de boeren die nog in de weiden staan. De speler met de meeste boeren in een weide krijgt de punten. Als meerdere spelers de meeste boeren in een weide hebben, krijgen ze allemaal de punten. De bezitter(s) van een weide krijg(t)(en) voor elke afgebouwde stad die aan deze weide grenst, 3 punten. Als een stad aan meerdere weiden grenst, krijgen alle bezitters van die weiden voor deze stad 3 punten.',
                    },
                    in_game: {
                        place_serf_button_tooltip: 'plaats een horige',
                        next_turn_button_tooltip: 'beurt beëindigen',
                        score: 'punten',
                        serfs: 'horigen',
                        placeSerf: 'Plaats een horige',
                        savePlacedSerf: 'Bevestig',
                        waitingForOpponent: 'Tegenstander moet een zet doen',
                        results: {
                            title: 'Resultaat',
                            subtitle: 'Einde van het spel!',
                            replay: 'Herhaling',
                            home: 'Hoofdpagina',
                            home_tooltip: 'Ga naar de hoofdpagina',
                        }
                    },
                    login:
                        {
                            text_login: 'login',
                            text_login_message: 'Vooruitgang in het spel vereist inloggen'
                        },
                    lobby_overview:
                        {
                            title: 'Lobbies',
                            text_lobbies_looking_for_players: 'Zoekend naar tegenstanders',
                            text_no_lobbies_available: 'Geen lobbies beschikbaar',
                            text_joined_lobbies: 'Gejoinde lobbies',
                            text_create_game: 'Maak een lobby',
                            text_running_games: 'Lopende spellen',
                            watch_game: 'Bekijk spel',
                            join: 'join',
                            watch: 'bekijk',
                            leave: 'verlaat'
                        },
                    lobby_item: {
                        text_start_game: 'Start het spel',
                        text_empty_slot: 'Lege slot',
                        text_error: 'Er ging iets mis',
                        text_alert_not_enough_players_to_start: 'De game mag enkel gestart worden met 2 of meer spelers',
                        lobby_name: 'Lobby naam',
                        amount_of_players: 'Aantal spelers',
                        settings: 'Settings',
                        short_game: 'Kort spel',
                        short_game_description: ' Ideaal voor een snel spel te spelen',
                        short_game_duration: 'Geschatte duur van 30-40 minuten',
                        short_game_turn_time: 'Elke beurt duurt maximaal 60 seconden',
                        long_game: 'Lang spel',
                        long_game_description: 'Spel dat over meerdere dagen gespeeld kan worden',
                        long_game_duration: 'Tijd om spel af te sluiten en terug te komen wanneer het jouw beurt is',
                        long_game_turn_time: 'Je krijgt 1 dag tijd om je beurt af te werken',
                        create_lobby: 'Maak lobby',

                    },
                    lobby_quick_join: {
                        text_quickjoin_short_game: 'Zoek naar een short game',
                        text_quickjoin_long_game: 'Zoek naar een long game',
                        text_quickjoin_waiting: 'We zoeken naar een lobby...',
                        text_quickjoin_no_lobby_found: 'We hebben geen lobby kunnen vinden',
                        text_quickjoin_join_button: 'Quick join',
                        text_quickjoin_try_again: 'Probeer opnieuw',
                        text_quickjoin_type_of_game: 'Kies je type spel'
                    },
                    statistics: {
                        game_history: {
                            title: 'Historiek van spelletjes',
                            watch_replay: 'Simuleer het spel',
                        },
                        user_statistics: {
                            games_won: 'Gewonnen wedstrijden',
                            games_played: 'Gespeelde wedstrijden',
                            tiles_placed: 'Gelegde landschappen',
                            serfs_placed: 'Gebruikte horigen',
                            contested_land_won: 'Overwonnen aangevochten landen',
                            contested_land_lost: 'Verloren aangevochten landen',
                            total_score_achieved: 'Totale score van alle wedstrijden',
                            games_lost: 'Verloren wedstrijden',
                            win_rate: 'Winstpercentage',
                            contested_land_win_rate: 'Winstpercentage aangevochten landen',
                            avg_serfs_placed_per_game: 'Gemiddeld aantal horigen geplaatst per wedstrijd',
                            avg_tiles_placed_per_game: 'Gemiddeld aantal landschappen geplaatst per wedstrijd',
                            avg_serfs_placed_per_tile: 'Gemiddeld aantal horigen geplaatst per plaatsing van landschap',
                            avg_score_per_game: 'Gemiddelde punten per wedstrijd',
                            avg_tiles_placed_per_contested_land_won: 'Gemiddeld aantal landschappen geplaatst per overwonnen aangevochten landen',
                            avg_serfs_required_per_contested_land_won: 'Gemiddeld aantal horigen nodig per overwinning van aangevochten landen',
                            avg_contested_lands_won_per_win: 'Gemiddeld aantal overwonnen aangevochten landen per gewonnen wedstrijd'
                        }
                    },
                    theme: {
                        text_loading_theme: 'We laden je thema in',
                        text_error_loading_theme: 'Er ging iets mis bij het inladen van je thema',
                    },
                    shop: {
                        text_error_equipping_item: 'Er ging iets mis bij het equippen van het item',
                        text_equip_item_question: 'Wil je dit item gebruiken?',
                        text_purchase_successful: 'Koop succesvol!',
                        text_purchase: 'Koop',
                        text_purchased: 'Gekocht',
                        text_purchasing: 'Aan het kopen...',
                        text_confirmation_question: 'Ben je zeker dat je dit item wilt kopen?',
                        text_use: 'Gebruik',
                        text_close: 'Sluit',
                        text_not_enough_points: 'Je hebt niet genoeg loyaltypoints',
                        shop: 'Shop',
                        avatars: 'Avatars',
                        themes: 'Thema\'s',
                    },
                    leaderboard: {
                        title: 'De beste spelers',
                        empty: 'Het lijkt erop dat we nog geen leidende spelers hebben.',
                        columns: {
                            rank: 'Plaats',
                            nickname: 'Speler naam',
                            experiencePoints: 'Punten'
                        }
                    },
                    error: {
                        not_found: 'Oeps, er is iets mis. Keer snel terug naar onze hoofdpagina',
                        home_button: 'Onze hoofdpagina',
                        text_not_enough_points: 'Je hebt niet genoeg loyaltypoints',
                    },
                    profile: {
                        header: 'profielInstellingen',
                        change_account_info_button: 'update accountinfo',
                        avatars_header: 'Aangekochte avatars',
                        avatars_no_avatars: 'Je hebt nog geen avatars gekocht',
                        avatar_alternative: 'Avatar afbeelding',
                        avatar_equipped: 'Avatar uitgerust',
                        loading: 'Aan het laden...',
                        error: 'Er ging iets mis',
                    }
                }
            }
        }
    });

export default i18n;