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
$(document).ready(function(){
	$.debug(true);
	// some jquery magic: add progressbar to mockupheader
	$('#circleForNumber').html($('#counter').html());
	$('#circleForNumber').css( "color", $(".um_background").css("background-color") );
	$('#header_config_text').html($('#moduleConfigurationDescription').html());
	
});

function startTimer() {
	function iLog(xxx) {
	    //$.debug(true);
	    //$.log(xxx);
	}

    function countDownHideMockupFrame() {
		var value = getMockupTimeout()-1;
		
		if(value > 0) {
			setMockupTimeout(value);
		} else {
			window.clearInterval(countDownInterval);
			hideMockupFrame();
		}    
    }
		
	// some jquery magic: add progressbar to mockupheader
	$('#circleForNumber').html($('#counter').html());
	$('#circleForNumber').css( "color", $(".um_background").css("background-color") );
	$('#header_config_text').html($('#moduleConfigurationDescription').html());

	
	var countDownInterval = setInterval(countDownHideMockupFrame, 1000);
    
	function hideMockupFrame() {
		iLog('hideMockupFrame called!');
		$('#mockupframe').parents("form").submit();
		//$('.mockupframe').fadeOut('fast');
		//$('#mockupframe').fadeOut('fast', function() {
		//	iLog('fadeOut finished!');
		//	$(this).parents("form").submit();
		//	
		//});
	}
	
	function getMockupTimeout() {
		return $('#mockuptime').html()*1;
	}
	
	function setMockupTimeout(value) {
		$('#mockuptime').html(value);
	}
		
}
