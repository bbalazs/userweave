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
package com.userweave.pages.components.slidableajaxtabpanel.addmethodpanel;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.dao.StudyDao;
import com.userweave.domain.service.ModuleService;
import com.userweave.module.AbstractModule;
import com.userweave.module.Module;
import com.userweave.module.ModuleConfiguration;
import com.userweave.module.methoden.freetext.FreeTextMethod;
import com.userweave.module.methoden.iconunderstandability.IconMatchingMethod;
import com.userweave.module.methoden.mockup.MockupMethod;
import com.userweave.module.methoden.questionnaire.QuestionnaireMethod;
import com.userweave.module.methoden.rrt.RrtMethod;
import com.userweave.pages.components.slidableajaxtabpanel.ChangeTabsCallback;

/**
 * Add a new method to the given study configuration.
 * 
 * @author opr
 */
public class AddMethodPanel extends AbstractAddPanel<Module<?>>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Renders the localized name of methods.
	 * 
	 * @author opr
	 */
	protected class LocalizedModuleNameChoiceRenderer extends ChoiceRenderer
	{
		private static final long serialVersionUID = 1L;
		
		/**
		 * Reference for properties to load name from.
		 */
		private final Component parent;
		
		/**
		 * Default constructor.
		 * 
		 * @param displayExpression
		 * 		Name of module
		 * @param parent
		 * 		Parent component
		 */
		public LocalizedModuleNameChoiceRenderer(String displayExpression, 
				Component parent)
		{
			super(displayExpression);
			
			this.parent = parent;
		}
		
		@Override
		public Object getDisplayValue(Object object)
		{
			/*
			 * Internationalization of module names
			 * using their names to map a 
			 * StringResourceModel
			 */
			AbstractModule<?> module = (AbstractModule<?>) object;
			
			StringResourceModel model = new StringResourceModel(
					module.getModuleId(), parent, null);
			
			return model.getObject();
		}
	}
	
	@SpringBean
	private ModuleService moduleService;
	
	@SpringBean
	private StudyDao studyDao;
	
	private final int studyId;
	
	/**
	 * Default constructor
	 * 
	 * @param id
	 * 		Component id.
	 * @param model
	 * 		Model with the study configuration.
	 */
	public AddMethodPanel(String id, int studyId, final IModel choices, final ChangeTabsCallback callback)
	{
		super(id, choices, callback);
		
		this.studyId = studyId;
	}

	@Override
	protected String getImageResource(Module<?> selectedItem)
	{
		if(selectedItem instanceof FreeTextMethod)
		{
			return "res/MethodeFreitext.png";
		}
		
		if(selectedItem instanceof IconMatchingMethod)
		{
			return "res/MethodeIconTest.png";
		}
		
		if(selectedItem instanceof MockupMethod)
		{
			return "res/MethodePraesentation.png";
		}
		
		if(selectedItem instanceof QuestionnaireMethod)
		{
			return "res/MethodeFragebogen.png";
		}
		
		if(selectedItem instanceof RrtMethod)
		{
			return "res/MethodeRelevanz.png";
		}
		
		return null;
	}
	
	@Override
	protected IChoiceRenderer getChoiceRenderer()
	{
		return new LocalizedModuleNameChoiceRenderer("toString", AddMethodPanel.this);
	}

	@Override
	protected IModel getPreviewNameOnUpdate(Module<?> selectedItem)
	{
		return new StringResourceModel(selectedItem.getModuleId() + "_PREVIEW", this, null);
	}

	@Override
	protected int createNewItem(Module<?> selectedItem, String name)
	{
		ModuleConfiguration moduleConfiguration = 
			moduleService.createNewConfigurationInStudyForModule(
					selectedItem.getModuleId(), 
					studyDao.findById(studyId));
		
		moduleConfiguration.setName(name);
		moduleConfiguration.save();
		
		return moduleConfiguration.getPosition();
	}
}
