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
package com.userweave.pages.configuration.study.moduleConfigurations;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.dao.StudyDao;
import com.userweave.domain.service.ModuleService;
import com.userweave.module.Module;
import com.userweave.module.ModuleConfiguration;
import com.userweave.pages.base.BaseUserWeavePage;
import com.userweave.pages.components.servicePanel.ServicePanel;
import com.userweave.pages.components.servicePanel.ServicePanel.ServicePanelType;

/**
 * @author oma
 */
@SuppressWarnings("serial")
@Deprecated
public class AddModuleConfigurationPage extends WebPage {

	private transient final Module selectedModule = null;

	@SpringBean
	private StudyDao studyDao;
		
	@SpringBean
	private ModuleService moduleService;
	
	private String name;

	private final Form form;

	private final int studyId;
	
	private FeedbackPanel feedbackPanel;
	
	public AddModuleConfigurationPage(final ModalWindow window, final int studyId) {

		this.studyId = studyId;
		
		form = new Form("form") {
			{								
				add(new TextField("name", new PropertyModel(AddModuleConfigurationPage.this, "name")).setRequired(true));
				
				add( 
					new DropDownChoice("modules", 
						new PropertyModel(AddModuleConfigurationPage.this, "selectedModule"),
						new LoadableDetachableModel() {

							@Override
							protected Object load() {
								return moduleService.getActiveModules();
							}
						},
						//new ChoiceRenderer("name")
						new LocalizedModuleNameChoiceRenderer("name", this)
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
						addStudy(target);
					}
		        });
			}
			
			@Override
			protected void onSubmit() {
				
			}
		};
		
		 add(form);
		 
		 add(feedbackPanel = new FeedbackPanel("feedback"));
		 
		 feedbackPanel.setOutputMarkupId(true);
		 
		 add(new ServicePanel("servicePanel", ServicePanelType.NOT_SURE_QUESTIONS));
	}

	@Override
	public void renderHead(IHeaderResponse response)
	{
		// add css for CustomModalWindow
		response.renderCSSReference(
			new PackageResourceReference(BaseUserWeavePage.class, "configBase.css"));
	}
	
	public int addStudy(AjaxRequestTarget target) {
		if (selectedModule != null) {
			ModuleConfiguration moduleConfiguration = moduleService.createNewConfigurationInStudyForModule(selectedModule.getModuleId(), studyDao.findById(studyId));
			moduleConfiguration.setName(name);
			moduleConfiguration.save();				
			onAdd(moduleConfiguration, target);
		}
		return 1;
	}
	
	protected void onAdd(ModuleConfiguration moduleConfiguration, AjaxRequestTarget target) {};

	private class LocalizedModuleNameChoiceRenderer extends ChoiceRenderer
	{
		private final Component parent;
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
			Object displayValue = super.getDisplayValue(object);
			
			String stringDisplayValue = ((String) displayValue).replace(" ", "_");
			
			StringResourceModel model = new StringResourceModel(
					stringDisplayValue, parent, null);
			
			return model.getObject();
		}
	}
}
