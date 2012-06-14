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
package com.userweave.utils;

import info.bliki.html.HTML2WikiConverter;
import info.bliki.html.wikipedia.ToWikipedia;
import info.bliki.wiki.model.WikiModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SanitizeHtml {
	private final static transient Logger logger = LoggerFactory.getLogger(SanitizeHtml.class);
	/**
	 * this method sanitizes HTML-Code somehow magically. Especially javascript is removed.
	 * <br/>
	 * FIXME: remove unnecessary inner &lt;a id=... links
	 * @param htmlCode
	 * @return sanitized htmlCode
	 */
	public static String sanitize(String htmlCode) {
		logger.info("sanitizing input string");
		if(htmlCode == null) return null;
		HTML2WikiConverter conv = new HTML2WikiConverter();
		conv.setInputHTML(htmlCode);
		String wikiCode = conv.toWiki(new ToWikipedia());

		WikiModel wikiModel = new WikiModel("http://www.bliki.info/wiki/${image}", "http://www.bliki.info/wiki/${title}");
		String result = wikiModel.render(wikiCode);

		return result;
	}
}
