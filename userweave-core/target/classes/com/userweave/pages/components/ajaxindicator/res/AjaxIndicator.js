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
var showModal = true;

function enableModalOverlay()
{
	showModal = true;
}

function disableModalOverlay()
{
	showModal = false;
}

/**
 * Convenient method to show an overlay.
 */
function showModalOverlay() 
{	
	if($.browser.msie && $.browser.version == "7.0")
	{
		// IE 7 does not understand overlay css property.
		$("#ajaxindicator-modal-overlay").css('filter', 'alpha(opacity=80)').fadeIn(100);
	}
	else
	{
		$("#ajaxindicator-modal-overlay").css("opacity", 0.8).fadeIn(100);
	}
}

/**
 * Convenient method to hide an overlay.
 */
function hideModalOverlay() 
{
	$("#ajaxindicator-modal-overlay").stop().fadeOut(100);
}

/**
 * Override precall handler to show an overlay
 * for ajax events.
 */
window.wicketGlobalPreCallHandler = function() {
	if(showModal)
		showModalOverlay();
};

/**
 * Override postcall handler to hide an overlay. Also 
 * start an update thread for the freetext module, 
 * because the onchange event handler does not work 
 * there.
 */
window.wicketGlobalPostCallHandler = function() {
	if(showModal)
		hideModalOverlay();
		
	var freetext = $("textarea[name^='freetext']");
	
	if (freetext.length > 0) {
		startThread(); // see initUpdateThread.js
	}
};
