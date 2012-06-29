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
package com.userweave.components.customModalWindow;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.string.AppendingStringBuffer;

/**
 * Modal window for the survey UI. Includes a modified ui for the 
 * base modal window.
 * 
 * @author opr
 *
 */
public class SurveyModalWindow extends ModalWindow
{
	private static final long serialVersionUID = 1L;

	/**
	 * Reference to the modified modal window javascript,
	 * wihich overrides the creator for the markup.
	 */
	private static ResourceReference JAVASCRIPT = 
		new JavaScriptResourceReference(
			SurveyModalWindow.class, "res/modal.js");

	/**
	 * Type of this modal dialog.
	 * 
	 * @author opr
	 */
	public enum TypeOfModal {
		IMPRINT("imprint"), 
		HELP("help"), 
		PRIVACY("privacy");
		
		private String title;
		
		TypeOfModal(String title)
		{
			this.title = title;
		}
		
		@Override
		public String toString()
		{
			return title;
		}
	}
	
	/**
	 * Type of this modal.
	 * 
	 * @see SurveyModalWindow.TypeOfModal
	 */
	private final TypeOfModal type;
	
	/**
	 * Color settings for close button, caption text and circle.
	 * 
	 * To modify the color setting, set this attribute.
	 */
	private String iconAndFontColor = "white";
	
	public void setIconAndFontColor(String color)
	{
		iconAndFontColor = color;
	}
	
	public String getIconAndFontColor()
	{
		return iconAndFontColor;
	}
	
	
	private AjaxLink<Void> close;
	
	public SurveyModalWindow(String id, SurveyModalWindow.TypeOfModal type)
	{
		super(id);
		
		this.type = type;
		
		init();
	}

	public SurveyModalWindow(String id, IModel<?> model, SurveyModalWindow.TypeOfModal type)
	{
		super(id, model);
		
		this.type = type;
		
		init();
	}
	
	private void init()
	{
		setCssClassName(CustomModalWindow.CSS_CLASS_CUSTOM);
		setMaskType(ModalWindow.MaskType.TRANSPARENT);
		setInitialHeight(CustomModalWindow.defaultHeight);
		setInitialWidth(CustomModalWindow.defaultWidth);
		
		add(close = new AjaxLink<Void>("close")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) 
			{
				SurveyModalWindow.this.close(target);
			}
		});
		
		close.setOutputMarkupId(true);
		close.setOutputMarkupPlaceholderTag(true);
	}
	
	@Override
	public void show(AjaxRequestTarget target)
	{
		super.show(target);
		
		if(! isShown())
		{
			target.add(close);
		}
	}
	
	@Override
	public void renderHead(IHeaderResponse response)
	{
		super.renderHead(response);
		
		response.renderJavaScriptReference(JAVASCRIPT);
		
		response.renderCSSReference(new PackageResourceReference(
				CustomModalWindow.class, "res/ModifiedModal.css"));
		
		// add IE hacks
		if(isIe7Or8())
		{
			response.renderCSSReference(new PackageResourceReference(
					SurveyModalWindow.class, "res/ModifiedModal_ie7.css"));
		}
	}
	
	private boolean isIe7Or8()
	{
		WebClientInfo clientInfo = (WebClientInfo)Session.get().getClientInfo();
		
		if(clientInfo == null || clientInfo.getUserAgent() == null)
		{
			return false;
		}
		else
		{
			return clientInfo.getUserAgent().contains("MSIE 7.0") ||
				   clientInfo.getUserAgent().contains("MSIE 8.0");
		}
	}

	@Override
	public String getContentId()
	{
		return "content";
	}
	
	@Override
	protected AppendingStringBuffer postProcessSettings(
			AppendingStringBuffer settings)
	{
		settings.append("settings.color").append("=\"");
		settings.append(getIconAndFontColor());
		settings.append("\";\n");
		
		settings.append("settings.typeOf").append("=\"");
		settings.append(this.type);
		settings.append("\";\n");
		
		return settings;
	}
}
