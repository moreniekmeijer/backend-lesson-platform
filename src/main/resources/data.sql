INSERT INTO styles (name, origin, description)
VALUES ('Maracatu', 'Brazil', 'Traditional Brazilian rhythm with powerful percussion and dancing.'),
       ('Samba', 'Brazil', 'Popular Brazilian music genre characterized by lively rhythms and percussion.'),
       ('Makru', 'Africa', 'Traditional West African rhythm, commonly played on djembe and dunun drums.');

-- TODO - needs to be updated with material in /uploads
INSERT INTO material (title, file_type, file_path, instrument, category, styles_id)
VALUES ('Surdo partij 2', 'VIDEO', 'Surdo partij 2.MP4', 'Surdo', 'Partij', 1),
       ('Snardrum partij', 'VIDEO', 'Snaredrum partij.MP4', 'Snaredrum', 'Partij', 1),
       ('Maracatu Arrangement', 'PDF', 'Arrangement Maracatu.pdf', 'Diversen', 'Arrangement', 1),
       ('Snaredrum partij (samba)', 'VIDEO', 'Snaredrum partij (samba).mp4', 'Surdo', 'Rhythm', 2),
       ('Samba Break', 'VIDEO', 'Break 1 (Samba Reggea).MOV', 'Diversen', 'Break', 2),
       ('Djembe partij 1', 'VIDEO', 'Djembe ritme 1.MP4', 'Djembe', 'Partij', 3),
       ('Djembe partij 2', 'VIDEO', 'Djembe ritme 2.MP4', 'Djembe', 'Partij', 3),
       ('Makru Arrangement', 'PDF', 'Arrangement Makru.pdf', 'Diversen', 'Arrangement', 3),
       ('Cool Djembe Video', 'LINK', 'https://www.youtube.com/watch?v=kFCaadAF6X0', 'Djembe', 'Voorbeeld', 3);

INSERT INTO lessons (scheduled_date_time, notes)
VALUES ('2025-05-20T15:00:00', 'Let goed op deze notitie!');

INSERT INTO lessons_styles (lessons_id, styles_id)
VALUES (1, 1),
       (1, 2);

INSERT INTO users (username, password, email)
VALUES ('admin', '$2a$12$MNoFFXXaTC2COC86QX3Axez0HOo6hTKOESSgGcMbIE/mj9I5diHn6', 'admin@test.nl'),
       ('user', '$2a$12$MNoFFXXaTC2COC86QX3Axez0HOo6hTKOESSgGcMbIE/mj9I5diHn6', 'user@test.nl');

INSERT INTO authorities (username, authority)
VALUES ('admin', 'ROLE_USER'),
       ('admin', 'ROLE_ADMIN'),
       ('user', 'ROLE_USER');