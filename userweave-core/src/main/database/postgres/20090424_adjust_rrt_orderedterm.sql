-- renamed field configurationid to result_id
alter table rrt_orderedterm drop column configurationid
;

-- create dummy surveyexecution, put wrongly mapped results to other surveyexecution
insert into surveyexecution (id,study_id) values (nextval('hibernate_sequence'),210479)
;

update questionnaire_result set surveyexecution_id = (select max(id) from surveyexecution) where id in (224476,224491)
;

update surveyexecution set study_id = 211883 where id = 211986
;

update surveyexecution set study_id = 209838 where id = 210154
;

update surveyexecution set study_id = 209838 where id = 211108
;

update surveyexecution set study_id = 210479 where id = 211155
;

update surveyexecution set study_id = 209838 where id = 211032
;

update surveyexecution set study_id = 211883 where id = 211994
;

update surveyexecution set study_id = 211883 where id = 211999
;

update surveyexecution set study_id = 212044 where id = 212344
;

update surveyexecution set study_id = 216720 where id = 217205
;

update surveyexecution set study_id = 212044 where id = 212372
;

-- create dummy surveyexecution, put wrongly mapped results to other surveyexecution
insert into surveyexecution (id,study_id) values (nextval('hibernate_sequence'),216720)
;

update questionnaire_result set surveyexecution_id = (select max(id) from surveyexecution) where id in (225590)
;

-- create dummy surveyexecution, put wrongly mapped results to other surveyexecution
insert into surveyexecution (id,study_id) values (nextval('hibernate_sequence'),212044)
;

update questionnaire_result set surveyexecution_id = (select max(id) from surveyexecution) where id in (224651)
;

-- create dummy surveyexecution, put wrongly mapped results to other surveyexecution
insert into surveyexecution (id,study_id) values (nextval('hibernate_sequence'),212044)
;

update questionnaire_result set surveyexecution_id = (select max(id) from surveyexecution) where id in (224601,224649)
;

-- forgot above :)
update surveyexecution set state = 0 where state is null
;

-- create dummy surveyexecution, put wrongly mapped results to other surveyexecution
insert into surveyexecution (id,study_id,state) values (nextval('hibernate_sequence'),212044,0)
;

update questionnaire_result set surveyexecution_id = (select max(id) from surveyexecution) where id in (224601,224649)
;


-- create dummy surveyexecution, put wrongly mapped results to other surveyexecution
insert into surveyexecution (id,state,study_id) values (nextval('hibernate_sequence'),0,211903)
;

update questionnaire_result set surveyexecution_id = (select max(id) from surveyexecution) where id in (224526)
;

update surveyexecution set study_id = 211883 where id = 211988
;

insert into surveyexecution (id,state,study_id) values (nextval('hibernate_sequence'),0,211903)
;

update questionnaire_result set surveyexecution_id = (select max(id) from surveyexecution) where id in (224528)
;

-- create dummy surveyexecution, put wrongly mapped results to other surveyexecution
insert into surveyexecution (id,state,study_id) values (nextval('hibernate_sequence'),0,211903)
;

update questionnaire_result set surveyexecution_id = (select max(id) from surveyexecution) where id in (224527)
;

update surveyexecution set study_id = 211883 where id = 211991
;

update surveyexecution set studystate = 1 where studystate is null
;
