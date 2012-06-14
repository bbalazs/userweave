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
package com.userweave.module.methoden.questionnaire.dao.filter;

import java.util.UUID;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.dao.filter.FilterFunctor;
import com.userweave.module.methoden.questionnaire.dao.QuestionnaireGroupDao;
import com.userweave.module.methoden.questionnaire.domain.group.QuestionnaireGroup;

@SuppressWarnings("serial")
public abstract class QuestionnaireFilterFunctorBase<T extends QuestionnaireGroup> implements FilterFunctor {

	@SpringBean
	private QuestionnaireGroupDao groupDao;
	
	private final Class<T> clazz;
	
	private final Integer groupId;
	
	public QuestionnaireFilterFunctorBase(Class<T> clazz, T group) {
		Injector.get().inject(this);
		groupId = group.getId();
		this.clazz = clazz;		
	}
	
	protected T getGroup() {
		return groupDao.findById(clazz, groupId);
	}

	protected String getUniqueAlias(String alias) {
		return alias + getUniqueAlias();
	}
		
	private String getUniqueAlias() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
//	protected SurveyExecutionDependentQuery connectToSurveyExecution(SurveyExecutionDependentQuery query, String answerAlias) {
//		String resultAlias = "r"+answerAlias;
//		query.addLeftJoin(answerAlias+".questionnaireResult",resultAlias);
//		query.connectToSurveyExecution(resultAlias + ".surveyExecution");
//		if(getGroup().getQuestion()!= null) {
//			query.addAndCondition(PropertyCondition.equals(answerAlias+".question",getGroup().getQuestion()));
//		}
//		return query;
//	}
}
