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
package com.userweave.application;
// SpringApplicationContext.java

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Wrapper to always return a reference to the Spring Application Context from
 * within non-Spring enabled beans. Unlike Spring MVC's WebApplicationContextUtils
 * we do not need a reference to the Servlet context for this. All we need is
 * for this bean to be initialized during application startup.
 */
public class SpringApplicationContext implements ApplicationContextAware {

  private static ApplicationContext CONTEXT;

  /**
   * This method is called from within the ApplicationContext once it is 
   * done starting up, it will stick a reference to itself into this bean.
   * @param context a reference to the ApplicationContext.
   */
  public void setApplicationContext(ApplicationContext context) throws BeansException {
    CONTEXT = context;
  }

  /**
   * This is about the same as context.getBean("beanName"), except it has its
   * own static handle to the Spring context, so calling this method statically
   * will give access to the beans by name in the Spring application context.
   * As in the context.getBean("beanName") call, the caller must cast to the
   * appropriate target class. If the bean does not exist, then a Runtime error
   * will be thrown.
   * @param beanName the name of the bean to get.
   * @return an Object reference to the named bean.
   */
  public static Object getBean(String beanName) {
    return CONTEXT.getBean(beanName);
  }
}