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
package com.userweave.config;

import java.io.File;
import java.util.Properties;

import org.hibernate.tool.hbm2ddl.SchemaExport;

public class MPSchemaExport {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
//		if (args.length != 1) {
//			System.out.println("please specify output filename");
//		}
//		
		String filename = "schema.ddl";//args[0];
		
		final AnnotationConfiguration configuration = new AnnotationConfiguration();

		final Properties properties = new Properties();
		properties.setProperty("hibernate.dialect","org.hibernate.dialect.PostgreSQLDialect");
		configuration.setProperties(properties);
		
		SchemaExport export = new SchemaExport(configuration);
		export.setDelimiter(";");
		export.setFormat(true);
		
		File outputFile = new File(filename);
		export.setOutputFile(outputFile.toString());
		export.execute(false, false, false, true);
	}

}
