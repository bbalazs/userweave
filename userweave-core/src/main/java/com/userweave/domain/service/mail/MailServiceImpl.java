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
package com.userweave.domain.service.mail;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
	
	private final static Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

	@Override
	public void sendMail(String recipient, String subject, String message, String from) throws MessagingException {
		sendMail(recipient, subject, message, from, true);
	}
		
	@Override
	public void sendMail(final String recipient, final String subject, String message, String from, boolean sendBCC) throws MessagingException 
	{
		final Message msg = 
			createMessage(recipient, subject, message, from, sendBCC);
		
		// FIXME(pavkovic 2009.10.06: perhaps message queue would be nicer here? who knows)
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					logger.debug("Trying to send a message to "+recipient);
					Transport.send(msg);
					logger.info("Message '"+subject+"' sent to "+recipient);
				} catch(Exception e) {
					logger.error("Sending a message failed",e);
				}
			}
		}).start();
		
	}
	
	public void sendMails(
		final List<String> recipients, 
		final String subject, 
		final String message, 
		final String from) throws MessagingException
	{
		final Message msg = createMessage(recipients, subject, message, from);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					logger.debug("Trying to send a mass message form "+from);
					Transport.send(msg);
					logger.info("Mass message '"+subject+"' has been send");
				} catch(Exception e) {
					logger.error("Sending a message failed",e);
				}
			}
		}).start();
	};

	private Message createMessage(
		final String recipient, final String subject, 
		String message, String from, boolean sendBCC) throws MessagingException
	{
		Properties props = new Properties();
		props.put("mail.smtp.host", "mail.smtp.com");
		Session session = Session.getDefaultInstance(props);
		final Message msg = new MimeMessage(session);
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);
		InternetAddress addressTo = new InternetAddress(recipient);
		msg.setRecipient(Message.RecipientType.TO, addressTo);
		if(sendBCC) {
			msg.setRecipient(Message.RecipientType.BCC, addressFrom);
		}
		
		try
		{
			msg.setSubject(MimeUtility.encodeText(subject, "utf-8", "Q"));
		}
		catch (UnsupportedEncodingException e)
		{
			msg.setSubject(subject);
		}
		
		msg.setContent(message, "text/plain; charset=utf-8");
		
		return msg;
	}
	
	private Message createMessage(
		final List<String> recipients, final String subject, String message, String from) throws MessagingException
	{
		Properties props = new Properties();
		props.put("mail.smtp.host", "mail.smtp.com");
		
		Session session = Session.getDefaultInstance(props);
		
		final Message msg = new MimeMessage(session);
		
		// set sender to mail writer
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);
		
		// create cc
		InternetAddress[] addresses = new InternetAddress[recipients.size()];
		
		for(int i = 0; i < recipients.size(); i++)
		{
			InternetAddress addressTo = new InternetAddress(recipients.get(i));
			addresses[i] = addressTo;
		}
		
		// set receiver to mail writer
		msg.setRecipient(Message.RecipientType.TO, addressFrom);
		
		// add other team members to cc
		msg.addRecipients(Message.RecipientType.CC, addresses);
		
		
		msg.setSubject(subject);
		msg.setContent(message, "text/plain; charset=ISO-8859-1");
		
		return msg;
	}
	
}