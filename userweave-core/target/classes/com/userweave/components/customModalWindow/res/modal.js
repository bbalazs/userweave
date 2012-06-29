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
/**
 * Override bindInit function to bind onclick close event on bottom close link.
 */
Wicket.Window.prototype.bindInit = function() 
{
    this.bind(this.caption, this.onMove);
        
    if (this.settings.resizable) 
    {      
        this.bind(this.bottomRight, this.onResizeBottomRight);
        this.bind(this.bottomLeft, this.onResizeBottomLeft);
        this.bind(this.bottom, this.onResizeBottom);
        this.bind(this.left, this.onResizeLeft);
        this.bind(this.right, this.onResizeRight);
        this.bind(this.topLeft, this.onResizeTopLeft);      
        this.bind(this.topRight, this.onResizeTopRight);
        this.bind(this.top, this.onResizeTop);
    } 
    else 
    {
        this.bind(this.bottomRight, this.onMove);
        this.bind(this.bottomLeft, this.onMove);
        this.bind(this.bottom, this.onMove);
        this.bind(this.left, this.onMove);
        this.bind(this.right, this.onMove);
        this.bind(this.topLeft, this.onMove);       
        this.bind(this.topRight, this.onMove);
        this.bind(this.top, this.onMove);
    }   
        
    this.caption.getElementsByTagName("a")[0].onclick = this.settings.onCloseButton.bind(this);
	document.getElementsByName("a_bottom_close")[0].onclick = this.settings.onCloseButton.bind(this);
}

/**
 * Override, because of the same reason as above.
 */
Wicket.Window.prototype.bindClean = function() 
{             
        this.unbind(this.caption);
        this.unbind(this.bottomRight);
        this.unbind(this.bottomLeft);
        this.unbind(this.bottom);
        this.unbind(this.left);
        this.unbind(this.right);
        this.unbind(this.topLeft);      
        this.unbind(this.topRight);
        this.unbind(this.top);
        
        this.caption.getElementsByTagName("a")[0].onclick = null;
		document.getElementsByName("a_bottom_close").onclick = null;
}

/**
 * Override markup.
 */
Wicket.Window.getMarkup = function(idWindow, idClassElement, idCaption, idContent, idTop, idTopLeft, idTopRight, idLeft, idRight, idBottomLeft, idBottomRight, idBottom, idCaptionText, isFrame) {
	var s =
			"<div class=\"wicket-modal\" id=\""+idWindow+"\" style=\"top: 10px; left: 10px; width: 100px;\"><form style='background-color:transparent;padding:0px;margin:0px;border-width:0px;position:static'>"+
			"<div id=\""+idClassElement+"\">"+
				
				"<div class=\"w_top_1\">"+

				"<div class=\"w_topLeft\" id=\""+idTopLeft+"\">"+
				"</div>"+				

				"<div class=\"w_topRight\" id=\""+idTopRight+"\">"+
				"</div>"+

				"<div class=\"w_top\" id='"+idTop+"'>"+									
				"</div>"+

				"</div>"+
								
				"<div class=\"w_left\" id='"+idLeft+"'>"+
					"<div class=\"w_right_1\">"+
						"<div class=\"w_right\" id='"+idRight+"'>"+
							"<div class=\"w_content_1\" onmousedown=\"if (Wicket.Browser.isSafari()) { event.ignore = true; }  else { Wicket.stopEvent(event); } \">"+																			
								"<div class=\"w_caption um_background w_caption_" + settings.typeOf + "_" + settings.color + "\"  id=\""+idCaption+"\">"+
									"<a class=\"w_close w_close_" + settings.color + "\" style=\"z-index:1\" href=\"#\"></a>"+									
									"<h3 id=\""+idCaptionText+"\" class=\"w_captionText\"></h3>"+
								"</div>"+
							
								"<div class=\"w_content_2\">"+
								"<div class=\"w_content_3\">"+
		 							"<div class=\"w_content\">";
				if (isFrame) {
					s+= "<iframe";
					if (Wicket.Browser.isIELessThan7()) {
						s+= " src=\"about:blank\""
					}
					s+= " frameborder=\"0\" id=\""+idContent+"\" allowtransparency=\"false\" style=\"height: 200px\" class=\"wicket_modal\"></iframe>";
				} else {
					s+=
										"<div id='"+idContent+"' class='w_content_container'></div>";
				}
					s+= 						
									"</div>"+
									
									"<a href='#' name='a_bottom_close' class='a_bottom_close um_background'><span>schliessen</span></a>" +
									
								"</div>"+
								"</div>"+
							"</div>"+
						"</div>"+
					"</div>"+
				"</div>"+


				"<div class=\"w_bottom_1\" id=\""+idBottom+"\">"+					
					
					"<div class=\"w_bottomRight\"  id=\""+idBottomRight+"\">"+
					"</div>"+
					
					"<div class=\"w_bottomLeft\" id=\""+idBottomLeft+"\">"+
					"</div>"+

					"<div class=\"w_bottom\" id=\""+idBottom+"\">"+				
					"</div>"+				


				"</div>"+				


			"</div>"+
		"</form></div>";
		
		return s;
}

if(Wicket.Browser.isIE())
{
	Wicket.Window.Mask.zIndex = 10000;
}