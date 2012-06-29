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

import java.util.Currency;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.joda.time.DateTime;

@Entity
public class Consideration extends EntityBase {

	private static final long serialVersionUID = -5557850674932704085L;
	
	private DateTime date;
	private Invoice invoice;
	private Double grossAmount;
	private String motivation;
	private boolean wouldLikeToPay = false;
	private boolean wouldLikeToWriteArticle = false;
	private boolean paid = false;
	private boolean written = false;
	private String payPalToken = "";
	private String articleURL;
	private boolean payPalSuccessRedirect = false;
	private boolean userCanceled = false;
	private String payPalPayerId;
	private String redirectedPayPalToken;
	private String suggestions;
	private Currency currency;
	
	
	public void setDate(DateTime date) {
		this.date = date;
	}
	
	@org.hibernate.annotations.Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	public DateTime getDate() {
		return date;
	}
	
	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	@OneToOne(cascade=CascadeType.ALL)
	public Invoice getInvoice() {
		return invoice;
	}
	
	public void setGrossAmount(Double grossAmount) {
		this.grossAmount = grossAmount;
	}
	
	public Double getGrossAmount() {
		return grossAmount;
	}
	
	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}
	
	public String getMotivation() {
		return motivation;
	}

	public void setWouldLikeToPay(boolean wouldLikeToPay) {
		this.wouldLikeToPay = wouldLikeToPay;
	}

	public boolean isWouldLikeToPay() {
		return wouldLikeToPay;
	}

	public void setWouldLikeToWriteArticle(boolean wouldLikeToWriteArticle) {
		this.wouldLikeToWriteArticle = wouldLikeToWriteArticle;
	}

	public boolean isWouldLikeToWriteArticle() {
		return wouldLikeToWriteArticle;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setWritten(boolean written) {
		this.written = written;
	}

	public boolean isWritten() {
		return written;
	}

	public void setPayPalToken(String payPalToken) {
		this.payPalToken = payPalToken;
	}

	public String getPayPalToken() {
		return payPalToken;
	}

	public void setArticleURL(String articleURL) {
		this.articleURL = articleURL;
	}

	public String getArticleURL() {
		return articleURL;
	}

	public void setPayPalSuccessRedirect(boolean payPalSuccessRedirect) {
		this.payPalSuccessRedirect = payPalSuccessRedirect;
	}

	public boolean isPayPalSuccessRedirect() {
		return payPalSuccessRedirect;
	}

	public void setUserCanceled(boolean userCanceled) {
		this.userCanceled = userCanceled;
	}

	public boolean isUserCanceled() {
		return userCanceled;
	}

	public void setPayPalPayerId(String payPalPayerId) {
		this.payPalPayerId = payPalPayerId;
	}

	public String getPayPalPayerId() {
		return payPalPayerId;
	}

	public void setRedirectedPayPalToken(String redirectedPayPalToken) {
		this.redirectedPayPalToken = redirectedPayPalToken;
	}

	public String getRedirectedPayPalToken() {
		return redirectedPayPalToken;
	}

	public void setSuggestions(String suggestions) {
		this.suggestions = suggestions;
	}

	public String getSuggestions() {
		return suggestions;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Currency getCurrency() {
		return currency;
	}
}
