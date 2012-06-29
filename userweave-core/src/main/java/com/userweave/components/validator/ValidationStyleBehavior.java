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
package com.userweave.components.validator;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.FormComponent;

@SuppressWarnings("serial")
public class ValidationStyleBehavior extends AbstractBehavior {

	@Override
	public void onComponentTag(final Component component, final ComponentTag tag) {
		FormComponent comp = (FormComponent) component;
		if (comp.isValid() && comp.getConvertedInput() != null) {
			tag.getAttributes().put("class", "input-text");
		} else if (!comp.isValid()) {
			tag.getAttributes().put("class", "input-text-invalid");
		}
	}
};