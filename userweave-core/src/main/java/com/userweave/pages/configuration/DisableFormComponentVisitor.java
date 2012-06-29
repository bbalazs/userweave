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

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import com.userweave.domain.StudyState;

public class DisableFormComponentVisitor<T extends FormComponent<?>> implements IVisitor<T, Void> {
	
	private final IModel<StudyState> studyStateModel;

	public DisableFormComponentVisitor(IModel<StudyState> statusModel) 
	{
		this.studyStateModel = statusModel;
	}
	
	private StudyState getStudyState() 
	{
		return studyStateModel.getObject();
	}

	protected void onFormComponent(T formComponent) 
	{
		if (getStudyState() != StudyState.INIT)
		{
			if (formComponent instanceof Button)
			{
				// hide buttons
				if (formComponent.isVisible())
				{
					formComponent.setVisible(false);
				}
			}
			else
			{
				// disable input fields
				if (formComponent.isEnabled())
				{
					formComponent.setEnabled(false);
				}
			}
		}
		
	}

	@Override
	public void component(T object, IVisit<Void> visit)
	{
		onFormComponent(object);
	}
}