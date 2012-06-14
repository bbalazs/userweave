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
package com.userweave.pages.test;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

import com.userweave.application.images.ImageResource;
import com.userweave.components.customModalWindow.SurveyModalWindow;
import com.userweave.domain.ImageBase;
import com.userweave.pages.CounterDisplay;
import com.userweave.pages.imprint.ImprintPage;
import com.userweave.pages.privacy.PrivacyPage;

/**
 * Base class for survey pages. Implements the frame for
 * the different modules.
 * 
 * @author opr
 *
 */
public abstract class BasePageSurvey extends WebPage implements CounterDisplay 
{
	private static final long serialVersionUID = 1L;
	private Label countLabelTop;
	
	private static final String COUNT_TOP = "countTop";
	
	private static final String PERCENT_COUNT = "percentCount";

	private static final String PROGRESS1 = "progress1";
	private static final String PROGRESS2 = "progress2";
	private static final String LOGO = "logo";
	
	private static final String HEAD_TITLE = "head_title";
	
	private Label headline;
	private Label description;
	private final Label headTitle;
	private Image logo;
	
	private final String headTitleString;

	private final int BAR_MAXWIDTH = 92;
	
	// set the color of the percent label to white, if progress bar 1 has this size
	private final int CHANGE_COLOR_TO_WHITE_WIDTH = 55;
	
	private Label moduleConfigurationDescription;
	
	private final NoScriptPanel noscriptPanel;
	
	public BasePageSurvey() {
		super();
		
		add(noscriptPanel = new NoScriptPanel("noscript_panel"));
		
		addLinksAndModalWindows();
		
		add(countLabelTop = createCountLabelTop(new Model<Integer>(0)));
		
		//add(createHelpArrow());
		
		add(moduleConfigurationDescription = new Label(
				"moduleConfigurationDescription", new Model<String>("")));

		add(headline = new Label("headline", ""));
		add(description = new Label("description", ""));
		add(logo = new NonCachingImage(LOGO));
		
		addLoadingPanel();
		
		WebMarkupContainer progressContainer = 
			new WebMarkupContainer("progressContainer") 
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() 
			{
				return progressCounterIsVisible();
			}
		};
		
		add(progressContainer);
		
		progressContainer.add(createProgress1());
		progressContainer.add(createProgress2());
		
		percentCountLabel = createPercentCountLabel(new Model<String>("0 %"));
		progressContainer.add(percentCountLabel);
		
		headTitleString = new StringResourceModel("you_can", this, null).getString() + " " +
			new StringResourceModel("agile", this, null).getString() +
			" - "+
			new StringResourceModel("url_short_usability_methods", this, null).getString();
		
		headTitle = new Label(HEAD_TITLE, new Model<String>(headTitleString));
		add(headTitle);
	}

	@Override
	public void renderHead(IHeaderResponse response)
	{
		super.renderHead(response);
		
//		response.renderCSSReference(
//			new PackageResourceReference(BasePageSurvey.class, "basepagesurvey.css"));
	}
	
	
	
	protected boolean progressCounterIsVisible() {
		return true;
	}
	
	private void addLoadingPanel() 
	{
		Component loading = new WebMarkupContainer("loading");
		loading.setMarkupId("loading");
		add(loading);

		final AbstractDefaultAjaxBehavior setTimeBehaviour = 
			new AbstractDefaultAjaxBehavior() 
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void respond(AjaxRequestTarget target) {
				pageLoaded(System.currentTimeMillis());
			}
		};

		// add js-function setTime to call setTimeBehaviour
		loading.add(new Behavior()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void renderHead(Component component, IHeaderResponse response)
			{
				String jsCall = "function setTime() { wicketAjaxGet( '"
					+ setTimeBehaviour.getCallbackUrl()
					+ "', null, null, null); }";
				
				response.renderJavaScript(jsCall, null);
			}
		});

		loading.add(setTimeBehaviour);
	}

	protected abstract void pageLoaded(long loadTime);

	public void setHeadlineAndDescription(String headlineText,
			String descriptionText) {
		headline.replaceWith(new Label("headline", headlineText));
		description.replaceWith(new Label("description", descriptionText));
		
		if (headlineText != null && !headlineText.isEmpty()) {
			headTitle.replaceWith(new Label(HEAD_TITLE, headlineText + " - " + headTitleString));
		}
		
	}

	public void setModuleConfigurationDescriptionModel(IModel<String> descriptionModel) 
	{
		moduleConfigurationDescription.replaceWith(new Label(
				"moduleConfigurationDescription", descriptionModel));
	}

	public void setLogo(ImageBase image) {
		if (image != null) {
			Image newLogo = ImageResource.createImage(LOGO, image);
			
			logo.replaceWith(newLogo);
			logo = newLogo;
			
			noscriptPanel.replaceNoScriptLogo(image);
		}
	}
	
	private Label createProgress1() {
		Label rv = new Label(PROGRESS1, new Model<String>(""));

		rv.add(new Behavior() 
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onComponentTag(Component component, ComponentTag tag) 
			{
				tag.getAttributes().put("style", widthString(widthProgress1()));
			}
		});
		return rv;
	}

	private Label createProgress2() {
		Label rv = new Label(PROGRESS2, new Model<String>(""));

		rv.add(new Behavior() 
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onComponentTag(Component component, ComponentTag tag) {
				tag.getAttributes().put("style", widthString(widthProgress2()));
			}
		});
		return rv;
	}

	private String widthString(int width) {
		return "width:" + Integer.toString(width) + "px";
	}

	private int widthProgress1() {
		return getBarWidth();
	}

	private int widthProgress2() {
		return BAR_MAXWIDTH - getBarWidth();
	}

	private int getBarWidth() {
		Integer count = (Integer) getCountModel().getObject();
		Integer maxCount = (Integer) getMaxCountModel().getObject();

		if (maxCount > 0) {
			return BAR_MAXWIDTH * count / maxCount;
		} else {
			return BAR_MAXWIDTH;
		}
	}

	private Image createHelpArrow()
	{
		return new Image("helpArrow", new Model<ResourceReference>()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public ResourceReference getObject()
			{
				return new PackageResourceReference(
						BasePageSurvey.class, 
						"arrow_" + getCssClassForColorComponents() + ".png");
			}
			
		});
	}
	
	protected abstract String getColorForModalWindow();
	
	private void addLinksAndModalWindows()
	{
		addImprintModalWindow();
		addPrivacyModalWindow();
		//addHelpModalWindow();
	}
	
	private void addImprintModalWindow()
	{
		final SurveyModalWindow imprintModal = new SurveyModalWindow(
				"imprintModalWindow", SurveyModalWindow.TypeOfModal.IMPRINT)
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getIconAndFontColor()
			{
				return getColorForModalWindow();
			}
		};
		
		imprintModal.setPageCreator(new ModalWindow.PageCreator()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public Page createPage() {
				return new ImprintPage();
			}
		});
		
		imprintModal.setInitialWidth(500);
		imprintModal.setInitialHeight(370);
		
		imprintModal.setTitle(new StringResourceModel("imprint", this, null));
		
		add(imprintModal);
		
		add(new AjaxLink<Void>("imprintLink")
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick(AjaxRequestTarget target) 
			{
				imprintModal.show(target);
			}
		});
	}
	
	private void addPrivacyModalWindow()
	{
		final SurveyModalWindow privacyModal = 
			new SurveyModalWindow("privacyModalWindow", SurveyModalWindow.TypeOfModal.PRIVACY)
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getIconAndFontColor()
			{
				return getColorForModalWindow();
			}
		};
		
		privacyModal.setPageCreator(new ModalWindow.PageCreator()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public Page createPage() {
				return new PrivacyPage();
			}
		});
		
		privacyModal.setInitialWidth(500);
		privacyModal.setInitialHeight(370);
		
		privacyModal.setTitle(new StringResourceModel("privacy", this, null));
		
		add(privacyModal);
		
		add(new AjaxLink<Void>("privacyLink")
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick(AjaxRequestTarget target) 
			{
				privacyModal.show(target);
			}
		});
	}
	
	private void addHelpModalWindow()
	{
		final SurveyModalWindow helpModal = 
			new SurveyModalWindow("helpModalWindow", SurveyModalWindow.TypeOfModal.HELP)
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getIconAndFontColor()
			{
				return getColorForModalWindow();
			}
		};
		
		helpModal.setPageCreator(new ModalWindow.PageCreator()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public Page createPage() {
				return new HelpPage(getHelpPanel(HelpPage.PANEL_MARKUP_ID));
			}
		});
		
		helpModal.setInitialWidth(500);
		helpModal.setInitialHeight(370);
		
		helpModal.setTitle(new StringResourceModel("help", this, null));
		
		add(helpModal);
		
		add(new AjaxLink<Void>("helpLink")
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick(AjaxRequestTarget target) 
			{
				helpModal.show(target);
			}
		});
	}
	
	protected abstract Component getHelpPanel(String id);
	
	/**
	 * Labels and Models to display current module index,
	 * progress in percent and maximum number of modules,
	 * to display the progress bar correctly
	 */
	
	// model and label for count
	private IModel countModel;

	private IModel getCountModel() {
		return countModel;
	}

	public void setCountModel(IModel model) {
		this.countModel = model;
		
		Label countLabelTopReplacement = createCountLabelTop(model);
		countLabelTop.replaceWith(countLabelTopReplacement);
		countLabelTop = countLabelTopReplacement;
	}
	
	private Label createCountLabelTop(final IModel model) {
		Label label = new Label(COUNT_TOP, new Model<String>() {
			@Override
			public String getObject() {
				return "<p>" + model.getObject().toString() + "</p>";
			}
		});
		label.add(new AttributeAppender("class", new Model<String>() 
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				return "circle_" + getCssClassForColorComponents();
			}
		}, " "));
		
		label.setEscapeModelStrings(false);
		
		return label;
	}
	
	// model and label for maxCount
	private IModel maxCountModel;
	
	private IModel getMaxCountModel() {
		return maxCountModel;
	}

	public void setMaxCountModel(IModel model) {
		this.maxCountModel = model;
	}
	
	// Model and label to count progress in percent
	private IModel percentCountModel;
	
	private Label percentCountLabel;
	
	public IModel getPercentCountModel() {
		return percentCountModel;
	}
	
	private Label createPercentCountLabel(IModel<String> model) {
		Label pc = new Label(PERCENT_COUNT, model);
		
		pc.add(new Behavior() 
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onComponentTag(Component component, ComponentTag tag) 
			{	
				if( widthProgress1() >= CHANGE_COLOR_TO_WHITE_WIDTH )
				{
					tag.getAttributes().put("class", "um_background_font_color");
				}
				
			}
		});
		
		return pc;
	}
	
	public void setPercentCountModel(IModel model) 
	{
		this.percentCountModel = model;
		
		Label percentCountModelReplacement = createPercentCountLabel(model);
		percentCountLabel.replaceWith(percentCountModelReplacement);
		percentCountLabel = percentCountModelReplacement;
	}
	
	/**
	 * Since the color for components is variable, this method
	 * returns a string with the css name prefix.
	 * 
	 * @return
	 * 		String: 'black' or 'white'
	 */
	protected abstract String getCssClassForColorComponents();
}
