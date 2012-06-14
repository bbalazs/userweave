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
package com.userweave.module.methoden.freetext.page.conf;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.authorization.AuthOnlyTextArea;
import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.dao.StudyDependendDao;
import com.userweave.domain.StudyState;
import com.userweave.module.methoden.freetext.dao.FreeTextConfigurationDao;
import com.userweave.module.methoden.freetext.domain.FreeTextConfigurationEntity;
import com.userweave.pages.configuration.module.ModuleConfigurationBaseUI;

/**
 * @author oma
 * 
 * @important: 
 * 		if you move the editor and his resources to another location,
 * 		adjust the path for the stylesheet in initJhtmlArea.js
 */
public class FreeTextConfigurationPanel 
	extends ModuleConfigurationBaseUI<FreeTextConfigurationEntity>
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private FreeTextConfigurationDao dao;
	
	@Override
	protected StudyDependendDao<FreeTextConfigurationEntity> getBaseDao() 
	{
		return dao;
	}
	
	@Override
	protected StudyDependendDao<FreeTextConfigurationEntity> getConfigurationDao() 
	{
		return dao;
	}
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component markup id.
	 * @param configurationId
	 * 		Id of configuration to load.
	 */
	public FreeTextConfigurationPanel(String id, Integer configurationId) 
	{
		super(id, configurationId);
		
		AuthOnlyTextArea textArea = new AuthOnlyTextArea(
				"freetext", 
				new LocalizedPropertyModel(getDefaultModel(), "freetext", getStudyLocale()),
				getUpdateBehavior(), 
				"freetext", 
				15, 58)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean isEditAllowed()
			{
				return getStudy().getState() == StudyState.INIT;
			}
		};
		
		// escape content here to enable html tags.
		textArea.setEscapeModelStrings(false);
		
		addFormComponent(textArea);
	}
	
	@Override
	public void renderHead(IHeaderResponse response)
	{
		super.renderHead(response);
		
		response.renderCSSReference(new PackageResourceReference(
				FreeTextConfigurationPanel.class, "res/jHtmlArea.css"));
		
		response.renderJavaScriptReference(new JavaScriptResourceReference(
				FreeTextConfigurationPanel.class, "res/jHtmlArea-0.6.0.min.js"));
	}
	
	/**
	 * Returns an ajax onclick behavior that disables the overlay 
	 * while the textarea saves. That is, because the update 
	 * thread saves every second.
	 * 
	 * @return
	 */
	private AjaxFormComponentUpdatingBehavior getUpdateBehavior()
	{
		return new AjaxFormComponentUpdatingBehavior("onclick") 
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator() 
			{	
				IAjaxCallDecorator decorator = new IAjaxCallDecorator() 
				{
					private static final long serialVersionUID = 1L;

					@Override
					public CharSequence decorateOnFailureScript(
							Component component, CharSequence script) 
					{
						return "disableModalOverlay();" + script + "enableModalOverlay();";
					}

					@Override
					public CharSequence decorateOnSuccessScript(
							Component component, CharSequence script) 
					{
						return "disableModalOverlay();" + script + "enableModalOverlay();";
					}

					@Override
					public CharSequence decorateScript(
							Component component, CharSequence script) 
					{
						return "disableModalOverlay();" + script + "enableModalOverlay();";
					}
				};
				
				return decorator;
			}

			@Override
			protected void onUpdate(AjaxRequestTarget target) 
			{
				FreeTextConfigurationPanel.this.onUpdate(target);
			}
		};
	}
	
	@Override
	protected IModel getTypeModel()
	{
		return new StringResourceModel(
			"freetext_type", FreeTextConfigurationPanel.this, null);
	}
}

