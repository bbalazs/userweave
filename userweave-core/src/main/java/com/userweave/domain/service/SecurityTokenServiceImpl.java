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

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.userweave.dao.SecurityTokenDao;
import com.userweave.domain.Invitation;
import com.userweave.domain.SecurityToken;
import com.userweave.domain.User;
import com.userweave.domain.SecurityToken.SecurityTokenType;
import com.userweave.domain.util.HashProvider;

@Service
@Transactional
public class SecurityTokenServiceImpl implements SecurityTokenService {

	@Resource
	private SecurityTokenDao securityTokenDao;
	
	public String createToken(User user, SecurityTokenType type) {
		// several security tokens for api-call allowed
		SecurityToken token = null;
		if(type != SecurityTokenType.API_ACCESS) {
			List<SecurityToken> tokens = securityTokenDao.findByUserAndType(user,type);
			if(!tokens.isEmpty()) {
				token = tokens.get(0);
			}
		}
		if(token==null) {
			token = createToken(type, false);
			token.setUser(user);
			securityTokenDao.save(token);
		}
		return token.getHashCode();
	}

	@Override
	public String createToken(Invitation invitation, SecurityTokenType type) {
		SecurityToken token = securityTokenDao.findByInvitationAndType(invitation, type);
		if(token==null) {
			token = createToken(type, true);
			token.setInvitation(invitation);
			securityTokenDao.save(token);
		}

		return token.getHashCode();
	}

	private SecurityToken createToken(SecurityTokenType type, boolean shortHash) {
		SecurityToken token = new SecurityToken();
		token.setCreationDate(new Date());
		token.setType(type);
		securityTokenDao.save(token);
		
	
		// create token
		String code = createHashCode(shortHash);
		if(shortHash) {
			// check for duplicates. This may occur because we usa a truncated hash code
			SecurityToken duplicateToken = securityTokenDao.findByHashCode(code);
			int maxTries = 5;
			while(duplicateToken != null && !duplicateToken.equals(token)) {
				if(maxTries-- <= 0) {
					// fallback to prevent ever running while loop
					code = createHashCode(false);
					break;
				} 
				code = createHashCode(shortHash);
				duplicateToken = securityTokenDao.findByHashCode(token.getHashCode());
			}
		}
		token.setHashCode(code);
		
		securityTokenDao.save(token);
		return token;
	}

	private String createHashCode(boolean shortHash) {
		String code = HashProvider.uniqueUUID();
		if(shortHash) {
			code = code.substring(0,10);
		}
		return code;
	}
	
	public SecurityToken validateToken(String code, SecurityTokenType... securityTokenTypes) {
		SecurityToken token = securityTokenDao.findByHashCode(code);
		if(token!=null) {
			if(!SecurityTokenType.contains(token.getType(), securityTokenTypes)) {
				return null;
			}
		}
		return token;
	}

	public void removeOldTokens() {
		List<SecurityToken> tokens = securityTokenDao.findByCreationDate(new DateTime().minusDays(7));
		for(SecurityToken token: tokens) {
			deleteToken(token);
		}
	}

	public void deleteToken(SecurityToken token) {
		if(token.getInvitation() != null) {
			token.getInvitation().setToken(null);
			//token.setInvitation(null);
		}
		securityTokenDao.delete(token);		
	}
	
	public String createTokenForApi(User user) {
		return createToken(user, SecurityTokenType.API_ACCESS);
	}

	@Override
	public List<SecurityToken> listTokens(User user, SecurityTokenType type) {
		return securityTokenDao.findByUserAndType(user, type);
	}
}
