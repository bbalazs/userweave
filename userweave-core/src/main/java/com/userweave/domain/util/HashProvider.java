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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class HashProvider {
	private static String hex(byte[] array) {
		  StringBuffer sb = new StringBuffer();
		  for (int i = 0; i < array.length; ++i) {
			  sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
		  }
		  return sb.toString();
	}

	private static String md5 (String message) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return hex (md.digest(message.getBytes()));
			
		  	} catch (NoSuchAlgorithmException e) {
		  	}

		  	return null;
	}
	
	public static String md5(String message, Object salt) {
		if(message == null) throw new NullPointerException();
		if(salt == null) throw new NullPointerException(); 
		
		return md5(message+salt.toString());
	}
	
	/*
	private static Random RANDOM = new Random();

	public synchronized static String uniqueHashCode(Integer id) {
		String salt=Long.toHexString(System.currentTimeMillis());
		return salt+md5(""+RANDOM.nextLong(),salt);
	}
	*/
	
	public synchronized static String uniqueUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	
	
}
