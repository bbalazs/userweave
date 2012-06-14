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
package com.userweave.pages.configuration.module;

import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.userweave.ajax.IAjaxUpdater;
import com.userweave.domain.ModuleConfigurationEntityBase;
import com.userweave.pages.configuration.DisableFormComponentVisitor;
import com.userweave.pages.configuration.DisableLinkVisitor;


public abstract class ModuleConfigurationFormPanelBase<T extends ModuleConfigurationEntityBase> 
	extends ModuleConfigurationPanelBase<T> 
	implements IAjaxUpdater
{

	private Form form;
	
	public ModuleConfigurationFormPanelBase(String id, Integer configurationId, Locale studyLocale)
	{
		super(id, configurationId, studyLocale);
	
		init();
	}

	public void init() 
	{
		form = new Form("form") 
		{
			{
				setModel(getConfigurationModel());
			}
			
			@Override
			protected void onSubmit()
			{
				saveConfiguration();
			}

			@Override
			protected void onBeforeRender()
			{
				IModel model = new PropertyModel(getConfigurationModel(),
						"study.state");
				visitChildren(new DisableLinkVisitor(model));
				visitFormComponents(new DisableFormComponentVisitor(model));
				super.onBeforeRender();
			}
		};
		
		add(form);
	}
	
	protected Form getForm() 
	{
		return form;
	}
	
	@Override
	public void onUpdate(AjaxRequestTarget target)
	{
		target.add(ModuleConfigurationFormPanelBase.this.get("feedbackPanel"));
		saveConfiguration();
	}
}

