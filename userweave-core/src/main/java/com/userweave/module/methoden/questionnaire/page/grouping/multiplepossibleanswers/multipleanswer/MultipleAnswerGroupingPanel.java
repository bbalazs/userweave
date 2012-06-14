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
package com.userweave.module.methoden.questionnaire.page.grouping.multiplepossibleanswers.multipleanswer;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.domain.LocalizedString;
import com.userweave.module.methoden.questionnaire.dao.QuestionDao;
import com.userweave.module.methoden.questionnaire.dao.QuestionnaireGroupDao;
import com.userweave.module.methoden.questionnaire.domain.group.MultipleAnswersGroup;
import com.userweave.module.methoden.questionnaire.domain.group.QuestionnaireGroup;
import com.userweave.module.methoden.questionnaire.domain.question.MultipleAnswersQuestion;
import com.userweave.module.methoden.questionnaire.page.grouping.GroupAddedCallback;
import com.userweave.module.methoden.questionnaire.page.grouping.multiplepossibleanswers.MultiplePossibleAnswersGroupingPanel;

@SuppressWarnings("serial")
public class MultipleAnswerGroupingPanel extends MultiplePossibleAnswersGroupingPanel<MultipleAnswersGroup> {

	@SpringBean
	private QuestionnaireGroupDao questionnaireGroupDao;
	
	@SpringBean
	private QuestionDao questionDao;
	
	private RadioChoice logicalChoices;
	
	private MultipleAnswersGroup.Operator operator;
	
	private LocalizedString text;
	
	public MultipleAnswerGroupingPanel(String id, MultipleAnswersQuestion multipleAnswersQuestion, Locale locale, GroupAddedCallback<QuestionnaireGroup> groupAddedCallback) {
		super(id, multipleAnswersQuestion, new MultipleAnswersGroup(), locale, groupAddedCallback);
		
		getStimulus().setDefaultModel(new LocalizedPropertyModel(multipleAnswersQuestion, "text", locale));
		getStimulus().modelChanged();
		
		add(
			logicalChoices = new RadioChoice(
				"operator", 
				Arrays.asList(MultipleAnswersGroup.Operator.values())));
		
		logicalChoices.setOutputMarkupId(true);
		
		logicalChoices.setEnabled(false);
	}

	@Override
	protected void submit(MultipleAnswersGroup group, List<LocalizedString> answers) {
		group.setAnswers(answers);
		group.setMultipleAnswersQuestion((MultipleAnswersQuestion) questionDao.findById(getQuestionId()));
		questionnaireGroupDao.save(group);					
	}
	
	@Override
	protected IModel getTitle() 
	{
		return new StringResourceModel("mult_pos_answers", this, null);
	}
	
	@Override
	protected boolean isOnChangeAjaxBehaviorNeeded()
	{
		return true;
	}
	
	@Override
	protected void onUpdate(AjaxRequestTarget target)
	{
		if(getAnswers().size() > 1)
		{
			logicalChoices.setEnabled(true);
			logicalChoices.setRequired(true);
		}
		else
		{
			logicalChoices.setEnabled(false);
			logicalChoices.setRequired(false);
		}
		
		target.addComponent(logicalChoices);
	}

	@Override
	protected boolean isStimulusVisible()
	{
		return true;
	}
}
