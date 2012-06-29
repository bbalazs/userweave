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
package com.userweave.pages.configuration.study.selection;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.customModalWindow.BaseModalWindowPage;
import com.userweave.dao.StudyDao;
import com.userweave.domain.Study;
import com.userweave.domain.service.StudyService;

/**
 * @author oma
 */
public abstract class EditStudyPage extends BaseModalWindowPage 
{
	@SpringBean
	private StudyService studyService;
	
	@SpringBean
	private StudyDao studyDao;
	
	private Panel displayComponent;
	
	private AjaxButton button;

	private WebMarkupContainer declineButton;
	
	private boolean isConfigShown = true;
	
	public EditStudyPage(
		final ModalWindow window, final Study study) 
	{
		super(window);
		
		displayComponent = 
			new EditStudyPageConfigurationPanel("displayComponent", study, window);
		 
		addToForm(displayComponent);
		
		displayComponent.setOutputMarkupId(true);
	}

	@Override
	protected WebMarkupContainer getAcceptButton(String componentId,
			final ModalWindow window)
	{
		AjaxButton button = new AjaxButton(componentId, getForm()) 
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form form) 
			{
				target.addComponent(displayComponent.get("feedback"));
			}
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) 
			{
				if(isConfigShown)
				{
					Study study = ((EditStudyPageConfigurationPanel) displayComponent).getStudy();
					
					if(studyService.isAtLeastOneAdminRegistered(study.getParentProject()))
					{
						studyDao.save(study);

						EditStudyPage.this.onFinish(target, study);
						
						window.close(target);
					}
				}
				else
				{
					replaceContent(target, window);
				}				
			}
        };
        
        button.setOutputMarkupId(true);
        
        return button;
	}
	
	@Override
	protected WebMarkupContainer getDeclineButton(String componentId,
			ModalWindow window)
	{
		declineButton = super.getDeclineButton(componentId, window);
	
		declineButton.setOutputMarkupId(true);
		
		return declineButton;
	}
	
	@Override
	protected IModel getDeclineLabel()
	{
		return super.getDeclineLabel();
	}
	
	private void replaceContent(AjaxRequestTarget target, final ModalWindow window)
	{
		isConfigShown = false;
		
		button.setVisible(false);
		
		WebMarkupContainer newDeclineButton = 
			super.getDeclineButton(declineButton.getId(), window);
		
		newDeclineButton.add(new Label("decline_label", "ACCEPT"));
		
		declineButton.replaceWith(newDeclineButton);
		declineButton = newDeclineButton;
		
		Panel replacement = new NotRegisteredInfoPanel("displayComponent");
		
		displayComponent.replaceWith(replacement);
		displayComponent = replacement;
		displayComponent.setOutputMarkupId(true);
		
		target.addComponent(button);
		target.addComponent(declineButton);
		target.addComponent(displayComponent);
	}
	
//	private List<Locale> getApplicationSupportedLocales()
//	{
//		return LocalizationUtils.getSupportedStudyLocales();
//	}
	
	protected abstract void onFinish(AjaxRequestTarget target, Study study);
}
