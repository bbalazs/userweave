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

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.joda.time.DateTime;

@Entity
public class Invoice extends EntityBase {
	
	private static final long serialVersionUID = -8396318396668612719L;
	
	private DateTime date;
	
	private String number;
	
	private byte[] generatedPdf;
	
	private Double netAmount;
	
	private Double grossAmount;
	
	private Double purchaseTaxPercent;
	
	private boolean purchaseTaxAccounted;
	
	private User owner;
	
	private List<Study> study;
	
	private DateTime validFrom;
	
	private DateTime validTo;

	public void setDate(DateTime date) {
		this.date = date;
	}

	@org.hibernate.annotations.Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	public DateTime getDate() {
		return date;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	
	public String getNumber() {
		return number;
	}

	public byte[] getGeneratedPdf() {
		return generatedPdf;
	}

	public void setGeneratedPdf(byte[] generatedPdf) {
		this.generatedPdf = generatedPdf;
	}
	
	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Double getNetAmount() {
		return netAmount;
	}
	
	public Double getPurchaseTaxPercent() {
		return purchaseTaxPercent;
	}

	public void setPurchaseTaxPercent(Double purchaseTaxPercent) {
		this.purchaseTaxPercent = purchaseTaxPercent;
	}

	public void setPurchaseTaxAccounted(boolean purchaseTaxAccounted) {
		this.purchaseTaxAccounted = purchaseTaxAccounted;
	}

	public boolean isPurchaseTaxAccounted() {
		return purchaseTaxAccounted;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	@ManyToOne
	public User getOwner() {
		return owner;
	}

	public void setStudy(List<Study> study) {
		this.study = study;
	}

	@OneToMany(mappedBy="invoice")
	public List<Study> getStudy() {
		return study;
	}

	public void setValidFrom(DateTime validFrom) {
		this.validFrom = validFrom;
	}

	@org.hibernate.annotations.Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	public DateTime getValidFrom() {
		return validFrom;
	}
	
	public void setValidTo(DateTime validTo) {
		this.validTo = validTo;
	}

	@org.hibernate.annotations.Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	public DateTime getValidTo() {
		return validTo;
	}

	public void setGrossAmount(Double grossAmount) {
		this.grossAmount = grossAmount;
	}

	public Double getGrossAmount() {
		return grossAmount;
	}
}
