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
package com.userweave.domain.util;

import com.userweave.domain.Study;

public class StudyPaymentInspector {
	public static boolean isPaid(Study study) {
		boolean isPaid = true;
		
		if(study.getConsideration() == null || 
//				(study.getConsideration().isPaid() && study.getConsideration().getInvoice().getGeneratedPdf() == null) ||
				(!study.getConsideration().isWouldLikeToPay() && !study.getConsideration().isWouldLikeToWriteArticle()) ||
				(study.getConsideration().isWouldLikeToPay() && !study.getConsideration().isPaid()) ||
				(study.getConsideration().isWouldLikeToWriteArticle() && !study.getConsideration().isWritten()))
		{
			isPaid = false;
		}
		
		
		return isPaid;
	}
	
} 
