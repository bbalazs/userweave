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
package com.userweave.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.userweave.domain.util.LocalizedAnswer;

/**
 * This entity represents a String in multiple Locales. If you add a new language you have to do the following:<br>
 * add a new field valueXX, new methods getValueXX, setValueXX and adjust the copy method.
 *
 */
@Entity
@Table(name="localized_string")
public class LocalizedString extends OrderedEntityBase<LocalizedString> implements LocalizedAnswer {

	private static final long serialVersionUID = 5421763168327447816L;
	
	public LocalizedString() {	};
	
	public LocalizedString(String value, Locale locale) {
		setValue(value, locale);
	}
	
	private String valueEN;

	@Column(name = "EN")
	@Lob
	public String getValueEN() {
		return valueEN;
	}

	public void setValueEN(String valueEN) {
		this.valueEN = valueEN;
	}

	private String valueEN_GB;
	
	@Column(name = "EN_GB")
	@Lob
	public String getValueEN_GB() {
		return valueEN_GB;
	}

	public void setValueEN_GB(String valueEN_GB) {
		this.valueEN_GB = valueEN_GB;
	}

	private String valueEN_US;
	
	@Column(name = "EN_US")
	@Lob
	public String getValueEN_US() {
		return valueEN_US;
	}

	public void setValueEN_US(String valueEN_US) {
		this.valueEN_US = valueEN_US;
	}

	private String valueDE;

	@Column(name = "DE")
	@Lob
	public String getValueDE() {
		return valueDE;
	}

	public void setValueDE(String valueDE) {
		this.valueDE = valueDE;
	}

	private String valueFR;
	
	@Column(name = "FR")
	@Lob
	public String getValueFR() {
		return valueFR;
	}

	public void setValueFR(String valueFR) {
		this.valueFR = valueFR;
	}
	
	private String valuePL;
	
	@Column(name = "PL")
	@Lob
	public String getValuePL() {
		return valuePL;
	}

	public void setValuePL(String valuePL) {
		this.valuePL = valuePL;
	}
	
	private String valueES;
	
	@Column(name = "ES")
	@Lob
	public String getValueES() {
		return valueES;
	}

	public void setValueES(String valueES) {
		this.valueES= valueES;
	}

	private String valuePT;

	@Column(name = "PT")
	@Lob
	public String getValuePT() {
		return valuePT;
	}

	public void setValuePT(String valuePT) {
		this.valuePT = valuePT;
	}
	
	private String valueIT;
	
	@Column(name = "IT")
	@Lob
	public String getValueIT() {
		return valueIT;
	}

	public void setValueIT(String valueIT) {
		this.valueIT = valueIT;
	}
	
	@Transient
	public String getValue(Locale l) {
		String rv = getValueWithLocale(l);
		if(rv == null || rv.length() == 0) {
			if(locale != null) {
				rv = getValueWithLocale(locale);
			}
		}
		return rv;
	}

	@Transient
	private String getValueWithLocale(Locale l) {
		String country = l.getCountry();
		String language = l.getLanguage();
		String rv = null;
		if(l.getCountry().length() > 0) {
			rv = getValueWithLocale(language+"_"+country);
			// disable fallback here. Fallback is the reference language stored
			// in this object
			//if(rv == null) {
			//	rv = getValueWithLocale(language);
			//}
		} else {
			rv = getValueWithLocale(language);
		}
		return rv;
	}		
	
	@Transient 
	private String getValueWithLocale(String locale) { 
		if (locale.equals("de")) {
			return getValueDE();
		} else if (locale.equals("en")) {
			return getValueEN();
		} else if (locale.equals("en_GB")) {
			return getValueEN_GB();		
		} else if (locale.equals("en_US")) {
			return getValueEN_US();
		} else if (locale.equals("fr")) {
			return getValueFR();
		} else if (locale.equals("pl")) {
			return getValuePL();
		} else if (locale.equals("pt")) {
			return getValuePT();
		} else if (locale.equals("es")) {
			return getValueES();
		} else if (locale.equals("it")) {
			return getValueIT();
		}
		
		
		return "";
	}

	
	public void setValue(String value, Locale l) {

		if(locale == null) {
			setLocale(l);
		};
		String locale = l.toString();
		if (locale.equals("de")) {
			setValueDE(value);
		} else if (locale.equals("en")) {
			setValueEN(value);
		} else if (locale.equals("fr")) {
			setValueFR(value);
		} else if (locale.equals("pl")) {
			setValuePL(value);
		} else if (locale.equals("pt")) {
			setValuePT(value);			
		} else if (locale.equals("es")) {
			setValueES(value);
		} else if (locale.equals("it")) {
			setValueIT(value);
		}
	}
	
	@Transient
	public static List<String> getStrings(List<LocalizedString> localizedStrings, Locale locale) {
		List<String> rv = new ArrayList<String>();
		for (LocalizedString localizedString : localizedStrings) {
			rv.add(localizedString.getValue(locale));
		}
		return rv;
	}
	
	@Transient
	public static boolean remove(List<LocalizedString> localizedStrings, String string, Locale locale) {
		LocalizedString localizedString = findLocalizedString(localizedStrings, string, locale);
		if(localizedString != null) {
			localizedStrings.remove(localizedString);
			return true;
		}
		return false;
	}
	
	@Transient
	public static LocalizedString findLocalizedString(List<LocalizedString> localizedStrings, String string, Locale locale) {
		for (LocalizedString localizedString : localizedStrings) {
			String value = localizedString.getValue(locale);
			if (value != null && value.equals(string)) {
				return localizedString;
			}
		}
		return null;
		
	}

	@Transient
	public static boolean changeValue(List<LocalizedString> localizedStrings, String oldValue, String newValue, Locale locale) {
		if(oldValue == null || newValue == null || oldValue.equals(newValue)) {
			return false;
		}
		LocalizedString localizedString = findLocalizedString(localizedStrings, oldValue, locale);
		if(localizedString != null) {
			localizedString.setValue(newValue, locale);
		}
		return false;
		
	}

	
	private Locale locale;
	
	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	
	@Transient 
	public LocalizedString copy() {
		LocalizedString clone = new LocalizedString();
		
		super.copy(clone);
		
		clone.setValueDE(valueDE);
		clone.setValueEN(valueEN);
		clone.setValueES(valueES);
		clone.setValueFR(valueFR);
		clone.setValuePL(valuePL);
		clone.setValuePT(valuePT);
		clone.setValueIT(valueIT);
		clone.setLocale(locale);

		return clone;
	}

	@Override
	@Transient
	public String toString() {
		return getLocale() +" - "+ getValue(getLocale());
	}
	
	@Transient
	public LocalizedString getLocalized() {
		return this;
	}
}
