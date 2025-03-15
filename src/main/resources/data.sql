-- Inserting some styles
INSERT INTO styles (name, origin, description) VALUES
                                                   ('Maracatu', 'Brazil', 'Traditional Brazilian rhythm with powerful percussion and dancing.'),
                                                   ('Samba', 'Brazil', 'Popular Brazilian music genre characterized by lively rhythms and percussion.'),
                                                   ('Makru', 'Africa', 'Traditional West African rhythm, commonly played on djembe and dunun drums.');

-- Inserting some materials (videos, PDFs, links) related to styles
INSERT INTO material (title, file_type, file_path, link, instrument, category, styles_id) VALUES
                                                                                                          ('Breakdown of Maracatu', 'VIDEO', 'path/to/maracatu_breakdown.mp4', NULL, 'Surdo', 'Rhythm', 1),
                                                                                                          ('Maracatu Arrangement', 'PDF', 'path/to/maracatu_arrangement.pdf', NULL, 'Snare Drum', 'Sheet Music', 1),
                                                                                                          ('Surdo Rhythm for Samba', 'VIDEO', 'path/to/samba_surdo_rhythm.mp4', NULL, 'Surdo', 'Rhythm', 2),
                                                                                                          ('Samba Break', 'VIDEO', 'path/to/samba_break.mp4', NULL, 'Snare Drum', 'Rhythm', 2),
                                                                                                          ('Makru Drum Rhythms', 'VIDEO', 'path/to/makru_drum_rhythms.mp4', NULL, 'Djembe', 'Rhythm', 3),
                                                                                                          ('Cool Djembe Video', 'LINK', NULL, 'https://www.youtube.com/watch?v=cooldjembe', 'Djembe', 'Video', 3);
