create table joboffer (
  id bigint generated by default as identity (start with 1),
  version bigint,
  publicationstartdate date not null,
  publicationenddate date,
  job_title varchar(255) not null,
  job_description varchar(255) not null,
  job_workingtimepercentagefrom integer not null,
  job_workingtimepercentageto integer not null,
  job_startdate date,
  job_enddate date,
  job_location_countrycode varchar(255) not null,
  job_location_locality varchar(255) not null,
  job_location_postalcode varchar(255) not null,
  job_location_additionaldetails varchar(255),
  company_name varchar(255) not null,
  company_countrycode varchar(255) not null,
  company_street varchar(255) not null,
  company_housenumber varchar(255) not null,
  company_locality varchar(255) not null,
  company_postalcode varchar(255) not null,
  company_phonenumber varchar(255) not null,
  company_email varchar(255) not null,
  company_website varchar(255) not null,
  company_postbox_number varchar(255) not null,
  company_postbox_locality varchar(255) not null,
  company_postbox_postalcode varchar(255) not null,
  contact_title varchar(255) not null,
  contact_firstname varchar(255) not null,
  contact_lastname varchar(255) not null,
  contact_phonenumber varchar(255) not null,
  contact_email varchar(255) not null,
  application_telephonic integer not null,
  application_written integer not null,
  application_electronic integer not null,

  primary key (id)
);

create table joboffer_job_languageskills (
  joboffer_id bigint not null,
  language varchar(255) not null,
  spokenlevel varchar(255) not null,
  writtenlevel varchar(255) not null
);

alter table joboffer_job_languageskills
add constraint FK_JOBOFFER
foreign key (joboffer_id) references joboffer;