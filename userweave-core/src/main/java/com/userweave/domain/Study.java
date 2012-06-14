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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CollectionOfElements;
import org.joda.time.DateTime;

import com.userweave.application.UserWeaveSession;
import com.userweave.domain.service.impl.ModuleServiceImpl;
import com.userweave.domain.util.HashProvider;
import com.userweave.module.Module;
import com.userweave.module.ModuleConfiguration;
import com.userweave.module.ModuleConfigurationState;

@Entity
public class Study extends EntityBase {

	private static final long serialVersionUID = 1L;

	public static final String DEFAULT_COLOR = "#000000";
	
	public static final String DEFAULT_FINISHED_URL = "http://www.user-weave.net/";
	
	private DateTime creationDate;
	
	@org.hibernate.annotations.Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	public DateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(DateTime creationDate) {
		this.creationDate = creationDate;
	}
	
	private ImageBase logo;

	@OneToOne(cascade=CascadeType.ALL)
	public ImageBase getLogo() {
		return logo;
	}

	public void setLogo(ImageBase logo) {
		this.logo = logo;
	}
	
	private String backgroundColor;

	public String getBackgroundColor() {
		return backgroundColor;
	}
	
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	private Locale locale;
	
	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	private List<Locale> supportedLocales = new ArrayList<Locale>();
	
	@CollectionOfElements
	public List<Locale> getSupportedLocales() {
		return supportedLocales;
	}

	@Transient
	public boolean isMultiLingual() {
		return supportedLocales.size() > 1;
	}
	
	
	/**
	 * If the study supports more than one locale,
	 * the configurator can decide, if the survey user
	 * should select the survey locale by himself or if
	 * his browser locale is selected. Default is browser
	 * locale.
	 * 
	 * @see #634
	 */
	private boolean isLocaleSelectable;
	
	public boolean isLocaleSelectable()
	{
		return isLocaleSelectable;
	}

	public void setLocaleSelectable(boolean isLocaleSelectable)
	{
		this.isLocaleSelectable = isLocaleSelectable;
	}

	public void setSupportedLocales(List<Locale> supportedLocales) {
		this.supportedLocales = supportedLocales;
	}
	
	@Transient
	public List<Locale> getSupportedLocalesWithoutMainLocale() {	    
		List<Locale> additionalLocales = new ArrayList<Locale>();
		additionalLocales.addAll(supportedLocales);
		additionalLocales.remove(getLocale());
		return additionalLocales;
	}
	
	@Transient
	public boolean supportsLocale(Locale locale) {
		return supportedLocales.contains(locale);
	}

	private String finishedPageUrl;

	public String getFinishedPageUrl() {
		return finishedPageUrl;
	}

	public void setFinishedPageUrl(String finishedPageUrl) {
		this.finishedPageUrl = finishedPageUrl;
	}

	private String notAvailableUrl;
	
	public String getNotAvailableUrl() {
		return notAvailableUrl;
	}

	public void setNotAvailableUrl(String notAvailableUrl) {
		this.notAvailableUrl = notAvailableUrl;
	}
	
	private Integer numberOfParticipants;

	public Integer getNumberOfParticipants() {
		return numberOfParticipants;
	}

	public void setNumberOfParticipants(Integer numberOfParticipants) {
		this.numberOfParticipants = numberOfParticipants;
	}

	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	private LocalizedString headline;

	@OneToOne
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	public LocalizedString  getHeadline() {
		return headline;
	}

	public void setHeadline(LocalizedString  headline) {
		this.headline = headline;
	}
	
	private LocalizedString description;

	@OneToOne
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	public LocalizedString  getDescription() {
		return description;
	}

	public void setDescription(LocalizedString  description) {
		this.description = description;
	}
	
	private StudyState state;
	
	@Enumerated
	public StudyState getState() {
		return state;
	}

	public void setState(StudyState state) {
		this.state = state;
	}

	@Transient
	public void setRunning() {
		setState(StudyState.RUNNING);
		setActivationDate(new DateTime());
	}
	
	@Transient
	public void setFinished() {
		setState(StudyState.FINISHED);
		setFinishDate(new DateTime());
	}
	
	private DateTime activationDate;
	
	@org.hibernate.annotations.Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	public DateTime getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(DateTime activationDate) {
		this.activationDate = activationDate;
	}

	private DateTime finishDate;

	@org.hibernate.annotations.Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	public DateTime getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(DateTime finishDate) {
		this.finishDate = finishDate;
	}

	/**
	 *
	 * @param modules
	 * @return List of all module configurations for this Study
	 */
	@Transient
	public List<ModuleConfiguration> getModuleConfigurations (List<Module<?>> modules) {
		return getModuleConfigurations(modules, null);
	}

	/**
	 *
	 * @param modules
	 * @return List of all active module configurations for this Study
	 */
	@Transient
	public List<ModuleConfiguration> getActiveModuleConfigurations (List<Module<?>> modules) {
		return getModuleConfigurations(modules, ModuleConfigurationState.ACTIVE);
	}


	/**
	 *
	 * @param modules
	 * @param state
	 * @return List of all module configurations for this study having the given state
	 * (state == null means: all configurations)
	 */
	@Transient
	private List<ModuleConfiguration> getModuleConfigurations(List<Module<?>> modules, ModuleConfigurationState state) {
		return ModuleServiceImpl.getSortedModuleConfigurationsForStudy(modules, state, this);
	}
	
	private String hashCode;

	public String getHashCode() {
		return hashCode;
	}

	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}

	private String reportCode;
	
	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	
	private List<SurveyExecution> surveyExecutions = new ArrayList<SurveyExecution>();

	private User owner;

	@OneToMany( cascade=CascadeType.ALL, mappedBy="study")
	//@OnDelete(action=OnDeleteAction.CASCADE)
	public List<SurveyExecution> getSurveyExecutions() {
		return surveyExecutions;
	}

	public void addToSurveyExecutions(SurveyExecution surveyExecution) {
		surveyExecutions.add(surveyExecution);
	}

	public void setSurveyExecutions(List<SurveyExecution> surveyExecution) {
		this.surveyExecutions = surveyExecution;
	}

	@Transient
	public boolean canBeActivated() {
		return getState() == StudyState.INIT
					&& getNumberOfParticipants() != null
					&& getNumberOfParticipants() > 0;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	@ManyToOne
	public User getOwner() {
		return this.owner;
	}

	
	private DateTime deletedAt;

	@org.hibernate.annotations.Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	public DateTime getDeletedAt() {
		return this.deletedAt;
	}
	
	public void setDeletedAt(DateTime deletedAt) {
		this.deletedAt = deletedAt;
	}

	@Transient
	public boolean isDeleted() {
		return this.deletedAt != null;	
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
	
	
	private List<StudyGroup> groups = new ArrayList<StudyGroup>();

	public void setGroups(List<StudyGroup> groups) {
		this.groups = groups;
	}

	/**
	 * get groups for study
	 * @return
	 */
	@OneToMany(mappedBy="study")
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	public List<StudyGroup> getGroups() {
		return groups;
	}	
		
	@Transient
	public List<LocalizedString> getLocalizedStrings() {
		List<LocalizedString> rv = new ArrayList<LocalizedString>();
		rv.add(getHeadline());
		rv.add(getDescription());
		return rv;
	}

	/**
	 * get groups for study and all active modules
	 * @param modules
	 * @return
	 */
	@Transient
	public List<Group> getGroups(List<Module<?>> modules) {
		List<Group> groups = new ArrayList<Group>();
		groups.addAll(getGroups());
		
		List<ModuleConfiguration> activeModuleConfigurations = getActiveModuleConfigurations(modules);
		for (ModuleConfiguration moduleConfiguration : activeModuleConfigurations) {
			groups.addAll(moduleConfiguration.getGroups());
		}
		
		return groups;
	}

	public void removeFromGroups(StudyGroup group) {
		getGroups().remove(group);		
	}

	@Transient
	public Study copy() {
		Study clone = new Study();
		clone.setActivationDate(activationDate);
		clone.setBackgroundColor(backgroundColor);
		// creationDate
		// deletedAt
		if(description != null) {
			clone.setDescription(description.copy());
		}
		// finishDate
		clone.setFinishedPageUrl(finishedPageUrl);
		
		// see #1375
//		if(groups != null) {
//			List<StudyGroup> cloneGroups = new ArrayList<StudyGroup>();
//			for(StudyGroup group : groups) {
//				StudyGroup cloneGroup = group.copy();
//				cloneGroup.setStudy(clone);
//				cloneGroups.add(cloneGroup);
//			}
//			clone.setGroups(cloneGroups);
//		}
		
		// clone.setHashCode(hashCode);
		if(headline != null) {
			clone.setHeadline(headline.copy());
		}
		clone.setLocale(locale);
		
		if(logo != null) {
			clone.setLogo(logo.copy());
		}
		clone.setName(name);
		clone.setNumberOfParticipants(numberOfParticipants);
		clone.setReportCode(HashProvider.uniqueUUID()); // new uuid
		clone.setState(state);
		if(supportedLocales != null) {
			clone.setSupportedLocales(new ArrayList<Locale>(supportedLocales));
		}
		// @see: #1356
		clone.setOwner(UserWeaveSession.get().getUser());
		
		return clone;
		
	}
	

	private Consideration consideration;
	
	public void setConsideration(Consideration consideration) {
		this.consideration = consideration;
	}

	@OneToOne(cascade=CascadeType.ALL)
	public Consideration getConsideration() {
		return consideration;
	}

	
	private Invoice invoice;
	
	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	@ManyToOne
	public Invoice getInvoice() {
		return invoice;
	}
	
	private Project parentProject;

	@ManyToOne
	public Project getParentProject()
	{
		return parentProject;
	}

	public void setParentProject(Project parentProject)
	{
		this.parentProject = parentProject;
	}
	
	private Boolean fontColor;

	public Boolean getFontColor()
	{
		return fontColor;
	}

	public void setFontColor(Boolean fontColor)
	{
		this.fontColor = fontColor;
	}
}
