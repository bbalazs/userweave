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
package com.userweave.pages.configuration.editentitypanel.webpages;

import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.customModalWindow.BaseModalWindowPage;
import com.userweave.module.methoden.questionnaire.dao.QuestionnaireConfigurationDao;
import com.userweave.module.methoden.questionnaire.domain.QuestionnaireConfigurationEntity;
import com.userweave.module.methoden.questionnaire.domain.group.QuestionnaireGroup;
import com.userweave.module.methoden.questionnaire.domain.question.Question;
import com.userweave.module.methoden.questionnaire.page.grouping.GroupAddedCallback;
import com.userweave.module.methoden.questionnaire.page.grouping.QuestionnaireGroupType;
import com.userweave.module.methoden.questionnaire.page.grouping.QuestionnaireGroupingPanelFactoryImpl;
import com.userweave.pages.grouping.GroupingPanel;
import com.userweave.presentation.model.SpringLoadableDetachableModel;

public abstract class FilterWebPage extends BaseModalWindowPage
{
	@SpringBean
	private QuestionnaireConfigurationDao questionnaireConfigurationDao;
	
	private final GroupingPanel panel;
	
	private final GroupAddedCallback<QuestionnaireGroup> callback;
	
	public FilterWebPage(final Question question, Locale locale, final ModalWindow window)
	{	
		super(window);
		
		setDefaultModel(
				new SpringLoadableDetachableModel(
						questionnaireConfigurationDao, 
						question.getConfiguration()));
		
		callback = new GroupAddedCallback<QuestionnaireGroup>() 
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onAdd(AjaxRequestTarget target, QuestionnaireGroup group) 
			{
				FilterWebPage.this.onAdd(group);
				FilterWebPage.this.onAfterAdd();
				
				window.close(target);
			}
		};
		
		QuestionnaireGroupType groupType = 
			new QuestionnaireGroupType(question, locale);
		
		panel = 
			new QuestionnaireGroupingPanelFactoryImpl().createGroupingPanel(
					"filterPanel", 
					groupType, 
					locale,
					callback
			);
		
		addToForm(panel);
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
				panel.submit();
				callback.onAdd(target, (QuestionnaireGroup)panel.getGroup());
				
				window.close(target);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form form) 
			{
				target.addComponent(panel.get("feedback"));
			}
		};
	}
	
	@Override
	protected IModel getAcceptLabel()
	{
		return new StringResourceModel("submitFilter", this, null);
	}
	
	private void onAdd(QuestionnaireGroup group)
	{
		getConfiguration().getGroups().add(group);
		
		questionnaireConfigurationDao.save(getConfiguration());
	}
	
	private QuestionnaireConfigurationEntity getConfiguration() 
	{
		return (QuestionnaireConfigurationEntity) getDefaultModelObject();
	};
	
	protected abstract void onAfterAdd();
}
