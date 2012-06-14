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
package com.userweave.pages.configuration.study.content;

import com.userweave.pages.configuration.study.StudyConfigurationFormPanelBase;

public class StudyContentConfigurationPanel extends StudyConfigurationFormPanelBase
{
	private static final long serialVersionUID = 1L;

	public StudyContentConfigurationPanel(String id, int studyId)
	{
		super(id, studyId);
		/*Headline und Beschreibung der Studie im Bereich Konfiguration->Config auskommentiert. Wenn es gefaellt, loeschen ;)
		AuthOnlyTextField headline = 
			new AuthOnlyTextField(
					"headline", 
					new LocalizedPropertyModel(getStudyModel(), "headline", getStudyLocale()),
					getUpdateBehavior("onblur"));
		
		FormComponentLabel headlineLabel = new FormComponentLabel("headlineLabel", headline);
		
		
		AuthOnlyTextArea descr = 
			new AuthOnlyTextArea(
					"description",
					new LocalizedPropertyModel(getStudyModel(), "description", getStudyLocale()),
					getUpdateBehavior("onblur"),
					null,
					3,
					54);
		
		FormComponentLabel descrLabel = new FormComponentLabel("descrLabel", descr);
		
		
//		ModuleConfigurationsListPanel methodList = 
//			new ModuleConfigurationsListPanel("methodList", getStudyModel(), callback);
		
		addFormComponent(headline);
		addFormComponent(headlineLabel);
		
		addFormComponent(descr);
		addFormComponent(descrLabel);
		
//		addFormComponent(methodList);*/
	}

}
