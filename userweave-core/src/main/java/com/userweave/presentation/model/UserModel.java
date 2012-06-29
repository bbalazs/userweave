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
package com.userweave.presentation.model;

import com.userweave.domain.Address;
import com.userweave.domain.Callnumber;
import com.userweave.domain.User;

@SuppressWarnings("serial")
public class UserModel extends EntityBaseLoadableDetachableModel<User> {

	public UserModel(User user) {
		super(user);
	}
	
	public UserModel(Integer id) {
		super(User.class, id);
	}
	
	@Override
	public User load() {
		User user = super.load();
		if(user != null) {
			if(user.getAddress() == null) {
				user.setAddress(new Address());
			}
			if(user.getCallnumber() == null) {
				user.setCallnumber(new Callnumber());
			}
			// silly helper function, may be removed later
			if(user.isReceivePersonalNews() == null) {
				user.setReceivePersonalNews(false);
			}
		}
		return user;
	}

}
