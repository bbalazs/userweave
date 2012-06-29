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
package com.userweave.components.authorization;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IOnChangeListener;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.BooleanConverter;
import org.apache.wicket.util.string.StringValueConversionException;
import org.apache.wicket.util.string.Strings;

@Deprecated
public class AuthOnlyCheckBox extends FormComponent implements IOnChangeListener, IAuthOnly {

	private boolean isAuthorized = false;
	
	private static final long serialVersionUID = 1L;

	/**
	 * @see org.apache.wicket.Component#Component(String)
	 */
	public AuthOnlyCheckBox(final String id)
	{
		super(id);
	}

	/**
	 * @see org.apache.wicket.Component#Component(String, IModel)
	 */
	public AuthOnlyCheckBox(final String id, IModel model)
	{
		super(id, model);
	}

	/**
	 * @see org.apache.wicket.markup.html.form.IOnChangeListener#onSelectionChanged()
	 */
	public void onSelectionChanged()
	{
		convertInput();
		updateModel();
		onSelectionChanged(getModelObject());
	}

	/**
	 * Template method that can be overridden by clients that implement IOnChangeListener to be
	 * notified by onChange events of a select element. This method does nothing by default.
	 * <p>
	 * Called when a option is selected of a dropdown list that wants to be notified of this event.
	 * This method is to be implemented by clients that want to be notified of selection events.
	 * 
	 * @param newSelection
	 *            The newly selected object of the backing model NOTE this is the same as you would
	 *            get by calling getModelObject() if the new selection were current
	 */
	protected void onSelectionChanged(Object newSelection)
	{
	}

	/**
	 * Whether this component's onSelectionChanged event handler should called using javascript if
	 * the selection changes. If true, a roundtrip will be generated with each selection change,
	 * resulting in the model being updated (of just this component) and onSelectionChanged being
	 * called. This method returns false by default.
	 * 
	 * @return True if this component's onSelectionChanged event handler should called using
	 *         javascript if the selection changes
	 */
	protected boolean wantOnSelectionChangedNotifications()
	{
		return false;
	}

	/**
	 * @see org.apache.wicket.MarkupContainer#getStatelessHint()
	 */
	@Override
	protected boolean getStatelessHint()
	{
		if (wantOnSelectionChangedNotifications())
		{
			return false;
		}
		return super.getStatelessHint();
	}

	/**
	 * CheckBox will by default always just use the boolean converter because the implementation
	 * expects that the string is can be converted to a boolean {@link Strings#isTrue(String)}
	 * 
	 * @see org.apache.wicket.Component#getConverter(java.lang.Class)
	 */
	@Override
	public IConverter getConverter(Class type)
	{
		return BooleanConverter.INSTANCE;
	}

	/**
	 * Processes the component tag.
	 * 
	 * @param tag
	 *            Tag to modify
	 * @see org.apache.wicket.Component#onComponentTag(ComponentTag)
	 */
	@Override
	protected void onComponentTag(final ComponentTag tag)
	{
		PackageResourceReference img;
		
		if (!isAuthorized) 
		{
			tag.setName("img");
			
			final String value = getValue();
			if (value != null && value.equals("true"))
			{
				img = new PackageResourceReference(AuthOnlyCheckBox.class, "res/check.png");
			}
			else
			{
				img = new PackageResourceReference(AuthOnlyCheckBox.class, "res/uncheck.png");
			}
			CharSequence url = RequestCycle.get().urlFor(img, null);
			
			tag.put("src", RequestCycle.get().getOriginalResponse().encodeURL(
					Strings.replaceAll(url, "&", "&amp;")));
		}
		else
		{
			checkComponentTag(tag, "input");
			checkComponentTagAttribute(tag, "type", "checkbox");
	
			final String value = getValue();
			if (value != null)
			{
				try
				{
					if (Strings.isTrue(value))
					{
						tag.put("checked", "checked");
					}
					else
					{
						// In case the attribute was added at design time
						tag.remove("checked");
					}
				}
				catch (StringValueConversionException e)
				{
					throw new WicketRuntimeException("Invalid boolean value \"" + value + "\"", e);
				}
			}
	
			// Should a roundtrip be made (have onSelectionChanged called) when the
			// checkbox is clicked?
			if (wantOnSelectionChangedNotifications())
			{
//				CharSequence url = urlFor(IOnChangeListener.INTERFACE);
//	
//				Form form = findParent(Form.class);
//				if (form != null)
//				{
//					RequestContext rc = RequestContext.get();
//					if (rc.isPortletRequest())
//					{
//						// restore url back to real wicket path as its going to be interpreted by the
//						// form itself
//						url = ((PortletRequestContext)rc).getLastEncodedPath();
//					}
//					tag.put("onclick", form.getJsForInterfaceUrl(url));
//				}
//				else
//				{
//					// TODO: following doesn't work with portlets, should be posted to a dynamic hidden
//					// form
//					// with an ActionURL or something
//					// NOTE: do not encode the url as that would give invalid
//					// JavaScript
//					tag.put("onclick", "window.location.href='" + url +
//						(url.toString().indexOf('?') > -1 ? "&amp;" : "?") + getInputName() +
//						"=' + this.checked;");
//				}
	
			}
		}
		
		super.onComponentTag(tag);
	}

//	/**
//	 * @see FormComponent#supportsPersistence()
//	 */
//	@Override
//	protected final boolean supportsPersistence()
//	{
//		return true;
//	}


	/**
	 * @see org.apache.wicket.markup.html.form.FormComponent#convertValue(String[])
	 */
	@Override
	protected Object convertValue(String[] value)
	{
		String tmp = value != null && value.length > 0 ? value[0] : null;
		try
		{
			return Strings.toBoolean(tmp);
		}
		catch (StringValueConversionException e)
		{
			throw new ConversionException("Invalid boolean input value posted \"" + getInput() +
				"\"", e).setTargetType(Boolean.class);
		}
	}

	@Override
	public void setIsAuthorized(boolean isAuthorized) 
	{
		this.isAuthorized = isAuthorized;
	}
}
