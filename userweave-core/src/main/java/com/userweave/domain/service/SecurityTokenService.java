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
package com.userweave.domain.service;

import java.util.List;

import com.userweave.domain.Invitation;
import com.userweave.domain.SecurityToken;
import com.userweave.domain.User;
import com.userweave.domain.SecurityToken.SecurityTokenType;

public interface SecurityTokenService {
	public String createToken(User user, SecurityTokenType type);
	public String createToken(Invitation invitation, SecurityTokenType type);
	public String createTokenForApi(User user);
	
	public void deleteToken(SecurityToken token);
	
	public SecurityToken validateToken(String token, SecurityTokenType ... securityTokenTypes);
	
	public List<SecurityToken> listTokens(User user, SecurityTokenType type);	
	
}
