-- ALTER TABLE!!!
update freetext_result
   set executiontime = extract(epoch from executionfinished-executionstarted)*1000
 where executiontime is null
;

update questionnaire_result
   set executiontime = extract(epoch from executionfinished-executionstarted)*1000
 where executiontime is null
;

update rrt_result
   set executiontime = extract(epoch from executionfinished-executionstarted)*1000
 where executiontime is null
;

update itm_result
   set executiontime = extract(epoch from executionfinished-executionstarted)*1000
 where executiontime is null
;
