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
package com.userweave.pages.homepage;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.dao.SuitorDao;
import com.userweave.domain.service.UserService;
import com.userweave.domain.service.mail.MailService;

public class MainContent extends BaseHomepage
{
	@SpringBean
	private UserService userService;
	
private static final long serialVersionUID = 1L;
	
	@SpringBean 
	private SuitorDao suitorDao;

	@SpringBean
	private MailService mailService;
	
	

	
	
	
	
}