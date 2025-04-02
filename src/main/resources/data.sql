INSERT INTO styles (name, origin, description)
VALUES ('Maracatu', 'Brazil', 'Traditional Brazilian rhythm with powerful percussion and dancing.'),
       ('Samba', 'Brazil', 'Popular Brazilian music genre characterized by lively rhythms and percussion.'),
       ('Makru', 'Africa', 'Traditional West African rhythm, commonly played on djembe and dunun drums.');

INSERT INTO material (title, file_type, file_path, instrument, category, styles_id)
VALUES ('Breakdown of Maracatu', 'VIDEO', 'path/to/maracatu_breakdown.mp4', 'Surdo', 'Rhythm', 1),
       ('Maracatu Arrangement', 'PDF', 'path/to/maracatu_arrangement.pdf', 'Snare Drum', 'Sheet Music', 1),
       ('Surdo Rhythm for Samba', 'VIDEO', 'path/to/samba_surdo_rhythm.mp4', 'Surdo', 'Rhythm', 2),
       ('Samba Break', 'VIDEO', 'path/to/samba_break.mp4', 'Snare Drum', 'Rhythm', 2),
       ('Makru Drum Rhythms', 'VIDEO', 'path/to/makru_drum_rhythms.mp4', 'Djembe', 'Rhythm', 3),
       ('Cool Djembe Video', 'LINK', 'https://www.youtube.com/watch?v=cooldjembe', 'Djembe', 'Video', 3);

INSERT INTO lessons (scheduled_date_time, notes)
VALUES ('2025-03-20T15:00:00', 'Let goed op deze notitie!');

INSERT INTO lessons_styles (lessons_id, styles_id)
VALUES (1, 1), (1, 2);
