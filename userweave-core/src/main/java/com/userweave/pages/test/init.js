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
var origPadding = "0px";
var origWidth = "910px"; 

$(document).ready(function()
{
	// enable page (only iff javascript is enabled))
	$('#page').show();

	// saving #mainContents attributes
	origPadding = $('#mainContent').css('padding');
	if($('#mainContent').css('width') != "0px") {
	    // konqueror bug, we must make sure that this does
	    // not break the layout
	    origWidth = $('#mainContent').css('width');
	}

	//$('#mainContent').css('padding', '0px').css('width', '910px');

	// show study after load
	if(true) {
		$(".showPostLoad").show();
		$(".removePostLoad").remove();
		setTime();
	}
	
	$(".imageButtonSubmitValue").click(function(){
		var parent = $(this).parents("form:first");
			parent.submit();
	});
	$(".imageButtonSubmitValueForMockup").click(function(){
		var parent = $(this).parents("form:first");
			parent.submit();
	});
});
	
function openPopup( val ) {
	// Set headline
	$('.headerInfo').html($('#headline'+val).html());
	
	// set headline image (get picture out of hidden popupData block)
	$('.containerCircle > img').attr('src', $('#image' + val).attr('src'));
			
	// make popup visible and draggable 
	$('.popupDrag').show().draggable({scroll: false, cursor:'pointer'});
		    
	// load data
	$('#popupContainer').empty()
		.append('<div id="popupContent" class="scroll-pane"><span style="padding-right:9px;">' + 
			$('#text'+val).html() + '</span></div>');

	// build a scrollPane
	$('.scroll-pane').jScrollPane();
}
			
function closePopup() {
	$('.popupDrag').hide();
}
