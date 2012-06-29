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
package com.userweave.pages.configuration.study.participants;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;

import com.userweave.ajax.form.AjaxBehaviorFactory;
import com.userweave.components.authorization.AuthOnlyTextField;
import com.userweave.domain.StudyState;
import com.userweave.pages.components.servicePanel.ServicePanel;
import com.userweave.pages.components.servicePanel.ServicePanel.ServicePanelType;
import com.userweave.pages.configuration.study.StudyConfigurationFormPanelBase;

/**
 * @author oma
 */
@SuppressWarnings("serial")
public class StudyParticipantsPanel extends StudyConfigurationFormPanelBase {

	public StudyParticipantsPanel(String id, int studyId) {
		super(id, studyId);
		
		if( ! (getStudy().getState() == StudyState.FINISHED) )
			add(new ServicePanel("servicePanel", ServicePanelType.TEST_MEMBER));
		else
			add(new ServicePanel("servicePanel", ServicePanelType.NOT_SURE_INTERPRETATION));
		
		//TextField participants = new TextField("numberOfParticipants");
		//participants.setRequired(true);
		
		AuthOnlyTextField participants = 
			new AuthOnlyTextField(
					"numberOfParticipants", 
					new PropertyModel(getStudyModel(), "numberOfParticipants"), 
					AjaxBehaviorFactory.getUpdateBehavior("onchange", StudyParticipantsPanel.this));
		
		participants.setRequired(true);
		
		addFormComponent(participants);
		
		//participants.add(getUpdateBehavior("onchange"));
		
		add(new FeedbackPanel("feedback").setOutputMarkupId(true));
	
	}
}

