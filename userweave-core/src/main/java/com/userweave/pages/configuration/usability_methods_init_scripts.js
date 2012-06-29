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
/** Signal, to check, if mouse is still pressed */
var mouseStillDown = false;

/** step size to move tabbed panel on each slide call */
var stepSize = 20;

/** Size of tabbed panel viewport */
var clippingSize = 650;
/**
 * Init scripts on document ready.
 */
$(document).ready(function()
{
	try
	{
		initColorPicker();
	}
	catch(e)
	{
		// do nothing
	}
	
	bindMouseEvents("studyTabs");
	bindMouseEvents("questionTabs");
	bindMouseEvents("icontestTabs");
});

/**
 * Compute width of tab row which is
 * child element of tabbed panel with
 * css class parentClass.
 */
function computeTabWidth(parentClass)
{
	var width = 1;
	
	$(parentClass + " .tab").each(function()
	{
		width += $(this).width();
	});
	
	$(parentClass + " div.tab-wrapper:first").width(width);
	
	// disable sliders
	if(width <= clippingSize)
	{
		$(parentClass + " .slideContainer div").hide();
	}
}

/**
 * Bind mouse events to sliders of tabbed panels.
 * 
 * @param whichTabbedPanel
 * 			The tab panel css class which is parent of the 
 * 			slider buttons.
 */
function bindMouseEvents(whichTabbedPanel)
{
	var parentClass = "." + whichTabbedPanel;
	
	computeTabWidth(parentClass);
	
	$(parentClass + " .slideLeft").mousedown(function() 
	{
		mouseStillDown = true;
		slideLeft(parentClass);
	});

	$(parentClass + " .slideLeft").mouseup(function() 
	{
		mouseStillDown = false;
	});

	
	$(parentClass + " .slideRight").mousedown(function() 
	{
		mouseStillDown = true;
		slideRight(parentClass);
	});

	$(parentClass + " .slideRight").mouseup(function() 
	{
		mouseStillDown = false;
	});
}

function slideLeft(parentClass) 
{
	if (!mouseStillDown) { return; }
	
	var elem = $(parentClass + " div.tab-wrapper:first");
	var posLeft = elem.position().left;
	
	if(posLeft + stepSize > 0) 
	{ 
		if(! posLeft == 0)
		{
			elem.css("left", posLeft - posLeft);
		}
		
		return;
	} 
	
	elem.css("left", posLeft + stepSize);

	if (mouseStillDown) 
	{
		setTimeout("slideLeft('" + parentClass + "')", 50);
	}
}

function slideRight(parentClass) 
{
	if (!mouseStillDown) { return; }

	// get slider parent
	var elem = $(parentClass + " div.tab-wrapper:first");
	
	// left position of tab wrapper
	var posLeft = elem.position().left;
	
	var leftPositionOfVisible;
	
	if(posLeft < 0)
	{
		leftPositionOfVisible = elem.width() + posLeft;
	}
	else
	{
		leftPositionOfVisible = elem.width() - posLeft;
	}
	
	if(leftPositionOfVisible > clippingSize)
	{
		var rest = leftPositionOfVisible - clippingSize;
		
		if(parentClass == ".studyTabs")
		{
			// we need to adjust width for study tabs, because 
			// they are moved to the left, so the overall width is
			// smaller then the actual given width.
			var lastTab = elem.children(":last");
			var lastTabLeft = parseInt(lastTab.css("left"), 10);
			
			rest += lastTabLeft;
		}
		
		// we can still slide to the right
		if(rest < stepSize)
		{
			// we cannot slide the full step size
			elem.css("left", posLeft - rest);
		}
		else
		{
			elem.css("left", posLeft - stepSize);
		}
		
	}

	if (mouseStillDown) 
	{
		setTimeout("slideRight('" + parentClass + "')", 50);
	}
}

function slideTo(parentClass, tab)
{
	if(tab == -1)
	{
		return;
	}
	
	var elem = $(parentClass + " .tab" + tab + ":first");
	var right = elem.position().left + elem.width();
	
	// scroll into view
	var newLeftPos = right <= clippingSize ? 0 : -(right - clippingSize);
	
	if(newLeftPos < 0)
	{
		// rest tab space on the right of clipping viewport
		var restInvisibleSpace = $(parentClass + " div.tab-wrapper:first").width() - right;
		
		// center tab as much as possible in viewport
		if(restInvisibleSpace < (clippingSize / 2))
		{
			newLeftPos -= restInvisibleSpace;
		}
		else
		{
			newLeftPos -= (clippingSize / 2);
			newLeftPos += elem.width() / 2;
		}
		
		var lastTab = $(parentClass + " .tab.last:first");
		
		
		if(! isNaN(parseInt(lastTab.css('left'))))
		{
			newLeftPos -= parseInt(lastTab.css('left'));
		}
	}
	
	$(parentClass + " div.tab-wrapper:first").css("left", (newLeftPos));
}