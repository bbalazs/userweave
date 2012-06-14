-- must be empty
select se.study_id, c.studyid, se.id as surveyexecution_id, r.id as result_id
  from surveyexecution se
  left join questionnaire_result r on se.id = r.surveyexecution_id
  left join questionnaire_configuration c on r.configuration_id = c.id
 where se.study_id != c.studyid
order by surveyexecution_id
;

-- must be empty
select se.study_id, c.studyid, r.*, c.*
  from surveyexecution se
  left join itm_result r on se.id = r.surveyexecution_id
  left join itm_configuration c on r.configuration_id = c.id
 where se.study_id != c.studyid
;

-- must be empty
select se.study_id, c.studyid, r.*, c.*
  from surveyexecution se
  left join rrt_result r on se.id = r.surveyexecution_id
  left join rrt_configuration c on r.configuration_id = c.id
 where se.study_id != c.studyid
; 
 
 
 
-- show all results for a given surveyexecution
select *
  from surveyexecution se
  left join 
(
select r.*,'Q' as t from questionnaire_result r
union
select r.*,'I' as t from itm_result r
union
select r.*,'R' as t from rrt_result r
) a on a.surveyexecution_id = se.id
  left join 
(select id,position,studyid from questionnaire_configuration 
union
select id,position,studyid from itm_configuration 
union
select id,position,studyid from rrt_configuration 
) b  on a.configuration_id = b.id
where se.id in (211991)
;