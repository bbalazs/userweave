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
package com.userweave.pages.user;

import java.util.ArrayList;

import org.apache.wicket.Page;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.pages.homepage.MainContent;

/**
 * 
 * @author opr
 *
 */
@SuppressWarnings("serial")
public class RegisterPanel extends Panel {

	private CustomModalWindow interestListModal;
	
	public RegisterPanel(String id, Form form) {
		this(id, form, true);
	}
	
	public RegisterPanel(String id, Form form, boolean isContainerVisible) {
		super(id);
		init(form, isContainerVisible);
	}
	
	protected void init(Form form, boolean isContainerVisible) {
		
		initMessages();
		add(new FeedbackPanel("feedback").setOutputMarkupId(true));

		BookmarkablePageLink link = 
			new BookmarkablePageLink("back_to_login", MainContent.class);
		
		add(link);
		
		setOutputMarkupId(true);
		
		form.add(new Label("invitationCode",
				new StringResourceModel("invitation_code", this, null)).setVisible(isContainerVisible));
		
		// FIXME: Redirect link to a page where user can show his interest(page accessed by joomla too)
		addLinkAndModalWindow(form, isContainerVisible);
	}
	
	protected void initMessages() {
		ArrayList<StringResourceModel> list = new ArrayList<StringResourceModel>(4);
		
		list.add(new StringResourceModel(
				"happy", this, null));
		list.add(new StringResourceModel(
				"register_note", this, null));
		list.add(new StringResourceModel(
				"please_enter_email", this, null));
		
		add(new MessageRepeater("init_messages", list));
	}
	
	protected void addLinkAndModalWindow(Form form, boolean isVisible) {
		WebMarkupContainer container = new WebMarkupContainer("containerForInterests");
		
//		container.add(new AjaxLink("interestList")
//		{
//
//			@Override
//			public void onClick(AjaxRequestTarget target) {
//				interestListModal.show(target);
//				
//			}
//			
//		});
		
		//addInterstListModalWindow(container);
		
		form.add(container);
		
		container.setVisible(isVisible);
	}
	
	private void addInterstListModalWindow(WebMarkupContainer container) {
		interestListModal = new CustomModalWindow("interestListModalWindow");
		
		interestListModal.setPageCreator(new ModalWindow.PageCreator() {
            public Page createPage() 
            {
            	return new InterestListPage(interestListModal);
            };
        });
		
		container.add(interestListModal);
	}
	
}
