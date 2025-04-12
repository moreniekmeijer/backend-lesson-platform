INSERT INTO styles (name, origin, description)
VALUES ('Maracatu', 'Brazil',
        'Maracatu is een krachtig en ritmisch muziekgenre uit Pernambuco, Brazilië, dat draait om percussie. De muziek wordt voornamelijk gespeeld op instrumenten zoals de alfaia (grote trommel), caixa (snaredrum), gonguê (ijzeren bel), agogô (dubbele bel) en abê (schudinstrument). Het ritme is diep, repetitief en energiek, en vormt de basis voor zowel dans als ceremoniële optochten, vooral tijdens het carnaval. Maracatu heeft twee hoofdvarianten: Baque Virado, met een zware, soliede beat, en Baque Solto, die lichter en meer ritmisch flexibel is. Het genre heeft zowel een historische wortel in Afro-Braziliaanse tradities als een moderne invloed op genres als manguebeat.'),
       ('Samba Reggae', 'Brazil',
        'Samba Reggae is een muzikale fusie van traditionele Braziliaanse samba en Jamaicaanse reggae, ontstaan in Salvador, Bahia. Het combineert de zware, syncopische ritmes van samba met de langzamere, relaxte feel van reggae. Typische instrumenten zijn de surdo (grootte basdrum), atabaque (houten trommel) en caixa (snaredrum), samen met melodieën op blaasinstrumenten en de kenmerkende reggae bassline. Samba Reggae is populair tijdens het carnaval en benadrukt een relaxed maar krachtige groove.'),
       ('Makru', 'Africa',
        'Makru is een Afrikaanse muziekstijl uit de Gambiaanse en Senegalese regio, vaak geassocieerd met de Mandinka-cultuur. Het is een ritmische en percussieve muziek die wordt gespeeld met instrumenten zoals de djembe, balafon en dunun (trommels). Makru is zowel een dans als een ritueel, met een focus op herhalende, complexe ritmes die de verbondenheid met de natuur en de voorouders weerspiegelen. Het wordt vaak uitgevoerd tijdens ceremonies en feesten.');

-- TODO - needs to be updated with material in /uploads
INSERT INTO material (title, file_type, file_path, instrument, category, styles_id)
VALUES ('Surdo partij 2', 'VIDEO', 'Surdo partij 2.MP4', 'Surdo', 'Partij', 1),
       ('Snardrum partij', 'VIDEO', 'Snaredrum partij.MP4', 'Snaredrum', 'Partij', 1),
       ('Maracatu Arrangement', 'PDF', 'Arrangement Maracatu.pdf', 'Diversen', 'Arrangement', 1),
       ('Snaredrum partij (samba)', 'VIDEO', 'Snaredrum partij (samba).mp4', 'Snaredrum', 'Rhythm', 2),
       ('Samba Break', 'VIDEO', 'Break 1 (Samba Reggea).MOV', 'Diversen', 'Break', 2),
       ('Djembe partij 1', 'VIDEO', 'Djembe ritme 1.MP4', 'Djembe', 'Partij', 3),
       ('Djembe partij 2', 'VIDEO', 'Djembe ritme 2.MP4', 'Djembe', 'Partij', 3),
       ('Makru Arrangement', 'PDF', 'Arrangement Makru.pdf', 'Diversen', 'Arrangement', 3),
       ('Cool Djembe Video', 'LINK', 'https://www.youtube.com/watch?v=kFCaadAF6X0', 'Djembe', 'Voorbeeld', 3);

INSERT INTO lessons (scheduled_date_time, notes)
VALUES ('2025-05-20T15:00:00', 'Let goed op deze notitie!'),
       ('2025-05-27T15:00:00', 'Let ook goed op deze notitie!');

INSERT INTO lessons_styles (lessons_id, styles_id)
VALUES (1, 1),
       (1, 2),
       (2, 2),
       (2, 3);

INSERT INTO users (username, password, email)
VALUES ('admin', '$2a$12$MNoFFXXaTC2COC86QX3Axez0HOo6hTKOESSgGcMbIE/mj9I5diHn6', 'admin@test.nl'),
       ('user', '$2a$12$MNoFFXXaTC2COC86QX3Axez0HOo6hTKOESSgGcMbIE/mj9I5diHn6', 'user@test.nl');

INSERT INTO authorities (username, authority)
VALUES ('admin', 'ROLE_USER'),
       ('admin', 'ROLE_ADMIN'),
       ('user', 'ROLE_USER');