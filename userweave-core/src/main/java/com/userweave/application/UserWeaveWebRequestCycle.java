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

import org.apache.wicket.Application;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.cycle.RequestCycleContext;

import com.userweave.domain.util.ExceptionHandlingHelper;

public class UserWeaveWebRequestCycle extends RequestCycle 
{
	private final ExceptionHandlingHelper exceptionHandlingHelper = new ExceptionHandlingHelper();

	private boolean ignoreNextException;

	public UserWeaveWebRequestCycle(RequestCycleContext context)
	{
		super(context);
	}
	
	public static UserWeaveWebRequestCycle get() 
	{
		return (UserWeaveWebRequestCycle) RequestCycle.get();
	}
	
	public void handleRuntimeException(RuntimeException e)
	{
		if(!ignoreNextException) 
		{
			if((Application.get().getConfigurationType() == RuntimeConfigurationType.DEPLOYMENT)) 
			{
				exceptionHandlingHelper.sendRuntimeException(e);
			}
		}
		ignoreNextException = false;
	}
	
//	@Override
//	protected void logRuntimeException(RuntimeException e) {
//		if(!ignoreNextException) {
//			super.logRuntimeException(e);
//			if(Application.DEPLOYMENT.equalsIgnoreCase(getApplication().getConfigurationType())) {
//				exceptionHandlingHelper.sendRuntimeException(e);
//			}
//		}
//		ignoreNextException = false;
//	}
	
	@Override
	protected void onEndRequest()
	{
		super.onEndRequest();
		((UserWeaveSession) Session.get()).invalidateUser();
		((UserWeaveSession) Session.get()).invalidateProject();
	}
	
	/**
	 * sometimes we force errors while developing. These errors are annoying. This is a 
	 * way how to ignore the next Exception once. Call this method only if you *know* what
	 * you are doing. Else it WILL swallow some useful exception messages!
	 */
	public void ignoreNextException() 
	{
		this.ignoreNextException = true;
	}
}
