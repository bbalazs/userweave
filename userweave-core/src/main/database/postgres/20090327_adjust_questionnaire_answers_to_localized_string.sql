-- Update Userweave Produktion: 

-- mvn exec:java -Dexec.mainClass=com.userweave.application.setup.Setup
-- psql -h localhost -U postgres -w < src/main/database/postgres/20090327_adjust_questionnaire_answers_to_localized_string.sql

-- correct questionnaire_answers, use german translation
update questionnaire_answer set answer = regexp_replace(answer, ':DE.*:EN$','') where answer like '%:EN';


-- correct questionnaire_answers, use german translation
update questionnaire_answer qa set answer = (
select ls.de
  from questionnaire_question_localized_string qqls 
  join localized_string ls on qqls.possibleanswers_id = ls.id
 where qa.question_id = qqls.questionnaire_question_id
   and qa.answer != ls.de
   and (qa.answer = ls.en or qa.answer = ls.es or qa.answer =ls.fr or qa.answer = ls.pl)
)
where id in
(
select qa.id
  from questionnaire_question_localized_string qqls 
  join questionnaire_answer qa on qqls.questionnaire_question_id = qa.question_id
  join localized_string ls on qqls.possibleanswers_id = ls.id
 where 1=1 
   and qa.answer != ls.de
   and (qa.answer = ls.en or qa.answer = ls.es or qa.answer =ls.fr or qa.answer = ls.pl)
)
;

-- correct questionnaire_answer_answers, use german translation
update questionnaire_answer_answers qqaa set element = (
select ls.de
  from questionnaire_question_localized_string qqls 
  join questionnaire_answer qa on qqls.questionnaire_question_id = qa.question_id
  join questionnaire_answer_answers qaa on qa.id = qaa.questionnaire_answer_id
  join localized_string ls on qqls.possibleanswers_id = ls.id
 where qa.question_id = qqls.questionnaire_question_id
   and qa.answer != ls.de
   and (qaa.element = ls.en or qaa.element = ls.es or qaa.element =ls.fr or qaa.element = ls.pl)
   and qqaa.element = qaa.element
)
where element in
(
select qaa.element--, ls.*
  from questionnaire_question_localized_string qqls 
  join questionnaire_answer qa on qqls.questionnaire_question_id = qa.question_id
  join questionnaire_answer_answers qaa on qa.id = qaa.questionnaire_answer_id
  join localized_string ls on qqls.possibleanswers_id = ls.id
 where qa.question_id = qqls.questionnaire_question_id
   and qaa.element != ls.de
   and (qaa.element = ls.en or qaa.element = ls.es or qaa.element =ls.fr or qaa.element = ls.pl)
)
;

delete from questionnaire_answer WHERE id in (211964,211995);

-- create (or set) answer_id in tables
update questionnaire_answer qa
   set answer_id = (select id from localized_string ls where ls.de = answer and ls.id in (select possibleanswers_id from questionnaire_question_localized_string where questionnaire_question_id = qa.question_id)), 
       answer = null
 where answer is not null
;

insert into questionnaire_answer_localized_string
select questionnaire_answer_id,id 
  from questionnaire_answer_answers qaa 
  left join localized_string ls on (en = element or de = element)
 where ls.id is not null;
;

-- Korrektur modulesexecuted
update surveyexecution SET modulesexecuted = 0;

create table xxx (surveyexecution_id integer, position integer);

insert into xxx
select r.surveyexecution_id, c.position
  from rrt_result r
  join surveyexecution se on r.surveyexecution_id = se.id and se.executionstarted is not null
  join rrt_configuration c on r.configuration_id = c.id
;

insert into xxx
select a.surveyexecution_id, a.position
--, cc
from (
select case when qa.dtype = 'MultipleAnswersAnwer'         THEN 'MA_' || (select count(*) from questionnaire_answer_localized_string qaa where qaa.questionnaire_answer_id = qa.id ) 
            when qa.dtype = 'FreeNumberAnswer'             THEN 'FN_' || (case when number is null then '' else number||'' end )
            when qa.dtype = 'FreeTextAnswer'               THEN 'TF_' || (case when text is null then '' else text||'' end )
            when qa.dtype = 'AnswerToSingleAnswerQuestion' THEN 'SA_' || (select de from localized_string s where id = qa.answer_id)
            when qa.dtype = 'MultipleDimensionsAnswer'     THEN 'MD_' || (select count(*) from questionnaire_singledimensionanswer qsa where qsa.multipledimensionsanswer_id = qa.id ) 
            when qa.dtype = 'MultipleRatingAnswer'         THEN 'MR_' || (select count(*) from questionnaire_singleratinganswer qaa where qaa.multipleratinganswer_id = qa.id ) 
            ELSE 'XXXX'||qa.dtype END as cc, 
       qa.*, qc.*
  from questionnaire_answer qa
  join questionnaire_question q on qa.question_id = q.id
  join questionnaire_configuration qc on q.configuration_id = qc.id
  join surveyexecution se on qa.surveyexecution_id = se.id and se.executionstarted is not null
) as a 
 where cc not in ('MR_0', 'MD_0', 'MA_0','TF_')
;

insert into xxx
select r.surveyexecution_id, c.position
  from itm_result r
  join surveyexecution se on r.surveyexecution_id = se.id and se.executionstarted is not null
  join itm_configuration c on r.configuration_id = c.id
 --where (select count(*) from itm_icontermmapping itm where itm.result_id = r.id) > 0
;

create table yyy as 
select surveyexecution_id, max(position) as position
from xxx group by surveyexecution_id
;

create index yyyidx on yyy (surveyexecution_id);

update surveyexecution se set modulesexecuted = (select position from yyy where yyy.surveyexecution_id = se.id)
where se.id in (select surveyexecution_id from yyy)
;

drop table yyy;

drop table xxx;

-- TODO: ALTE DATEN LÖSCHEN
alter table questionnaire_answer drop column answer;
drop table questionnaire_answer_answers;
