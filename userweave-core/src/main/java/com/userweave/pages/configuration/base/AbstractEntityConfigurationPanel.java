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
package com.userweave.pages.configuration.base;

import java.awt.Image;
import java.util.Arrays;

import javax.swing.ImageIcon;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.PackageResourceReference;

import com.userweave.components.authorization.AuthOnlyDropDownChoice;
import com.userweave.components.authorization.AuthOnlyTextField;
import com.userweave.components.imageUpload.IconUploadPanel;
import com.userweave.dao.BaseDao;
import com.userweave.domain.ImageBase;
import com.userweave.domain.Role;
import com.userweave.module.methoden.iconunderstandability.page.conf.images.ImageUploadCallback;
import com.userweave.pages.base.BaseUserWeavePage;

public abstract class AbstractEntityConfigurationPanel<T> extends Panel
{
	private static final long serialVersionUID = 1L;

	/**
	 * Represents the reset color link.
	 * 
	 * @author opr
	 */
	@AuthorizeAction(action = Action.RENDER, 
					 roles = {Role.PROJECT_ADMIN, 
							  Role.PROJECT_PARTICIPANT})
	protected class AuthOnlyWebMarkupContainer extends WebMarkupContainer
	{
		private static final long serialVersionUID = 1L;

		public AuthOnlyWebMarkupContainer(String id)
		{
			super(id);
		}
		
		@Override
		protected void onComponentTag(ComponentTag tag)
		{
			if(! isEnabled())
			{
				tag.setName("span");
			}
			else
			{
				super.onComponentTag(tag);
			}
		}
	}
	
	/**
	 * Max width of an uploaded logo.
	 */
	protected final int MAX_WIDTH = 170;
	
	/**
	 * Max height of an uploaded logo.
	 */
	protected final int MAX_HEIGHT = 40;
	
	/**
	 * Error message, if logo is to large.
	 */
	private final StringResourceModel logoCutAcrossMaxSizeMessage = 
		new StringResourceModel(
				"individual_logo_cut_across_max_size", 
				this, 
				null, 
				new Object[] { MAX_WIDTH, MAX_HEIGHT});
	
	

	/**
	 * Reference to the font color drop down
	 */
	private AuthOnlyDropDownChoice fontColorDropDown;
	
	/**
	 * Deafult constructor.
	 * 
	 * @param id
	 * 		Component markup id.
	 * @param entityId
	 * 		Id of entity to configure.
	 */
	public AbstractEntityConfigurationPanel(String id, final Integer entityId)
	{
		super(id);
		
		setDefaultModel(
				new CompoundPropertyModel<T>(
						new LoadableDetachableModel<T>()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			protected T load()
			{
				return getBaseDao().findById(entityId);
			}
		}));
		
		Form<Void> form = new Form<Void>("form");
		
		add(form);
		
		init(form);
	}
	
	@Override
	public void renderHead(IHeaderResponse response)
	{
		response.renderCSSReference(new PackageResourceReference(
				BaseUserWeavePage.class, "res/colorpicker.css"));
		
		response.renderJavaScriptReference(new PackageResourceReference(
				BaseUserWeavePage.class, "res/colorpicker.js"));
	}
	
	/**
	 * Initializes this component.
	 * 
	 * @param form
	 * 		Form to add new components to.
	 */
	protected void init(Form<Void> form)
	{
		form.add(getColorField());
		form.add(getResetColorLink());
		initColorPicker();
		
		form.add(getIconUploadPanel());
		
		form.add(getFontColorDropDwn());
		
		initComponent(form);
	}
	
	/**
	 * Factory method  forthe color picker text field.
	 * 
	 * @return
	 * 		An auth only text field.
	 */
	protected AuthOnlyTextField getColorField()
	{
		return new AuthOnlyTextField(
			"backgroundColor", 
			new PropertyModel<String>(getDefaultModel(), "backgroundColor"),
			getUpdateBehavior("onblur"));
	}
	
	/**
	 * Factory method to get a reset color link.
	 * @return
	 */
	protected WebMarkupContainer getResetColorLink()
	{
		return new AuthOnlyWebMarkupContainer("resetColorLink");
	}	
	
	/**
	 * Initializes the color picker script on this panel.
	 */
	private void initColorPicker()
	{
		AjaxRequestTarget target = AjaxRequestTarget.get();
		
		if(target != null)
		{
			if(userIsAuthorized()) 
			{	
				target.appendJavaScript("initColorPicker();");
			}
			else
			{
				target.appendJavaScript(
					"$('div.colorpicker-comp div')" +
					".css('backgroundColor', " +
					"$('span.newBackgroundColor:first').text());");
			}
		}
	}
	
	/**
	 * Needed to save From components like textarea and textfields.
	 * 
	 * @usage: component.add(getUpdateBehavior(event, markupId))
	 * @param event the javascript event trigger (mostly 'onblur')
	 *
	 * @return
	 */
	protected AjaxFormComponentUpdatingBehavior getUpdateBehavior(String event)
	{
		return new AjaxFormComponentUpdatingBehavior(event) 
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) 
			{				
				onBackgroundColorUpdate(target);
				
				target.add(fontColorDropDown);
			}
		};
	}
	
	protected IconUploadPanel getIconUploadPanel()
	{
		return new IconUploadPanel("logoUpload", new ImageUploadCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public byte[] getImageData()
			{
				return AbstractEntityConfigurationPanel.this.getImageData();	
			}

			@Override
			public void remove()
			{
				AbstractEntityConfigurationPanel.this.deleteLogo();
			}

			@Override
			public void saveFileUpload(FileUpload fileUpload) throws Exception
			{
				ImageBase logo = getEntityLogo();
				
				T entity = getEntity();
				
				if (logo == null)
				{
					logo = new ImageBase();
					setEntityLogo(entity, logo);
				}

				Image inImage = new ImageIcon(fileUpload.getBytes()).getImage();

				if (inImage.getWidth(null)  < 1 		|| 
					inImage.getWidth(null)  > MAX_WIDTH || 
					inImage.getHeight(null) < 1 		|| 
					inImage.getHeight(null) > MAX_HEIGHT)
				{
					// throw exception to handle in upload page.
					throw new Exception(logoCutAcrossMaxSizeMessage.getString());
				}
				else
				{
					logo.setImageData(fileUpload.getBytes());
					logo.incrementVersion();
					getBaseDao().save(entity);
				}
			}
		});
	}
	
	/**
	 * Factory method for the font color drop down choice.
	 */
	protected AuthOnlyDropDownChoice getFontColorDropDwn()
	{		
		fontColorDropDown = new AuthOnlyDropDownChoice(
				"fontColor",
				new PropertyModel(getDefaultModel(), "fontColor"),
				Arrays.asList(new Boolean[] {true, false}),
				getFontColorChoiceRender());
	
		fontColorDropDown.setOutputMarkupId(true);
		
		fontColorDropDown.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				getBaseDao().save(getEntity());
			}
		});
		
		return fontColorDropDown;
	}
	
	/**
	 * Factory method to create a font color choice renderer.
	 * 
	 * @return
	 * 		A boolean valued choice renderer.
	 */
	private IChoiceRenderer getFontColorChoiceRender()
	{
		return new ChoiceRenderer()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Object getDisplayValue(Object object)
			{
				if((Boolean) object)
				{
					return new StringResourceModel(
						"font_color_white", 
						AbstractEntityConfigurationPanel.this, 
						null).getObject();
				}
				
				return new StringResourceModel(
						"font_color_black", 
						AbstractEntityConfigurationPanel.this, 
						null).getObject();
			}
		};
	}
	
	/**
	 * Compute brightness of given background color
	 * and returns matching font color.
	 * 
	 * Uses the HSV color model.
	 * 
	 * @param backgroundColor
	 * @return
	 */
	protected boolean computeBrightnessOfBackgroundAndSetColorValue(String backgroundColor) {
		
		if(backgroundColor == null) {
			return true;
		}
		
		int[] bounds = {0, 2, 4};
		
		if(backgroundColor.startsWith("#"))
		{
			bounds[0]++;
			bounds[1]++;
			bounds[2]++;
		}
		
		double[] rgb = { Integer.parseInt(backgroundColor.substring(bounds[0], bounds[0] + 2), 16) / 255.0,
						 Integer.parseInt(backgroundColor.substring(bounds[1], bounds[1] + 2), 16) / 255.0,
						 Integer.parseInt(backgroundColor.substring(bounds[2], bounds[2] + 2), 16) / 255.0};
		
		double max = Math.max(rgb[0], Math.max(rgb[1], rgb[2]));
		
		if(max > 0.8)
			return false;
		else
			return true;
	}
	
	/**
	 * Get the underlying entity object.
	 * 
	 * @return
	 * 		The entity.
	 */
	@SuppressWarnings("unchecked")
	protected T getEntity()
	{
		return (T) getDefaultModelObject();
	}
	
	/**
	 * Returns the DAO for the entity to load.
	 * 
	 * @return
	 * 		A BaseDao sub class.
	 */
	protected abstract BaseDao<T> getBaseDao();
	
	/**
	 * Override, to initalizes this component further.
	 * @param form
	 */
	protected abstract void initComponent(Form<Void> form);
	
	/**
	 * Delegate update of background color to sub classes.
	 * 
	 * @param target
	 * 		The ajax request target.
	 */
	protected abstract void onBackgroundColorUpdate(AjaxRequestTarget target);

	/**
	 * Enables the reset color link.
	 * 
	 * @return
	 * 		True, if the user has the rights to change
	 * 		the color.
	 */
	protected abstract boolean userIsAuthorized();
	
	/**
	 * Get the image date from the entity or null,
	 * if the entity has no logo.
	 * 
	 * @return
	 */
	protected abstract byte[] getImageData();
	
	/**
	 * Deletes the logo of this entity.
	 */
	protected abstract void deleteLogo();
	
	/**
	 * Get the logo from the entity.
	 */
	protected abstract ImageBase getEntityLogo();
	
	/**
	 * Sets the logo of this entity.
	 * 
	 * @param entity
	 * @param image
	 */
	protected abstract void setEntityLogo(T entity, ImageBase image);

	/**
	 * Sets the font color of this entity.
	 * 
	 * @param entity
	 * @param fontColor
	 */
	//protected abstract void setEntityFontColor(T entity, boolean fontColor);
}
