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

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.userweave.domain.util.Ordered;

@MappedSuperclass
public abstract class OrderedEntityBase<T extends Ordered> extends EntityBase implements Ordered<T> {

	private static final long serialVersionUID = 1588782476569036357L;

	private Integer position = Integer.MAX_VALUE;
	
	public OrderedEntityBase() {}
	
	public OrderedEntityBase(Integer position) {
		this.position = position;
	}
	
	@Column(name="`position`")
	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public static void renumberPositions(List<? extends Ordered> entities) {
		if(entities == null) return;
		int position = 0;
		for(Ordered entity: entities) {
			if(entity.getPosition() == null || entity.getPosition().intValue() != position) {			
				entity.setPosition(position);
			}
			position++;
		}
	}
	
	public int comparePositions(OrderedEntityBase<T> orderedEntity) {
		if ((getPosition() != null) && (orderedEntity.getPosition() != null)) {
			return getPosition().compareTo(orderedEntity.getPosition());
		} else {
			return 0;
		}
	}
	
	public void moveUp(List<T> orderedEntities) {
		moveUp(orderedEntities, this);
	}
	
	public void moveDown(List<T> orderedEntities) {
		moveDown(orderedEntities, this);
	}
	
	public static void moveUp(List<? extends  Ordered> orderedList, Ordered ordered) {
		if(orderedList.size()<2 || ordered == null) {
			return;
		}
		Integer position = orderedList.get(0).getPosition();
		if(position == null || position == Integer.MAX_VALUE) {
			renumberPositions(orderedList);
		}
		int otherPos = orderedList.indexOf(ordered) - 1;
		swapModuleConfigurations(orderedList, ordered, otherPos);		
	}


	public static void moveDown(List<? extends Ordered> orderedList, Ordered ordered) {
		if(orderedList.size()<2 || ordered == null) {
			return;
		}
		Integer position = orderedList.get(0).getPosition();
		if(position == null || position == Integer.MAX_VALUE) {
			renumberPositions(orderedList);
		}
		int otherPos = orderedList.indexOf(ordered) + 1;
		swapModuleConfigurations(orderedList, ordered, otherPos);		
	}


	private static void swapModuleConfigurations(List<? extends Ordered> orderedList, Ordered ordered, int otherPos) {
		Ordered siblingOrdered = orderedList.get(otherPos);
		swapPositions(ordered, siblingOrdered);
	}

	private static void swapPositions(Ordered m1, Ordered m2) {
		Integer swap = m1.getPosition();
		m1.setPosition(m2.getPosition());
		m2.setPosition(swap);
	}

	@Transient
	protected OrderedEntityBase copy(OrderedEntityBase clone) {
		clone.setPosition(position);
		return clone;
	}
	
}
