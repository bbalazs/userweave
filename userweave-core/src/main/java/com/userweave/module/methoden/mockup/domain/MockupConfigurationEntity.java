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
package com.userweave.module.methoden.mockup.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import com.userweave.domain.LocalizedString;
import com.userweave.domain.ModuleConfigurationWithResultsEntity;
import com.userweave.module.methoden.mockup.MockupMethod;

@Entity
@Table(name="mockup_configuration")
public class MockupConfigurationEntity extends ModuleConfigurationWithResultsEntity<MockupConfigurationEntity, MockupResult> {

	private static final long serialVersionUID = 1L;

	@Override
	@Transient
	protected String getSpringApplicationContextName() {
		return MockupMethod.moduleId;
	}

	private String url;
	
	public String getURL() {
		return url;
	};
	
	public void setURL(String url) {
		this.url = url;
	}

	/**
	 * Locale specific url to test.
	 * 
	 * @see #1129
	 */
	private LocalizedString localeUrl;
	
	@OneToOne
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	public LocalizedString getLocaleUrl()
	{
		return localeUrl;
	}

	public void setLocaleUrl(LocalizedString localeUrl)
	{
		this.localeUrl = localeUrl;
	}

	private Integer time = 7;
	
	public Integer getTime() {
		return time;
	};
	
	public void setTime(Integer time) {
		this.time = time;
	}
	
	private boolean timeVisible = false;
	
	public boolean isTimeVisible() {
		return timeVisible;
	}

	public void setTimeVisible(boolean timeVisible) {
		this.timeVisible = timeVisible;
	}
	
	private LocalizedString freetext = new LocalizedString();
	
	@OneToOne
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	public LocalizedString getFreeText() {
		return freetext;
	};
	
	public void setFreeText(LocalizedString freetext) {
		this.freetext = freetext;
	}
	
	@Override
	@Transient
	public MockupConfigurationEntity copy() {
		MockupConfigurationEntity clone = new MockupConfigurationEntity();
		super.copy(clone);
		clone.setURL(url);
		clone.setTime(time);
		clone.setTimeVisible(timeVisible);
		clone.setLayerVisible(layerVisible);
		clone.setSwitchToNextConfigType(switchToNextConfigType);
		if(freetext != null) {
			clone.setFreeText(freetext.copy());
		}
		return clone;
	}

	@Override
	@Transient
	public List<LocalizedString> getLocalizedStrings() {
		List<LocalizedString> rv = super.getLocalizedStrings();
		if(freetext != null) {
			rv.add(freetext);
		}
		
		if(localeUrl != null)
		{
			rv.add(localeUrl);
		}
		
		return rv;
	}


	private boolean layerVisible = true;
	
	public void setLayerVisible(boolean layerVisible) {
		this.layerVisible = layerVisible;
	}

	public boolean isLayerVisible() {
		return layerVisible;
	}

	public enum SwitchToNextConfigType {
		TIMER("Timer"), 
		BUTTON("Button"),
		TIMER_AND_BUTTON("Timer and Button");
		
		private final String text;
		
		private SwitchToNextConfigType(String text) {
			this.text = text;
		}
		
		@Override
		public String toString() {
			return text;
		}
	};
	
	private SwitchToNextConfigType switchToNextConfigType = SwitchToNextConfigType.TIMER;

	@Enumerated
	public SwitchToNextConfigType getSwitchToNextConfigType() {
		return switchToNextConfigType;
	}

	public void setSwitchToNextConfigType(SwitchToNextConfigType switchToNextConfigType) {
		this.switchToNextConfigType = switchToNextConfigType;
	}

}
