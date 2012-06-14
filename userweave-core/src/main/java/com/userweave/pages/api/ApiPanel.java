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

import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.UrlValidator;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.navigation.RoundedBorderGray;
import com.userweave.dao.ApiCredentialsDao;
import com.userweave.domain.ApiCredentials;
import com.userweave.domain.SecurityToken;
import com.userweave.domain.service.SecurityTokenService;
import com.userweave.pages.base.BaseUserWeavePage;
import com.userweave.pages.test.jquery.JQuery;
import com.userweave.utils.LocalizationUtils;

public class ApiPanel extends Panel {

	public static final String COMMAND_PARAM = "cmd";
	public static final String COMMAND_GET_TOKEN = "_getToken";
	public static final String COMMAND_CREATE_STUDY = "_createStudy";
	public static final String HASH_PARAM = "token";
	public static final String IMG_URL_PARAM = "imgUrl"; 
	public static final String LANGUAGE_URL_PARAM = "hl";
	public static final String STUDY_NAME_PARAM = "name";

	@SpringBean
	private ApiCredentialsDao apiCredentialsDao;
	
	@SpringBean
	protected SecurityTokenService securityTokenService;
	
	
	public ApiPanel(String id) {
		super(id);
		
		init(null);
	}
	
	public ApiPanel(String id, PageParameters parameters) {
		super(id);
		
		init(parameters);
	}

	
	private void init(PageParameters parameters) 
	{	
		RoundedBorderGray border = new RoundedBorderGray("border");
		add(border);
		
		RepeatingView repeater = new RepeatingView("repeater");
		border.add(repeater);
		
		Label noParameter = new Label("noParameter", new Model("Keine Parameter uebergeben!"));
		border.add(noParameter);
		
		Label errorMessageLabel = new Label("errorMessage", new Model(""));
		border.add(errorMessageLabel);
		
		Label secTokenHash = new Label("secTokenHash", new Model(""));
		border.add(secTokenHash);
		
		
		if (parameters != null && !parameters.isEmpty()) 
		{
			noParameter.setVisible(false);
			
			// show parameters on panel
			Iterator iter = parameters.getNamedKeys().iterator();
			
			while ( iter.hasNext() ) 
			{
				Object o = iter.next();
				String key = (String) o;
				String val = parameters.get(key).toString();
				
				final WebMarkupContainer line = new WebMarkupContainer(repeater.newChildId());
				
				line.add( new Label("key", new Model<String>(key)) );
				line.add( new Label("value", new Model<String>(val)) );
				
				repeater.add(line);
			}
			
			
			/*
			 * Create pre configured study
			 */
			Set<String> keys = parameters.getNamedKeys();
			
			if( !keys.contains(COMMAND_PARAM) 
					|| (keys.contains(COMMAND_PARAM)
							&& parameters.get(COMMAND_PARAM) == null || parameters.get(COMMAND_PARAM).equals("") ) 
					|| ( keys.contains(COMMAND_PARAM) && parameters.get(COMMAND_PARAM) != null 
							&& parameters.get(COMMAND_PARAM).equals(COMMAND_CREATE_STUDY) ) 
			) 
			{
				// redirect doesn't contain study id
				if( !keys.contains(HASH_PARAM) 
						|| (keys.contains(HASH_PARAM)
								&& parameters.get(HASH_PARAM) == null || parameters.get(HASH_PARAM).equals("") ) ) 
				{
					errorMessageLabel.setDefaultModel( new StringResourceModel("noHashParam", this, null));
				}
				
				/*
				 * validate hash
				 */
				else if(securityTokenService.validateToken(
						parameters.get(HASH_PARAM).toString(), SecurityToken.SecurityTokenType.API_ACCESS) == null )
				{
					errorMessageLabel.setDefaultModel(new StringResourceModel("wrongSecurityTokenHash", this, null));
				}
				
				
				else if (!keys.contains(IMG_URL_PARAM) 
						|| (keys.contains(IMG_URL_PARAM)
								&& parameters.get(IMG_URL_PARAM) == null || parameters.get(IMG_URL_PARAM).equals("") )) 
				{
					errorMessageLabel.setDefaultModel( new StringResourceModel("noImgUrlParam", this, null));
				}
				
				/*
				 * check, if url is valid
				 */
				else if( !isValidImgUrl(parameters.get(IMG_URL_PARAM).toString()) )
				{
					errorMessageLabel.setDefaultModel( new StringResourceModel("invalidImgUrl", this, null));
				}
				
				else if (!keys.contains(LANGUAGE_URL_PARAM) 
						|| (keys.contains(LANGUAGE_URL_PARAM)
								&& parameters.get(LANGUAGE_URL_PARAM) == null || parameters.get(LANGUAGE_URL_PARAM).equals("") )) 
				{
					errorMessageLabel.setDefaultModel( new StringResourceModel("noLanguageUrlParam", this, null));
				}
				
				/*
				 * locale param is correct, but maybe not a
				 * supported one
				 */
				else if( !isValidLanguageParam(parameters.get(LANGUAGE_URL_PARAM).toString()) )
				{
					errorMessageLabel.setDefaultModel( new StringResourceModel("notSupportedLanguageUrlParam", this, null));
				}
				
				/*
				 * name parameter set but contains no value
				 */
				else if(keys.contains(STUDY_NAME_PARAM) &&
							(parameters.get(STUDY_NAME_PARAM) == null ||
									parameters.get(STUDY_NAME_PARAM).equals("")))
				{
					errorMessageLabel.setDefaultModel(new StringResourceModel("noNameGiven", this, null));
				}
				
				
				else {
					CreateMockupStudyData data = new CreateMockupStudyData();
					
					String localeString = parameters.get(LANGUAGE_URL_PARAM).toString();
					
					data.setMockupUrl(parameters.get(IMG_URL_PARAM).toString(), new Locale(localeString));
					data.setSecurityTokenHash(parameters.get(HASH_PARAM).toString());
					data.setLocaleString(localeString);
					
					if(keys.contains(STUDY_NAME_PARAM))
					{
						data.setStudyName(parameters.get(STUDY_NAME_PARAM).toString());
					}
					
					UserWeaveSession.get().setCreateMockupStudyData(data);
					
					setResponsePage(ApiCreateStudyPage.class);
				}
			}
			else 
			{
				/*
				 * Create SecurityToken
				 */
				if (parameters.get(COMMAND_PARAM).equals(COMMAND_GET_TOKEN) ) {
					
					// redirect doesn't contain study id
					if( !keys.contains(HASH_PARAM) 
							|| (keys.contains(HASH_PARAM)
									&& parameters.get(HASH_PARAM) == null || parameters.get(HASH_PARAM).equals("") ) ) {
						errorMessageLabel.setDefaultModel( new StringResourceModel("noHashParam", this, null));
					}
					else {
						
						if(isValidHash(parameters.get(HASH_PARAM).toString(), errorMessageLabel))
						{
							ApiCredentials curPartner = apiCredentialsDao.findByHash(parameters.get(HASH_PARAM).toString());
							secTokenHash.setDefaultModel( new Model( securityTokenService.createTokenForApi(curPartner.getUser())));
						}
					}
				}	
				else {
					errorMessageLabel.setDefaultModel( new StringResourceModel("unkownCommandParam", this, null, new Object[]{ parameters.get(COMMAND_PARAM) } ));
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
	
	private boolean isValidHash(String hash, Label errorMessageLabel)
	{
		ApiCredentials curPartner = apiCredentialsDao.findByHash(hash);
		
		if (curPartner == null) {
			errorMessageLabel.setDefaultModel( new StringResourceModel("wrongSecurityTokenHash", this, null));
			return false;
		}
		else if (!curPartner.isActive()) {
			errorMessageLabel.setDefaultModel( new StringResourceModel("apiAccessDeactivated", this, null));
			return false;
		}
		else if (curPartner.getUser() == null) {
			errorMessageLabel.setDefaultModel( new StringResourceModel("noUserForApiCredentials", this, null));
			return false;
		}
		
		return true;
	}
	
	private boolean isValidLanguageParam(String locale)
	{
		Locale l = LocalizationUtils.getSupportedStudyLocale(locale);
		
		if(l != null)
		{
			/*
			 * current support only for de and en.
			 * TODO: remove in the future.
			 */
			if (l.getLanguage().equals(new Locale("de", "", "").getLanguage()) ||
					l.getLanguage().equals(new Locale("en", "", "").getLanguage()))
			{
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isValidImgUrl(String url)
	{
		UrlValidator validator = new UrlValidator();
		
		if(validator.isValid(url))
		{
			return true;
		}
		
		return false;
	}
}