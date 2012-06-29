-- set study state to running for old surveyexecutions
update surveyexecution set studystate = 1 where studystate is null;

-- attach answers to best known surveyexecution
update questionnaire_answer qa set surveyexecution_id = (select max(id) from surveyexecution se where se.id < qa.id)
where surveyexecution_id is null;

-- drop unused table
drop table questionnaire_simpleanwer;

--delete from questionnaire_answer where question_id in (
--select id from questionnaire_question where configuration_id in 
--(select id from questionnaire_configuration where studyid in (select id from study where name like 'AoW%')))
;
