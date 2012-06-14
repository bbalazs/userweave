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
 * Implements the drag & drop and
 * click functionality in the icon
 * test.
 *
 * @author: opr
 */

/** 
 * free space in content_left,
 * if an icon has been removed
 */
var freeSpace = new Array();

/** 
 * variable to avoid that
 * more than one icon can be moved
 */
var iconInMotion = false;

/**
 * because a drag event always calls the
 * click event afterwards, this var prevents
 * the execution of this click event.
 */
var iconDragged = false;

/**
 * disables the mouse events
 * as long as animation is in
 * progress.
 */
var disableMouseEvents = false;

/**
 * bind necessary functions on
 * load of page to img_content
 * divs.
 */
$(window).bind('load', function(){
    
	// wrap a placeholder around each tile
    $(".icon_container_wrapper").each(function(){
		
    	var imageContainer = $(this).find("div.tile_center")
    	
		var h = imageContainer.height();
		var w = imageContainer.width();
		
		w += 28;
		h += 20;
		
        $(this).wrap("<div class='tile_placehoder' " + 
		             "style='width:" + w + "px;height:" + h + "px'></div>");
		
		// force ie6 & ie7 to show borders
		$(this).find(".tile_header_center").css("width", w - 11 + "px");
		$(this).find(".tile_footer_center").css("width", w - 11 + "px");
    });

    
    appendTermCounter();
    enableMouseOver();
    enableMouseOut();
    enableClickableIcons();
    enableDragableIcons();
});

/** 
 * Appends the span 'termCounter' to
 * either content_left or content_right
 **/
function appendTermCounter(){
    if ($("#termCounter span:first-child:contains('Icon')").length > 0) 
        $('#termCounter').prependTo($('#content_left').parent().get(0));
    
    $('#termCounter').css('display', 'inline');
}

/**
 * replaces or fills the drop
 * zone of the term with an
 * image on an click event
 */
function enableClickableIcons()
{
    $('.icon_container_wrapper').click(function(){
		if(iconInMotion || iconDragged) {
			return;
        }
        
		iconInMotion = true;
		disableMouseEvents = true;
		$image = $(this);
              
        if ($image.parent().hasClass('drop_field_container')) 
		{  
            addImageToContentLeft($image, freeSpace.shift());
		    callWicket($image.parent().attr('dropComponentId'), -1);
        }
        else 
		{
            var dropField = $('.drop_field_container').get(0);
                    
            if ($(dropField).is(':not(:has(.icon_container_wrapper))'))
                addImageToDropField($image, dropField);
            else 
			{
                var $dropedImage = $(dropField).children(".icon_container_wrapper:first");
			    replaceImageOnDropField($dropedImage, $image, dropField);
            }
				
			callWicket($(dropField).attr('dropComponentId'), $image.attr('imageId'));
        }
    });
}

/**
 * enables the draggable functionality
 */
function enableDragableIcons()
{
    $('.icon_container_wrapper').draggable({
        //appendTo: 'footer', // ie hack
        helper: 'original',
        revert: true,
//		distance: 10,
        zIndex: 1001,
        start: function(ev, ui){
            iconDragged = true;
        },
        stop: function(ev, ui) {            
            iconDragged = false;
        }
    });
    
    /*
     * make the drop_field sections droppable,
     * accepting images from the content
     * section
     */
    $('.drop_field_container').droppable({		
        accept: '.icon_container_wrapper',
        activeClass: 'active',
        hoverClass: 'um_background',
        drop: function(ev, ui){
			disableMouseEvents = true;
            var $dropTarget = $(this);
            
			if( $dropTarget.children().length > 0 )
			{
				var $dropedImage = $dropTarget.children(".icon_container_wrapper:first");
				replaceImageOnDropField($dropedImage, ui.draggable, $dropTarget);
			}
			else
				addImageToDropField(ui.draggable, $dropTarget);

			callWicket($dropTarget.attr('dropComponentId'), ui.draggable.attr('imageId'));
        }
    });
    
    /*
     * make the gallery droppable as well,
     * accepting images from the trash only
     */
    $('#iconContainer').droppable({
        accept: '.isDroppable',
        activeClass: 'active',
        hoverClass: 'um_background',
		tolerance: 'intersect',
        drop: function(ev, ui){
			disableMouseEvents = true;
            callWicket(ui.draggable.parent().attr('dropComponentId'), -1);          
            addImageToContentLeft(ui.draggable, freeSpace.shift());
        }
    });
}

/**
 * Adds an image to the drop
 * field.
 * 
 * @param {Object} $image
 */
function addImageToDropField( $image, dropField )
{
	$image.fadeOut('fast', function(){
        freeSpace.push($image.parent());
                            
        $(this).addClass('dropped');
        $(this).hide()
		    .addClass('isDroppable')
		    .appendTo(dropField)
			.fadeIn('fast', function(){ iconInMotion = false; })
			
		$(dropField).addClass('um_background');
		
		disableMouseEvents = false;
    });
	
	highlightDropZone();
	var tile = $(this).children(".tile").get(0);
    $(tile).trigger("mouseout", tileMouseOut($(tile)));
}

/**
 * removes the image from drop field
 * and apeend it to the pool of icons
 * @param {Object} $image
 * @param {Object} $dropTarget
 */
function addImageToContentLeft( $image, $dropTarget )
{
	$image.parent().removeClass('um_background');
	
    $image.fadeOut('fast', function(){
        $(this).hide()
			.removeClass('dropped')
			.removeClass('isDroppable')
			.appendTo($dropTarget)
			.fadeIn('fast', function() { iconInMotion = false; })
			
		disableMouseEvents = false;
    });
	
	removeHighlightOnDropZone();
	var tile = $(this).children(".tile").get(0);
    $(tile).trigger("mouseout", tileMouseOut($(tile)));
}

/**
 * Replace droped Image with
 * current object ($image)
 * @param {Object} $dropedImage
 * @param {Object} $image
 */
function replaceImageOnDropField( $dropedImage, $image, dropField )
{   
	addImageToContentLeft($dropedImage, freeSpace.shift());
	addImageToDropField($image, dropField);
}

function highlightDropZone()
{
	$(".function_box")
	   .addClass("um_background")
	   .removeClass("function_box_background");
}

function removeHighlightOnDropZone()
{
	$(".function_box")
        .removeClass("um_background")
		.addClass("function_box_background");
}

/**
 * replaces the tile of an image on hover
 */
function enableMouseOver()
{
	$(".tile").mouseover(function(){
		tileMouseOver($(this))
	});
}

function tileMouseOver( $tile )
{
	$tile.addClass("um_background");
        
    $tile.find(".tileImage_horizontal")
         .addClass("tileImage_horizontal_hover")
         .removeClass("tileImage_horizontal");
          
    $tile.find(".tileImage_vertical")
         .addClass("tileImage_vertical_hover")
         .removeClass("tileImage_vertical");
}

/**
 * resets the tile of an image
 */
function enableMouseOut()
{
	$(".tile").mouseout(function(){
        tileMouseOut($(this));
    });
}

function tileMouseOut($tile) {
	$tile.removeClass("um_background");
        
        $tile.find(".tileImage_horizontal_hover")
             .removeClass("tileImage_horizontal_hover")
             .addClass("tileImage_horizontal");
          
        $tile.find(".tileImage_vertical_hover")
             .removeClass("tileImage_vertical_hover")
             .addClass("tileImage_vertical");
}

/**
 * Resets the hover box to
 * his normal size
 * @param {Object} parentObj
 */
function resetHoverBoxLayout(parentObj){
    var hoverbox = $(parentObj).children(".hoverBox").get(0);
    $(hoverbox).removeClass("hoverBoxRight").addClass("hoverBox");
}

/**
 * makes the hoverBox smaller to
 * fit the drop zone
 * @param {Object} parentObj
 */
function makeHoverBoxSmaller(parentObj){
    var hoverbox = $(parentObj).children(".hoverBox").get(0);
    $(hoverbox).addClass("hoverBoxRight").removeClass("hoverBox");
}
