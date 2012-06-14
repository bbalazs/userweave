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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.apache.wicket.Application;
import org.apache.wicket.IRequestCycleProvider;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authorization.strategies.role.IRoleCheckingStrategy;
import org.apache.wicket.authroles.authorization.strategies.role.RoleAuthorizationStrategy;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.MarkupFactory;
import org.apache.wicket.markup.MarkupParser;
import org.apache.wicket.markup.MarkupResourceStream;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.parser.AbstractMarkupFilter;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.cycle.RequestCycleContext;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.caching.NoOpResourceCachingStrategy;
import org.apache.wicket.resource.loader.ClassStringResourceLoader;
import org.apache.wicket.resource.loader.ComponentStringResourceLoader;
import org.apache.wicket.resource.loader.IStringResourceLoader;
import org.apache.wicket.settings.IExceptionSettings;
import org.apache.wicket.settings.ISecuritySettings;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.crypt.CachingSunJceCryptFactory;
import org.hibernate.annotations.Entity;

import com.userweave.application.images.ImageResource;
import com.userweave.domain.User;
import com.userweave.domain.util.HashProvider;
import com.userweave.pages.api.ApiCreateStudyPage;
import com.userweave.pages.api.ApiLandingPage;
import com.userweave.pages.api.ApiPage;
import com.userweave.pages.configuration.ConfigurationPage;
import com.userweave.pages.configuration.report.ReportPage;
import com.userweave.pages.configuration.study.localization.TranslationPage;
import com.userweave.pages.error.internal.InternalErrorPage;
import com.userweave.pages.error.internal.TestInternalErrorPage;
import com.userweave.pages.error.internal.TestInternalErrorPageOnSurvey;
import com.userweave.pages.error.notfound.PageNotFoundPage;
import com.userweave.pages.homepage.ImprintPage;
import com.userweave.pages.homepage.MainContent;
import com.userweave.pages.homepage.NoIe6Support;
import com.userweave.pages.login.SigninPage;
import com.userweave.pages.login.SignoutPage;
import com.userweave.pages.playground.PlaygroundForComponentsPage;
import com.userweave.pages.test.DisplaySurveyUI;
import com.userweave.pages.user.RegisterUser;
import com.userweave.pages.user.ResetPassword;
import com.userweave.utils.LocalizationUtils;

@Entity
public class UserWeaveApplication extends WebApplication {

	private final static boolean ENCRYPTION = true;
	
	@Override
	public Class<? extends WebPage> getHomePage() {
		return ConfigurationPage.class;
	}
	
	@Override
	public void init() 
	{
		super.init();

		getComponentInstantiationListeners().add(new SpringComponentInjector(this));
		
	    setupAuthorization();
	    
		mountPages();
		mountResources();
		
		getMarkupSettings().setStripWicketTags(true);

		setupErrorHandling();

		setupProductionSettings();
		                
        setDefaultResourceLocale(LocalizationUtils.getDefaultLocale());
        
		getMarkupSettings().setMarkupFactory(new MarkupFactory() 
		{
			
			@Override
			public MarkupParser newMarkupParser(MarkupResourceStream resource) 
			{
				MarkupParser markupParser = new MarkupParser(resource);
				
				markupParser.add(new AbstractMarkupFilter() 
				{	
//					@Override
//					public MarkupElement nextTag() throws ParseException {
//				        
//						// Get the next tag. If null, no more tags are available
//				        final ComponentTag tag = (ComponentTag)getParent().nextTag();
//				        if ( null == tag || null != tag.getId() )
//				            return tag;
//
//				        // Process open <html> tags
//				        if( null != tag.getName() && tag.getName().equals( "html" ) && tag.isOpen())
//				        {
//				        	String language = UserWeaveSession.get().getLocale().getLanguage();
//			            	tag.getAttributes().put("lang", language);
//			            	tag.getAttributes().put("xml:lang", language);
//			            	tag.setModified( true );
//				        }
//
//				        return tag;
//					}

					@Override
					protected MarkupElement onComponentTag(ComponentTag tag)
							throws ParseException
					{
						if ( null == tag || null != tag.getId() )
				        {
							return tag;
				        }

				        // Process open <html> tags
				        if( null != tag.getName() && tag.getName().equals( "html" ) && tag.isOpen())
				        {
				        	String language = UserWeaveSession.get().getLocale().getLanguage();
			            	tag.getAttributes().put("lang", language);
			            	tag.getAttributes().put("xml:lang", language);
			            	tag.setModified( true );
				        }

				        return tag;
					}
				});
				
				return markupParser;
			}
		});
		
		/*
		 * @see: migration guide to wicket 1.5 Request cycle
		 * 
		 * Instead of overrride newRequestCycle, we have to create a factory,
		 * that builds our custom RequestCycle.
		 */
		setRequestCycleProvider(new IRequestCycleProvider()
		{
			@Override
			public RequestCycle get(RequestCycleContext context)
			{
				return new UserWeaveWebRequestCycle(context);
			}
		});
		
		/*
		 * @see: https://cwiki.apache.org/WICKET/request-mapping.html
		 * 
		 * newRequestCycleProcessor is obsolete, so we replace the
		 * CryptedUrlWebRequestCodingStrategy with a CryptoMapper,
		 */
//		if(ENCRYPTION)
//		{
//			setRootRequestMapper(new CryptoMapper(getRootRequestMapper(), this));
//		}
		
		// add custom listener for runtime exceptions.
		getRequestCycleListeners().add(new AbstractRequestCycleListener()
		{
			@Override
			public IRequestHandler onException(RequestCycle cycle, Exception ex)
			{
				if(ex instanceof RuntimeException)
				{
					((UserWeaveWebRequestCycle) cycle).handleRuntimeException((RuntimeException)ex);
				}
				
				return null;
			}
		});
		
		// disable caching strategy
		getResourceSettings().setCachingStrategy(NoOpResourceCachingStrategy.INSTANCE);
	}

	private void setupProductionSettings() 
	{
		boolean inDevelopment = Application.CONFIGURATION.equals(getConfigurationType());
		if(inDevelopment) 
		{
			return;
		}
		
		getMarkupSettings().setStripComments(true);
		getResourceSettings().setThrowExceptionOnMissingResource(false);
	}	

	private void setDefaultResourceLocale(final Locale defaultResourceLocale) 
	{			
		Collection<IStringResourceLoader> stringResourceLoaders =
			new ArrayList<IStringResourceLoader>(getResourceSettings().getStringResourceLoaders());

        for (IStringResourceLoader loader : stringResourceLoaders) {
           getResourceSettings().getStringResourceLoaders().add(loader);
        }

        // support for default _en.properties files
        getResourceSettings().getStringResourceLoaders().add(
            new ComponentStringResourceLoader() {                                
                @Override
				public String loadStringResource(
					Class<?> clazz, String key, Locale locale, String style, String variation) 
                {
					return super.loadStringResource(clazz, key, defaultResourceLocale, style, variation);
                }                
            }
        );
        getResourceSettings().getStringResourceLoaders().add(
        	new ClassStringResourceLoader(this.getClass()) {
        		@Override
        		public String loadStringResource(
        			Class<?> clazz, String key, Locale locale, String style, String variation) 
        		{
        			return super.loadStringResource(clazz, key, defaultResourceLocale, style, variation);
        		}
        	}
        );
	}

	private void mountPages() 
	{
		mountPage("register", RegisterUser.class);
		mountPage("resetpassword", ResetPassword.class);

		mountPage("signin", SigninPage.class);
		mountPage("signout", SignoutPage.class);

		mountPage("survey", DisplaySurveyUI.class);
		
		mountPage("portal", ConfigurationPage.class);		
		
		mountPage("report", ReportPage.class);

		// translation page
		mountPage("locale", TranslationPage.class);
		
		// test pages
		mountPage("testerror", TestInternalErrorPage.class);
		mountPage("testerrorsurvey", TestInternalErrorPageOnSurvey.class);
		mountPage("testcomponents", PlaygroundForComponentsPage.class);
		
		// paypal pages
		//mountPage("paypal", PayPalPage.class);
		
		// api pages
		mountPage("api", ApiPage.class);
		mountPage("apiCreateStudy", ApiCreateStudyPage.class);
		mountPage("apiLanding", ApiLandingPage.class);
		
		// Start Website pages
		mountPage("start.html", MainContent.class);
		mountPage("imprint.html", ImprintPage.class);

		mountPage("404.html", PageNotFoundPage.class);
		mountPage("noIe6Support.html", NoIe6Support.class);
	}

	private void mountResources() {
		mountImageResource();
	}
	
	private void mountImageResource() 
	{		
		ResourceReference imgRef = new ResourceReference(ImageResource.IMAGE_RESOURCE)
		{
			private static final long serialVersionUID = 1L;
			
			ImageResource resource = new ImageResource();
			
			@Override
			public IResource getResource()
			{
				return resource;
			}
		};
		
		mountResource("images", imgRef);
		
//		getSharedResources().add(ImageResource.IMAGE_RESOURCE, new ImageResource());
//		
//		final String key = ImageResource.getResourceReference().getSharedResourceKey();
//		mount(new IndexedSharedResourceCodingStrategy("images", key));
	}
	
	private void setupAuthorization() {
		UserWeaveAuthorizationStrategy authStrat = 
			new UserWeaveAuthorizationStrategy();
		
		RoleAuthorizationStrategy roleStrat = 
			new RoleAuthorizationStrategy(
					new IRoleCheckingStrategy()
		{
			@Override
			public boolean hasAnyRole(Roles roles)
			{
				/*
				 * Only deny roles contain the empty role. If this fact changes,
				 * go to hell.
				 */
				if(roles.hasRole(""))
				{
					return false;
				}
				else
				{
					User user = UserWeaveSession.get().getUser();
					return user.isAdmin() || user.hasAnyRole(roles);
				}
			}
			
		});
		
		roleStrat.add(authStrat);
		
	    ISecuritySettings securitySettings = getSecuritySettings();
	    //securitySettings.setAuthorizationStrategy(authStrat);
	    securitySettings.setAuthorizationStrategy(roleStrat);
	    securitySettings.setUnauthorizedComponentInstantiationListener(authStrat);
	    if(!ENCRYPTION) {
	    	securitySettings.setCryptFactory(new CachingSunJceCryptFactory(HashProvider.md5("do what you desire",System.currentTimeMillis())));
	    }
	}

	private void setupErrorHandling() {
		getApplicationSettings().setInternalErrorPage(InternalErrorPage.class);
		getExceptionSettings().setUnexpectedExceptionDisplay(IExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);
		//getApplicationSettings().setPageExpiredErrorPage(PageExpiredPage.class);
		getApplicationSettings().setPageExpiredErrorPage(getHomePage());
	}

	// @see: Migration guide to wicket 1.5 (This method is not needed anymore).
//	@Override
//	public RequestCycle newRequestCycle(final Request request, final Response response) {
//		return new UserWeaveWebRequestCycle(this, (WebRequest)request, (WebResponse)response);
//	}
	
	/*
	 * Obosolete since 1.5
	 * @see https://cwiki.apache.org/WICKET/request-mapping.html
	 */
//	@Override
//	protected IRequestCycleProcessor newRequestCycleProcessor() {
//		
//		return new WebRequestCycleProcessor() {
//			@Override
//			protected IRequestCodingStrategy newRequestCodingStrategy() {
//				if(ENCRYPTION) {
//					return new CryptedUrlWebRequestCodingStrategy(new WebRequestCodingStrategy());
//				} else {
//					return super.newRequestCodingStrategy();
//				}
//			}
//		};
//	 }
	
	@Override
	public Session newSession(Request request, Response response) 
	{
		return new UserWeaveSession(request);
	}
	
	
}
