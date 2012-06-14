alter table itm_term add column configuration_id int4
;

update itm_term set configuration_id = configurationid
;

alter table itm_term drop column configurationid
;
