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
Wicket.Window.prototype.center = function()
{
	var scTop = 0;
	var scLeft = 0;

	if (Wicket.Browser.isIE() || Wicket.Browser.isGecko()) 	{
		scLeft = Wicket.Window.getScrollX();
		scTop = Wicket.Window.getScrollY();
	}
	
	var width = Wicket.Window.getViewportWidth();
	var height = Wicket.Window.getViewportHeight();
	
	var modalWidth = this.window.offsetWidth;
	var modalHeight = this.window.offsetHeight;
	
	// BUGFIX: Adjust the width and height to maximum values
	if (modalWidth > width - 10) 
	{
		this.window.style.width = (width - 10) + "px";
		modalWidth = this.window.offsetWidth;
	}
	
	if (modalHeight > height - 40) 
	{
		this.content.style.height = (height - 40) + "px";
		modalHeight = this.window.offsetHeight;
	} 
	
	var left = (width / 2) - (modalWidth / 2) + scLeft;
	var top = (height / 2) - (modalHeight / 2) + scTop;
	
	// BUGFIX: Ensure positive position values
	this.window.style.left = Math.max(left, 0) + "px";
	this.window.style.top = Math.max(top, 0) + "px";
}