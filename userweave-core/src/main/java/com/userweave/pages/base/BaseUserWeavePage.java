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
package com.userweave.pages.base;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.PackageResourceReference;

import com.userweave.application.UserWeaveApplication;
import com.userweave.application.UserWeaveSession;
import com.userweave.components.callback.EventHandler;
import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.components.header.UserMenuPanel;
import com.userweave.domain.EntityBase;
import com.userweave.domain.User;
import com.userweave.pages.homepage.ImprintPage;
import com.userweave.pages.test.jquery.JQuery;

/**
 * Base page for all pages, that are not part of the survey.
 * 
 * Defines the Header and Footer of each page and brings 
 * the base layout.
 * 
 * TODO: Remove unnecessary sources from this package
 */
public abstract class BaseUserWeavePage extends WebPage 
{	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Main Navigation
	 */
	private Component breadcrumb;
	
	public Component getBreadcrumb()
	{
		return breadcrumb;
	}

	public void setBreadcrumb(Component breadcrumb)
	{
		this.breadcrumb = breadcrumb;
	}

	/**
	 * Default constructor.
	 */
	public BaseUserWeavePage() 
	{
		addHeaderComponents(null, null);
		
		addFooterComponents();
	}
	
	@Override
	public void renderHead(IHeaderResponse response)
	{
		response.renderJavaScriptReference(JQuery.getLatest());

		response.renderJavaScriptReference(
			new PackageResourceReference(JQuery.class, "jquery.tablesorter.min.js"));
	}
	
	/**
	 * Shortcut to get the current user from the session.
	 * 
	 * @return
	 * 		The current logged in user.
	 */
	public User getUser()
	{
		return UserWeaveSession.get().getUser();
	}
	
	/**
	 * Add a breadcrumb navigation and a user enu panel 
	 * to the header part of this page.
	 * 
	 * @param entity
	 * 		Entity for the breadcrumb panel.
	 * @param callback
	 * 		Callback to fire on event in breadcrumb.
	 */
	protected void addHeaderComponents(final EntityBase entity, final EventHandler callback)
	{
		breadcrumb = getHeaderLinkPanel("breadcrumb", entity, callback);
		
		add(breadcrumb);		
		
		IModel<String> userName;
		
		User user = getUser();
		
		if(user != null && 
		   (user.getForename() != null || user.getSurname() != null)) 
		{
			userName = new Model<String>(user.getForename() + " " + user.getSurname());
		} 
		else 
		{
			userName = new StringResourceModel("guest", this, null);
		}
		
		add(getUserMenuPanel("userMenuPanel", userName));
	}
	
	/**
	 * Creates a homepage link in the header, if not
	 * overriden.
	 * 
	 * @param id
	 * 		Component markup id.
	 * @param entity
	 * 		Entity for breadcrum navigation.
	 * @param callback
	 * 		Callback for breadcrumb navigation
	 * 
	 * @return
	 * 		A bookmarkable page link.
	 */
	protected Component getHeaderLinkPanel(
			String id, EntityBase entity, EventHandler callback)
	{
		BookmarkablePageLink<Void> link = new BookmarkablePageLink<Void>(
				id, UserWeaveApplication.get().getHomePage())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTag(ComponentTag tag)
			{
				tag.setName("a");
				
				super.onComponentTag(tag);
			}
			
			@Override
			public final void onComponentTagBody(MarkupStream markupStream,
					ComponentTag openTag)
			{
				replaceComponentTagBody(markupStream, openTag, "Start");
			}
		};
		
		link.add(new AttributeAppender(
				"class", new Model<String>("homeLinkContainer home"), " "));
		
		return link;
	}
	
	/**
	 * Adds the imprint and contacts link to the footer part
	 * of this page.
	 */
	protected void addFooterComponents()
	{
		add(new BookmarkablePageLink<Void>("imprint", ImprintPage.class));
		
		// Worflow: see #1275
		if(UserWeaveSession.get().isAuthenticated())
		{
			final ModalWindow window = createContactModalWindow();
			
			add(window);
			
			add(new AjaxLink<Void>("contact")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target)
				{
					window.show(target);
				}	
			});
		}
		else
		{
			add(new ExternalLink("contact", "mailto:" + "info@userweave.net"));
			
			add(new WebMarkupContainer("contactModal"));
		}
	}
	
	private ModalWindow createContactModalWindow()
	{
		final CustomModalWindow window = new CustomModalWindow("contactModal");
		
		window.setTitle(new StringResourceModel(
			"contact_feedback", BaseUserWeavePage.this, null));
		
		window.setPageCreator(new ModalWindow.PageCreator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page createPage()
			{
				return new ContactAndFeedbackPage(window);
			}
		});
		
		return window;
	}
	
	protected Component getUserMenuPanel(String id, IModel<String> userModel)
	{
		return new UserMenuPanel(id, userModel);
	}
}