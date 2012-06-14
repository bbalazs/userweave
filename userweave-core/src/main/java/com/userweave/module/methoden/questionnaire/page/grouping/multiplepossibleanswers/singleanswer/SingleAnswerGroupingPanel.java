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
package com.userweave.module.methoden.questionnaire.page.grouping.multiplepossibleanswers.singleanswer;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.domain.LocalizedString;
import com.userweave.module.methoden.questionnaire.dao.QuestionDao;
import com.userweave.module.methoden.questionnaire.dao.QuestionnaireGroupDao;
import com.userweave.module.methoden.questionnaire.domain.group.QuestionnaireGroup;
import com.userweave.module.methoden.questionnaire.domain.group.SingleAnswerGroup;
import com.userweave.module.methoden.questionnaire.domain.question.SingleAnswerQuestion;
import com.userweave.module.methoden.questionnaire.page.grouping.GroupAddedCallback;
import com.userweave.module.methoden.questionnaire.page.grouping.multiplepossibleanswers.MultiplePossibleAnswersGroupingPanel;

@SuppressWarnings("serial")
public class SingleAnswerGroupingPanel extends MultiplePossibleAnswersGroupingPanel<SingleAnswerGroup> {

	@SpringBean
	private QuestionnaireGroupDao questionnaireGroupDao;
	
	@SpringBean
	private QuestionDao questionDao;
	
	public SingleAnswerGroupingPanel(String id, SingleAnswerQuestion singleAnswerQuestion, Locale locale, GroupAddedCallback<QuestionnaireGroup> groupAddedCallback) {
		super(id, singleAnswerQuestion, new SingleAnswerGroup(), locale, groupAddedCallback);
		
		getStimulus().setDefaultModel(new LocalizedPropertyModel(singleAnswerQuestion, "text", locale));
		getStimulus().modelChanged();
	}

	@Override
	protected void submit(SingleAnswerGroup group, List<LocalizedString> answers) {
		group.setAnswers(answers);
		group.setSingleAnswerQuestion((SingleAnswerQuestion) questionDao.findById(getQuestionId()));
		questionnaireGroupDao.save(group);					
	}
	
	@Override
	protected IModel getTitle() 
	{
		return new StringResourceModel("single_answer", this, null);
	}

	@Override
	protected boolean isStimulusVisible()
	{
		return true;
	}
}
