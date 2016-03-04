create table joboffer (
  id number(19) not null,
  version number(19),
  publicationstartdate date not null,
  publicationenddate date,
  job_title varchar2(255 char) not null,
  job_description varchar2(255 char) not null,
  job_workingtimepercentagefrom integer not null,
  job_workingtimepercentageto integer not null,
  job_startdate date,
  job_enddate date,
  job_location_countrycode varchar2(255 char) not null,
  job_location_locality varchar2(255 char) not null,
  job_location_postalcode varchar2(255 char) not null,
  job_location_additionaldetails varchar2(255 char),
  company_name varchar2(255 char) not null,
  company_countrycode varchar2(255 char) not null,
  company_street varchar2(255 char) not null,
  company_housenumber varchar2(255 char) not null,
  company_locality varchar2(255 char) not null,
  company_postalcode varchar2(255 char) not null,
  company_phonenumber varchar2(255 char) not null,
  company_email varchar2(255 char) not null,
  company_website varchar2(255 char) not null,
  company_postbox_number varchar2(255 char) not null,
  company_postbox_locality varchar2(255 char) not null,
  company_postbox_postalcode varchar2(255 char) not null,
  contact_title varchar2(255 char) not null,
  contact_firstname varchar2(255 char) not null,
  contact_lastname varchar2(255 char) not null,
  contact_phonenumber varchar2(255 char) not null,
  contact_email varchar2(255 char) not null,
  application_telephonic integer not null,
  application_written integer not null,
  application_electronic integer not null
);

alter table joboffer add (
  constraint joboffer_pk primary key (id));

create table joboffer_job_languageskills (
  joboffer_id number(19) not null,
  language varchar2(255 char) not null,
  spokenlevel varchar2(255 char) not null,
  writtenlevel varchar2(255 char) not null
);

alter table joboffer_job_languageskills
add constraint FK_JOBOFFER
foreign key (joboffer_id) references joboffer;