-- delete results in icon term matching mapping
delete from itm_icontermmapping where id in (
select distinct itm.id
  from study s,
       surveyexecution se,
       itm_result ir,
       itm_icontermmapping itm
  where s.id = se.study_id
   and se.id = ir.surveyexecution_id 
   and ir.id = itm.result_id
   and s.name = 'PräsentationsStudie für Kunden'
)
;
-- delete results in icon term matching
delete from itm_result where id in (
select distinct ir.id
  from study s,
       surveyexecution se,
       itm_result ir
  where s.id = se.study_id
   and se.id = ir.surveyexecution_id 
   and s.name = 'PräsentationsStudie für Kunden'
)
;
-- delete from rrt_result mapping ! ATTENTION result.id is stored in rrt_orderedterm.configurationid!!!!
delete from rrt_orderedterm where id in (
select distinct rrt.id
  from study s,
       surveyexecution se,
       rrt_result rr,
       rrt_orderedterm rrt
  where s.id = se.study_id
   and se.id = rr.surveyexecution_id 
   and rrt.configurationid = rr.id -- FIXME: THIS IS UGLy, BAD AND WRONG!!!
   and s.name = 'PräsentationsStudie für Kunden'
)
;
-- delete from rrt_result
delete from rrt_result where id in (
select distinct rr.id
  from study s,
       surveyexecution se,
       rrt_result rr
  where s.id = se.study_id
   and se.id = rr.surveyexecution_id 
   and s.name = 'PräsentationsStudie für Kunden'
)
;
-- delete the survey executions
delete from surveyexecution where id in (
select distinct se.id
  from study s,
       surveyexecution se
  where s.id = se.study_id
   and s.name = 'PräsentationsStudie für Kunden'
)
;
