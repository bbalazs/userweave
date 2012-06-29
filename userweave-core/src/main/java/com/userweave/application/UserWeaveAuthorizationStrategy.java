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
package com.userweave.application;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;

import com.userweave.application.auth.AdminOnly;
import com.userweave.application.auth.AuthenticatedOnly;
import com.userweave.components.IToolTipComponent;
import com.userweave.components.IToolTipComponent.ToolTipType;
import com.userweave.components.authorization.IAuthOnly;
import com.userweave.domain.Role;
import com.userweave.domain.User;
import com.userweave.pages.login.LoginPage;

public class UserWeaveAuthorizationStrategy implements
		IAuthorizationStrategy, IUnauthorizedComponentInstantiationListener {

	public boolean isActionAuthorized(Component component, Action action) 
	{
		if (action.equals(Component.RENDER)) 
		{
			Class<? extends Component> c = component.getClass();
			AdminOnly adminOnly = c.getAnnotation(AdminOnly.class);
			
			if (adminOnly != null) 
			{
				return UserWeaveSession.get().isAdmin();
			}
			
			/**
			 * To set the state of an AuthOnlyTextComponent
			 * may be stupid here, but there is currently
			 * no other option.
			 */
			if(component instanceof IAuthOnly)
			{
				IAuthOnly comp = (IAuthOnly) component;
				
				User user = UserWeaveSession.get().getUser();
				
				AuthorizeAction authAction = 
					comp.getClass().getAnnotation(AuthorizeAction.class);
				
				boolean auth = false;
				
				if(authAction == null)
				{
					// if no annotation is given, admins and participants
					// have access
					auth = user.hasRole(Role.PROJECT_ADMIN) ||
						   user.hasRole(Role.PROJECT_PARTICIPANT);
				}
				else
				{
					// evaluate annotation roles
					auth = user.hasAnyRole(new Roles(authAction.roles()));
				}
				
				comp.setIsAuthorized(auth || user.isAdmin());
				
				return true;
			}
		}
		
		if(action.equals(Component.ENABLE))
		{
			AuthorizeAction authAction = 
				component.getClass().getAnnotation(AuthorizeAction.class);
		
			if(authAction != null)
			{
				User user = UserWeaveSession.get().getUser();
				
				boolean isEnabled = user.hasAnyRole(new Roles(authAction.roles()));
				
				if(!isEnabled)
				{
					if(component instanceof IToolTipComponent)
					{
						((IToolTipComponent)component).setToolTipType(ToolTipType.RIGHTS);
					}
				}
				
				return isEnabled && component.isEnabled();
			}
			
			return true;
		}
		
		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean isInstantiationAuthorized(Class c) 
	{
		AuthenticatedOnly a = (AuthenticatedOnly) c.getAnnotation(AuthenticatedOnly.class);
		if(a != null) {
			return UserWeaveSession.get().isAuthenticated();
		}
		return true;
	}

	public void onUnauthorizedInstantiation(Component component) {
		throw new RestartResponseAtInterceptPageException(LoginPage.class);
	}
}
