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
package com.userweave.pages.user.configuration;

import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.resolver.IComponentResolver;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.ajax.IAjaxUpdater;
import com.userweave.application.UserWeaveSession;
import com.userweave.dao.UserDao;
import com.userweave.presentation.model.UserModel;

@SuppressWarnings("serial")
public abstract class UserEditBasePanel 
	extends UserBasePanel 
	implements IAjaxUpdater, IComponentResolver 
{

	@SpringBean
	private UserDao userDao;

	private Form<Void> form;
	
	public UserEditBasePanel(String id, UserModel userModel) {
		super(id, new CompoundPropertyModel(userModel));

		setOutputMarkupId(true);
		
		super.add(form = new Form<Void>("form"));
	
		form.add(new FeedbackPanel("feedback"));
	}
	
	protected void onError(AjaxRequestTarget target) {
		target.add(UserEditBasePanel.this);	
	}

	protected void onSubmit(AjaxRequestTarget target) 
	{
		userDao.save(getUser());
		
		 Locale currentLocale = getLocale();
		 UserWeaveSession.get().setLocaleForUser();
		 
		 if(currentLocale.equals(getLocale())) {
			 target.add(this);
		 } else { 
			 setResponsePage(getPage());
		 }		
	}

	protected Form<Void> getForm() 
	{
		return form;
	}
	
	@Override
	public void onUpdate(AjaxRequestTarget target)
	{
		onSubmit(target);
	}
	
	@Override
	public MarkupContainer add(Component... childs)
	{
		return form.add(childs);
	}
	
	@Override
	public Component resolve(MarkupContainer container, MarkupStream markupStream, ComponentTag tag)
	{
		if(tag.getId().equals("form"))
		{
			return getForm();
		}
		
		Component resolvedComponent = getParent().get(tag.getId());
		if (resolvedComponent != null && getPage().wasRendered(resolvedComponent))
		{
			return null;
		}
		return resolvedComponent;
	}
}
