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
package com.userweave.components.notification;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.IClusterable;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.apache.wicket.util.visit.Visit;


/**
 * usage:
 * 
 * new MyEvent().fire(this, target);
 *
 * Class to be notified must implement IAjaxUpdateListener.
 * 
 * optional: implement 
 *  @AjaxUpdateEvent
    protected void onStudySelectionChangedEvent(MyEvent event) {
		...
    }	
 * 
 */

public class AjaxUpdateEvent implements IAjaxUpdateEvent { //implements IAjaxUpdateEvent {
    private transient Component source;
    private transient AjaxRequestTarget requestTarget;

    public AjaxUpdateEvent() {
    }

    protected AjaxUpdateEvent(Component source, AjaxRequestTarget requestTarget) {
        this.source = source;
        this.requestTarget = requestTarget;
    }

    public Component getSource() {
        return source;
    }

    public IAjaxUpdateEvent setSource(Component source) {
        this.source = source;
        return this;
    }

    public AjaxRequestTarget getRequestTarget() {
        return requestTarget;
    }

    public IAjaxUpdateEvent setTarget(AjaxRequestTarget target) {
        this.requestTarget = target;
        return this;
    }

    public final void fire(Component source, AjaxRequestTarget target) {
        this.source = source;
        this.requestTarget = target;

        if (eventRegistry == null) {
            visitComponents(getSource().getPage());
        } else {
            eventRegistry.eventFired(this);

            MarkupContainer page = getSource() instanceof Page
                    ? (Page)getSource()
                    : getSource().findParent(Page.class);

            if (page != null) {
                visitComponents(page);
            }
        }
    }

    private void visitComponents(MarkupContainer page) 
    {
        NotifyVisitor visitor = new NotifyVisitor(this);
        
        visitor.component(page, new Visit<Void>());
    
        page.visitChildren(visitor);
    }

    private static final class NotifyVisitor implements IVisitor 
    {
        private final AjaxUpdateEvent event;

        public NotifyVisitor(final AjaxUpdateEvent event) {
            this.event = event;
        }

        public void component(Component component, final IVisit<Void> visit) 
        {
        	if (component instanceof IAjaxUpdateListener) 
        	{
	            ((IAjaxUpdateListener)component).notifyAjaxUpdate(event);
	            executeTaggedMethods(component);
        	}
        }

        private void executeTaggedMethods(Component component) {
            Set executed = new HashSet();

            Class aClass = component.getClass();
            for (; aClass != null; aClass = aClass.getSuperclass()) {
                for (Method method : aClass.getDeclaredMethods()) {                    
                    if (event.executeMethod(method)) {
                        executeAnnotatedMethod(component, executed, method);
                    }
                }
            }
        }
               
        private void executeAnnotatedMethod(Component component, Set executed, Method method) {
            Class[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1) {
                throw new RuntimeException("Requires a single event parameter in " + method);
            }

            if (parameterTypes[0].isAssignableFrom(event.getClass()) && !executed.contains(method.getName())) {
                executed.add(method.getName());
                try {
                    if (!method.isAccessible()) {
                        method.setAccessible(true);
                    }
                    method.invoke(component, event);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

		@Override
		public void component(Object object, IVisit visit)
		{
		}
    }

    protected boolean executeMethod(Method method) {
    	return method.getAnnotation(AjaxEventListener.class) != null;           
    }
    
    private static IEventRegistry eventRegistry;

    public interface IEventRegistry {
        void eventFired(IAjaxUpdateEvent event);
    }

    public static void setEventRegistry(IEventRegistry eventRegistry) {
        AjaxUpdateEvent.eventRegistry = eventRegistry;
    }
    
    public static interface IAjaxUpdateListener extends IClusterable {
        public void notifyAjaxUpdate(final IAjaxUpdateEvent event);
    }
}
