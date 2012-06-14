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
package com.userweave.module.methoden.questionnaire.page.conf.question.multipleanswers;

import java.util.Locale;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.module.methoden.questionnaire.domain.question.MultipleAnswersQuestion;
import com.userweave.module.methoden.questionnaire.page.conf.question.MultiplePossibleAnswersConfigurationPanel;

public class MultipleAnswersConfigurationPanel 
	extends MultiplePossibleAnswersConfigurationPanel<MultipleAnswersQuestion> 
{
	private static final long serialVersionUID = 1L;

	public MultipleAnswersConfigurationPanel(
		String id, int configurationId, Integer theQuestionId, Locale studyLocale) 
	{
		super(id, configurationId, theQuestionId, MultipleAnswersQuestion.TYPE, studyLocale);		
	}

	@Override
	protected MultipleAnswersQuestion createNewQuestion() 
	{
		return 	new MultipleAnswersQuestion();
	}
	
	@Override
	protected IModel getTypeModel()
	{
		return new StringResourceModel("mutipleAnswers_type", this, null);
	}

}
