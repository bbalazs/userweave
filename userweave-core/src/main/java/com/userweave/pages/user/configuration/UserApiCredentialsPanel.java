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

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.dao.ApiCredentialsDao;
import com.userweave.dao.UserDao;
import com.userweave.domain.ApiCredentials;
import com.userweave.domain.User;
import com.userweave.domain.util.HashProvider;
import com.userweave.pages.components.button.DefaultButton;
import com.userweave.pages.configuration.study.details.AssurancePage;
import com.userweave.presentation.model.UserModel;

public class UserApiCredentialsPanel extends Panel {

	@SpringBean
	private UserDao userDao;
	
	@SpringBean
	private ApiCredentialsDao apiCredentialsDao;
	
	private boolean reallyDeleteItem = false;
	private boolean reallyGenerateNewHash = false;
	private ModalWindow deleteModalWindow;
	private ModalWindow generateHashModalWindow;
	private final UserModel model;
	final WebMarkupContainer createApiCredentialsContainer;
	final WebMarkupContainer apiCredentialsContainer;
	
	public UserApiCredentialsPanel(String id, UserModel userModel) {
		super(id);
		
		model = userModel;
		
		AddModalWindows();
		
		final FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackPanel");
		feedbackPanel.setOutputMarkupId(true);
		add(feedbackPanel);
		

		createApiCredentialsContainer = new WebMarkupContainer("createApiCredentialsContainer") {
			@Override
			public boolean isVisible() {
				return !hasUserApiCredentials();
			}
		};
		createApiCredentialsContainer.setOutputMarkupPlaceholderTag(true);
		add(createApiCredentialsContainer);
		
		
		apiCredentialsContainer = new WebMarkupContainer("apiCredentialsContainer") {
			@Override
			public boolean isVisible() {
				return hasUserApiCredentials();
			}
		};
		apiCredentialsContainer.setOutputMarkupPlaceholderTag(true);
		add(apiCredentialsContainer);
		
		
		Form form = new Form("form");
		createApiCredentialsContainer.add(form);
		
		form.add(new DefaultButton("createButton", new ResourceModel("createApiCredentials"), form) {

			@Override
			protected void onError(AjaxRequestTarget target, Form form) {
				target.addComponent(feedbackPanel);
			}
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				target.addComponent(feedbackPanel);
				
				// create new apiCredential
				ApiCredentials apiCred = new ApiCredentials();
				apiCred.setHash(HashProvider.uniqueUUID());
				apiCred.setActiveLastChange( new DateTime() );
				apiCredentialsDao.save(apiCred);
				
				User user = model.getObject();
				user.setApiCredentials(apiCred);
				userDao.save(user);
				
				apiCredentialsContainer.setVisible(true);
				createApiCredentialsContainer.setVisible(false);
				target.addComponent(apiCredentialsContainer);
				target.addComponent(createApiCredentialsContainer);
			};
		});
		
		
		
		
		apiCredentialsContainer.add( new Label("apiAuthHash", new PropertyModel(model, "apiCredentials.hash")));
		apiCredentialsContainer.add(new AjaxLink("genHash"){

			@Override
			public void onClick(AjaxRequestTarget target) {
				target.addComponent(feedbackPanel);
				
				generateHashModalWindow.show(target);
			}
			
			@Override
			public boolean isVisible() {
				return UserWeaveSession.get().isAdmin();
			}
        });
		
		
		WebMarkupContainer activeAdminContainer = new WebMarkupContainer("activeAdminContainer") {
			@Override
			public boolean isVisible() {
				return UserWeaveSession.get().isAdmin();
			}
		};
		apiCredentialsContainer.add(activeAdminContainer);
		final AjaxCheckBox activeChkBx = new AjaxCheckBox("active", new PropertyModel(model, "apiCredentials.active")){

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.addComponent(feedbackPanel);
				userDao.save(model.getObject());
			}
        };
        activeChkBx.setOutputMarkupId(true);
        activeAdminContainer.add(activeChkBx);
        
        
        WebMarkupContainer activeContainer = new WebMarkupContainer("activeContainer") {
			@Override
			public boolean isVisible() {
				return !UserWeaveSession.get().isAdmin();
			}
		};
		apiCredentialsContainer.add(activeContainer);
		String dateString = new StringResourceModel("noActiveDate", this, null).getString();
		if (model.getObject().getApiCredentials() != null && model.getObject().getApiCredentials().getActiveLastChange() != null) {
			DateTimeFormatter dateDTF = DateTimeFormat.longDate();
		    DateTimeFormatter usFmt = dateDTF.withLocale(UserWeaveSession.get().getLocale());
		    dateString = model.getObject().getApiCredentials().getActiveLastChange().toString(usFmt);
		}
		
        Label activeLabel = new Label("activeText", new StringResourceModel("deactiveFormat", this, null, new Object[]{ dateString } ));
        if (model.getObject().getApiCredentials() != null && model.getObject().getApiCredentials().isActive()) {
        	activeLabel.setDefaultModel( new StringResourceModel("activeFormat", this, null, new Object[]{ dateString } ) );
        }
        activeContainer.add(activeLabel);
		
        
        WebMarkupContainer deleteContainer = new WebMarkupContainer("deleteContainer") {
			@Override
			public boolean isVisible() {
				return UserWeaveSession.get().isAdmin();
			}
		};
        apiCredentialsContainer.add(deleteContainer);
        deleteContainer.add(new AjaxLink("delete"){
			@Override
			public void onClick(AjaxRequestTarget target) {
				target.addComponent(feedbackPanel);
				
				deleteModalWindow.show(target);
			}
        });
	}
	
	private void AddModalWindows()
	{
		deleteModalWindow = new CustomModalWindow("deleteItemModalWindow");
		add(deleteModalWindow);

		deleteModalWindow.setInitialHeight(250);
		deleteModalWindow.setInitialWidth(350);
		
		final IModel sureModel = new StringResourceModel("sure", this, null);
		final IModel yesModel = new StringResourceModel("yes", this, null);
		
		// use AssurancePage for delete reconfirmation dialog
		deleteModalWindow.setPageCreator(new ModalWindow.PageCreator() {
			public Page createPage() {
				return new AssurancePage(deleteModalWindow) 
				{
					@Override
					protected void onOk(AjaxRequestTarget target) {
						reallyDeleteItem = true;
					}
					
					@Override
					protected IModel getSureModel()
					{
						return sureModel;
					}
					
					@Override
					protected IModel getAcceptLabel()
					{
						return yesModel;
					}
				};
			}
		});

		// reset delete item on closing
		deleteModalWindow
				.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
					public void onClose(AjaxRequestTarget target) {
						if(reallyDeleteItem) {
							reallyDeleteItem = false;
							
							ApiCredentials api = model.getObject().getApiCredentials();
							model.getObject().setApiCredentials(null);
							userDao.save(model.getObject());
							
							apiCredentialsDao.delete(api);
							
							apiCredentialsContainer.setVisible(false);
							createApiCredentialsContainer.setVisible(true);
							target.addComponent(apiCredentialsContainer);
							target.addComponent(createApiCredentialsContainer);
						}
					}
				});

		deleteModalWindow
				.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
					public boolean onCloseButtonClicked(AjaxRequestTarget target) {
						return true;
					}
				});
		
		
		
		
		
		
		generateHashModalWindow = new CustomModalWindow("generateHashModalWindow");
		add(generateHashModalWindow);

		generateHashModalWindow.setInitialHeight(250);
		generateHashModalWindow.setInitialWidth(350);
		
		final IModel sureGenHashModel = new StringResourceModel("sureGenHash", this, null);
		final IModel yesGenHashModel = new StringResourceModel("yesGenHash", this, null);
		
		// use AssurancePage for delete reconfirmation dialog
		generateHashModalWindow.setPageCreator(new ModalWindow.PageCreator() {
			public Page createPage() {
				return new AssurancePage(generateHashModalWindow) 
				{
					@Override
					protected void onOk(AjaxRequestTarget target) {
						reallyGenerateNewHash = true;
					}
					
					@Override
					protected IModel getSureModel()
					{
						return sureGenHashModel;
					}
					
					@Override
					protected IModel getAcceptLabel()
					{
						return yesGenHashModel;
					}
				};
			}
		});

		// reset delete item on closing
		generateHashModalWindow
				.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
					public void onClose(AjaxRequestTarget target) {
						if(reallyGenerateNewHash) {
							reallyGenerateNewHash = false;
							if (model.getObject().getApiCredentials() != null) {
								model.getObject().getApiCredentials().setHash(HashProvider.uniqueUUID());
								userDao.save(model.getObject());
							}
						}
					}
				});

		//
		generateHashModalWindow
				.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
					public boolean onCloseButtonClicked(AjaxRequestTarget target) {
						return true;
					}
				});
	}
	
	
	private boolean hasUserApiCredentials() {
		Object o = model.getObject().getApiCredentials();
		
		if (o == null) {
			return false;
		}
		else {
			return true;
		}
	}

}
