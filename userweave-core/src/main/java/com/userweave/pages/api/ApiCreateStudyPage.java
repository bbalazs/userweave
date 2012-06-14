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

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.userweave.application.UserWeaveSession;
import com.userweave.application.auth.AuthenticatedOnly;
import com.userweave.components.navigation.RoundedBorderGray;
import com.userweave.domain.SecurityToken;
import com.userweave.domain.Study;
import com.userweave.domain.SecurityToken.SecurityTokenType;
import com.userweave.domain.service.SecurityTokenService;
import com.userweave.domain.service.StudyService;
import com.userweave.pages.base.BaseUserWeavePage;
import com.userweave.pages.configuration.ConfigurationPage;
import com.userweave.pages.test.jquery.JQuery;
import com.userweave.utils.LocalizationUtils;

@AuthenticatedOnly
public class ApiCreateStudyPage extends BaseUserWeavePage {
	
	private final static Logger logger = LoggerFactory.getLogger(ApiCreateStudyPage.class);
	
	@SpringBean
	private StudyService studyService;
	
	@SpringBean
	protected SecurityTokenService securityTokenService;
	
	
	public ApiCreateStudyPage() {
		init(null);
	}
	
	public ApiCreateStudyPage(PageParameters parameters)
	{	
		init(null);
	}
	
	private void init(PageParameters parameters) 
	{
		add(new BookmarkablePageLink("start", getApplication().getHomePage()));
		
		RoundedBorderGray border = new RoundedBorderGray("border");
		add(border);
		
		Label errorMessageLabel = new Label("errorMessage", new Model(""));
		border.add(errorMessageLabel);
		
		if (UserWeaveSession.get().getCreateMockupStudyData() == null) {
			errorMessageLabel.setDefaultModel(new StringResourceModel("NoCreateStudyParameter", this, null ));
		}
		else if (UserWeaveSession.get().getCreateMockupStudyData().getMockupUrl() == null
				|| UserWeaveSession.get().getCreateMockupStudyData().getMockupUrl().equals("") ) {
			errorMessageLabel.setDefaultModel(new StringResourceModel("NoCreateStudyParameter", this, null, new Object[]{ "Image Url" } ));
		}
		else if (UserWeaveSession.get().getCreateMockupStudyData().getSecurityTokenHash() == null
				|| UserWeaveSession.get().getCreateMockupStudyData().getSecurityTokenHash().equals("") ) {
			errorMessageLabel.setDefaultModel(new StringResourceModel("NoCreateStudyParameter", this, null, new Object[]{ "Security Token Hash" } ));
		}
		else if (UserWeaveSession.get().getCreateMockupStudyData().getLocaleString() == null
				|| UserWeaveSession.get().getCreateMockupStudyData().getLocaleString().equals("") ) {
			errorMessageLabel.setDefaultModel(new StringResourceModel("NoCreateStudyParameter", this, null, new Object[]{ "Study language" } ));
		}
		else {
			
			errorMessageLabel.setDefaultModel(new ResourceModel("creatingStudy"));
			
			SecurityToken token = securityTokenService.validateToken(UserWeaveSession.get().getCreateMockupStudyData().getSecurityTokenHash(), SecurityTokenType.API_ACCESS);
			
			if(token == null) {
				errorMessageLabel.setDefaultModel(new StringResourceModel("WrongSecurityTokenHash", this, null));
			}
			else {
				if (token.getUser() != null) {
					logger.info("Api-Call from " + token.getUser().toString() + "!");
				}
				else {
					logger.info("Api-Call from unkown user, because token.getUser() is null!");
				}
				// invalid token
				securityTokenService.deleteToken(token);
				
				Locale studyLocale = LocalizationUtils.getSupportedStudyLocale(UserWeaveSession.get().getCreateMockupStudyData().getLocaleString());
				// creating pre configured study
				Study result = studyService.createPreConfiguredPresentationStudy(
						UserWeaveSession.get().getCreateMockupStudyData().getMockupUrl(), 
						studyLocale,
						UserWeaveSession.get().getCreateMockupStudyData().getStudyName());
				
				// reset pre configured study parameter in session
				UserWeaveSession.get().setCreateMockupStudyData(null);
				
				// move to study overview
				if(UserWeaveSession.get().getUser() != null)
				{
					setResponsePage(new ConfigurationPage(result));
				}
				else
				{
					setResponsePage(getApplication().getHomePage());
				}
			}
		}
	}
	
	@Override
	public void renderHead(IHeaderResponse response)
	{
		response.renderJavaScriptReference(JQuery.getLatest());
		
		response.renderJavaScriptReference(new PackageResourceReference(
				BaseUserWeavePage.class, "hover.js"));
		
		response.renderCSSReference(new PackageResourceReference(
				BaseUserWeavePage.class, "configBase.css"));	
	}
}

