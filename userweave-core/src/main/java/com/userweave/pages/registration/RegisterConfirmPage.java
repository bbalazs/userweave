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
package com.userweave.pages.registration;

import java.util.Locale;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.dao.UserDao;
import com.userweave.domain.SecurityToken.SecurityTokenType;
import com.userweave.domain.User;
import com.userweave.domain.service.UserService;
import com.userweave.pages.base.BaseUserWeavePage;
import com.userweave.pages.test.DisplaySurveyUI;

public class RegisterConfirmPage extends BaseUserWeavePage {
	
	@SpringBean
	private UserService userService;
	
	@SpringBean
	protected UserDao userDao;
	
	public RegisterConfirmPage(PageParameters parameters) {
		String verifyCode = parameters.get(0).toString();
		Locale locale = DisplaySurveyUI.getLocale(parameters);
		if(locale != null) {
			Session.get().setLocale(locale);
		}
		
		User user = VerifyUser(verifyCode);
		
		if(user != null)
		{
			user.setVerified(true);
			
			userDao.save(user);
			
			add(new Label("confirmtext", new StringResourceModel("confirmtext", RegisterConfirmPage.this, null)));
			add(new BookmarkablePageLink<Void>("base_start", getApplication().getHomePage()));
		}
		else
		{
			
		}
	}

	private User VerifyUser(String verifyCode){
		
		if(verifyCode==null) {
			return null;
		}

		User user = userService.validateToken(verifyCode, SecurityTokenType.REGISTER_USER);
				
		return user;

	}
	
}
