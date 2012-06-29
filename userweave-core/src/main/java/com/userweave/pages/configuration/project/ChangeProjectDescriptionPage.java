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
package com.userweave.pages.configuration.project;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.dao.ProjectDao;
import com.userweave.domain.Project;
import com.userweave.pages.base.BaseUserWeavePage;
import com.userweave.pages.test.jquery.JQuery;

@Deprecated
public class ChangeProjectDescriptionPage extends WebPage 
{
	@SpringBean
	private ProjectDao projectDao;
	
	private String description;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ChangeProjectDescriptionPage(final Project project, final ModalWindow window)
	{
		if(project.getDescription() != null)
		{	
			this.description = project.getDescription();
		}
		else
		{
			description = "";
		}
		Form form = new Form("form");
		
		add(form);
		
		form.add(new TextArea("description", new PropertyModel(this, "description")));
		
		form.add(new AjaxButton("save")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				project.setDescription(description);
				projectDao.save(project);
				
				window.close(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form)
			{
			}
			
		});
	}
	
	@Override
	public void renderHead(IHeaderResponse response)
	{
		response.renderCSSReference(new PackageResourceReference(
				BaseUserWeavePage.class, "configBase.css"));
		
		response.renderJavaScriptReference(JQuery.getLatest());
		
		response.renderJavaScriptReference(new JavaScriptResourceReference(
				BaseUserWeavePage.class, "hover.js"));
	}
}
