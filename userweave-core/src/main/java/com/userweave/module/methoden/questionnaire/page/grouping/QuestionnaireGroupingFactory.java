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
package com.userweave.module.methoden.questionnaire.page.grouping;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.userweave.module.methoden.questionnaire.domain.group.QuestionnaireGroup;
import com.userweave.module.methoden.questionnaire.domain.question.Question;

public interface QuestionnaireGroupingFactory {
	
	/**
	 * get list of available group types for questions
	 * @param questions
	 * @return
	 */
	public List<GroupType> getGroupTypes(List<Question> questions, Locale locale);
	
	
	public Panel getGroupingDescriptionPanel(IModel groupModel, Locale locale);
	
	/**
	 * create grouping panel for question
	 * @param id
	 * @param question
	 * @param locale
	 * @param groupAddedCallback
	 * @return
	 */
	public Component createGroupingPanel(String id, QuestionnaireGroupType groupType, Locale locale, GroupAddedCallback<QuestionnaireGroup> groupAddedCallback);
}
