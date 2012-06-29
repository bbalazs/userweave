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

import java.util.List;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.form.AbstractSingleSelectChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.IOnChangeListener;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.apache.wicket.util.string.Strings;

@Deprecated
public class AuthOnlyRadioChoice extends AbstractSingleSelectChoice 
	implements IOnChangeListener, IAuthOnly
{
	private static final long serialVersionUID = 1L;

//	/** suffix change record. */
//	private class SuffixChange extends Change
//	{
//		private static final long serialVersionUID = 1L;
//
//		final String prevSuffix;
//
//		SuffixChange(String prevSuffix)
//		{
//			this.prevSuffix = prevSuffix;
//		}
//
//		/**
//		 * @see org.apache.wicket.version.undo.Change#undo()
//		 */
//		@Override
//		public void undo()
//		{
//			setSuffix(prevSuffix);
//		}
//
//		/**
//		 * @see java.lang.Object#toString()
//		 */
//		@Override
//		public String toString()
//		{
//			return "SuffixChange[component: " + getPath() + ", suffix: " + prevSuffix + "]";
//		}
//	}
//
//	/**
//	 * Prefix change record.
//	 */
//	private class PrefixChange extends Change
//	{
//		private static final long serialVersionUID = 1L;
//
//		private final String prevPrefix;
//
//		/**
//		 * Construct.
//		 * 
//		 * @param prevSuffix
//		 */
//		PrefixChange(String prevSuffix)
//		{
//			prevPrefix = prevSuffix;
//		}
//
//		/**
//		 * @see org.apache.wicket.version.undo.Change#undo()
//		 */
//		@Override
//		public void undo()
//		{
//			setPrefix(prevPrefix);
//		}
//
//		/**
//		 * @see java.lang.Object#toString()
//		 */
//		@Override
//		public String toString()
//		{
//			return "PrefixChange[component: " + getPath() + ", prefix: " + prevPrefix + "]";
//		}
//	}


	private String prefix = "";
	private String suffix = "<br />\n";
	
	private boolean isAuthorized = false;
	
	@Override
	public void setIsAuthorized(boolean isAuthorized)
	{
		this.isAuthorized = isAuthorized;
	}
	

	public AuthOnlyRadioChoice(String id, List choices,
			IChoiceRenderer renderer)
	{
		super(id, choices, renderer);
	}

	public AuthOnlyRadioChoice(String id, IModel model, List choices,
			IChoiceRenderer renderer)
	{
		super(id, model, choices, renderer);
	}
	
	/**
	 * @see org.apache.wicket.markup.html.form.FormComponent#onComponentTag(org.apache.wicket.markup.ComponentTag)
	 */
	@Override
	protected void onComponentTag(ComponentTag tag)
	{
		super.onComponentTag(tag);
		
		// since this component cannot be attached to input tag the name
		// variable is illegal
		tag.remove("name");
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
	
	protected void onSelectionChanged(Object newSelection){}

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
	 * @return Prefix to use before choice
	 */
	public String getPrefix()
	{
		return prefix;
	}

	/**
	 * @param prefix
	 *            Prefix to use before choice
	 * @return this
	 */
	public final AuthOnlyRadioChoice setPrefix(String prefix)
	{
		// Tell the page that this component's prefix was changed
		//addStateChange(new PrefixChange(this.prefix));
		this.prefix = prefix;
		return this;
	}
	
	/**
	 * @return Separator to use between radio options
	 */
	public String getSuffix()
	{
		return suffix;
	}

	/**
	 * @param suffix
	 *            Separator to use between radio options
	 * @return this
	 */
	public final AuthOnlyRadioChoice setSuffix(String suffix)
	{
		// Tell the page that this component's suffix was changed
		//addStateChange(new SuffixChange(this.suffix));
		this.suffix = suffix;
		return this;
	}

	/**
	 * @see org.apache.wicket.Component#onComponentTagBody(MarkupStream, ComponentTag)
	 */
	@Override
	public final void onComponentTagBody(final MarkupStream markupStream,
		final ComponentTag openTag)
	{
		// Iterate through choices
		final List choices = getChoices();

		// Buffer to hold generated body
		final AppendingStringBuffer buffer = new AppendingStringBuffer((choices.size() + 1) * 70);

		// The selected value
		final String selected = getValue();

		// Loop through choices
		for (int index = 0; index < choices.size(); index++)
		{
			// Get next choice
			final Object choice = choices.get(index);

			Object displayValue = getChoiceRenderer().getDisplayValue(choice);
			Class objectClass = displayValue == null ? null : displayValue.getClass();
			// Get label for choice
			String label = "";
			if (objectClass != null && objectClass != String.class)
			{
				label = getConverter(objectClass).convertToString(displayValue, getLocale());
			}
			else if (displayValue != null)
			{
				label = displayValue.toString();
			}

			// If there is a display value for the choice, then we know that the
			// choice is automatic in some way. If label is /null/ then we know
			// that the choice is a manually created radio tag at some random
			// location in the page markup!
			if (label != null)
			{
				if(isAuthorized)
				{
					// Append option suffix
					buffer.append(getPrefix());
	
					String id = getChoiceRenderer().getIdValue(choice, index);
					final String idAttr = getMarkupId() + "_" + id;
	
					boolean enabled = isEnabled() && isEnableAllowed() &&
						!isDisabled(choice, index, selected);

				
					// Add radio tag
					buffer.append("<input name=\"")
						.append(getInputName())
						.append("\"")
						.append(" type=\"radio\"")
						.append((isSelected(choice, index, selected) ? " checked=\"checked\"" : ""))
						.append((enabled ? "" : " disabled=\"disabled\""))
						.append(" value=\"")
						.append(id)
						.append("\" id=\"")
						.append(idAttr)
						.append("\"");
				
					// Should a roundtrip be made (have onSelectionChanged called)
					// when the option is clicked?
					if (wantOnSelectionChangedNotifications())
					{
						CharSequence url = urlFor(IOnChangeListener.INTERFACE);

						Form form = findParent(Form.class);
						if (form != null)
						{
//							RequestContext rc = RequestContext.get();
//							if (rc.isPortletRequest())
//							{
//								// restore url back to real wicket path as its going to be interpreted
//								// by the form itself
//								url = ((PortletRequestContext)rc).getLastEncodedPath();
//							}
//							buffer.append(" onclick=\"").append(form.getJsForInterfaceUrl(url)).append(
//								";\"");
						}
						else
						{
							// TODO: following doesn't work with portlets, should be posted to a dynamic
							// hidden form
							// with an ActionURL or something
							// NOTE: do not encode the url as that would give
							// invalid JavaScript
							buffer.append(" onclick=\"window.location.href='")
								.append(url)
								.append(
									(url.toString().indexOf('?') > -1 ? "&amp;" : "?") + getInputName())
								.append("=")
								.append(id)
								.append("';\"");
						}
					}

					buffer.append("/>");

					// Add label for radio button
					String display = label;
					if (localizeDisplayValues())
					{
						display = getLocalizer().getString(label, this, label);
					}
					CharSequence escaped = Strings.escapeMarkup(display, false, true);
					buffer.append("<label for=\"").append(idAttr).append("\">").append(escaped).append(
						"</label>");

					// Append option suffix
					buffer.append(getSuffix());
				}
				else
				{
					buffer.append("<span class=\"radioChoice\">");
					
					// Add simple span with text and icon
					if(isSelected(choice, index, selected))
					{
						buffer.append("<span class=\"radioSelected\"></span>");
					}
					else
					{
						buffer.append("<span class=\"radioNotSelected\"></span>");
					}
					
					String display = label;
					if (localizeDisplayValues())
					{
						display = getLocalizer().getString(label, this, label);
					}
					CharSequence escaped = Strings.escapeMarkup(display, false, true);
					
					buffer.append("<span>" + escaped + "</span>")
						  .append("</span><br />");
				}
			}
		}

		// Replace body
		replaceComponentTagBody(markupStream, openTag, buffer);
	}
}
