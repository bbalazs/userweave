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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.Session;

import com.userweave.domain.LocalizedString;

/**
 * This class defines which languages are supported. <br>WARNING!!!!: If you add a language here
 * you also have to adjust {@link LocalizedString} !!!.  
 *
 */
public class LocalizationUtils {
	
	public static Locale getSupportedStudyLocale(String localeString) {
		for(Locale locale: getSupportedStudyLocales()) {
			if(locale.getLanguage().equals(localeString)) {
				return locale;
			}
		}
		return null;
	}

	public static List<Locale> getSupportedStudyLocales() {
		Locale SPANISH = new Locale("es");
		Locale POLISH = new Locale("pl");
		Locale PORTUGUESE = new Locale ("pt");
		Locale ITALIAN = new Locale ("it");
		return Arrays.asList(new Locale[] {Locale.ENGLISH, Locale.UK, Locale.US, Locale.GERMAN, Locale.FRENCH, SPANISH, POLISH, PORTUGUESE, ITALIAN});
	};
		
	public static void initDefaultLocale() {
		// enforce usage of _de.properties as default
		Locale.setDefault(getDefaultLocale());
	}
	
	public static Locale getDefaultLocale() {
		return Locale.ENGLISH;
	}

	public static String getLocaleShort(Locale locale) {
		if(locale == null) {
			return null;
		}
		return locale.getLanguage().toLowerCase();
	}
	
	public static Locale getRegistrationLocaleShort(Locale locale) {
		Locale l = locale;
		if(!(getSupportedStudyLocales().contains(l))) {
			// no supported language found, fallback to default language
			l = getDefaultLocale();
		}
		
		return l;
	}
	
	public static List<String> getSupportedStudyLocalesShort() {
		List<String> rv = new ArrayList<String>();
		for (Locale locale : getSupportedStudyLocales()) {
			rv.add(getLocaleShort(locale));
		}
		return rv;
	}


	public static List<String> getSupportedConfigurationFrontendLocalesShort() {
		List<String> rv = new ArrayList<String>();
		for (Locale locale : getSupportedConfigurationFrontendLocales()) {
			rv.add(getLocaleShort(locale));
		}
		return rv;
	}

	public static List<Locale> getSupportedConfigurationFrontendLocales() {
		return Arrays.asList(new Locale[] {Locale.ENGLISH, Locale.GERMAN});
	}

	public static Locale getSupportedConfigurationFrontendLocale(String localeString) {
		for(Locale locale: getSupportedConfigurationFrontendLocales()) {
			if(locale.getLanguage().equals(localeString)) {
				return locale;
			}
		}
		return null;
	}

	public static Locale mapLocale(Locale locale) {
		if (locale.equals(Locale.GERMANY)) {
			return Locale.GERMAN;
		} else if (locale.equals(Locale.FRANCE)) {
			return Locale.FRENCH;
		}
	
		return locale;
	};

	public static String getValue(LocalizedString string) {
		return getValue(string, Session.get().getLocale());
	}

	public static String getValue(LocalizedString string, Locale aLocale) {
		if(string == null) return null;
		
		Locale locale = aLocale != null ? aLocale : LocalizationUtils.getDefaultLocale();
		return string.getValue(locale);
	}

	public static String getDisplayLanguage(Locale locale, Locale inLocale) {
		if(inLocale == null) {
			inLocale = locale;
		}
		String display = locale.getDisplayLanguage(inLocale);
		display = display.substring(0,1).toUpperCase(locale)+display.substring(1);
		if(locale.getCountry() != null && locale.getCountry().length() > 0) {
			display += " ("+locale.getCountry()+")";
		}
		return display;
	} 
	
	public static String getIsoLangCode(Locale aLocale) {
		String language = aLocale.getLanguage();
		String country  = aLocale.getCountry();
		if(country.length() > 0) {
			return language+"_"+country;
		} else { 
			return language;
		}	
	}
	
	public static void main(String[] args) {
		System.out.println(getDisplayLanguage(Locale.GERMAN, Locale.GERMAN));
		System.out.println(getDisplayLanguage(Locale.GERMANY, Locale.GERMAN));
		System.out.println(getDisplayLanguage(Locale.UK, Locale.GERMAN));
		System.out.println(getDisplayLanguage(Locale.ENGLISH, Locale.GERMAN));
	}
}
