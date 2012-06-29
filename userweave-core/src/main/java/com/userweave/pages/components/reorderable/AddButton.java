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
package com.userweave.pages.components.reorderable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.domain.Role;

/**
 * @author oma
 */
@Deprecated
@SuppressWarnings("serial")
@AuthorizeAction(action = Action.RENDER, roles = Role.PROJECT_ADMIN)
public abstract class AddButton extends Panel {
	
	public static final String ADD_METHOD = "add_method";
	public static final String ADD_QUESTION = "add_question";
	public static final String ADD_TERM = "add_term";
	public static final String ADD_ANSWER = "add_answer";
	public static final String ADD_DIMENSION = "add_dimension";
	
	public AddButton(String id, IModel buttonTextModel) {
		super(id);		
		init(buttonTextModel);		   
	}

	public AddButton(String id) {
		super(id);
		init(new StringResourceModel(ADD_METHOD, this, null));
	}

	public AddButton(String id, String resourceString)
	{
		super(id);
		init(new StringResourceModel(resourceString, AddButton.this, null));
	}
	
	private void init(IModel buttonTextModel) {
		add(
			new AjaxLink("add") {
	            @Override
	            public void onClick(AjaxRequestTarget target) {
	            	onAdd(target);
	            }
			}.add(new Label("text",buttonTextModel))
	   );
	}
	
	protected abstract void onAdd(AjaxRequestTarget target);
}

