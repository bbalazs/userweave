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
package com.userweave.module.methoden.iconunderstandability.page.survey;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.userweave.domain.EntityBase;
import com.userweave.domain.LocalizedString;
import com.userweave.module.methoden.iconunderstandability.domain.ITMImage;
import com.userweave.module.methoden.iconunderstandability.domain.ITMTestResult;

@Entity
@Table(name="itm_icontermmapping")
public class IconTermMapping extends EntityBase {

	private static final long serialVersionUID = 3332957548634944132L;

	public IconTermMapping() {		
	}
	
	public IconTermMapping(ITMImage icon, LocalizedString term) {		
		this.icon = icon;
		this.term = term;
	}
	
	private ITMTestResult itmTestResult;
	
	@ManyToOne
	@JoinColumn(name="result_id")
	public ITMTestResult getItmTestResult() {
		return itmTestResult;
	}

	public void setItmTestResult(ITMTestResult itmTestResult) {
		this.itmTestResult = itmTestResult;		
	}
	
	private ITMImage icon;
	
	private LocalizedString term;

	private Long executionTime;

	@Transient
	public boolean hasIcon() {
		return icon != null;
	}
	
	@ManyToOne
	@JoinColumn(name="image_id")
	public ITMImage getIcon() {
		return icon;
	}

	public void setIcon(ITMImage icon) {
		this.icon = icon;
	}

	@Transient
	public boolean hasTermAndIcon() {
		return hasTerm() && hasIcon();
	}
	
	@Transient
	public boolean hasTerm() {
		return term != null;
	}
	
	@OneToOne
	public LocalizedString getTerm() {
		return term;
	}

	public void setTerm(LocalizedString term) {
		this.term = term;
	}

	public Long getExecutionTime() {
		return executionTime;
	}
	
	public void setExecutionTime(Long date) {
		this.executionTime = date;
	}
	
}
