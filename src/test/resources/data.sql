insert into JobOffer (
  id, version, publicationstartdate, publicationenddate,
  job_title, job_description, job_workingtimepercentagefrom, job_workingtimepercentageto, job_startdate, job_enddate,
  job_location_countrycode, job_location_locality, job_location_postalcode, job_location_additionaldetails,
  company_name, company_countrycode, company_street, company_housenumber, company_locality, company_postalcode, company_phonenumber, company_email, company_website, company_postbox_number, company_postbox_locality, company_postbox_postalcode,
  contact_title, contact_firstname, contact_lastname, contact_phonenumber, contact_email,
  application_telephonic, application_written, application_electronic
  )
values (1, 0, '2000-01-01', '2001-02-02', 'Ingénieur', 'Software development', 80, 100, '2000-03-01', '2002-04-02',
  'BE', 'Bern', '3010', 'All day in an office',
  'SECO', 'CH', 'Finkenhubelweg', '12', 'Bern', '3001', '0581234567', 'info@seco.admin.ch', 'www.seco.admin.ch', '3002', 'Bern', '3001',
  'mister', 'Jean', 'Dupont', '0791234567', 'jean.dupont@seco.admin.ch',
  false, true, true);

insert into JobOffer_job_languageskills(JobOffer_id, language, spokenlevel, writtenlevel)
values(1, 'de', 'very_good', 'good');
insert into JobOffer_job_languageskills(JobOffer_id, language, spokenlevel, writtenlevel)
values(1, 'fr', 'average', 'average');
