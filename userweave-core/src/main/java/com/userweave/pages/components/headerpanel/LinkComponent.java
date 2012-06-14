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
package com.userweave.pages.components.headerpanel;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * @author oma
 */
@SuppressWarnings("serial")
public abstract class LinkComponent extends Panel {

	protected AbstractLink link;
	
	public LinkComponent(String markupId, IModel label, boolean openInNewWindow) {
		super(markupId);
		init(label, openInNewWindow);
	}
	
	public LinkComponent(String markupId, IModel label) {
		super(markupId);
		init(label, false);
	}

	public LinkComponent(String id, IModel label, String linkTarget)
	{
		super(id);
		initLinkWithTarget(label, linkTarget);
	}
	
	private void initLinkWithTarget(IModel label, String linkTarget) 
	{
		link = createLink("link");
		link.add(new AttributeModifier("target", true, new Model(linkTarget)));
		link.add(new Label("label", label));
		add(link);
	}

	private void init(IModel label, boolean openInNewWindow) {
		//AbstractLink link = createLink("link");
		
		link = createLink("link");
		
		if (openInNewWindow) {
			link.add(new AttributeModifier("target", true, new Model("_blank")));
		}
		link.add(new Label("label", label));
		add(link);
	}
	
	protected abstract AbstractLink createLink(String markupId);
	
	public AbstractLink getLink()
	{
		return link;
	}
}

