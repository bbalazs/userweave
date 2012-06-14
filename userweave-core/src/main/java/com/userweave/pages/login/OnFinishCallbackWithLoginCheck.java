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

import org.apache.wicket.Page;

import com.userweave.application.OnFinishCallbackWithReturnValue;
import com.userweave.application.UserWeaveSession;

/**
 * don't use anymore in the future, try to implement WebComponents with a
 * abstract method onFinish
 * 
 * @author ipavkovic
 *
 * @param <T>
 */
@SuppressWarnings("serial")
@Deprecated
public class OnFinishCallbackWithLoginCheck implements OnFinishCallbackWithReturnValue<Page> {

	private final LoginCheck check;
	
	public OnFinishCallbackWithLoginCheck(LoginCheck check) {
		this.check = check;
	}
	
	@Override
	public void onFinish(Page page) {
		finish(check);
		SigninPage.stopInterception(page);
	}

	public static boolean finish(LoginCheck check) {
		boolean finished = check.finished();
		if(finished) {
			UserWeaveSession.get().setLoginState(check.nextState());
		}
		return finished;
	}
}
