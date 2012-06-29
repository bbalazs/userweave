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
package com.userweave.module.methoden.questionnaire.page.report.question;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Triple<T,U,V> implements Serializable {

	private final T t;
	
	private final U u;
	
	private V v;
	
	public Triple(T t, U u, V v) {
		this.t = t;
		this.u = u;
		this.v = v;		
	}
	
	public T getFirst() {
		return t;
	}
	
	public U getSecond() {
		return u;
	}
	
	public V getThird() {
		return v;
	}
	
	public void setThird(V v) {
		this.v = v;
	}
}
