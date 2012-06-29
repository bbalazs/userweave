group query;

root(query) ::= <<
SELECT <query.result> 
  FROM <query.queryEntities:entity(); separator=",
       ">
<query.leftJoins:leftJoin();separator=" "> 
<query.leftJoinFetches:leftJoinFetch();separator=" ">  
<if(query.hasConditions)>
 WHERE <query.andConditions:condition()><endif>
<if(query.hasOrderBy)>
 ORDER BY <query.orderBy><endif>
<if(query.hasGroupBy)>
 GROUP BY <query.groupBy><endif>
<if(query.hasHaving)>
 HAVING <query.having><endif>
>>

entity(entity) ::= "<entity.name> <entity.instance>"

leftJoin(join) ::= "left join <join.path> <join.alias> <if(join.hasOn)> on <join.onConditionFirstParameter> = <join.onConditionSecondParameter><endif>"

leftJoinFetch(join) ::= "left join fetch <join.path> <join.alias>"

// dispatch to correct template by condition type 
condition(condition) ::= <<
(<condition:("condition_" + condition.type)()>)
>>

// recurselively call condition-template on sub-conditions
condition_and(condition) ::= "<condition.conditions:condition(); separator=\" and \">"
condition_or(condition) ::= "<condition.conditions:condition(); separator=\" or \">"

// template for single propery condition
condition_property(condition) ::= "<condition.property> <condition.operator> <condition.value>"
condition_object(condition) ::= "<condition.objectPath1> <condition.operator> <condition.objectPath2>"



