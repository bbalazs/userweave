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
import java.util.Locale;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.joda.time.DateTime;

import com.userweave.domain.util.HashProvider;

@Entity
@Table(name="all_user")
public class User extends EntityBase {
	public static enum Gender { male, female };
	
	public static enum Position { 
		Entrepreneur, Freelancer,Executive, Manager, 
		Employee, Alumnus, Scholar, Others };
		
	public static enum BusinessRole { Company, Individual }

	private static final long serialVersionUID = 1L;

	public User()
	{
		this.currentProjectRoles = new Roles();
	}
	
	
	@Embedded
	private Callnumber callnumber;

	public Callnumber getCallnumber() {
		return callnumber;
	}

	public void setCallnumber(Callnumber callnumber) {
		this.callnumber = callnumber;
	}

	
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	private Locale locale;

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	
	private String company;

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	
	@Embedded
	private Address address;

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}


	private String education;

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}
	

	private String companyUrl;

	public String getCompanyUrl() {
		return companyUrl;
	}

	public void setCompanyUrl(String companyUrl) {
		this.companyUrl = companyUrl;
	}

	
	private boolean receiveNews;
	
	public boolean isReceiveNews() {
		return receiveNews;
	}

	public void setReceiveNews(boolean receiveNews) {
		this.receiveNews = receiveNews;
	}

	private String surname;

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname=surname;
	}

	
	private String forename;

	public String getForename() {
		return this.forename;
	}

	public void setForename(String forename) {
		this.forename=forename;
	}


	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Transient
	public void setPasswordMD5(String password) {
		setPassword(getPasswordMD5(password));
	}

	@Transient
	public String getPasswordMD5(String password) {
		return HashProvider.md5(password, this.getId());
	}

	
	private boolean isAdmin;

	public boolean isAdmin() {
		return this.isAdmin;
	}
	
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	
	private boolean isVerified;	
	
	/**
	 * a user is verified if the terms of use have been accepted
	 * 
	 * @param isVerified
	 */
	public boolean isVerified() {
		return this.isVerified;
	}
	
	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	/**
	 * a user is registered if he fully delivered all necessary details
	 * a not registered user is *not* allowed to publish a study or a report
	 * link 
	 *   
	 * @return isRegistered
	 */
	@Transient
	public boolean isRegistered() {
		boolean isRegistered = this.forename != null && !this.forename.trim().isEmpty();
		
		isRegistered &= this.surname != null && !this.surname.trim().isEmpty();
		
		isRegistered &= this.locale != null;

		if(this.callnumber == null) {
			isRegistered = false;
		} else {
			isRegistered &= this.callnumber.getPhone() != null && !this.callnumber.getPhone().isEmpty();
		}
		
		if(this.businessRole == BusinessRole.Company) {
			isRegistered &= this.company != null && ! this.company.isEmpty();
		}
		
		if(this.address == null) {
			isRegistered = false;
		} else {
			isRegistered &= this.address.getStreet() != null && ! this.address.getStreet().isEmpty();
			isRegistered &= this.address.getHouseNumber() != null && ! this.address.getHouseNumber().isEmpty();
			isRegistered &= this.address.getPostcode() != null && ! this.address.getPostcode().isEmpty();
			isRegistered &= this.address.getCity() != null && ! this.address.getCity().isEmpty();
			isRegistered &= this.address.getCountry() != null;
		}
		
		return isRegistered;
	} 
		
	private Gender gender;
	
	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	// FIXME: change to boolean after database change
	private Boolean receivePersonalNews;
	
	public Boolean isReceivePersonalNews() {
		return receivePersonalNews;
	}

	public void setReceivePersonalNews(Boolean receivePersonalNews) {
		this.receivePersonalNews = receivePersonalNews;
	}

	private String employment;
	
	public String getEmployment() {
		return this.employment;
	}

	public void setEmployment(String employment) {
		this.employment = employment;
	}
	
	private Position position;
	
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	
	private String sector;
	
	public String getSector() {
		return this.sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}
	
	private DateTime deactivatedAt;
	
	@org.hibernate.annotations.Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	public DateTime getDeactivatedAt() {
		return this.deactivatedAt;
	}

	public void setDeactivatedAt(DateTime deactivatedAt) {
		this.deactivatedAt = deactivatedAt;
	}

	@Transient
	public boolean isDeactivated() {
		return this.deactivatedAt != null;	
	}
	
	@Override
	@Transient
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if(getForename() != null) {
			sb.append(getForename());
			sb.append(" ");
		}
		if(getSurname() != null) {
			sb.append(getSurname());
			sb.append(" ");
		}
		if(sb.length() > 0) {
			sb.append("(").append(getEmail()).append(")");
		} else {
			sb.append(getEmail());
		}
		return sb.toString();
	}

	// Value added tax identification number
	private String VATIN;
	
	public void setVATIN(String VATIN) {
		this.VATIN = VATIN;
	}

	public String getVATIN() {
		return VATIN;
	}
	
	
	private List<Invitation> invitations;
	
	@OneToMany(mappedBy="owner",cascade=CascadeType.ALL)
	public List<Invitation> getInvitations() {
		return invitations;
	}
	
	public void setInvitations(List<Invitation> invitations) {
		this.invitations = invitations;
	}
	
	private List<Invoice> invoices;

	public void setInvoices(List<Invoice> invoices) {
		this.invoices = invoices;
	}
	
	@OneToMany(mappedBy="owner",cascade=CascadeType.ALL)
	@OrderBy("date desc")
	public List<Invoice> getInvoices() {
		return invoices;
	}

	private BusinessRole businessRole = BusinessRole.Company;
	
	public void setBusinessRole(BusinessRole businessRole) {
		this.businessRole = businessRole;
	}

	public BusinessRole getBusinessRole() {
		return businessRole;
	}
	
	private boolean subscription = false;
	
	public void setSubscription(boolean subscription) {
		this.subscription = subscription;
	}

	public boolean isSubscription() {
		return subscription;
	}
	


	private ApiCredentials apiCredentials;
	
	public void setApiCredentials(ApiCredentials apiCredentials) {
		this.apiCredentials = apiCredentials;
	}

	
	@OneToOne(cascade=CascadeType.ALL)
	public ApiCredentials getApiCredentials() {
		return apiCredentials;
	}
	
	
	private Boolean hasAlreadyDefaultProject;

	public Boolean getHasAlreadyDefaultProject()
	{
		return hasAlreadyDefaultProject;
	}

	public void setHasAlreadyDefaultProject(Boolean hasAlreadyDefaultProject)
	{
		this.hasAlreadyDefaultProject = hasAlreadyDefaultProject;
	}
	
	
	private Roles currentProjectRoles;
	
	@Transient
	public Roles getCurrentProjectRoles()
	{
		return currentProjectRoles;
	}

	public void setCurrentProjectRoles(Roles currentProjectRoles)
	{
		this.currentProjectRoles = currentProjectRoles;
	}
	
	@Transient
	public boolean hasRole(String role)
	{
		return this.currentProjectRoles.hasRole(role);
	}
	
	@Transient
	public boolean hasAnyRole(Roles roles)
    {
        return this.currentProjectRoles.hasAnyRole(roles);
    }
}
