CREATE TABLE results_singledimensionanswer
(
  singledimensionanswer_id integer,
  surveyexecution_id integer,
  question_id integer,
  antipodepair_id integer
)
WITH (
  OIDS=FALSE
);
ALTER TABLE results_singledimensionanswer OWNER TO postgres;


INSERT INTO results_singledimensionanswer(
	singledimensionanswer_id, surveyexecution_id, question_id, antipodepair_id)
SELECT sda.id, se.id, qa.question_id, app.id 
FROM questionnaire_singledimensionanswer AS sda
LEFT JOIN questionnaire_answer AS qa ON (qa.id = sda.multipledimensionsanswer_id)
LEFT JOIN questionnaire_result as qr ON (qa.result_id = qr.id)
LEFT JOIN surveyexecution AS se ON(se.id = qr.surveyexecution_id)
LEFT JOIN antipodepair AS app ON(app.id = sda.antipodepair_id)

WHERE qr.executionstarted is not null AND
se.state >= 2;
