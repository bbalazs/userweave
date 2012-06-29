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

import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.resource.PackageResourceReference;

import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.module.methoden.questionnaire.page.conf.question.feedbackl.CustomFeedbackPanel;
import com.userweave.pages.base.BaseUserWeavePage;

/**
 * @author Florian Pavkovic
 */
@SuppressWarnings("serial")
@Deprecated
public abstract class ChangeLocalizedNamePage extends WebPage {
	
	private CustomFeedbackPanel feedbackPanel;
	protected boolean changeName;

	public ChangeLocalizedNamePage(final ModalWindow window, final LoadableDetachableModel questionModel, final Locale studyLocale) {

		changeName = false;
		add(
			new Form("form") {
				{
					add(new TextField("name", new LocalizedPropertyModel(questionModel, "name", studyLocale)).setRequired(true));				
					
					add(feedbackPanel = new CustomFeedbackPanel("feedbackPanel"));
					feedbackPanel.setOutputMarkupId(true);
					
					add(new AjaxButton("ok") {
				           
						@Override
						protected void onError(AjaxRequestTarget target, Form form) {
							target.add(feedbackPanel);
						};
						
						@Override
						protected void onSubmit(AjaxRequestTarget target, Form form) {	
							target.add(feedbackPanel);
							changeName();
							changeName = true;
							window.close(target);	
						}
			        });
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
	
	protected abstract void changeName();
}
