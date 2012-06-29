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
package com.userweave.module.methoden.rrt.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import com.userweave.domain.LocalizedString;
import com.userweave.domain.ModuleConfigurationWithResultsEntity;
import com.userweave.domain.OrderedEntityBase;
import com.userweave.module.methoden.rrt.RrtMethod;

@Entity
@Table(name="rrt_Configuration")
public class RrtConfigurationEntity extends ModuleConfigurationWithResultsEntity<RrtConfigurationEntity, RrtResult>  {

	private static final long serialVersionUID = 1L;
	
	private List<RrtTerm> terms;

	@OneToMany
	@JoinColumn(name="configurationId")
	@OrderBy(value="position")
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	public List<RrtTerm> getTerms() {
		return terms;
	}

	public void setTerms(List<RrtTerm> terms) {
		this.terms = terms;
	}

	public void addToTerms(RrtTerm term) {
		terms.add(term);
		OrderedEntityBase.renumberPositions(terms);
	}

	public void removeFromTerms(RrtTerm term) {
		terms.remove(term);
		OrderedEntityBase.renumberPositions(terms);
	}

	private LocalizedString postfix;

	@OneToOne
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	public LocalizedString getPostfix() {
		return postfix;
	}
	
	public void setPostfix(LocalizedString s) {
		this.postfix = s;
	}

	private LocalizedString prefix;

	@OneToOne
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	public LocalizedString getPrefix() {
		return prefix;
	}
	
	public void setPrefix(LocalizedString s) {
		this.prefix = s;
	}
	
	@Transient
	public List<Integer> getTermIds() {		
		List<Integer> rv = new ArrayList<Integer>();
		
		List<RrtTerm> terms = getTerms();
		if (terms != null) {
			for (RrtTerm term : terms) {
				rv.add(term.getId());
			}
		}		
		return rv;
	}
	
	@Transient
	public RrtTerm getTermById(String termId) {
		return getTermById(Integer.decode(termId));
	}
	
	@Transient
	public RrtTerm getTermById(int termId) {
		List<RrtTerm> terms = getTerms();
		if (terms != null) {
			for (RrtTerm term : terms) {
				if (term.getId().intValue() == termId) {
					return term;
				}
			}
		}
		return null;
	}

	@Override
	@Transient
	protected String getSpringApplicationContextName() {
		return RrtMethod.moduleId;
	}
	
	@Override
	@Transient
	public List<LocalizedString> getLocalizedStrings() {
		List<LocalizedString> rv = super.getLocalizedStrings();
		for (RrtTerm term : getTerms()) {
			rv.addAll(term.getLocalizedStrings());
		}
		rv.add(prefix);
		rv.add(postfix);
		return rv;
	}

	@Override
	@Transient
	public RrtConfigurationEntity copy() {
		RrtConfigurationEntity clone = new RrtConfigurationEntity();
		super.copy(clone);
		if(postfix != null) {
			clone.setPostfix(postfix.copy());
		}
		if(prefix != null) {
			clone.setPrefix(prefix.copy());
		}

		if(terms != null) {
			List<RrtTerm> cloneRrtTerms = new ArrayList<RrtTerm>();
			for(RrtTerm rrtTerm : terms) {
				cloneRrtTerms.add(rrtTerm.copy());
			}
			clone.terms = cloneRrtTerms;
		}
		
		return clone;
	}

}
