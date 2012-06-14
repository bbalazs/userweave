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
package com.userweave.pages.api;

import java.util.Locale;

import com.userweave.domain.LocalizedString;

public class CreateMockupStudyData {

	private final LocalizedString mockupUrl = new LocalizedString();
	
	public void setMockupUrl(String url, Locale locale){
		mockupUrl.setValue(url, locale);
	}
	
	public LocalizedString getMockupUrl(){
		return this.mockupUrl;
	}
	
	private String securityTokenHash = "";
	public void setSecurityTokenHash(String securityTokenhash) {
		this.securityTokenHash = securityTokenhash;
	}

	public String getSecurityTokenHash() {
		return securityTokenHash;
	}
	

	private String localeString = "";	
	public void setLocaleString(String localeString) {
		this.localeString = localeString;
	}

	public String getLocaleString() {
		return localeString;
	}
	
	private String studyName = "";
	public String getStudyName() {
		return studyName;
	}

	public void setStudyName(String studyName) {
		this.studyName = studyName;
	}
}
