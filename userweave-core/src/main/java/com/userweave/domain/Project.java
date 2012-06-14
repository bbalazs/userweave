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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.joda.time.DateTime;

@Entity
public class Project extends EntityBase 
{
	private static final long serialVersionUID = 1L;
	
	private String name;

	public String getName() 
	{
		return this.name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}
	
		
	private boolean isPrivate;

	public boolean isPrivate() 
	{
		return this.isPrivate;
	}

	public void setPrivate(boolean isPrivate) 
	{
		this.isPrivate = isPrivate;
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
	
	
	private List<ProjectUserRoleJoin> projectUserRoleJoins;

	@OneToMany(mappedBy="project")
	public List<ProjectUserRoleJoin> getProjectUserRoleJoins()
	{
		return projectUserRoleJoins;
	}

	public void setProjectUserRoleJoins(
			List<ProjectUserRoleJoin> projectUserRoleJoins)
	{
		this.projectUserRoleJoins = projectUserRoleJoins;
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
	
	private String description;

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
	
	/**
	 * Font color can be white(true) or black(false).
	 */
	private Boolean fontColor;

	public Boolean getFontColor()
	{
		return fontColor;
	}

	public void setFontColor(Boolean fontColor)
	{
		this.fontColor = fontColor;
	}
	
	/**
	 * Default logo for studies.
	 */
	private ImageBase logo;

	@OneToOne(cascade=CascadeType.ALL)
	public ImageBase getLogo() {
		return logo;
	}

	public void setLogo(ImageBase logo) {
		this.logo = logo;
	}
	
	/**
	 * Default backgroud color for studies.
	 */
	private String backgroundColor;

	public String getBackgroundColor() {
		return backgroundColor;
	}
	
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
}
