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

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.panel.Panel;

import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.pages.configuration.study.FilterOverviewPage;

public abstract class FilterActionPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	public FilterActionPanel(String id, final Integer studyId)
	{
		super(id);
		
		final CustomModalWindow filterOverviewModal = 
			new CustomModalWindow("filterOverviewModal");
		
		add(new AjaxLink("filterOverview")
		{
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				filterOverviewModal.show(target);
			}
			
		});
		
		add(filterOverviewModal); 
		
		filterOverviewModal.setPageCreator(new ModalWindow.PageCreator()
		{
			@Override
			public Page createPage()
			{
				return new FilterOverviewPage(filterOverviewModal, studyId);
			}
		});
		
		filterOverviewModal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
		{
			@Override
			public void onClose(AjaxRequestTarget target)
			{
				onWindowClose(target);
				
//				groupFilterList.triggerModelChangeOfChoices();
//				target.addComponent(groupFilterList);
			}
		});
	}
	
	protected abstract void onWindowClose(AjaxRequestTarget target);
}
