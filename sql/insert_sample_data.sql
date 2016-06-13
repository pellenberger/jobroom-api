-- insert new accesskey
insert into api_accesskey (
    id, accesskey, active, owner_email, owner_name
)
values (
    1, 'abc123', 1, 'test@example.com', 'Kevin'
);

-- insert new joboffer
insert into joboffer (
  id, version, publicationstartdate, publicationenddate,
  job_title, job_description, job_workingtimepercentagefrom, job_workingtimepercentageto, job_startdate, job_enddate,
  job_location_countrycode, job_location_locality, job_location_postalcode, job_location_additionaldetails,
  company_name, company_countrycode, company_street, company_housenumber, company_locality, company_postalcode, company_phonenumber, company_email, company_website, company_postbox_number, company_postbox_locality, company_postbox_postalcode,
  contact_title, contact_firstname, contact_lastname, contact_phonenumber, contact_email,
  application_telephonic, application_written, application_electronic, application_additionaldetails,
  owner_id,
  creationdate, lastmodificationdate
  )
values (1, 0, to_date('2016-06-01', 'yyyy-mm-dd'), to_date('2101-02-02', 'yyyy-mm-dd'),
  'Software engineer', 'Development of eGov applications', 80, 100, to_date('2000-03-01', 'yyyy-mm-dd'), to_date('2002-04-02', 'yyyy-mm-dd'),
  'CH', 'Bern', '3010', 'All day in an office',
  'SECO', 'CH', 'Finkenhubelweg', '12', 'Bern', '3001', '0581234567', 'info@seco.admin.ch', 'www.seco.admin.ch', '3002', 'Bern', '3001',
  'mister', 'Alizée', 'Dupont', '0791234567', 'jean.dupont@seco.admin.ch',
  0, 1, 1, 'Please use web form',
  1,
  current_timestamp, null
  ); 

-- add 3 language skills to new joboffer
insert into joboffer_job_languageskills(joboffer_id, language, spokenlevel, writtenlevel)
values(1, '1', 'very_good', 'good');
insert into joboffer_job_languageskills(joboffer_id, language, spokenlevel, writtenlevel)
values(1, '2', 'basic_knowledge', 'no_knowledge');
insert into joboffer_job_languageskills(joboffer_id, language, spokenlevel, writtenlevel)
values(1, '3', 'good', 'basic_knowledge');