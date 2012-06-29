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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;

import com.userweave.ajax.form.AjaxBehaviorFactory;
import com.userweave.components.authorization.AuthOnlyTextField;
import com.userweave.components.configuration.ConfigurationBaseUI;
import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.dao.StudyDependendDao;
import com.userweave.domain.ModuleConfigurationEntityBase;
import com.userweave.domain.Study;
import com.userweave.domain.StudyState;

/**
 * <p>
 * Module configuration base class. Displays the type
 * of the module (i.e. 'Questionnaire') and a text field
 * to edit the headline.
 * </p>
 * 
 * @important 
 * 		Extending this class means, that the markup 
 * 		for new input fields has to be structured
 * 		as follows:
 *
 * <pre>
 * &lt;tr&gt;
 * 		&lt;td&gt; &lt;wicket:message key="conf_key"/&gt; &lt;/td&gt;
 * 		&lt;td&gt; &lt;input/&gt; &lt;/td&gt;
 * 		&lt;td&gt; &lt;wicket:message key="conf_key_legend"/&gt; &lt;/td&gt;
 * &lt;/tr&gt;
 * </pre>
 * 
 * @author opr
 */
public abstract class ModuleConfigurationBaseUI<T extends ModuleConfigurationEntityBase<?>> 
	extends ConfigurationBaseUI<T> 
{
	private static final long serialVersionUID = 1L;

	/**
	 * Feedback panel for errors.
	 */
	private final FeedbackPanel feedback;
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component id.
	 * @param configurationId
	 * 		Entity id.
	 */
	public ModuleConfigurationBaseUI(String id, int configurationId)
	{	
		super(id, configurationId);
		
		AuthOnlyTextField headline = 
			new AuthOnlyTextField(
					"headline", 
					new LocalizedPropertyModel(getDefaultModel(), "description", getStudyLocale()),
					AjaxBehaviorFactory.getUpdateBehavior("onblur", ModuleConfigurationBaseUI.this))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean isEditAllowed()
			{
				return getStudy().getState() == StudyState.INIT;
			}
		};
		
		addFormComponent(headline);
		
		feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		
		addFormComponent(feedback);
		
		addFormComponent(new Label("typeOfModule", getTypeModel()));
	}
	
	@Override
	protected Study getStudy() 
	{
		return getConfiguration().getStudy();
	}
	
	@Override
	public void onUpdate(AjaxRequestTarget target)
	{
		target.add(feedback);
		saveConfiguration();
	}
	
	/**
	 * Get the underling configuration entity.
	 * 
	 * @return
	 * 		A sublcass of ModuleConfigurationEntityBase.
	 */
	@SuppressWarnings("unchecked")
	protected T getConfiguration() 
	{
		return (T) getDefaultModelObject();
	}
	
	/**
	 * Shortcut for saving the underlying configuration object.
	 */
	private void saveConfiguration() 
	{
		getConfigurationDao().save(getConfiguration());
	}
	
	/**
	 * Each module has its own configuation dao for load/save operations.
	 * 
	 * @return
	 * 		A base dao for a specific configuration type.
	 */
	protected abstract StudyDependendDao<T> getConfigurationDao();
	
	/**
	 * Model for the label to display the type of the module
	 * 
	 * @return
	 * 		IModel (Probably a StringResourceModel)
	 */
	protected abstract IModel getTypeModel();
}

