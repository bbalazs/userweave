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
package com.userweave.domain.util;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.mail.MessagingException;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.UserWeaveSession;
import com.userweave.domain.User;
import com.userweave.domain.service.mail.MailService;

/**
 * Helper class to send a runtime exception via mail to admins.
 * 
 * @author opr
 *
 */
public class ExceptionHandlingHelper implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	@SpringBean
	private MailService mailService;
	
	public void sendRuntimeException(RuntimeException e) 
	{
		// only inject if needed
		if(mailService == null) 
		{
			Injector.get().inject(this);
		}
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		String hostname = getHostname();
		
		pw.println("HOST: "+hostname);

		User user = UserWeaveSession.get().getUser();
		if(user != null) {
			pw.println("USER: "+user.getEmail());
		} else {
			pw.println("USER: unknown");
		}
		
		pw.println();
		
		e.printStackTrace(pw);
		pw.flush();
		pw.close();
		
		String message = sw.toString();

		try {
			mailService.sendMail("bugs@userweave.net", "UserWeave BUG on "+hostname, message, "info@userweave.net", false);
		} catch (MessagingException e1) {
			// do nothing here for now
		}
	}

	private String getHostname() {
		String addr = null;
		String host = null;
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
    
			// Get IP Address
			addr = inetAddress.getHostAddress();
    
			// Get hostname
			host = inetAddress.getHostName();
		} catch (UnknownHostException e) {
			return "";
		}

		return host + "(" + addr + ")";
	}
	
}
