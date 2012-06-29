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
package com.userweave.module.methoden.iconunderstandability.domain;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.userweave.domain.LocalizedString;
import com.userweave.domain.ModuleConfigurationWithResultsEntity;
import com.userweave.domain.OrderedEntityBase;
import com.userweave.module.methoden.iconunderstandability.BestMatchingIconMethod;
import com.userweave.module.methoden.iconunderstandability.BestMatchingTermMethod;
import com.userweave.module.methoden.iconunderstandability.IconTermMatchingMethod;
import com.userweave.module.methoden.iconunderstandability.dao.IconTermMatchingType;

@Entity
@Table(name="itm_configuration")
public class IconTermMatchingConfigurationEntity extends ModuleConfigurationWithResultsEntity<IconTermMatchingConfigurationEntity, ITMTestResult>  {

	private static final long serialVersionUID = 1L;
	
	// specific for this moduleConfiguration
	private List<ITMImage> images = new ArrayList<ITMImage>();

	@Transient
	public ITMImage getImage(int imageId) {
		for (ITMImage image : getImages()) {
			if (image.getId().intValue() == imageId) {
				return image;
			}
		}
		return null;
	}
	
	@OneToMany( cascade={CascadeType.ALL})
	@JoinColumn(name="configurationId")
	@OrderBy(value="position")
	public List<ITMImage> getImages() {
		return images;
	}

	public void addToImages(ITMImage image) {
		images.add(image);
	}
	
	public void removeFromImages(ITMImage image) {
		images.remove(image);
	}

	public void setImages(List<ITMImage> images) {
		this.images = images;
	}


	private List<ItmTerm> terms;
	private IconTermMatchingType type;

	@OneToMany(cascade={CascadeType.ALL}, mappedBy="configuration")
	@OrderBy(value="position")
	public List<ItmTerm> getTerms() {
		return terms;
	}

	public void setTerms(List<ItmTerm> terms) {
		this.terms = terms;
		//OrderedEntityBase.renumberPositions(terms);
	}

	@Transient
	public ItmTerm getTerm(Integer termId) {
		for(ItmTerm term : getTerms()) {
			if(term.getId().equals(termId)) {
				return term;
			}
		}
		return null;
	} 

	
	public void addToTerms(ItmTerm term) {
		terms.add(term);
		OrderedEntityBase.renumberPositions(terms);
	}

	public void removeFromTerms(ItmTerm term) {
		terms.remove(term);
		OrderedEntityBase.renumberPositions(terms);
	}

	@Enumerated
	public void setType(IconTermMatchingType type) {
		// type changes attached method!
		this.type = type;
	}
	
	public IconTermMatchingType getType() {
		return type;
	}

	@Override
	@Transient
	protected String getSpringApplicationContextName() {
		if(getType() == IconTermMatchingType.ITM) {
			return IconTermMatchingMethod.moduleId;
		}
		if(getType() == IconTermMatchingType.BMI) {
			return BestMatchingIconMethod.moduleId;
		}
		if(getType() == IconTermMatchingType.BMT) {
			return BestMatchingTermMethod.moduleId;
		}
		throw new IllegalStateException();
	}


	public void removeFromImages(Integer id) {
		if (id == null) {
			return;
		}
		for(ITMImage image: getImages()) {
			if (image.getId().equals(id)) {
				removeFromImages(image);
				break;
			}
		}		
	}

	@Override
	@Transient
	public IconTermMatchingConfigurationEntity copy() {
		IconTermMatchingConfigurationEntity clone = new IconTermMatchingConfigurationEntity();
		super.copy(clone);

		if(images != null) {
			List<ITMImage> cloneItmImages = new ArrayList<ITMImage>();
			for(ITMImage itmImage : images) {
				cloneItmImages.add(itmImage.copy());
			}
			clone.setImages(cloneItmImages);
		}

		if(terms != null) {
			List<ItmTerm> cloneItmTerms = new ArrayList<ItmTerm>();
			for(ItmTerm itmTerm : terms) {
				ItmTerm cloneItmTerm = itmTerm.copy();
				cloneItmTerm.setConfiguration(clone);
				cloneItmTerms.add(cloneItmTerm);
			}
			clone.setTerms(cloneItmTerms);
		}

		clone.setType(type);
		
		return clone;
	}

	@Override
	@Transient
	public List<LocalizedString> getLocalizedStrings() {
		List<LocalizedString> localizedStrings = super.getLocalizedStrings();

		for(ItmTerm itmTerm: getTerms()) {
			localizedStrings.addAll(itmTerm.getLocalizedStrings());
		}
		return localizedStrings;
	}
	
	@Transient
	public Dimension getDimiensionOfIcons()
	{
		if(images == null || images.isEmpty())
		{
			return null;
		}
		else
		{
			// Find largest dimension in icon set. This is the largest
			// width and the largest height. 
			Dimension d = new Dimension(0, 0);
			
			for(ITMImage image : images)
			{
				Dimension imageDimension = image.getDimension();
				
				if(imageDimension.width > d.width)
				{
					d.setSize(imageDimension.width, d.height);
				}
				
				if(imageDimension.height > d.height)
				{
					d.setSize(d.width, imageDimension.height);
				}
				
			}
			
			return d;
		}
	}
}
