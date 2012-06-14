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
package com.userweave.module.methoden.questionnaire.page.grouping.rating.freequestionnumber;

import java.util.Locale;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.module.methoden.questionnaire.dao.QuestionDao;
import com.userweave.module.methoden.questionnaire.dao.QuestionnaireGroupDao;
import com.userweave.module.methoden.questionnaire.domain.group.FreeQuestionNumberGroup;
import com.userweave.module.methoden.questionnaire.domain.group.QuestionnaireGroup;
import com.userweave.module.methoden.questionnaire.domain.question.AntipodePair;
import com.userweave.module.methoden.questionnaire.domain.question.FreeQuestion;
import com.userweave.module.methoden.questionnaire.page.grouping.GroupAddedCallback;
import com.userweave.module.methoden.questionnaire.page.grouping.rating.RatingGroupingPanel;


/**
 * @author oma
 */
@SuppressWarnings("serial")
public class FreeQuestionNumberGroupingPanel extends RatingGroupingPanel<FreeQuestionNumberGroup> {
	
	@SpringBean
	private QuestionDao questionDao;
	
	private final Integer questionId;
	
	@SpringBean
	private QuestionnaireGroupDao questionnaireGroupDao;
	
	private AntipodePair antipodePair;
	
	public FreeQuestionNumberGroupingPanel(String id, FreeQuestion question, FreeQuestionNumberGroup group, final Locale locale, GroupAddedCallback<QuestionnaireGroup> groupAddedCallback) {
		super(id, null, group, locale, groupAddedCallback, false);		
		questionId = question.getId();		
		
		getStimulus().setDefaultModel(new LocalizedPropertyModel(question, "text", locale));
		getStimulus().modelChanged();
	}
	
	@Override
	protected IModel getTitle() 
	{
		return new StringResourceModel("free_question", this, null);
	}
	
	
	@Override
	public void submit() 
	{
		super.submit();
		getGroup().setFreeQuestion((FreeQuestion) questionDao.findById(questionId));
		questionnaireGroupDao.save(getGroup());			
	}

	@Override
	protected boolean isStimulusVisible()
	{
		return true;
	}

}

