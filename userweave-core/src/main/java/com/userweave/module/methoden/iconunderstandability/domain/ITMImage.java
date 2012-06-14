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

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.userweave.domain.ImageBase;
import com.userweave.domain.OrderedEntityBase;
import com.userweave.domain.util.Ordered;

@Entity
@Table(name="itm_image")
public class ITMImage extends ImageBase implements Ordered<ITMImage> {
	
	private static final long serialVersionUID = -919661405926767512L;
	
	public ITMImage() {};
	
	public ITMImage(Integer position) {	
		setPosition(position);
	}
	
	private Integer position = Integer.MAX_VALUE;

	public void setPosition(Integer position) {
		this.position = position;
	}
	
	@Column(name="`position`")
	public Integer getPosition() {
		return position;
	}
	
	public void moveUp(List<ITMImage> orderedEntities) {
		OrderedEntityBase.moveUp(orderedEntities, this);
	}
	
	public void moveDown(List<ITMImage> orderedEntities) {
		OrderedEntityBase.moveDown(orderedEntities, this);
	}
	
	@Override
	@Transient
	public ITMImage copy() {
		ITMImage clone  = new ITMImage();
		super.copy(clone);
		clone.setPosition(position);
		return clone;
	}
	
}
