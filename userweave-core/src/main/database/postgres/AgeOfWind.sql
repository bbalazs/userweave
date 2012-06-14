-- markiere alle Studien als beendet, die in der 
-- Frage Demografie mindestens einen Wert beantwortet haben
update surveyexecution set executionfinished = executionstarted + interval '300 seconds'
where id in
(
select DISTINCT surveyexecution_id
  from (
select case when qa.dtype = 'MultipleAnswersAnwer'         THEN (select max(qaa.element) from questionnaire_answer_answers qaa where qaa.questionnaire_answer_id = qa.id ) 
            when qa.dtype = 'FreeNumberAnswer'             THEN '' || number
            when qa.dtype = 'AnswerToSingleAnswerQuestion' THEN answer
            ELSE 'nullaa' END as cc, 
       qa.*
  from questionnaire_answer qa
  join questionnaire_question q on qa.question_id = q.id and q.configuration_id = 216752
  join surveyexecution se on qa.surveyexecution_id = se.id and se.studystate = 1 and se.executionfinished is null
) as a 
 where cc is not null and cc != ''
)