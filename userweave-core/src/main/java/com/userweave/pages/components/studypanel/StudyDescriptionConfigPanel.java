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
package com.userweave.pages.components.studypanel;

import com.userweave.ajax.form.AjaxBehaviorFactory;
import com.userweave.components.authorization.AuthOnlyTextArea;
import com.userweave.components.authorization.AuthOnlyTextField;
import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.domain.StudyState;
import com.userweave.pages.configuration.study.StudyConfigurationFormPanelBase;

public class StudyDescriptionConfigPanel extends StudyConfigurationFormPanelBase
{
	private static final long serialVersionUID = 1L;
	
	public StudyDescriptionConfigPanel(String id, Integer studyId)
	{
		super(id, studyId);
		
		@SuppressWarnings("serial")
		AuthOnlyTextField headline = 
			new AuthOnlyTextField(
					"headline", 
					new LocalizedPropertyModel(getStudyModel(), "headline", getStudyLocale()),
					AjaxBehaviorFactory.getUpdateBehavior("onblur", StudyDescriptionConfigPanel.this))
			{
				@Override
				protected boolean isEditAllowed()
				{
					return getStudy().getState() == StudyState.INIT;
				}
			};
		
		@SuppressWarnings("serial")
		AuthOnlyTextArea descr = 
			new AuthOnlyTextArea(
					"description",
					new LocalizedPropertyModel(getStudyModel(), "description", getStudyLocale()),
					AjaxBehaviorFactory.getUpdateBehavior("onblur", StudyDescriptionConfigPanel.this),
					null,
					3,
					54)
		{
			@Override
			protected boolean isEditAllowed()
			{
				return getStudy().getState() == StudyState.INIT;
			}
		};
		
		addFormComponent(headline);
		addFormComponent(descr);
	}
}
