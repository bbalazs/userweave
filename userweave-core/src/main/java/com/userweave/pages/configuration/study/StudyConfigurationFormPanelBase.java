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
package com.userweave.pages.configuration.study;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.userweave.ajax.IAjaxUpdater;
import com.userweave.pages.configuration.DisableFormComponentVisitor;
import com.userweave.pages.configuration.DisableLinkVisitor;

/**
 * Configuration panel with a form, which disables
 * links and form components, if child links and
 * components are not wanted in a specific study
 * state.
 * 
 * NOTICE: Subclasses must use addFormComponent instead
 * 		   of just add, except they override the layout.
 * 
 * @author opr
 */
public abstract class StudyConfigurationFormPanelBase 
	extends StudyConfigurationPanelBase  
	implements IAjaxUpdater
{
	private static final long serialVersionUID = 1L;

	/**
	 * The Form component on this panel.
	 */
	private Form form;

	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component id.
	 * @param studyId
	 * 		Id of study, which backs ths component.
	 */
	public StudyConfigurationFormPanelBase(String id, Integer studyId) 
	{
		super(id, studyId);
	
		init();
	}	

	/**
	 * Initializes this panel with components.
	 */
	public void init() 
	{
		form = new Form("form") 
		{	
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() 
			{
				save();
			}
			
			@Override
			protected void onBeforeRender() 
			{				
				IModel stateModel = new PropertyModel(getStudyModel(), "state");
			
				// disable link when study not in init-state
				visitChildren(new DisableLinkVisitor(stateModel));
				
				// disable form components, when study not in init-state
				visitFormComponents(new DisableFormComponentVisitor(stateModel));
				
				super.onBeforeRender();
			}
		};
		
		add(form);
	}
	
	/**
	 * Returns this components form component.
	 * 
	 * @return
	 */
	protected Form getForm() 
	{
		return form;
	}
	
	/**
	 * Convinient method to add components to this panel.
	 * 
	 * @param component
	 * 		Tne component to add.
	 */
	protected void addFormComponent(Component component) 
	{
		form.add(component);
	}
	
	@Override
	public void onUpdate(AjaxRequestTarget target)
	{
		save();
	}
	
//	/**
//	 * Needed to save From components like textarea and textfields.
//	 * 
//	 * @usage: component.add(getUpdateBehavior(event, markupId))
//	 * @param event the javascript event trigger (mostly 'onblur')
//	 *
//	 * @return
//	 */
//	protected AjaxFormComponentUpdatingBehavior getUpdateBehavior(String event)
//	{
//		return new AjaxFormComponentUpdatingBehavior(event) 
//		{
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			protected void onUpdate(AjaxRequestTarget target) 
//			{
//				save();
//			}
//		};
//	}
}

