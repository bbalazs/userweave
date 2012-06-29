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
import org.apache.wicket.request.resource.PackageResourceReference;

import com.userweave.pages.base.BaseUserWeavePage;

@Deprecated
public abstract class DeleteProjectPage extends WebPage
{
	private static final long serialVersionUID = 1L;

	public DeleteProjectPage(final ModalWindow window)
	{
		Form form = new Form("form");
		
		add(form);
		
		form.add(new AjaxButton("okButton")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				DeleteProjectPage.this.onOk(target);
				window.close(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form)
			{
				// do nothing
			}
			
		});
		
		form.add(new AjaxButton("chancelButton")
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				window.close(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form)
			{
				// do nothing
			}
		});
	}
	
	@Override
	public void renderHead(IHeaderResponse response)
	{
		response.renderCSSReference(new PackageResourceReference(
				BaseUserWeavePage.class, "configBase.css"));
	}
	
	public abstract void onOk(AjaxRequestTarget target);
}
