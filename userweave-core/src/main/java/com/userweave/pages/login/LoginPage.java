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
package com.userweave.pages.login;

import org.apache.wicket.markup.html.WebPage;

import com.userweave.application.UserWeaveSession;
import com.userweave.application.UserWeaveSession.LoginState;
import com.userweave.pages.homepage.MainContent;
import com.userweave.pages.user.ChangePassword;
import com.userweave.pages.user.verification.UserVerificationPage;

public class LoginPage extends WebPage {
	
	public LoginPage() {
		checkLogin();
	}

	private void checkLogin() {
		LoginState state = UserWeaveSession.get().getLoginState();
		if (state == LoginState.INIT) {
			setResponsePage(MainContent.class);
		}
		WebPage page = null;
		LoginCheck check = null;
		if (state == LoginState.AUTHENTICATED) {
			check = new PasswordLoginCheck();
			if (OnFinishCallbackWithLoginCheck.finish(check)) {
				state = UserWeaveSession.get().getLoginState();
			} else {
				page = new ChangePassword(new OnFinishCallbackWithLoginCheck(check));
				setResponsePage(page);
			}
		}
		if (state == LoginState.PASSWORD_CHECKED) {
			check = new UserLoginCheck();
			if (OnFinishCallbackWithLoginCheck.finish(check)) {
				state = UserWeaveSession.get().getLoginState();
			} else {
				page = new UserVerificationPage(UserWeaveSession.get().getUser(), new OnFinishCallbackWithLoginCheck(check));
				setResponsePage(page);
			}
		}
		if (state == LoginState.USER_VERIFIED) {
			UserWeaveSession.get().setLoginState(LoginState.LOGGED_IN);
			// getRequestCycle().setRedirect(true);
			SigninPage.stopInterception(this);
		}
	}
}
