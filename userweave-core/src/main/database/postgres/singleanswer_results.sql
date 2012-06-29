CREATE TABLE results_singleanswer
(
  singleanswer_id integer,
  question_id integer,
  surveyexecution_id integer,
  answer_id integer
)
WITH (
  OIDS=FALSE
);
ALTER TABLE results_singleanswer OWNER TO postgres;



﻿INSERT INTO results_singleanswer(singleanswer_id, surveyexecution_id, question_id, answer_id)
SELECT qa.id, se.id, qa.question_id, qa.answer_id
FROM questionnaire_answer AS qa
LEFT JOIN questionnaire_result as qr ON (qa.result_id = qr.id)
LEFT JOIN surveyexecution AS se ON(se.id = qr.surveyexecution_id)
WHERE qa.dtype = 'AnswerToSingleAnswerQuestion' AND
      qr.executionstarted is not null AND
      se.state >= 2;
