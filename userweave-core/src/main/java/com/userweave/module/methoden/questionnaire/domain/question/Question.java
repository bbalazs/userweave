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
package com.userweave.module.methoden.questionnaire.domain.question;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import com.userweave.domain.LocalizedString;
import com.userweave.domain.OrderedEntityBase;
import com.userweave.module.methoden.questionnaire.domain.QuestionnaireConfigurationEntity;
import com.userweave.module.methoden.questionnaire.domain.answer.Answer;

@Entity
@Table(name="questionnaire_question")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class Question extends OrderedEntityBase<Question> {

	private static final long serialVersionUID = -3856289066909065754L;

	private QuestionnaireConfigurationEntity configuration;

	private List<Answer> answers;
	
	@OneToMany(mappedBy="question")
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	private List<Answer> getAnswers() {
		return answers;
	}
	
	private void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	
	private LocalizedString name;
	
	@OneToOne(cascade=CascadeType.ALL)
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	public LocalizedString getName() {
		return name;
	}

	public void setName(LocalizedString name) {
		this.name = name;
	}

	@ManyToOne
	public QuestionnaireConfigurationEntity getConfiguration() {
		return configuration;
	}
	
	public void setConfiguration(QuestionnaireConfigurationEntity configuration) {
		this.configuration = configuration;
	}

	private LocalizedString text;

	@OneToOne(cascade=CascadeType.ALL)
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	public LocalizedString getText() {
		return text;
	}

	public void setText(LocalizedString text) {
		this.text = text;
	}
	
	@Transient
	public abstract String getType();

	@Transient
	public List<LocalizedString> getLocalizedStrings() {
		List<LocalizedString> rv = new ArrayList<LocalizedString>();
		rv.add(getText());
		return rv; 
	}

	@Transient
	public abstract Question copy();

	@Transient
	protected Question copy(Question clone) {
		super.copy(clone);
		clone.setConfiguration(configuration);
		if(name != null) {
			clone.setName(name.copy());
		}
		if(text != null) {
			clone.setText(text.copy());
		}
		
		clone.setOrderType(orderType);
		
		return clone;
	}
	
	
	public enum OrderType {
		ORIGINAL("In der definierten Reihenfolge"), 
		ALPHABETICAL("Alphabetisch"),
		RANDOM("Randomisiert");
		
		private final String text;
		
		private OrderType(String text) {
			this.text = text;
		}
		
		@Override
		public String toString() {
			return text;
		}
	};
	
	private OrderType orderType = OrderType.ORIGINAL;

	@Enumerated
	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}
	
}
