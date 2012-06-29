-- put answers from same surveyexecution and same configuration to 1st questionnaire_result
update questionnaire_answer qqa 
set result_id = (
select min(qr2.id)
  from questionnaire_answer qa
  join questionnaire_question qq on qa.question_id = qq.id 
  join questionnaire_result qr on qa.result_id = qr.id
  , questionnaire_result qr2
 where qq.configuration_id = qr.configuration_id
   and qr.configuration_id = qr2.configuration_id and qr.surveyexecution_id = qr2.surveyexecution_id
   and qa.id = qqa.id
group by qa.id
)
;

-- delete questionnaire_results that are now without any answer
delete 
--select count(*) 
  from questionnaire_result
 where id not in (select result_id from questionnaire_answer)
;


-- delete AnswerToSingleAnswerQuestion if answer_id is null
delete 
--select count(*)
  from questionnaire_answer
 where dtype = 'AnswerToSingleAnswerQuestion'
   and answer_id is null
;


-- delete duplicates in icon term matching
delete from itm_icontermmapping where result_id in (
select min(id) --, configuration_id, surveyexecution_id 
from itm_result group by configuration_id, surveyexecution_id having count(*) > 1)
;

-- remove itm_result that have no itm_icontermmapping
delete from itm_result where id in
(select r.id
 from itm_result r 
 left join itm_icontermmapping itm on itm.result_id = r.id
 where itm.id is null
)
;

-- delete duplicates in icon term matching
delete from itm_icontermmapping where result_id in (
select min(id) --, configuration_id, surveyexecution_id 
from itm_result group by configuration_id, surveyexecution_id having count(*) > 1)
;

-- remove itm_result that have no itm_icontermmapping
delete from itm_result where id in
(select r.id
 from itm_result r 
 left join itm_icontermmapping itm on itm.result_id = r.id
 where itm.id is null
)
;

-- delete duplicates in icon term matching
delete from itm_icontermmapping where result_id in (
select min(id) --, configuration_id, surveyexecution_id 
from itm_result group by configuration_id, surveyexecution_id having count(*) > 1)
;

-- remove itm_result that have no itm_icontermmapping
delete from itm_result where id in
(select r.id
 from itm_result r 
 left join itm_icontermmapping itm on itm.result_id = r.id
 where itm.id is null
)
;


-- rename column configurationid to result_id in rrt_orderedterm
alter table rrt_orderedterm add column result_id int4;

update rrt_orderedterm set result_id = configurationid;

alter table rrt_orderedterm drop column configurationid;





delete from rrt_orderedterm where result_id in (
select min(id)--, count(*), configuration_id, surveyexecution_id 
from rrt_result group by configuration_id, surveyexecution_id having count(*) > 1
)
;

delete from rrt_result where id not in (select result_id from rrt_orderedterm)
;


delete from rrt_orderedterm where result_id in (
select min(id)--, count(*), configuration_id, surveyexecution_id 
from rrt_result group by configuration_id, surveyexecution_id having count(*) > 1
)
;

delete from rrt_result where id not in (select result_id from rrt_orderedterm)
;


delete from rrt_orderedterm where result_id in (
select min(id)--, count(*), configuration_id, surveyexecution_id 
from rrt_result group by configuration_id, surveyexecution_id having count(*) > 1
)
;

delete from rrt_result where id not in (select result_id from rrt_orderedterm)
;
