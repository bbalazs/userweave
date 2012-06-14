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
package com.userweave.module.methoden.mockup.page.conf;

import java.util.Arrays;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.ajax.form.AjaxBehaviorFactory;
import com.userweave.application.UserWeaveSession;
import com.userweave.components.authorization.AuthOnlyDropDownChoice;
import com.userweave.components.authorization.AuthOnlyTextArea;
import com.userweave.components.authorization.AuthOnlyTextField;
import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.dao.StudyDependendDao;
import com.userweave.domain.Role;
import com.userweave.domain.StudyState;
import com.userweave.module.methoden.mockup.dao.MockupConfigurationDao;
import com.userweave.module.methoden.mockup.domain.MockupConfigurationEntity;
import com.userweave.module.methoden.mockup.domain.MockupConfigurationEntity.SwitchToNextConfigType;
import com.userweave.pages.configuration.module.ModuleConfigurationBaseUI;

/**
 * Configuration Panel for the mockup method.
 * 
 * @author oma
 */
public abstract class MockupConfigurationPanel 
	extends ModuleConfigurationBaseUI<MockupConfigurationEntity>
{
	private static final long serialVersionUID = 1L;

	/**
	 * WebMarkupContainer that displays itself as a span, if
	 * it is not enabled.
	 * 
	 * @author opr
	 *
	 */
	private class DisableWebMarkupContainer extends WebMarkupContainer
	{
		private static final long serialVersionUID = 1L;
		
		/**
		 * Default constructor.
		 * 
		 * @param id
		 * 		Component markup id.
		 */
		public DisableWebMarkupContainer(String id)
		{
			super(id);
		}
		
		@Override
		public boolean isEnabled()
		{
			return !UserWeaveSession.get().getUser().hasRole(Role.PROJECT_GUEST) && 
					getStudy().getState() == StudyState.INIT;
		}
		
		@Override
		protected void onComponentTag(ComponentTag tag)
		{
			if(! isEnabled())
			{
				tag.setName("span");
			}
			else
			{
				super.onComponentTag(tag);
			}
		}
	}
	
	@SpringBean
	private MockupConfigurationDao dao;

	@Override
	protected StudyDependendDao<MockupConfigurationEntity> getBaseDao() 
	{
		return dao;
	}
	
	@Override
	protected StudyDependendDao<MockupConfigurationEntity> getConfigurationDao() 
	{
		return dao;
	}
	
	private final WebMarkupContainer timerContainer;
	
	private boolean timeVisible, layerVisible;
	
	public MockupConfigurationPanel(String id, Integer configurationId) 
	{
		super(id, configurationId);
		
		AuthOnlyTextField url = new AuthOnlyTextField(
			"url", 
			new LocalizedPropertyModel(getDefaultModel(), "localeUrl", getStudyLocale()), 
			AjaxBehaviorFactory.getUpdateBehavior("onblur", MockupConfigurationPanel.this));
		
		url.setRequired(true);
		
		DisableWebMarkupContainer resetUrlLink = new DisableWebMarkupContainer("resetUrlLink");
		
		addFormComponent(resetUrlLink);
		
		if(resetUrlLink.isEnabled())
		{
			resetUrlLink.add(new AttributeModifier("onclick", true, new Model("resetUrl();return false;")));
		}
		
		AuthOnlyTextField time = 
			new AuthOnlyTextField(
				"time", 
				AjaxBehaviorFactory.getUpdateBehavior("onblur", MockupConfigurationPanel.this))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible() {
					return !(getConfiguration().getSwitchToNextConfigType() == SwitchToNextConfigType.BUTTON);
				}
			};
		time.setType(Integer.class);
		time.setRequired(true);
		
		AuthOnlyTextArea freetext = 
			new AuthOnlyTextArea(
					"mockuptext", 
					new LocalizedPropertyModel(getDefaultModel(), "freetext", getStudyLocale()),
					AjaxBehaviorFactory.getUpdateBehavior("onblur", MockupConfigurationPanel.this));

		timeVisible = getConfiguration().isTimeVisible();
		layerVisible = getConfiguration().isLayerVisible();
		
		AuthOnlyDropDownChoice timeVisibleDropDown = 
			createDropDown("timeVisible", "timeVisible");
		
		AuthOnlyDropDownChoice layerVisibleDropDown = 
			createDropDown("layerVisible", "layerVisible");
		
		timerContainer = new WebMarkupContainer("timerContainer") 
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return !(getConfiguration().getSwitchToNextConfigType() == SwitchToNextConfigType.BUTTON);
			}
		};
		timerContainer.setOutputMarkupPlaceholderTag(true);
		timerContainer.add(time);
		timerContainer.add(timeVisibleDropDown);
		
		
		AuthOnlyDropDownChoice switchToNextConfigTypeDropDown = 
			new AuthOnlyDropDownChoice(
				"switchToNextConfigType", 
				Arrays.asList(SwitchToNextConfigType.values()),
				new LocalizedAnswerTypeChoiceRenderer(this)
		);
		
		addFormComponent(url);
		addFormComponent(freetext);
		addFormComponent(switchToNextConfigTypeDropDown);
		addFormComponent(layerVisibleDropDown);
		addFormComponent(timerContainer);
	
		switchToNextConfigTypeDropDown.add(
				AjaxBehaviorFactory.getUpdateBehavior("onchange", MockupConfigurationPanel.this));
	}
	
	private class LocalizedAnswerTypeChoiceRenderer implements IChoiceRenderer 
	{
		private static final long serialVersionUID = 1L;
		MockupConfigurationPanel parent;
		
		public LocalizedAnswerTypeChoiceRenderer(MockupConfigurationPanel parent)
		{
			this.parent = parent;
		}
		
		@Override
		public Object getDisplayValue(Object object) {
			StringResourceModel srm = new StringResourceModel(((SwitchToNextConfigType) object).name(), parent, null);
			
			return srm.getObject();
		}

		@Override
		public String getIdValue(Object object, int index) {
			return ((SwitchToNextConfigType) object).toString();
		}
		
	}
	
	@Override
	public void onUpdate(AjaxRequestTarget target) 
	{
		super.onUpdate(target);
		target.addComponent(timerContainer);
	}
	
	private AuthOnlyDropDownChoice createDropDown(
			String markupId, final String property)
	{
		AuthOnlyDropDownChoice dropDown = 
			new AuthOnlyDropDownChoice(
				markupId,
				new PropertyModel(MockupConfigurationPanel.this, property),
				Arrays.asList(new Boolean[] {true, false}),
				new IChoiceRenderer()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public String getIdValue(Object object, int index)
					{
						return object.toString() + index;
					}
					
					@Override
					public Object getDisplayValue(Object object)
					{
						if(((Boolean) object))
						{
							return new StringResourceModel(
								property, 
								MockupConfigurationPanel.this,
								null).getObject();
						}
						
						return new StringResourceModel(
								property + "Reset", 
								MockupConfigurationPanel.this,
								null).getObject();
					}
				});
		
		dropDown.add(new AjaxFormComponentUpdatingBehavior("onchange") 
		{	
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) 
			{
				getConfiguration().setTimeVisible(timeVisible);

				getConfiguration().setLayerVisible(layerVisible);
				
				MockupConfigurationPanel.this.onUpdate(target);
			}
			
			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator()
			{
				return AjaxBehaviorFactory.getCallDecorator();
			}
		});
		
		dropDown.setOutputMarkupId(true);
		
		return dropDown;
	}
	
	@Override
	protected IModel getTypeModel()
	{
		return new StringResourceModel(
			"mockup_type", MockupConfigurationPanel.this, null);
	}
}

