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

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.components.navigation.NavigationBorder;
import com.userweave.components.navigation.NavigationBorder.Color;
import com.userweave.domain.User;
import com.userweave.domain.service.UserService;
import com.userweave.pages.components.headerpanel.HeaderPanel;
import com.userweave.pages.components.headerpanel.LinkButtonComponent;
import com.userweave.pages.components.headerpanel.LinkComponent;
import com.userweave.pages.components.headerpanel.LinkComponentCreator;
import com.userweave.pages.configuration.study.details.AssurancePage;
import com.userweave.pages.login.SignoutPage;
import com.userweave.presentation.model.UserModel;

/**
 * @author ipavkovic
 */
public class UserEditHeaderPanel extends HeaderPanel 
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private UserService userService;
	
	public UserEditHeaderPanel(String id, UserModel model) {
		super(id, new CompoundPropertyModel(model));
		
		addLinks();
	}
	
	private User getUser() {
		return (User) getDefaultModelObject();
	}
	
	@Override
	protected Component createHeaderComponent(String markupId) {
		NavigationBorder nav = new NavigationBorder(markupId, new StringResourceModel("my_account", this, null), Color.Gray);
		
		nav.add(new Label("surname"));
		
		nav.add(new Label("forename"));
		
		return nav;
	}

	private void addLinks() {
		addDisableUserLink();
		addEnableUserLink();
	}

	
	private void addEnableUserLink() {
		addLinkComponent(new LinkComponentCreator() {
			
			@Override
			public LinkComponent create(String markupId) {
				return new LinkButtonComponent(markupId, new StringResourceModel("reactivate", UserEditHeaderPanel.this, null)) {
					
					@Override
					protected AbstractLink createLink(String markupId) {
						return new AjaxLink(markupId) {
							
							@Override
							public void onClick(AjaxRequestTarget target) {
			        			userService.activate(getUser());
								target.addComponent(UserEditHeaderPanel.this);								
							}
							
							@Override
							public boolean isVisible() {
								return getUser().isDeactivated() && UserWeaveSession.get().isAdmin();
							}
						};
					}
				};

			}
		});
		
	}
	
	private boolean disableUser = false;

	private void addDisableUserLink() {
		
		final CustomModalWindow disableUserModalWindow = addLinkModalWindow();
		
		disableUserModalWindow.setPageCreator(new ModalWindow.PageCreator() {
            public Page createPage() {
                return new AssurancePage(disableUserModalWindow) 
                {
					@Override
					protected void onOk(AjaxRequestTarget target) {
						disableUser = true;
					}
					
					@Override
					protected IModel getAcceptLabel()
					{
						return new StringResourceModel(
							"end_membership", UserEditHeaderPanel.this, null);
					}
					
					@Override
					protected IModel getSureModel()
					{
						return new StringResourceModel(
							"sure",UserEditHeaderPanel.this,null);
					}
                };
            }
        });

		disableUserModalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            public void onClose(AjaxRequestTarget target) {
            	
        		if (disableUser) {
        			userService.deactivate(getUser());
        			disableUser = false;
        			if(UserWeaveSession.get().isAdmin()) {
        				target.addComponent(UserEditHeaderPanel.this);
        			} else {
        				setResponsePage(SignoutPage.class);
        			}
        		}
            }
        });

		disableUserModalWindow.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
            public boolean onCloseButtonClicked(AjaxRequestTarget target) {
                return true;
            }
        });
		
		addLinkComponent(new LinkComponentCreator() {
			@Override
			public LinkComponent create(String markupId) {
				return new LinkButtonComponent(markupId, new StringResourceModel("end_membership", UserEditHeaderPanel.this, null)) {
					
					@Override
					protected AbstractLink createLink(String markupId) {
						return new AjaxLink(markupId) {
							
							@Override
							public void onClick(AjaxRequestTarget target) {
								disableUserModalWindow.show(target);
							}
							
							@Override
							public boolean isVisible() {
								return !getUser().isDeactivated();
							}
						};
					}
				};
			}
		});

	}

}

