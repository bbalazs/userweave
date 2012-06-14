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

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.userweave.domain.ModuleReachedGroup;
import com.userweave.domain.Study;
import com.userweave.domain.StudyGroup;
import com.userweave.domain.StudyLocalesGroup;
import com.userweave.pages.grouping.ModuleReachedGroupingDescriptionPanel;
import com.userweave.pages.grouping.ModuleReachedGroupingPanel;
import com.userweave.pages.grouping.StudyLocalesGroupingDescriptionPanel;
import com.userweave.pages.grouping.StudyLocalesGroupingPanel;

public class StudyGroupingFactoryImpl implements StudyGroupingFactory {

	private static final String localGroupTypeId = "LOCALES_GROUP";
	
	private static final String  reachedModuleGroupTypeId = "REACHED_MODULE_GROUP";
	
	@Override
	public List<StudyGroupType> getGroupTypes(Study study) {		
		return Arrays.asList(new StudyGroupType[] {
			new StudyGroupType(new Model("Locale group"), localGroupTypeId, study),
			new StudyGroupType(new Model("Reached module group"), reachedModuleGroupTypeId, study),								
		});
	}

	@Override
	public Component createGroupingPanel(String id, StudyGroupType groupType, Locale locale, GroupAddedCallback<StudyGroup> groupAddedCallback) {
		
		if (groupType.getId().equals(localGroupTypeId)) {
			return new StudyLocalesGroupingPanel(id, groupType.getStudy(), locale, groupAddedCallback);
		} else if (groupType.getId().equals(reachedModuleGroupTypeId)) {
			return new ModuleReachedGroupingPanel(id, new ModuleReachedGroup(), locale, groupType.getStudy().getId(), groupAddedCallback);
		} else {
			return null;
		}
	}

	@Override
	public Panel getGroupingDescriptionPanel(IModel groupModel, Locale locale) {
		Object studyGroup = groupModel.getObject();
		if (studyGroup instanceof StudyLocalesGroup) {
			return new StudyLocalesGroupingDescriptionPanel("group", groupModel, locale);
		} else if (studyGroup instanceof ModuleReachedGroup) {
			return new ModuleReachedGroupingDescriptionPanel("group", groupModel, locale);
		} else {
			return null;
		}
	}

}
