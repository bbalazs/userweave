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
package com.userweave.pages.configuration.editentitypanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.dao.ProjectDao;
import com.userweave.domain.EntityBase;
import com.userweave.domain.Project;

/**
 * Web Page to edit the project title and
 * the project description.
 * 
 * @author opr
 *
 */
@Deprecated
public abstract class EditProjectEntityWebPage extends WebPage
{
	@SpringBean
	private ProjectDao projectDao;

	/**
	 * Default constructor.
	 * 
	 * @param project
	 * 		Project to edit
	 * @param modal
	 * 		Modal window to which this page is attached.
	 */
	public EditProjectEntityWebPage(Project project, final ModalWindow modal)
	{
		setDefaultModel(new CompoundPropertyModel(project));
		
		Form form = new Form("form");
		
		add(form);
		
		form.add(new TextField("name"));
		
//		form.add(new TextArea("description"));
		
		form.add(new AjaxButton("submit", form)
		{
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form)
			{
				Project project = (Project)EditProjectEntityWebPage.this.getDefaultModelObject();
				
				projectDao.save(project);
				
				modal.close(target);
				
				EditProjectEntityWebPage.this.onSubmit(target, project);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form)
			{
			}
		});
	}
	
	protected abstract void onSubmit(AjaxRequestTarget target, EntityBase entity);
}
