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
package com.userweave.components.customModalWindow;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.protocol.http.ClientProperties;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.request.ClientInfo;
import org.apache.wicket.request.resource.PackageResourceReference;

import com.userweave.pages.base.BaseUserWeavePage;
import com.userweave.pages.homepage.BaseHomepage;

/**
 * Base class for all modal window web pages.
 * 
 * @author opr
 */
public class BaseModalWindowPage extends WebPage
{
	private static final long serialVersionUID = 1L;

	/**
	 * Form to contain child components.
	 */
	private final Form<Void> form = new Form<Void>("form");
	
	/**
	 * Link to accept changes on the page and to close the window.
	 */
	private WebMarkupContainer acceptLink;
	
	public WebMarkupContainer getAcceptLink()
	{
		return acceptLink;
	}
	
	protected void setAcceptLink(WebMarkupContainer acceptLink)
	{
		this.acceptLink = acceptLink;
	}
	
	/**
	 * The users client properties. Used to identify IE7.
	 */
	private ClientProperties clientProperties = null;
	
	/**
	 * Default constructor.
	 * 
	 * @param window
	 * 		The ModalWindow this page is attached to.
	 */
	public BaseModalWindowPage(final ModalWindow window)
	{
		init(window);
	}

	/**
	 * Constructor with no reference to the ModalWindow. Make sure
	 * you override onAccept() and onDecline().
	 */
	public BaseModalWindowPage()
	{
		init(null);
	}
	
	/**
	 * @param window
	 */
	private void init(final ModalWindow window)
	{		
		add(form);
		
		
		acceptLink = getAcceptButton("accept", window);
		
		acceptLink.add(new Label("accept_label", getAcceptLabel()));
		
		add(acceptLink);
		
		
		WebMarkupContainer declineLink = getDeclineButton("decline", window);
		
		declineLink.add(new Label("decline_label", getDeclineLabel()));
		
		add(declineLink);
	}
	
	@Override
	public void renderHead(IHeaderResponse response)
	{
		response.renderCSSReference(new PackageResourceReference(
				BaseUserWeavePage.class, "res/PopupBaseLayout.css"));
		
		if(isIe())
		{
			response.renderCSSReference(new PackageResourceReference(BaseHomepage.class, "res/iehacks.css"));
		}
				
		if(isIe7())
		{
			response.renderCSSReference(new PackageResourceReference(
					BaseHomepage.class, "res/ie7hacks.css"));
		}
	}
	
	protected Form<?> getForm()
	{
		return form;
	}
	
	protected void addToForm(Component child)
	{
		form.add(child);
	}
	
	protected WebMarkupContainer getAcceptButton(String componentId, final ModalWindow window)
	{
		return new AjaxLink<Void>(componentId)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				if(window != null)
				{
					window.close(target);
				}
				else
				{
					onAccept(target);
				}
			}
		};
	}
	
	protected IModel<String> getAcceptLabel()
	{
		return new StringResourceModel("accept", this, null);
	}
	
	protected WebMarkupContainer getDeclineButton(String componentId, final ModalWindow window)
	{
		return new AjaxLink<Void>(componentId)
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				if(window != null)
				{
					window.close(target);
				}
				else
				{
					onDecline(target);
				}
			}
		};
	}
	
	protected IModel<String> getDeclineLabel()
	{
		return new StringResourceModel("decline", this, null);
	}
	
	protected ClientProperties getClientProperties()
	{
		if(clientProperties == null)
		{
			ClientInfo clientInfo = WebSession.get().getClientInfo();	      
			if( clientInfo == null || !(clientInfo instanceof WebClientInfo) )
			{
				clientInfo = new WebClientInfo( getRequestCycle() );
				WebSession.get().setClientInfo( clientInfo );
			}
			clientProperties = ((WebClientInfo) clientInfo).getProperties();
		}
		
		return clientProperties;
	}
	
	protected void onAccept(AjaxRequestTarget target){}
	
	protected void onDecline(AjaxRequestTarget target){}
	
	protected boolean isIe()
	{
		return (getClientProperties().isBrowserInternetExplorer() 
				&& getClientProperties().getBrowserVersionMajor() <= 8);
	}
	
	protected boolean isIe7() 
	{
		return (getClientProperties().isBrowserInternetExplorer() 
				&& getClientProperties().getBrowserVersionMajor() == 7);
	}
}
