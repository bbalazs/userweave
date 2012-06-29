-- temporary field to put id values from tables
alter table localized_string add column temp_id integer;

alter table itm_term add column value_id integer;

INSERT INTO localized_string (id, de,en,temp_id)
SELECT nextval('hibernate_sequence'), value, value, id FROM itm_term;

UPDATE itm_term t set value_id = (select l.id FROM localized_string l WHERE temp_id = t.id);

ALTER TABLE itm_term DROP COLUMN value;

UPDATE LOCALIZED_STRING SET temp_id = null;

ALTER TABLE localized_string DROP COLUMN temp_id;

alter table itm_icontermmapping add column term_id integer;

update itm_icontermmapping xxx set term_id = (
select min(l.id)
  from itm_icontermmapping itm
  left join itm_result r on itm.result_id = r.id
  left join itm_configuration c on r.configuration_id = c.id
  left join itm_term t on t.configurationid = c.id
  left join localized_string l on t.value_id = l.id
 where itm.term = l.de
  and itm.id = xxx.id
);

delete from itm_icontermmapping where term is not null and term_id is null;

ALTER TABLE itm_icontermmapping DROP COLUMN term;
