UPDATE surveyexecution 
SET locale = 
	(SELECT locale FROM study WHERE id = surveyexecution.study_id)
WHERE locale is null