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
package com.userweave.module.methoden.iconunderstandability.page.report.bmi;



import java.util.List;
import java.util.Locale;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.components.customModalWindow.BaseModalWindowPage;
import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.module.methoden.iconunderstandability.domain.report.TermAssignment;

/**
 * @author oma
 */
public class IconOrderPage extends BaseModalWindowPage 
{
	private final Locale studyLocale;
	
	public IconOrderPage(
		ModalWindow modalWindow, 
		List<TermAssignment> termAssignments, 
		Locale aStudyLocale) 
	{
		super(modalWindow);
		
		modalWindow.setTitle(new StringResourceModel("assi_diff", this, null));
		
		this.studyLocale = aStudyLocale;
		
		addToForm(
			new PropertyListView("termAssignments", termAssignments) 
			{

				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem item) {
					item.add(new Label(
							"term", 
							new LocalizedPropertyModel(
									item.getModelObject(), "term", studyLocale)));
					
					item.add(new Label("assignment"));					
				}
			
			}
		);
	}
	
	@Override
	protected WebMarkupContainer getAcceptButton(String componentId,
			ModalWindow window)
	{
		WebMarkupContainer container =
			new WebMarkupContainer(componentId);
		
		container.setVisible(false);
		
		return container;
	}
	
	@Override
	protected IModel getDeclineLabel()
	{
		return new StringResourceModel("close", this, null);
	}
}

