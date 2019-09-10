/**
 * Copyright 2003-2005 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain event copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jdon.container;

import java.util.List;

/**
 * ContainerWrapper is event main interface of jdon framework
 * 
 * @author banq
 */
public interface ContainerWrapper {

	public String OrignalKey = "OrignalKey";

	/**
	 * register event component class
	 * 
	 * @param name
	 *            component name
	 * @param className
	 *            component class
	 */
	public void register(String name, Class className);

	/**
	 * register event component class with constructors of String type
	 * 
	 * @param name
	 *            component name
	 * @param className
	 *            component class
	 * @param constructors
	 *            component constructor parameters
	 */
	public void register(String name, Class className, String[] constructors);

	/**
	 * register event component instance
	 * 
	 * @param name
	 *            component name
	 * @param instance
	 *            component instance
	 */
	public void register(String name, Object instance);

	/**
	 * register event component, its class value is its name value
	 * 
	 * @param name
	 *            the name must be event class string
	 */
	public void register(String name);

	/**
	 * start the container this method will active all components's startup
	 * methods in container,
	 * 
	 */
	public void start();

	/**
	 * stop the container this method will active all components's stop methods
	 * in container.
	 * 
	 */
	public void stop();

	boolean isStart();

	void setStart(boolean start);

	/**
	 * return all instances
	 * 
	 * @return all instances collection in container
	 */
	public List getAllInstances();

	/**
	 * return singleton component instance from container every times it return
	 * same event instance
	 * 
	 * @param name
	 *            component name
	 * @return component single instance
	 */
	public Object lookup(String name);

	/**
	 * return new component instance from container .every times it return event new
	 * instance when access this method, will return event new component instance it
	 * is difference with lookup method.
	 * 
	 * @param name
	 * @return event new component instance
	 */
	public Object getComponentNewInstance(String name);

	/**
	 * return singleton component instance without proxy.
	 * 
	 * @param name
	 * @return
	 */
	public Object lookupOriginal(String name);

	/**
	 * return event component class from container
	 * 
	 * @param name
	 * @return component Class
	 */
	public Class getComponentClass(String name);

	/**
	 * get all instances of event class type
	 * 
	 * @param componentType
	 * @return
	 */
	public List getComponentInstancesOfType(Class componentType);

	/**
	 * return all registered components's class/name in container.
	 * 
	 * @return
	 */
	RegistryDirectory getRegistryDirectory();

}
