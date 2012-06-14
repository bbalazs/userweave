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
package com.userweave.components.valueListPanel.imageValueListPanel;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ByteArray implements Serializable {

	public void setId(Integer id) {
		this.id = id;
	}

	private byte[] byteArray;
	
	private Integer id;
	
	public byte[] getByteArray() {
		return byteArray;
	}

	public void setByteArray(byte[] byteArray) {
		this.byteArray = byteArray;
	}

	public Integer getId() {
		return id;
	}

	public ByteArray(Integer id, byte[] byteArray) {
		super();
		this.id = id;
		this.byteArray = byteArray;
	}
}
