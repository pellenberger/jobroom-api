INSERT INTO job_position (id, version, title, description, company_name, company_country_code, company_locality, company_postal_code, company_street, company_house_number, start_immediate)
VALUES (1, 0, 'John Doe', '...........hey!
more blah,...........', 'SQL Power', 'CA', 'Montreal Nord', 'QC H1G 2V1', 'Hockey Avenue', '101', 0);
INSERT INTO job_position_language_skill(job_position_id, language, spoken_level, written_level)
VALUES (1, 'fr', 1, 0);
INSERT INTO job_position_language_skill(job_position_id, language, spoken_level, written_level)
VALUES (1, 'en', 2, 2);

INSERT INTO job_position (id, version, title, description, company_name, company_country_code, company_locality, company_postal_code, company_street, company_house_number, start_immediate)
VALUES (2, 0, 'Web Developer', 'SECO TCIT', 'a
b
c
d
é', 'CH', 'Bern', '3002', 'Finkenhubelweg', '12', 1);
