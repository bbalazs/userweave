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
package com.userweave.pages.configuration;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;

import com.userweave.components.IToolTipComponent;
import com.userweave.components.IToolTipComponent.ToolTipType;
import com.userweave.domain.StudyState;

public class DisableEventLinkVisitor extends DisableLinkVisitor
{
	public DisableEventLinkVisitor(IModel<StudyState> statusModel)
	{
		super(statusModel);
	}
	
	@Override
	protected void onLinkComponent(AbstractLink link) 
	{
		EnabledInStudyState enabledInStudyState = 
			link.getClass().getAnnotation(EnabledInStudyState.class);
		
		if (enabledInStudyState != null) 
		{
			for (StudyState studyState: enabledInStudyState.states()) 
			{	
				if (studyState == getStudyState()) 
				{
					return;
				} 
			}			
			disable(link);
			
			if(link instanceof IToolTipComponent)
			{
				((IToolTipComponent) link).setToolTipType(ToolTipType.PHASE);
			}
		}
	}
	
	@Override
	protected void disable(AbstractLink link) 
	{
		link.setEnabled(false);
	}
}
