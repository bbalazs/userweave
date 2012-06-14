update freetext_result r
   set executionstarted = (select executionstarted from surveyexecution where id = r.surveyexecution_id)
 where executionstarted is null
;

update questionnaire_result r
   set executionstarted = (select executionstarted from surveyexecution where id = r.surveyexecution_id)
 where executionstarted is null
;

update rrt_result r
   set executionstarted = (select executionstarted from surveyexecution where id = r.surveyexecution_id)
 where executionstarted is null
;

update itm_result r
   set executionstarted = (select executionstarted from surveyexecution where id = r.surveyexecution_id)
 where executionstarted is null
;

update freetext_result
  set executionfinished = executionstarted
where executionfinished is null
;

update questionnaire_result
  set executionfinished = executionstarted
where executionfinished is null
;

update rrt_result
  set executionfinished = executionstarted
where executionfinished is null
;

update itm_result r
  set executionfinished = executionstarted + (select sum(executiontime) from itm_icontermmapping  where result_id = r.id)*interval '1 millisecond'
where executionfinished is null
;
