-- varchar2 lengths are the same as in table AOSTE, if existing
-- for other fields, length is by default 255 char 
-- for the moment, we don't care about BYTE/CHAR compatibility
-- in comments are the aoste related fields
create table joboffer (
  id integer not null, -- stellennummeregov
  version integer,
  publicationstartdate date not null,
  publicationenddate date,
  job_title varchar2(255 char) not null, -- bezeichnung
  job_description nclob not null, -- beschreibung
  job_workingtimepercentagefrom integer not null, -- pensum_von
  job_workingtimepercentageto integer not null, -- pensum_bis
  job_startdate date, -- ?
  job_enddate date, -- ?
  job_location_countrycode char(2 char) not null, -- arbeitsort_land
  job_location_locality varchar2(50 char) not null, -- arbeitsort_ort
  job_location_postalcode varchar2(50 char) not null, -- arbeitsort_plz
  job_location_additionaldetails varchar2(50 char), -- arbeitsort_text
  company_name varchar2(255 char) not null, -- unt_name
  company_countrycode varchar2(100 char) not null, -- unt_land
  company_street varchar2(60 char) not null, -- unt_strasse
  company_housenumber varchar2(50 char) not null, -- unt_haus_nr
  company_locality varchar2(100 char) not null, -- unt_ort
  company_postalcode varchar2(10 char) not null, -- unt_plz
  company_phonenumber varchar2(50 char) not null, -- unt_telefon
  company_email varchar2(50 char) not null, -- unt_email
  company_website varchar2(255 char) not null, -- unt_url
  company_postbox_number varchar2(6 char) not null, -- unt_postfach
  company_postbox_locality varchar2(100 char) not null, -- unt_postfach_ort
  company_postbox_postalcode varchar2(10 char) not null, -- unt_postfach_plz
  contact_title varchar2(255 char) not null,
  contact_firstname varchar2(255 char) not null, -- kp_vorname
  contact_lastname varchar2(255 char) not null, -- kp_name
  contact_phonenumber varchar2(50 char) not null, -- kp_telefon_nr
  contact_email varchar2(50 char) not null, -- kp_email
  application_telephonic integer not null, -- bewer_telefonisch_b
  application_written integer not null, -- bewer_schriftlich_b
  application_electronic integer not null, -- bewer_elektronisch_b
  owner_id integer not null
);

alter table joboffer add (
constraint joboffer_pk primary key (id)
);

alter table joboffer
add constraint fk_owner
foreign key (owner_id) references aoste_accesskeys;

alter table joboffer add (
constraint application_telephonic_ck check (application_telephonic in (0, 1)),
constraint application_written_ck check (application_written in (0, 1)),
constraint application_electronic_ck check (application_electronic in (0, 1)),
constraint contact_title_ck check (contact_title in ('mister', 'madam')),
constraint job_startenddate_ck check (job_startdate < job_enddate),
constraint workingtimepercentage_ck check (job_workingtimepercentagefrom <= job_workingtimepercentageto),
constraint workingtimepercentagefrom_ck check (job_workingtimepercentagefrom > 0),
constraint workingtimepercentageto_ck check (job_workingtimepercentageto <= 100),
constraint publication_date_ck check (publicationstartdate < publicationenddate)
);

create table joboffer_job_languageskills (
  joboffer_id integer not null,
  language varchar2(10 char) not null, -- skx_sprache_code
  spokenlevel varchar2(255 char) not null,
  writtenlevel varchar2(255 char) not null
);

alter table joboffer_job_languageskills
add constraint fk_joboffer
foreign key (joboffer_id) references joboffer;

alter table joboffer_job_languageskills add (
constraint languageskills_spokenlevel_ck check (spokenlevel in ('average', 'good', 'very_good')),
constraint languageskills_writtenlevel_ck check (writtenlevel in ('average', 'good', 'very_good'))
);
