INSERT INTO job_position (id, title, description, country_code, city, zip, start_immediate)
VALUES (1, 'John Doe', '...........hey!
more blah,...........', 'CA', 'Montreal Nord', 'QC H1G 2V1', 0);
INSERT INTO job_position_language_skill(job_position_id, language, spoken_level, written_level)
VALUES (1, 'fr', 1, 0);
INSERT INTO job_position_language_skill(job_position_id, language, spoken_level, written_level)
VALUES (1, 'en', 2, 2);

INSERT INTO job_position (id, title, description, country_code, city, zip, start_immediate)
VALUES (2, 'Web Developer', 'a
b
c
d
é', 'CH', 'Bern', '3002', 1);
