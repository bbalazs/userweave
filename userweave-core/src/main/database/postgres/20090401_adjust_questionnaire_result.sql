alter table questionnaire_result add column question_id integer;

delete from questionnaire_result;

insert into questionnaire_result
select nextval('hibernate_sequence'),q.configuration_id, q.surveyexecution_id, q.question_id
from (
select distinct q.configuration_id, qa.surveyexecution_id, qa.question_id
 from questionnaire_answer qa
  join questionnaire_question q on qa.question_id = q.id
 where qa.surveyexecution_id not in (select surveyexecution_id from questionnaire_result)
) q
;

update questionnaire_answer qa 
   set result_id = (select id from questionnaire_result qr where qr.surveyexecution_id = qa.surveyexecution_id and qr.question_id = qa.question_id),
       surveyexecution_id = null
 where surveyexecution_id is not null
;

alter table questionnaire_answer drop column surveyexecution_id;

alter table questionnaire_result drop column question_id;
