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
package com.userweave.components.userpanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.callback.EntityEvent;
import com.userweave.components.callback.EventHandler;
import com.userweave.dao.UserDao;
import com.userweave.domain.User;

@SuppressWarnings("serial")
@Deprecated
public class SelectUserPanel extends Panel {

	@SpringBean
	private UserDao userDao;
	
	private final User user;

	public SelectUserPanel(String id, User user, final EventHandler callback) {
		super(id);
		
		this.user = user;
		
		Form form = new Form("form");
		add(form);

		DropDownChoice DDC = new DropDownChoice("choices", new PropertyModel(this,"user"), userDao.findAllByEmail(), new ChoiceRenderer("email", "id"));
		form.add(DDC);
		
		DDC.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				EntityEvent.Selected(target, SelectUserPanel.this.user).fire(callback);
			}
		});
		
	}
}

 