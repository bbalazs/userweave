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
package com.userweave.pages.configuration.report;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.userweave.components.authorization.AuthOnlyAjaxLink;
import com.userweave.components.callback.EventHandler;
import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.domain.Study;
import com.userweave.domain.StudyState;

public abstract class StudyBaseFilterPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	public StudyBaseFilterPanel(
		String id, IModel<Study> studyModel, EventHandler addFilterCallback)
	{
		super(id, studyModel);

		final CustomModalWindow filterModal = createFilterModal(addFilterCallback);
		
		add(filterModal);
		
		add(new AuthOnlyAjaxLink("addFilter")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				filterModal.show(target);
			}
			
			@Override
			public boolean isVisible()
			{
				return getStudy().getState() == StudyState.FINISHED;
			}
		});
	}
	
	protected Study getStudy()
	{
		return (Study) getDefaultModelObject();
	}
	
	protected abstract CustomModalWindow createFilterModal(EventHandler addFilterCallback);
}
