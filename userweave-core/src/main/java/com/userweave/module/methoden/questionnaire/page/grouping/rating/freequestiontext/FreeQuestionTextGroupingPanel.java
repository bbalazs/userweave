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
package com.userweave.module.methoden.questionnaire.page.grouping.rating.freequestiontext;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.module.methoden.questionnaire.dao.QuestionDao;
import com.userweave.module.methoden.questionnaire.dao.QuestionnaireGroupDao;
import com.userweave.module.methoden.questionnaire.domain.group.FreeQuestionTextGroup;
import com.userweave.module.methoden.questionnaire.domain.question.FreeQuestion;
import com.userweave.module.methoden.questionnaire.page.grouping.GroupAddedCallback;
import com.userweave.pages.grouping.GroupingPanelWithName;

public class FreeQuestionTextGroupingPanel extends GroupingPanelWithName<FreeQuestionTextGroup>
{
	@SpringBean
	private QuestionDao questionDao;
	
	@SpringBean
	private QuestionnaireGroupDao questionnaireGroupDao;
	
	private final Integer questionId;
	
	private String choice;
	
	private String filterText;
	
	public FreeQuestionTextGroupingPanel(
			String id, FreeQuestion question, FreeQuestionTextGroup group, 
			final Locale locale, GroupAddedCallback groupAddedCallback)
	{
		super(id, group, locale, groupAddedCallback);
		questionId = question.getId();
		
		getStimulus().setDefaultModel(new LocalizedPropertyModel(question, "text", locale));
		getStimulus().modelChanged();
		
		final List choices = Arrays.asList(new String[] {
			new StringResourceModel("equals", this, null).getString(),
			new StringResourceModel("not_equals", this, null).getString(),
			new StringResourceModel("contains", this, null).getString(),
			new StringResourceModel("not_contains", this, null).getString()
		});
				
		add(new RadioChoice("filterType", new PropertyModel(this, "choice"), choices));
		
		add(new TextField("filterString", new PropertyModel(this, "filterText")));
	}

	@Override
	protected IModel getTitle() 
	{
		return new StringResourceModel("free_question", this, null);
	}

	@Override
	public void submit() 
	{
		getGroup().setFreeQuestion((FreeQuestion) questionDao.findById(questionId));
		getGroup().setChoice(getFilterTextForQuery(choice));
		getGroup().setFilterText(filterText);
		
		questionnaireGroupDao.save(getGroup());	
	}
	
	/**
	 * Ugly hack to convert localized string back to logicaly
	 * String.
	 * @param filterType
	 * @return
	 */
	private String getFilterTextForQuery(String filterType)
	{
		if (filterType.compareTo(new StringResourceModel("equals", this, null).getString()) == 0)
		{
			return "Equals";
		}
		else if(filterType.compareTo(new StringResourceModel("not_equals", this, null).getString()) == 0)
		{
			return "Not Equals";
		}
		else if(filterType.compareTo(new StringResourceModel("contains", this, null).getString()) == 0)
		{
			return "Contains";
		}
		else if(filterType.compareTo(new StringResourceModel("not_contains", this, null).getString()) == 0)
		{
			return "Not Contains";
		}
		
		return "";
	}

	@Override
	protected boolean isStimulusVisible()
	{
		return true;
	}
}
