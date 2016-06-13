-- loop that creates 1000 joboffers with each 5 language skills
-- change publicationstartdate, publicationenddate, job_startdate, job_enddate according to current date
DECLARE
   a number(2);
BEGIN
   FOR a in 1 .. 1000 LOOP
          insert into joboffer (
          id, version, publicationstartdate, publicationenddate,
          job_title, job_description, job_workingtimepercentagefrom, job_workingtimepercentageto, job_startdate, job_enddate,
          job_location_countrycode, job_location_locality, job_location_postalcode, job_location_additionaldetails,
          company_name, company_countrycode, company_street, company_housenumber, company_locality, company_postalcode, company_phonenumber, company_email, company_website, company_postbox_number, company_postbox_locality, company_postbox_postalcode,
          contact_title, contact_firstname, contact_lastname, contact_phonenumber, contact_email,
          application_telephonic, application_written, application_electronic, application_additionaldetails,
          creationdate, lastmodificationdate,
          owner_id
          )
        values (a, 0, to_date('2016-06-02', 'yyyy-mm-dd'), to_date('2101-02-02', 'yyyy-mm-dd'),
          'Software engineer', 'Development of eGov applications', 80, 100, to_date('2018-03-01', 'yyyy-mm-dd'), to_date('2022-04-02', 'yyyy-mm-dd'),
          'CH', 'Bern', '3010', 'All day in an office',
          'SECO', 'CH', 'Finkenhubelweg', '12', 'Bern', '3001', '0581234567', 'info@seco.admin.ch', 'www.seco.admin.ch', '3002', 'Bern', '3001',
          'mister', 'Alizée', 'Dupont', '0791234567', 'jean.dupont@seco.admin.ch',
          0, 1, 1, 'Please use web form',
          current_timestamp, null,
          1
          );
          
            insert into joboffer_job_languageskills(joboffer_id, language, spokenlevel, writtenlevel)
            values(a, '1', 'very_good', 'very_good');
            insert into joboffer_job_languageskills(joboffer_id, language, spokenlevel, writtenlevel)
            values(a, '2', 'no_knowledge', 'basic_knowledge');
            insert into joboffer_job_languageskills(joboffer_id, language, spokenlevel, writtenlevel)
            values(a, '3', 'good', 'good');
            insert into joboffer_job_languageskills(joboffer_id, language, spokenlevel, writtenlevel)
            values(a, '4', 'good', 'good');
            insert into joboffer_job_languageskills(joboffer_id, language, spokenlevel, writtenlevel)
            values(a, '5', 'good', 'good');
  END LOOP;
END;
/