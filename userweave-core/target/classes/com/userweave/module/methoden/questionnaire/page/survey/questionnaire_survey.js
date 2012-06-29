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
$(window).load(
		function() {
			$('.questions:last').css('margin-bottom', '0px');

			$(".multipleRating, .dimensions_rating").each(
					function() {
						var $parent;
						var borderSize = 6;
						var offset = 20;
						var dimensionPadding = 20; // padding of dimension container left and right
						var childTableOuterWidth = $(this).children(
								"table:first").outerWidth(true)
								+ offset;

						$(this).width(childTableOuterWidth);

						if ($(this).hasClass("dimensions_rating"))
							childTableOuterWidth += dimensionPadding;

						$parent = $(this).parent().width(childTableOuterWidth);
						$parent = $parent.parent().width(
								childTableOuterWidth + borderSize);

						computeRoundedBorderBounds($parent,
								childTableOuterWidth);

					});
		});

function computeRoundedBorderBounds($parent, childTableOuterWidth) {
	$parent.children(".questions_header_left").width(childTableOuterWidth + 6)
			.children(".questions_header_right").children(
					".questions_header_center").width(childTableOuterWidth - 4);

	$parent.children(".questions_footer_left").width(childTableOuterWidth + 6)
			.children(".questions_footer_right").children(
					".questions_footer_center").width(childTableOuterWidth - 4);
}