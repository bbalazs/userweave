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
$(function(){ 
    $('textarea[name="freetext"]').htmlarea({
		toolbar: [ ["html"], ["bold", "italic", "underline"] , ["h1", "h2"], ["justifyleft",
		          "justifycenter", "justifyright"], ["orderedlist", "unorderedlist"], ["indent", "outdent"] ],
		css: "wicket/resource/com.userweave.module.methoden.freetext.page.conf.FreeTextConfigurationPanel/res/jHtmlArea.Editor.css"
	});
});