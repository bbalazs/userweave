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

import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.components.customModalWindow.BaseModalWindowPage;
import com.userweave.domain.ModuleReachedGroup;
import com.userweave.domain.StudyGroup;
import com.userweave.module.methoden.questionnaire.page.grouping.GroupAddedCallback;
import com.userweave.pages.grouping.ModuleReachedGroupingPanel;

public abstract class ModuleReachedGroupingPage extends BaseModalWindowPage
{
	private final ModuleReachedGroupingPanel displayComponent;
	
	private final GroupAddedCallback<StudyGroup> callback;
	
	public ModuleReachedGroupingPage(Locale locale, int studyId, final ModalWindow window)
	{
		super(window);
		
		callback = new GroupAddedCallback<StudyGroup>() 
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onAdd(AjaxRequestTarget target, StudyGroup group) 
			{
				addGroupAndSaveStudy(group);
				
				window.close(target);
			}
		};
		
		addToForm(displayComponent = 
			new ModuleReachedGroupingPanel(
				"moduleReached", 
				new ModuleReachedGroup(), 
				locale, studyId, callback));
	}
	
	@Override
	protected WebMarkupContainer getAcceptButton(String componentId,
			final ModalWindow window)
	{
		return new AjaxButton(componentId, getForm()) 
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) 
			{
				displayComponent.submit();
				callback.onAdd(target, displayComponent.getGroup());
				
				window.close(target);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form form) 
			{
				target.addComponent(displayComponent.get("feedback"));
			}
		};
	}
	
	@Override
	protected IModel getAcceptLabel()
	{
		return new StringResourceModel("submitFilter", this, null);
	}
	
	protected abstract void addGroupAndSaveStudy(StudyGroup group);
}
