/*******************************************************************************
 * This file is part of UserWeave.
 *
 *     UserWeave is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     UserWeave is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with UserWeave.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2012 User Prompt GmbH | Psychologic IT Expertise
 *******************************************************************************/
package com.userweave.module.methoden.questionnaire.dao;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.userweave.dao.SurveyExecutionDependentQuery;
import com.userweave.dao.filter.FilterFunctor;
import com.userweave.dao.impl.BaseDaoImpl;
import com.userweave.domain.SurveyExecution;
import com.userweave.module.methoden.questionnaire.domain.answer.Answer;
import com.userweave.module.methoden.questionnaire.domain.answer.AnswerToSingleAnswerQuestion;
import com.userweave.module.methoden.questionnaire.domain.answer.FreeAnwer;
import com.userweave.module.methoden.questionnaire.domain.answer.FreeNumberAnswer;
import com.userweave.module.methoden.questionnaire.domain.answer.FreeTextAnswer;
import com.userweave.module.methoden.questionnaire.domain.answer.MultipleAnswersAnwer;
import com.userweave.module.methoden.questionnaire.domain.answer.SingleDimensionAnswer;
import com.userweave.module.methoden.questionnaire.domain.answer.SingleRatingAnswer;
import com.userweave.module.methoden.questionnaire.domain.question.DimensionsQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.FreeQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.MultipleAnswersQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.MultipleRatingQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.Question;
import com.userweave.module.methoden.questionnaire.domain.question.SingleAnswerQuestion;

import de.userprompt.utils_userweave.query.model.Join;
import de.userprompt.utils_userweave.query.model.PropertyCondition;
import de.userprompt.utils_userweave.query.model.QueryEntity;
import de.userprompt.utils_userweave.query.model.QueryObject;
import de.userprompt.utils_userweave.query.template.QueryTemplate;

@Repository
@Transactional
public class AnswerDaoImpl extends BaseDaoImpl<Answer> implements AnswerDao {
	
	@Override
	public Class<Answer> getPersistentClass() {
		return Answer.class;
	}

	/**
	 * Creates the query object to fetch answers.
	 * 
	 * @param question
	 * 		Question object to laod answers for.
	 * @param filterFunctor
	 * 		Filters to apply on query.
	 * @return
	 * 		A survey dependend query object.
	 */
	@Override
	public QueryObject getQueryObject(Question question, FilterFunctor filterFunctor)
	{
		SurveyExecutionDependentQuery query = 
			new SurveyExecutionDependentQuery("se", "id");		
		
		QueryEntity result = new QueryEntity(getEntityName(), "a");
		query.setDistinctResult(result);
		query.addQueryEntity(result);
		
		query.addLeftJoin(new Join("a.questionnaireResult", "result"));
		query.addLeftJoin(new Join("result.surveyExecution", "se"));
		
		query.addAndCondition(PropertyCondition.equals("a.question", question));
		
		// only count started or completed surveys
		//query.addAndCondition(PropertyCondition.in("se.state", Arrays.asList(SurveyExecutionState.STARTED,SurveyExecutionState.COMPLETED)));
		// only count them if they are started?
		query.addAndCondition(PropertyCondition.isNotNull("result.executionStarted"));
		
		if (filterFunctor != null) {
			filterFunctor.apply(query);
		}
		
		return query;
	}
	
	/**
	 * Creates the query object to fetch results for questions.
	 * 
	 * @param question
	 * 		Question to fetch answers for.
	 * @param filterFunctor
	 * 		Filter object to filter results with.
	 * @param srcTableName
	 * 		Name of the table to get results from.
	 * @param joinTableName
	 * 		Name of the table to join srcTbl with for results.
	 * 
	 * @return
	 * 		A Query object containing the sql query for results.
	 * 		Resulting query (without applied filters):
	 * 
	 * 		FROM questionnaire_&lt;srcTbl&gt; qa
	 * 		LEFT JOIN results_&lt;joinTbl&gt; r_qa ON qa.id = r_qa.&lt;srcTbl&gt;_id
	 *		WHERE r_qa.question_id = :questionId;
	 *
	 * @important Result must be set in the calling method!
	 */
	private QueryObject getQueryObject(
		Question question, FilterFunctor filterFunctor, 
		String srcTableName, String joinTableName)
	{
		SurveyExecutionDependentQuery queryObject = 
			new SurveyExecutionDependentQuery("r_qa", "surveyexecution_id");

		queryObject.addQueryEntity(
			new QueryEntity("questionnaire_" + srcTableName , "qa"));
		
		queryObject.addLeftJoin(
			new Join("results_" + joinTableName, "r_qa",
					 "qa.id", "r_qa." + joinTableName + "_id"));
		
		queryObject.addAndCondition(
			PropertyCondition.equals("r_qa.question_id", question.getId()));
		
		
		if (filterFunctor != null) 
		{
			filterFunctor.apply(queryObject);
		}
		
		return queryObject;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AnswerToSingleAnswerQuestion> getValidAnswersForQuestion(
			SingleAnswerQuestion question, FilterFunctor filterFunctor) 
	{
		QueryObject queryObject = 
			getQueryObject(question, filterFunctor, "answer", "singleanswer");
		
		queryObject.setResult("{qa.*}");
		
		if(queryObject.getHasGroupBy())
		{
			queryObject.setGroupBy("qa.id, qa.question_id, qa.result_id, qa.answer_id");
		}
		
		SQLQuery q = new QueryTemplate(queryObject).createSqlQuery(getCurrentSession());
		
		q.addEntity("qa", AnswerToSingleAnswerQuestion.class);
		
		return q.list();		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MultipleAnswersAnwer> getValidAnswersForQuestion(
			MultipleAnswersQuestion question, FilterFunctor filterFunctor) 
	{
		QueryObject queryObject = 
			getQueryObject(question, filterFunctor, "answer", "multipleanswer");
		
		queryObject.setResult("{qa.*}");
		
		if(queryObject.getHasGroupBy())
		{
			queryObject.setGroupBy("qa.id, qa.question_id, qa.result_id");
		}
		
		SQLQuery q = new QueryTemplate(queryObject).createSqlQuery(getCurrentSession());
		
		q.addEntity("qa", MultipleAnswersAnwer.class);
		
		return q.list();		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FreeAnwer> getValidAnswersForQuestion(
		FreeQuestion question, FilterFunctor filterFunctor) 
	{
		QueryObject queryObject;
		
		SQLQuery q;
		
		if(question.getAnswerType().equals(FreeQuestion.AnswerType.NUMBER))
		{
			queryObject = 
				getQueryObject(question, filterFunctor, "answer", "freenumberanswer");
		
			queryObject.setResult("{qa.*}");
			
			if(queryObject.getHasGroupBy())
			{
				queryObject.setGroupBy("qa.id, qa.question_id, qa.result_id, qa.number");
			}
			
			q = new QueryTemplate(queryObject).createSqlQuery(getCurrentSession());
			
			q.addEntity("qa", FreeNumberAnswer.class);
		}
		else
		{
			queryObject = 
				getQueryObject(question, filterFunctor, "answer", "freetextanswer");
		
			queryObject.setResult("{qa.*}");
			
			if(queryObject.getHasGroupBy())
			{
				queryObject.setGroupBy("qa.id, qa.question_id, qa.result_id, qa.TEXT");
			}
			
			q = new QueryTemplate(queryObject).createSqlQuery(getCurrentSession());
			
			q.addEntity("qa", FreeTextAnswer.class);
		}
		
		return q.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getValidAnswersForQuestion(
		MultipleRatingQuestion question, FilterFunctor filterFunctor) 
	{	
		QueryObject queryObject = 
			getQueryObject(
				question, filterFunctor, "singleratinganswer", "singleratinganswer");
		
		queryObject.setResult("{qa.*}, r_qa.ratingterm_id");
		
		if(queryObject.getHasGroupBy())
		{
			queryObject.setGroupBy(
				"qa.id, " +
				"qa.multipleRatingAnswer_id, " +
				"qa.rating, " +
				"qa.ratingTerm_id, " +
				"r_qa.ratingterm_id");
		}
		
		SQLQuery q = new QueryTemplate(queryObject).createSqlQuery(getCurrentSession());
		
		q.addEntity("qa", SingleRatingAnswer.class);
		q.addScalar("ratingterm_id", Hibernate.INTEGER);
		
		return q.list();
	}

	@Override
	public Long getGroupedValidAnswersForMultipleRatingQuestion(
			MultipleRatingQuestion question, FilterFunctor filterFunctor)
	{
		QueryObject query = 
			getQueryObject(
				question, filterFunctor, "singleratinganswer", "singleratinganswer");
		
		query.setResult("count(distinct r_qa.surveyexecution_id)");
		
		SQLQuery q = new QueryTemplate(query).createSqlQuery(getCurrentSession());
		
		if(query.getHasGroupBy())
		{
			return ((Integer)q.list().size()).longValue();
		}
		
		return ((BigInteger)q.uniqueResult()).longValue();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SingleDimensionAnswer> getValidAnswersForQuestion(
		DimensionsQuestion question, FilterFunctor filterFunctor) 
	{
		QueryObject query = 
			getQueryObject(
				question, filterFunctor, "singledimensionanswer", "singledimensionanswer");
		
		query.setResult("{qa.*}");
		
		if(query.getHasGroupBy())
		{
			query.setGroupBy(
				"qa.id, " +
				"qa.antipodePair_id, " +
				"qa.multipleDimensionsAnswer_id, " +
				"qa.rating");
		}
		
		SQLQuery q = new QueryTemplate(query).createSqlQuery(getCurrentSession());
		
		q.addEntity("qa", SingleDimensionAnswer.class);

		return q.list();
	}

	@Override
	public Long getGroupedValidAnswersForDimensionQuestion(
			DimensionsQuestion question, FilterFunctor filterFunctor)
	{
		QueryObject query = 
			getQueryObject(
				question, filterFunctor, "singledimensionanswer", "singledimensionanswer");
		
		query.setResult("count(distinct r_qa.surveyexecution_id)");
		
		SQLQuery q = new QueryTemplate(query).createSqlQuery(getCurrentSession());
		
		if(query.getHasGroupBy())
		{
			return ((Integer)q.list().size()).longValue();
		}
		
		return ((BigInteger)q.uniqueResult()).longValue();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Answer<? extends Question> findByQuestionAndSurveyExecution(Question question, SurveyExecution surveyExecution) {
		String query = "select a from "+getEntityName()+ " a left join a.questionnaireResult r"+
			" where a.question = :question and r.surveyExecution = :surveyexecution";
		return (Answer) getCurrentSession().createQuery(query)
			.setParameter("question", question)
			.setParameter("surveyexecution", surveyExecution).
			uniqueResult();
	}

}
