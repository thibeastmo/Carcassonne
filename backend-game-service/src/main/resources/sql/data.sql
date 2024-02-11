INSERT INTO account(account_id, subject_id, nick_name, experience_points, loyalty_points, theme, user_level)
VALUES ('60e2f67a-bf9f-4f2f-aca2-ba8d113bfa2b', '60e2f67a-bf9f-4f2f-aca2-ba8d113bfa2b', 'thibeastmo', 100, 0, 0, 8);
INSERT INTO account(account_id, subject_id, nick_name, experience_points, loyalty_points, theme, user_level)
VALUES ('60e2f67a-bf9f-4f2f-aca2-ba8d113bfa3b', 'c557502a-0e3d-4946-8d6f-cf57c3170bab', 'Jean paul gautier', 100, 0, 0, 5);
INSERT INTO account(account_id, subject_id, nick_name, experience_points, loyalty_points, theme, user_level)
VALUES ('60e2f67a-bf9f-4f2f-aca2-ba8d113bfa4b', '2d00c8d4-3626-4e17-8bae-b6a10ca5556e', 'tba', 100, 0, 0, 8);
INSERT INTO account(account_id, subject_id, nick_name, experience_points, loyalty_points, theme, user_level)
VALUES ('bcf903ae-997d-466c-b4ac-410d1fe420be', 'bcf903ae-997d-466c-b4ac-410d1fe420be', 'Atrophius', 1020, 0, 0, 2);
INSERT INTO account(account_id, subject_id, nick_name, experience_points, loyalty_points, theme, user_level)
VALUES ('e5e92098-fc3b-4daa-b337-be740fb610e5', 'e5e92098-fc3b-4daa-b337-be740fb610e5', 'Pavelski', 10, 12, 0, 6);
INSERT INTO account(account_id, subject_id, nick_name, experience_points, loyalty_points, theme, user_level)
VALUES ('c47e8e47-92d7-4ee9-a77d-3f6a3d63e289', '58540443-4003-49e6-9b4c-336f2e915a34', 'MichielIsKoning', 100, 500, 0,  4);
INSERT INTO account(account_id, subject_id, nick_name, experience_points, loyalty_points, theme, user_level)
VALUES ('60e2f67a-bf9f-4f2f-aca2-ba8d113bfa5b', 'a0a29b50-b8e4-4de8-bac8-b1d52ed45073', 'kvdw', 100, 0, 0, 2);

INSERT INTO friendship (friended_on, account1_account_id, account2_account_id)
VALUES (CURRENT_TIMESTAMP, 'c47e8e47-92d7-4ee9-a77d-3f6a3d63e289', 'e5e92098-fc3b-4daa-b337-be740fb610e5');

INSERT INTO lobby (lobby_id, lobby_name, owner_account_id, start_date, max_players, game_type_enum, closed)
VALUES ('1bc3f9bb-6e16-4526-a82a-af8c25604ef7', 'lobby1', '60e2f67a-bf9f-4f2f-aca2-ba8d113bfa2b', CURRENT_TIMESTAMP, 4,
        0, false);
INSERT INTO lobby (lobby_id, lobby_name, owner_account_id, start_date, max_players, game_type_enum, closed)
VALUES ('1bc3f9bb-6e16-4526-a82a-af8c25604ef8', 'lobby2', '60e2f67a-bf9f-4f2f-aca2-ba8d113bfa2b', CURRENT_TIMESTAMP, 4,
        0, true);

INSERT INTO lobby (lobby_id, lobby_name, owner_account_id, start_date, max_players, game_type_enum, closed)
VALUES ('89c2f9fa-6e16-45c6-a82a-af8c2520aefb', 'lobby3', '60e2f67a-bf9f-4f2f-aca2-ba8d113bfa2b', CURRENT_TIMESTAMP, 4,
        0, false);


INSERT INTO game (game_id, game_type_enum, max_turn_duration_time, is_game_over)
VALUES ('2bc3f9bb-6e16-4526-a82a-af8c25604ef8', 0, 2, false);


INSERT INTO lobby_participant (account_account_id, lobby_lobby_id)
VALUES ('60e2f67a-bf9f-4f2f-aca2-ba8d113bfa2b', '1bc3f9bb-6e16-4526-a82a-af8c25604ef7');
INSERT INTO lobby_participant (account_account_id, lobby_lobby_id)
VALUES ('bcf903ae-997d-466c-b4ac-410d1fe420be', '1bc3f9bb-6e16-4526-a82a-af8c25604ef7');
INSERT INTO lobby_participant (account_account_id, lobby_lobby_id)
VALUES ('e5e92098-fc3b-4daa-b337-be740fb610e5', '1bc3f9bb-6e16-4526-a82a-af8c25604ef7');

INSERT INTO lobby_participant (account_account_id, lobby_lobby_id)
VALUES ('e5e92098-fc3b-4daa-b337-be740fb610e5', '1bc3f9bb-6e16-4526-a82a-af8c25604ef8');
INSERT INTO lobby_participant (account_account_id, lobby_lobby_id)
VALUES ('bcf903ae-997d-466c-b4ac-410d1fe420be', '1bc3f9bb-6e16-4526-a82a-af8c25604ef8');

INSERT INTO lobby_participant (account_account_id, lobby_lobby_id)
VALUES ('e5e92098-fc3b-4daa-b337-be740fb610e5', '89c2f9fa-6e16-45c6-a82a-af8c2520aefb');
INSERT INTO lobby_participant (account_account_id, lobby_lobby_id)
VALUES ('bcf903ae-997d-466c-b4ac-410d1fe420be', '89c2f9fa-6e16-45c6-a82a-af8c2520aefb');


INSERT INTO player (player_number, account_account_id, game_game_id, points, contested_land_lost, contested_land_won)
VALUES (100000, '60e2f67a-bf9f-4f2f-aca2-ba8d113bfa2b', '2bc3f9bb-6e16-4526-a82a-af8c25604ef8', 2, 0, 0);
INSERT INTO player (player_number, account_account_id, game_game_id, points, contested_land_lost, contested_land_won)
VALUES (100001, 'bcf903ae-997d-466c-b4ac-410d1fe420be', '2bc3f9bb-6e16-4526-a82a-af8c25604ef8', 15, 0, 0);
INSERT INTO player (player_number, account_account_id, game_game_id, points, contested_land_lost, contested_land_won)
VALUES (100002, 'e5e92098-fc3b-4daa-b337-be740fb610e5', '2bc3f9bb-6e16-4526-a82a-af8c25604ef8', 22, 0, 0);

INSERT INTO public.hello_world (hello_id, hello_value)
VALUES ('67c02eb2-8c2f-4b1f-b788-820f36e7e85d', 'hello world!');

