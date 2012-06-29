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
package com.userweave.module.methoden.questionnaire.page.conf;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.PackageResourceReference;

import com.userweave.components.customModalWindow.BaseModalWindowPage;
import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.module.ModuleConfiguration;
import com.userweave.module.methoden.questionnaire.page.QuestionConfigurationPanelFactory;
import com.userweave.pages.base.BaseUserWeavePage;
import com.userweave.pages.components.servicePanel.ServicePanel;
import com.userweave.pages.components.servicePanel.ServicePanel.ServicePanelType;

/**
 * @author oma
 */
@SuppressWarnings("serial")
@Deprecated
public abstract class AddQuestionPage extends BaseModalWindowPage {

	private final Form form;

	private String name;
	
	private final List<String> questionTypes;
	
	private String selectedQuestionType;
	
	private FeedbackPanel feedbackPanel;
	
	public AddQuestionPage(final CustomModalWindow window) {
	
		super(window);
		
		questionTypes = new QuestionConfigurationPanelFactory().getQuestionTypes();
		
		form = new Form("form") {
			{								
				add(new TextField("name", new PropertyModel(AddQuestionPage.this, "name")).setRequired(true));
				
				add( 
					new DropDownChoice("questionTypes", 
						new PropertyModel(AddQuestionPage.this, "selectedQuestionType"), 
						new PropertyModel(AddQuestionPage.this, "questionTypes"),
						new LocalizedQuestionTypesChoiceRenderer("toString", this)
					).setRequired(true)
				);
				
				add(new AjaxButton("ok") {
			           
					@Override
					protected void onError(AjaxRequestTarget target, Form form) {
						target.addComponent(feedbackPanel);
					}
					
					@Override
					protected void onSubmit(AjaxRequestTarget target, Form form) {						
						target.addComponent(feedbackPanel);
						window.close(target);	
						addQuestion(target, name, selectedQuestionType);
					}
		        });
			}
			
			@Override
			protected void onSubmit() {}
		};
		
		 add(form);
		 
		 add(feedbackPanel = new FeedbackPanel("feedback"));
		 
		 feedbackPanel.setOutputMarkupId(true);
		 
		 add(new ServicePanel("servicePanel", ServicePanelType.NEW_METHODS));
	}

	@Override
	public void renderHead(IHeaderResponse response)
	{
		response.renderCSSReference(new PackageResourceReference(
				BaseUserWeavePage.class,
				"configBase.css"));	
	}
	
	public abstract void addQuestion(AjaxRequestTarget target, String name, String questionType);
	
	protected void onAdd(ModuleConfiguration moduleConfiguration, AjaxRequestTarget target) {};
	
	private class LocalizedQuestionTypesChoiceRenderer extends ChoiceRenderer
	{
		private final Component parent;
		
		public LocalizedQuestionTypesChoiceRenderer(
				String displayExpression, Component parent)
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
			Object displayValue = super.getDisplayValue(object);
			
			String stringDisplayValue = ((String) displayValue).replace(" ", "_");
			
			StringResourceModel model = new StringResourceModel(
					stringDisplayValue, parent, null);
			
			return model.getObject();
		}
	}
}
