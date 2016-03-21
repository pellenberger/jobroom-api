-- *****
-- This script creates data used in the test class that generates the documentation (ApiDocumentation.java)
-- It creates a user that has one joboffer that contains two languageskills
-- *****

insert into aoste_accesskeys (
  id, owner, accesskey, active_b
)
values (1, 'user', 'password', 1);

insert into joboffer (
  id, version, publicationstartdate, publicationenddate,
  job_title, job_description, job_workingtimepercentagefrom, job_workingtimepercentageto, job_startdate, job_enddate,
  job_location_countrycode, job_location_locality, job_location_postalcode, job_location_additionaldetails,
  company_name, company_countrycode, company_street, company_housenumber, company_locality, company_postalcode, company_phonenumber, company_email, company_website, company_postbox_number, company_postbox_locality, company_postbox_postalcode,
  contact_title, contact_firstname, contact_lastname, contact_phonenumber, contact_email,
  application_telephonic, application_written, application_electronic,
  owner_id
  )
values (1, 0, '2100-01-01', '2101-02-02',
  'Software engineer', 'Development of eGov applications', 80, 100, '2100-03-01', '2102-04-02',
  'CH', 'Bern', '3010', 'All day in an office',
  'SECO', 'CH', 'Finkenhubelweg', '12', 'Bern', '3001', '0581234567', 'info@seco.admin.ch', 'www.seco.admin.ch', '3002', 'Bern', '3001',
  'mister', 'Alizée', 'Dupont', '0791234567', 'jean.dupont@seco.admin.ch',
  0, 1, 1,
  1
  );

insert into joboffer_job_languageskills(joboffer_id, language, spokenlevel, writtenlevel)
values(1, 'de', 'very_good', 'good');
insert into joboffer_job_languageskills(joboffer_id, language, spokenlevel, writtenlevel)
values(1, 'fr', 'average', 'average');
