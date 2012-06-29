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
package com.userweave.pages.configuration.study.details;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.PackageResourceReference;

import com.userweave.module.methoden.questionnaire.page.conf.question.feedbackl.CustomFeedbackPanel;
import com.userweave.pages.base.BaseUserWeavePage;

/**
 * @author oma
 */
@SuppressWarnings("serial")
public abstract class ChangeNamePage extends WebPage {

	private final String name;
	private CustomFeedbackPanel feedbackPanel;

	public ChangeNamePage(final ModalWindow window, final String name) {

		this.name = name;
		
		add(
			new Form("form") {
				{								
					add(new TextField("name", new PropertyModel(ChangeNamePage.this, "name")).setRequired(true));
					
					add(new AjaxButton("ok") {
				           
						@Override
						protected void onError(AjaxRequestTarget target, Form form) {
							target.add(feedbackPanel);
						};
						
						@Override
						protected void onSubmit(AjaxRequestTarget target, Form form) {
							target.add(feedbackPanel);
							changeName(ChangeNamePage.this.name);
							window.close(target);	
						}
			        });
					
					add(feedbackPanel = new CustomFeedbackPanel("feedbackPanel"));
					feedbackPanel.setOutputMarkupId(true);
				}
				
				@Override
				protected void onSubmit() {}
			}
		);
	}
	
	@Override
	public void renderHead(IHeaderResponse response)
	{
		response.renderCSSReference(new PackageResourceReference(
				BaseUserWeavePage.class, "configBase.css"));
	}
	
	protected abstract void changeName(String name);
}
