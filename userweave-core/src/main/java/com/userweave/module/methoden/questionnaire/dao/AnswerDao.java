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

import java.util.List;

import com.userweave.dao.BaseDao;
import com.userweave.dao.filter.FilterFunctor;
import com.userweave.domain.SurveyExecution;
import com.userweave.module.methoden.questionnaire.domain.answer.Answer;
import com.userweave.module.methoden.questionnaire.domain.answer.AnswerToSingleAnswerQuestion;
import com.userweave.module.methoden.questionnaire.domain.answer.FreeAnwer;
import com.userweave.module.methoden.questionnaire.domain.answer.MultipleAnswersAnwer;
import com.userweave.module.methoden.questionnaire.domain.answer.SingleDimensionAnswer;
import com.userweave.module.methoden.questionnaire.domain.question.DimensionsQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.FreeQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.MultipleAnswersQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.MultipleRatingQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.Question;
import com.userweave.module.methoden.questionnaire.domain.question.SingleAnswerQuestion;

import de.userprompt.utils_userweave.query.model.QueryObject;

public interface AnswerDao extends BaseDao<Answer> {
	/*
	public List<AnswerToSingleAnswerQuestion> getAnswersForQuestion(SingleAnswerQuestion question);

	public List<MultipleAnswersAnwer> getAnswersForQuestion(MultipleAnswersQuestion question);

	public List<FreeAnwer> getAnswersForQuestion(FreeQuestion question);

	public List<MultipleRatingAnswer> getAnswersForQuestion(MultipleRatingQuestion question);

	public List<MultipleDimensionsAnswer> getAnswersForQuestion(DimensionsQuestion question);
*/
	public QueryObject getQueryObject(Question question, FilterFunctor filterFunctor);
	
	public List<AnswerToSingleAnswerQuestion> getValidAnswersForQuestion(SingleAnswerQuestion question, FilterFunctor applyFilterFunctor);

	public List<MultipleAnswersAnwer> getValidAnswersForQuestion(MultipleAnswersQuestion question, FilterFunctor applyFilterFunctor);

	public List<FreeAnwer> getValidAnswersForQuestion(FreeQuestion question, FilterFunctor applyFilterFunctor);

	public List<Object[]> getValidAnswersForQuestion(
		MultipleRatingQuestion question, FilterFunctor applyFilterFunctor);
	
	public Long getGroupedValidAnswersForMultipleRatingQuestion(
			MultipleRatingQuestion question, FilterFunctor applyFilterFunctor);
	
	public List<SingleDimensionAnswer> getValidAnswersForQuestion(
		DimensionsQuestion question, FilterFunctor applyFilterFunctor);

	public Long getGroupedValidAnswersForDimensionQuestion(
			DimensionsQuestion question, FilterFunctor applyFilterFunctor);
	
	public Answer<? extends Question> findByQuestionAndSurveyExecution(Question question, SurveyExecution surveyExecution);
};