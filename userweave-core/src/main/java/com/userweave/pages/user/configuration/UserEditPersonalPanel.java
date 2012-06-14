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

import java.util.Arrays;
import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.ajax.form.AjaxBehaviorFactory;
import com.userweave.application.UserWeaveSession;
import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.components.localerenderer.LocaleChoiceRenderer;
import com.userweave.dao.UserDao;
import com.userweave.domain.User;
import com.userweave.domain.service.UserService;
import com.userweave.pages.administration.ChangePasswordPage;
import com.userweave.pages.configuration.study.details.AssurancePage;
import com.userweave.pages.login.SignoutPage;
import com.userweave.presentation.model.UserModel;
import com.userweave.utils.LocalizationUtils;

public class UserEditPersonalPanel extends UserEditBasePanel 
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private UserService userService;
	
	@SpringBean
	private UserDao userDao;
	
//	private WebMarkupContainer container;
//	private DropDownChoice position;
//	private TextField employment;
//	private TextField sector;
	
	private CustomModalWindow changePasswordModal, endMembershipModal;
	
	private boolean disableUser = false;
	
	private Boolean isReceivingNews = false;
	
	private final Locale selectedLocale;
	
	public UserEditPersonalPanel(String id, UserModel model) 
	{
		super(id, model);
	
		isReceivingNews = getUser().isReceiveNews();
		
		selectedLocale = getUser().getLocale();
		
		addActions();
		
		addModalDialogs();
		
//		RadioChoice choice = 
//			new RadioChoice(
//				"gender", Arrays.asList(User.Gender.values()),
//				new LocalizedGenderChoiceRenderer(this));
//		
//		choice.add(getFormChoiceComponentUpdatingBehavior());
//		
//		getForm().add(choice);
		
		Label email = new Label("email");
		
		getForm().add(email);
		
		
		TextField surname = new TextField("surname");
		
		surname.setRequired(true);
		
		surname.add(AjaxBehaviorFactory.getUpdateBehavior("onblur", UserEditPersonalPanel.this));
		
		getForm().add(surname);
		
		
		TextField forename = new TextField("forename");
		
		forename.setRequired(true);
		
		forename.add(AjaxBehaviorFactory.getUpdateBehavior("onblur", UserEditPersonalPanel.this));
		
		getForm().add(forename);
		
		
		DropDownChoice lang = new DropDownChoice(
				"locale",
				new PropertyModel(UserEditPersonalPanel.this, "selectedLocale"),
				LocalizationUtils.getSupportedConfigurationFrontendLocales(),
				new LocaleChoiceRenderer(null)	
		);
		
		Behavior behavior = new AjaxFormComponentUpdatingBehavior("onchange")
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				getUser().setLocale(selectedLocale);
				onSubmit(target);
			}
		};
		
		lang.add(behavior);
		
		getForm().add(lang);
		
		addReceiveNewsChoice();
		
		
		//CheckBox news = new CheckBox("receiveNews");
		
//		news.add(getFormComponentUpdatingBehavior("onchange"));
//		
//		getForm().add(news);
		
//		getForm().add(new AjaxCheckBox("receivePersonalNews") {
//
//			@Override
//			protected void onUpdate(AjaxRequestTarget target) {
//				toggleVisibility(target);				
//			}
//			
//		});
		
//		getForm().add(container = new WebMarkupContainer("container"));
//		
//		container.add(employment = new TextField("employment"));
//		container.add(position   = new DropDownChoice(
//				"position", Arrays.asList(User.Position.values()),
//						new LocalizedPositionChoiceRenderer(this)
//						));
//		container.add(sector     = new TextField("sector"));
//		
//
//		toggleVisibility(getUser().isReceivePersonalNews());
	}

//	private void toggleVisibility(boolean b) {
//		container.setVisible(b);
//
//		position.setRequired(b);
//		employment.setRequired(b);
//		sector.setRequired(b);
//	}
//
//	private void toggleVisibility(AjaxRequestTarget target) {
//		boolean visible = container.isVisible();
//		toggleVisibility(!visible);
//		
//		if(target != null) {
//			target.addComponent(this);
//		}
//	}
	
	private void addReceiveNewsChoice()
	{
		DropDownChoice receiveNews = new DropDownChoice(
				"receiveNews",
				new PropertyModel(UserEditPersonalPanel.this, "isReceivingNews"),
				Arrays.asList(new Boolean[] { true, false }),
				new IChoiceRenderer()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public String getIdValue(Object object, int index)
					{
						return object.toString() + index;
					}

					@Override
					public Object getDisplayValue(Object object)
					{
						if (((Boolean) object))
						{
							return new StringResourceModel("receiveNews",
									UserEditPersonalPanel.this, null)
									.getObject();
						}

						return new StringResourceModel("notReceiveNews",
								UserEditPersonalPanel.this, null).getObject();
					}
				});

		receiveNews.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				getUser().setReceiveNews(isReceivingNews);
				userDao.save(getUser());
			}
		});
			
		getForm().add(receiveNews);
	}
	
	private class LocalizedGenderChoiceRenderer implements IChoiceRenderer
	{
		private final Component parent;
		public LocalizedGenderChoiceRenderer(Component parent)
		{
			this.parent = parent;
		}
		
		@Override
		public Object getDisplayValue(Object object) {
			StringResourceModel srm = new StringResourceModel(
					((User.Gender) object).name().toLowerCase(), parent, null);
			
			return srm.getObject();
		}

		@Override
		public String getIdValue(Object object, int index) {
			return ((User.Gender) object).toString();
		}
	}
	
	private void addActions()
	{
		AjaxLink changePasswordLink = new AjaxLink("changePassword")
		{
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				changePasswordModal.show(target);
			}
		};
		
		add(changePasswordLink);
		
		AjaxLink endMembershipLink = new AjaxLink("endMembership")
		{
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				endMembershipModal.show(target);
			}
		};
		
		add(endMembershipLink);
		
		AjaxLink enableUser = new AjaxLink("enableUser")
		{
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				userService.activate(getUser());
				target.addComponent(UserEditPersonalPanel.this);								
			}
			
			@Override
			public boolean isVisible() {
				return getUser().isDeactivated() && 
						UserWeaveSession.get().isAdmin();
			}
		};
		
		add(enableUser);
		
		
	}
	
	private void addModalDialogs()
	{
		changePasswordModal = new CustomModalWindow("changePasswordModal");
		
		changePasswordModal.setTitle(
				new StringResourceModel("changePassword", this, null));
		
		changePasswordModal.setInitialHeight(167);
		
		changePasswordModal.setPageCreator(new ModalWindow.PageCreator()
		{
			@Override
			public Page createPage()
			{
				return new ChangePasswordPage(changePasswordModal);
			}
		});
		
		add(changePasswordModal);
		
		
		endMembershipModal = new CustomModalWindow("endMembershipModal");
		
		endMembershipModal.setTitle(
			new StringResourceModel("endMembership", this, null));
		
		endMembershipModal.setPageCreator(new ModalWindow.PageCreator()
		{
			@Override
			public Page createPage()
			{
				return new AssurancePage(endMembershipModal)
				{
					@Override
					protected void onOk(AjaxRequestTarget target)
					{
						disableUser = true;
					}
					
					@Override
					protected IModel getSureModel()
					{
						return new StringResourceModel(
							"sure",UserEditPersonalPanel.this,null);
					}
					
					@Override
					protected IModel getAcceptLabel()
					{
						return new StringResourceModel(
							"endMembership", UserEditPersonalPanel.this, null);
					}
				};
			}
		});
		
		endMembershipModal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() 
		{
            public void onClose(AjaxRequestTarget target) 
            {	
        		if (disableUser) {
        			userService.deactivate(getUser());
        			disableUser = false;
        			if(UserWeaveSession.get().isAdmin()) {
        				target.addComponent(UserEditPersonalPanel.this);
        			} else {
        				setResponsePage(SignoutPage.class);
        			}
        		}
            }
        });
		
		add(endMembershipModal);
	}
}
