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

import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.form.AbstractSingleSelectChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.IOnChangeListener;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.handler.ListenerInterfaceRequestHandler;
import org.apache.wicket.request.handler.PageAndComponentProvider;

public class AuthOnlyDropDownChoice extends AbstractSingleSelectChoice implements IOnChangeListener, IAuthOnly {

	/**
	 * If the user is authorized, render the input field,
	 * else render a label.
	 */
	private boolean isAuthorized = false;
	
	@Override
	public void setIsAuthorized(boolean isAuthorized)
	{
		this.isAuthorized = isAuthorized;
	}
	
	public AuthOnlyDropDownChoice(String id) {
		super(id);
	}

	public AuthOnlyDropDownChoice(String id, List choices) {
		super(id, choices);
	}

	public AuthOnlyDropDownChoice(String id, IModel choices) {
		super(id, choices);
	}

	public AuthOnlyDropDownChoice(String id, List data, IChoiceRenderer renderer) {
		super(id, data, renderer);
	}

	public AuthOnlyDropDownChoice(String id, IModel model, List choices) {
		super(id, model, choices);
	}

	public AuthOnlyDropDownChoice(String id, IModel model, IModel choices) {
		super(id, model, choices);
	}

	public AuthOnlyDropDownChoice(String id, IModel choices,
			IChoiceRenderer renderer) {
		super(id, choices, renderer);
	}

	public AuthOnlyDropDownChoice(String id, IModel model, List data,
			IChoiceRenderer renderer) {
		super(id, model, data, renderer);
	}

	public AuthOnlyDropDownChoice(String id, IModel model, IModel choices,
			IChoiceRenderer renderer) {
		super(id, model, choices, renderer);
	}
	
	/**
	 * Called when a selection changes.
	 */
	public final void onSelectionChanged()
	{
		convertInput();
		updateModel();
		onSelectionChanged(getModelObject());
	}

	/**
	 * Processes the component tag.
	 * 
	 * @param tag
	 *            Tag to modify
	 * @see org.apache.wicket.Component#onComponentTag(org.apache.wicket.markup.ComponentTag)
	 */
	@Override
	protected void onComponentTag(final ComponentTag tag)
	{
		if (!isAuthorized) 
		{
			tag.setName("em");
		} 
		else
		{
			checkComponentTag(tag, "select");

			// Should a roundtrip be made (have onSelectionChanged called) when the
			// selection changed?
			if (wantOnSelectionChangedNotifications())
			{
				// we do not want relative URL here, because it will be used by
				// Form#dispatchEvent
				CharSequence url = urlFor(new ListenerInterfaceRequestHandler(
					new PageAndComponentProvider(getPage(), this), IOnChangeListener.INTERFACE));

				Form<?> form = findParent(Form.class);
				if (form != null)
				{
					tag.put("onchange", form.getJsForInterfaceUrl(url.toString()));
				}
				else
				{
					tag.put("onchange", "window.location.href='" + url +
						(url.toString().indexOf('?') > -1 ? "&" : "?") + getInputName() +
						"=' + this.options[this.selectedIndex].value;");
				}
			}
		}
		
		super.onComponentTag(tag);
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
	protected void onSelectionChanged(final Object newSelection)
	{
	}

	/**
	 * Whether this component's onSelectionChanged event handler should be called using javascript
	 * <tt>window.location</tt> if the selection changes. If true, a roundtrip will be generated
	 * with each selection change, resulting in the model being updated (of just this component) and
	 * onSelectionChanged being called. This method returns false by default. If you wish to use
	 * Ajax instead, let {@link #wantOnSelectionChangedNotifications()} return false and add an
	 * {@link AjaxFormComponentUpdatingBehavior} to the component using the <tt>onchange</tt>
	 * event.
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
	
	@Override
	public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag tag)
	{
		if (!isAuthorized)
		{
			/**
			 * Find the selected value to display.
			 */
			List choices = getChoices();
			final String selected = getValue();
			
			for (int index = 0; index < choices.size(); index++)
			{
				final Object choice = choices.get(index);
				
				if(isSelected(choice, index, selected))
				{
					replaceComponentTagBody(
							markupStream, 
							tag, 
							getChoiceRenderer().getDisplayValue(choice).toString());
					
					break;
				}
			}
		}
		else
		{	
            super.onComponentTagBody(markupStream, tag);
		}
	}

}
