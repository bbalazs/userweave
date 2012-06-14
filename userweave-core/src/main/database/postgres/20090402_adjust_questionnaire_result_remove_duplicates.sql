
-- remove duplicates for AnswerToSingleAnswerQuestion and FreeTextAnswer (keep the ones with min(id) (see condition ... id not in ...)
delete 
 from questionnaire_answer qa
 where result_id in (
         select result_id from questionnaire_answer qaa
         where qaa.question_id = qa.question_id
         group by result_id having count(*) > 1)
   and dtype in ('AnswerToSingleAnswerQuestion','FreeTextAnswer','FreeNumberAnswer')
   and id not in (
         select min(id) from questionnaire_answer
         group by result_id having count(*) > 1)
;


-- remove duplicates of multidimension and multirating questions if the studies are invalid or not even started
delete from questionnaire_singleratinganswer
where multipleratinganswer_id in (
  select id 
    from questionnaire_answer 
   where result_id in (select result_id from questionnaire_answer group by result_id having count(*) > 1)
     and id        not in (select min(id) from questionnaire_answer group by result_id having count(*) > 1) -- keep min(id)
--     and result_id in (select qr.id from questionnaire_result qr join surveyexecution se on qr.surveyexecution_id = se.id and se.state in (0,1))
)
;

delete from questionnaire_singledimensionanswer
where multipledimensionsanswer_id in (
  select id 
    from questionnaire_answer 
   where result_id in (select result_id from questionnaire_answer group by result_id having count(*) > 1)
     and id        not in (select min(id) from questionnaire_answer group by result_id having count(*) > 1) -- keep min(id)
--     and result_id in (select qr.id from questionnaire_result qr join surveyexecution se on qr.surveyexecution_id = se.id and se.state in (0,1))
)
;

delete from questionnaire_answer_localized_string
WHERE questionnaire_answer_id in (
  select id 
    from questionnaire_answer 
   where result_id in (select result_id from questionnaire_answer group by result_id having count(*) > 1)
     and id        not in (select min(id) from questionnaire_answer group by result_id having count(*) > 1) -- keep min(id)
--     and result_id in (select qr.id from questionnaire_result qr join surveyexecution se on qr.surveyexecution_id = se.id and se.state in (0,1))
)
;


delete 
    from questionnaire_answer qa
   where result_id in (select result_id from questionnaire_answer qaa where qa.question_id = qaa.question_id group by result_id having count(*) > 1)
     and id        not in (select min(id) from questionnaire_answer group by result_id having count(*) > 1) -- keep min(id)
--     and result_id in (select qr.id from questionnaire_result qr join surveyexecution se on qr.surveyexecution_id = se.id and se.state in (0,1))
;

