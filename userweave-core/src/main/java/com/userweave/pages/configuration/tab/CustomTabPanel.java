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
/**
 * 
 */
package com.userweave.pages.configuration.tab;

import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.pages.components.ajaxindicator.AjaxIndicator;
import com.userweave.pages.components.userfeedback.UserFeedbackPage;
import com.userweave.pages.login.SignoutPage;
import com.userweave.pages.user.invitation.InviteUserPage;

/**
 * @author Oliver Prinz
 *
 */
@Deprecated
@SuppressWarnings("serial")
public class CustomTabPanel extends AjaxTabbedPanel {

	/**
	 * @param id
	 * @param tabs
	 */
	public CustomTabPanel(String id, List tabs) {
		super(id, tabs);
		
		add(createSignoutLink("logout"));
	
		addFeedbackLink();
		
		addInviteLink();
		
		add(new AjaxIndicator("ajaxIndicator"));
	}

	private ModalWindow inviteModalWindow;

	private void addInviteLink() {
		inviteModalWindow = new CustomModalWindow("inviteModalWindow");

		inviteModalWindow.setInitialWidth(563);
		inviteModalWindow.setInitialHeight(407);
		inviteModalWindow.setPageCreator(new ModalWindow.PageCreator() {
			@Override
            public Page createPage() {
                return new InviteUserPage(inviteModalWindow);
            }
        });
		
		add(inviteModalWindow);

		add(new AjaxLink("invite") {
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				inviteModalWindow.show(target);
			}
		});
		
	}

	private ModalWindow userFeedbackModalWindow;

	private void addFeedbackLink() {
		
		userFeedbackModalWindow = new CustomModalWindow( "feedbackModalWindow" );
		
		userFeedbackModalWindow.setInitialHeight(395);
		
		userFeedbackModalWindow.setPageCreator(new ModalWindow.PageCreator() {
			@Override
            public Page createPage() {
                return new UserFeedbackPage(userFeedbackModalWindow);
            }
        });
		
		add(userFeedbackModalWindow);
		
		add(new AjaxLink("feedback") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				userFeedbackModalWindow.show(target);
			}
		});

	}
	
	private BookmarkablePageLink createSignoutLink(String linkId) {
		return new BookmarkablePageLink(linkId, SignoutPage.class) {
			@Override
			public boolean isVisible() {
				return UserWeaveSession.get().isAuthenticated();
			}
		};
	}

	public ModalWindow getInviteModalWindow() {
		return inviteModalWindow;
	}
}