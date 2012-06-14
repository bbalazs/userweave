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
	$('.list').bind('mousedown', function() {
	  		$(this).append($('#pointerOfBox'));
	  		$('#pointerOfBox').css('display', 'block');
	});

	$('.list').bind('mouseover', function() {
	  	$(this).css('color', '#2e2d2d');
	});
	$('.list').bind('mouseout', function() {
	  	$(this).css('color', '#5c5959');
	});

var rrt_changed = false;

 	$("#myList").sortable({
 		appendTo: 'body',
 		items: 'div.list',
  		containment: 'parent',
  		axis: 'y',
        delay: '10',
        placeholder: 'hover',
        stop: function(ev, ui) { 
          rrt_changed = true;
        },
        tolerance: 'pointer'
  		});
	
	// Beschreibungen austauschen

	//var description = $("#description").show();

	function klick()
		{	 
			$("div.highlightakt").removeClass("highlightakt").addClass("list");
			$(this).removeClass("list").addClass("highlightakt");
			var current = $(this);
			var id = current.attr("id");
			$('div.visible').removeClass("visible").addClass("unvisible");
			$('div#des'+id+'').removeClass("unvisible").addClass("visible");
  		}

	$("div.list").mousedown(klick);

// passt Wert der Formulare an

   $("form.rrt").submit(
		   function() {

			  $("div.highlightakt").removeClass("highlightakt").addClass("list");

			  terms =$('#myList').sortable('serialize').split("&");

			  $('.result').each(
				  function(index)  {
					  this.value = terms[index];
				  }
			  )
			  
			  $('.rrtChanged').each(
				  function(index)  {
					  this.value = rrt_changed;
				  }
			  )
			  
		   }
   );


});