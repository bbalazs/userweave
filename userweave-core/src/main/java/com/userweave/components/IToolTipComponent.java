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
package com.userweave.components;

/**
 * Interface for components, which implements a tool tip
 * (i.e. a title attribute in the tag).
 * 
 * @author opr
 *
 */
public interface IToolTipComponent
{
	/**
	 * Type of tooltip to display.
	 * 
	 * The displayed tooltip depends on the choosen ToolTipType
	 * as follows:
	 * 		If the ToolTipTpye of this component changes (i.e.
	 * 		if setToolTipType is called) the new value will be
	 * 		compared to the old value and overwrites it only,
	 * 		if new_value &leq; old_value.
	 * 
	 * @author opr
	 *
	 */
	public static enum ToolTipType 
	{
		/**
		 * The lowest type. Gets displayed, if a component will
		 * never be enabled, because its not possible (e.x. moving
		 * the first or last module of a study).
		 */
		IMPOSSIBLE("disabled_impossible"), 
		
		/**
		 * Links can be shown in every study state but don't work,
		 * because it makes no sense or is forbidden (i.e. changing
		 * a module name, if the study runs or is already finished).
		 */
		PHASE("disabled_phase"), 
		
		/**
		 * Depending on the current view state (config or report),
		 * this component makes only sense in one of this states
		 * (e.x. the filter button in a question panel).
		 */
		VIEW("disabled_view"), 
		
		/**
		 * This component is disabled because of the lacking rights
		 * of the user (mostly, this applies to guest users).
		 */
		RIGHTS("disabled_rights"); 
		
		/**
		 * Resource key for string resource models, which holds the
		 * display string for the selected type;
		 */
		private final String stringResourceKey;
		
		public String getStringResourceKey()
		{
			return stringResourceKey;
		}
		
		/**
		 * Default constructor.
		 * 
		 * @param stringResourceKey
		 * 		Key for a string resource model.
		 */
		ToolTipType(String stringResourceKey)
		{
			this.stringResourceKey = stringResourceKey;
		}
	}
	
	/**
	 * Sets the type of tool tip to display;
	 * 
	 * @param toolTipType
	 * 		Type of tooltip.
	 */
	public void setToolTipType(ToolTipType toolTipType);
	
	/**
	 * Gets the current tool tip type.
	 * @return
	 */
	public ToolTipType getToolTipType();
}
