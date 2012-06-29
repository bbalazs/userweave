CREATE TABLE results_freenumberanswer
(
  freenumberanswer_id integer,
  question_id integer,
  surveyexecution_id integer,
  "number" integer
)
WITH (
  OIDS=FALSE
);
ALTER TABLE results_freenumberanswer OWNER TO postgres;

CREATE TABLE results_freetextanswer
(
  freetextanswer_id integer,
  question_id integer,
  surveyexecution_id integer,
  "text" text
)
WITH (
  OIDS=FALSE
);
ALTER TABLE results_freetextanswer OWNER TO postgres;



INSERT INTO results_freenumberanswer(freenumberanswer_id, surveyexecution_id, question_id, "number")
SELECT qa.id, se.id, qa.question_id, qa.number
FROM questionnaire_answer AS qa
LEFT JOIN questionnaire_result as qr ON (qa.result_id = qr.id)
LEFT JOIN surveyexecution AS se ON(se.id = qr.surveyexecution_id)
WHERE qa.dtype = 'FreeNumberAnswer' AND
      qr.executionstarted is not null AND
      se.state >= 2;
      
INSERT INTO results_freetextanswer(freetextanswer_id, surveyexecution_id, question_id, "text")
SELECT qa.id, se.id, qa.question_id, qa.text
FROM questionnaire_answer AS qa
LEFT JOIN questionnaire_result as qr ON (qa.result_id = qr.id)
LEFT JOIN surveyexecution AS se ON(se.id = qr.surveyexecution_id)
WHERE qa.dtype = 'FreeTextAnswer' AND
      qr.executionstarted is not null AND
      se.state >= 2;
