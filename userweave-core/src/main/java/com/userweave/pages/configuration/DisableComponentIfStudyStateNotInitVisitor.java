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
package com.userweave.pages.configuration;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

import com.userweave.domain.StudyState;
import com.userweave.pages.components.visitor.FormComponentVisitor;

public class DisableComponentIfStudyStateNotInitVisitor extends FormComponentVisitor {
	
	
	private final IModel studyStateModel;

	public DisableComponentIfStudyStateNotInitVisitor(IModel statusModel) 
	{
		this.studyStateModel = statusModel;
	}
	
	private StudyState getStudyState() 
	{
		return (StudyState) studyStateModel.getObject();
	}

	@Override
	protected void onFormComponent(Component component) {
		EnabledInStudyState enabledInStudyState = component.getClass().getAnnotation(EnabledInStudyState.class);
		if (enabledInStudyState != null) {
			for (StudyState studyState: enabledInStudyState.states()) {
				if (studyState == getStudyState()) {
					//link.setEnabled(true);
					return;
				} 
			}
			disable(component);
		} else {
			if(getStudyState() != StudyState.INIT) {
				disable(component);
			}
		}
	}

	private final static boolean HIDE = false;

	private void disable(Component component) {
		if(HIDE) {
			component.setVisible(false);
		} else {
			component.setEnabled(false);
		}
	}
	
}