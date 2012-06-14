CREATE TABLE results_singleratinganswer
(
  singleratinganswer_id integer,
  surveyexecution_id integer,
  question_id integer,
  ratingterm_id integer,
  CONSTRAINT sra_key FOREIGN KEY (singleratinganswer_id)
      REFERENCES questionnaire_singleratinganswer (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE results_singleratinganswer OWNER TO postgres;


﻿INSERT INTO results_singleratinganswer(
	singleratinganswer_id, surveyexecution_id, question_id, ratingterm_id)
SELECT sra.id, se.id, qa.question_id, rt.id 
FROM questionnaire_singleratinganswer AS sra
LEFT JOIN questionnaire_answer AS qa ON (qa.id = sra.multipleratinganswer_id)
LEFT JOIN questionnaire_result as qr ON (qa.result_id = qr.id)
LEFT JOIN surveyexecution AS se ON(se.id = qr.surveyexecution_id)
LEFT JOIN questionnaire_ratingterm AS rt ON(rt.id = sra.ratingterm_id)

WHERE qr.executionstarted is not null AND
se.state >= 2;
