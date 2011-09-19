/**
 * Copyright 2003-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jdon.container.pico;

import java.lang.reflect.Constructor;
import java.util.List;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.picocontainer.defaults.ConstantParameter;
import org.picocontainer.defaults.DefaultPicoContainer;

import com.jdon.container.ContainerWrapper;
import com.jdon.container.finder.ContainerCallback;
import com.jdon.domain.advsior.ComponentAdvsior;
import com.jdon.util.Debug;

/**
 * Picocontainer is the implemention of containerWrapper.
 * 
 * 
 * @author banq
 */
public class PicoContainerWrapper implements ContainerWrapper {
	public final static String module = PicoContainerWrapper.class.getName();

	private final MutablePicoContainer container;

	private volatile boolean start;

	/**
	 * construct a picocontainer without cache.
	 * 
	 */
	public PicoContainerWrapper(ConfigInfo configInfo) {
		this.container = new JdonPicoContainer(new JdonComponentAdapterFactory(configInfo));
		configInfo.setContainerWrapper(this);
		registerContainerCallback();
	}

	public PicoContainerWrapper(MutablePicoContainer container) {
		this.container = container;
		registerContainerCallback();
	}

	public synchronized void registerContainerCallback() {
		ContainerCallback containerCallback = new ContainerCallback(this);
		register(ContainerCallback.NAME, containerCallback);
		register(ComponentAdvsior.NAME, new ComponentAdvsior(containerCallback));
	}

	/**
	 * registe a child container in parent container
	 */
	public synchronized void registerChild(String name) {
		try {
			MutablePicoContainer child = new DefaultPicoContainer(container);
			register(name, child);
			// todo: bidirector register, so components can find each other
			// child.registerComponentInstance(name, container);
		} catch (Exception ex) {
			Debug.logWarning(" registe error: " + name, module);
		}
	}

	/**
	 * get the child container from the parent container
	 */
	public ContainerWrapper getChild(String name) {
		MutablePicoContainer child = (MutablePicoContainer) lookup(name);
		return new PicoContainerWrapper(child);
	}

	public synchronized void register(String name, Class className) {
		try {
			Debug.logVerbose("[JdonFramework]register: name=" + name + " class=" + className.getName(), module);
			container.registerComponentImplementation(name, className);

		} catch (Exception ex) {
			Debug.logWarning(" registe error: " + name, module);
		}
	}

	public synchronized void register(String name) {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			Class oClass = classLoader.loadClass(name);

			Debug.logVerbose("[JdonFramework]register: name=" + name + " class=" + oClass.getName(), module);
			register(name, oClass);

		} catch (Exception ex) {
			Debug.logWarning(" registe error: " + name, module);
		}
	}

	public synchronized void registerOriginal(String name, Object componentInstance) {
		try {
			container.registerComponentInstance(name + ContainerWrapper.OrignalKey, componentInstance);
		} catch (Exception ex) {
			Debug.logWarning(" registe error: " + name, module);
		}
	}

	public synchronized void register(String name, Class className, String[] constructors) {
		if (constructors == null) {
			register(name, className);
			return;
		}
		try {
			Debug.logVerbose("[JdonFramework]register: name=" + name + " class=" + className.getName(), module);
			Debug.logVerbose("[JdonFramework] constructor params size =" + constructors.length, module);

			// check the class 's construtor parameter type is String[] or
			// String s1, String s2 ..
			Constructor[] cs = className.getConstructors();
			Class[] types = cs[0].getParameterTypes();
			Debug.logVerbose("[JdonFramework] constructor first ParameterType is " + types[0], module);
			if (types[0].isAssignableFrom(String.class)) {// parameter type is
				// String s1, String
				// s2 ..
				Debug.logVerbose("[JdonFramework]parameter type is  String s1, String s2 ", module);
				Parameter[] params = new Parameter[constructors.length];
				for (int i = 0; i < constructors.length; i++) {
					ConstantParameter param = new ConstantParameter(new String(constructors[i]));
					Debug.logVerbose("[JdonFramework] register its constructor value is " + constructors[i], module);
					params[i] = param;
				}
				container.registerComponentImplementation(name, className, params);
			} else if (types[0].isArray()) { // parameter type is String[]
				Debug.logVerbose("[JdonFramework]parameter type is String[] " + constructors, module);
				ConstantParameter param = new ConstantParameter(constructors);
				Parameter[] params = new Parameter[] { param };
				container.registerComponentImplementation(name, className, params);
			} else {
				throw new Exception("constructors.types netiher is not String[] or String s1, String s2...");
			}

		} catch (Exception ex) {
			Debug.logError(" registe " + name + " error: " + ex, module);
		}

	}

	public synchronized void register(String name, Object instance) {
		try {
			Debug.logVerbose("[JdonFramework]register: name=" + name + " class=" + instance.getClass().getName(), module);
			container.registerComponentInstance(name, instance);
		} catch (Exception ex) {
			Debug.logWarning(" registe error: " + name, module);
		}

	}

	public synchronized void start() {
		try {
			container.start();
		} catch (RuntimeException e) {
			Debug.logError("[JdonFramework] container start error: " + e, module);
		}
	}

	public synchronized void stop() {
		try {
			container.stop();
			container.dispose();
			start = false;
		} catch (Exception e) {
		}
	}

	public boolean isStart() {
		return start;
	}

	public synchronized void setStart(boolean start) {
		if (start)
			this.notifyAll();
		this.start = start;
	}

	public List getAllInstances() {
		if (!isStart()) {
			Debug.logError("container not start", module);
			return null;
		}
		return container.getComponentInstances();
	}

	public Object lookup(String name) {
		Object object = container.getComponentInstance(name);
		if (object == null)
			Debug.logWarning("[JdonFramework]Not find the component in container :" + name, module);
		return object;
	}

	public Object lookupOriginal(String name) {
		Object object = container.getComponentInstance(name + ContainerWrapper.OrignalKey);
		if (object == null)
			object = lookup(name);
		return object;
	}

	public Object getComponentNewInstanceOriginal(String name) {
		ComponentAdapter componentAdapter = container.getComponentAdapter(name + ContainerWrapper.OrignalKey);
		if (componentAdapter == null)
			return getComponentNewInstance(name);
		return componentAdapter.getComponentInstance(container);
	}

	/**
	 * This method will usually create a new instance each time it is called
	 * 
	 * @param name
	 *            component name
	 * @return object new instance
	 */
	public Object getComponentNewInstance(String name) {
		if (!isStart()) {
			Debug.logError("[JdonFramework]container not start,  try later", module);
			return null;
		}

		Debug.logVerbose("[JdonFramework]getComponentNewInstance: name=" + name, module);
		ComponentAdapter componentAdapter = container.getComponentAdapter(name);
		if (componentAdapter == null) {
			Debug.logWarning("[JdonFramework]Not find the component in container :" + name, module);
			return null;
		}
		return componentAdapter.getComponentInstance(container);
	}

	public Class getComponentClass(String name) {
		if (!isStart()) {
			Debug.logError("container not start", module);
			return null;
		}

		Debug.logVerbose("[JdonFramework]getComponentClass: name=" + name, module);
		ComponentAdapter componentAdapter = container.getComponentAdapter(name);
		if (componentAdapter == null)
			Debug.logVerbose("[JdonFramework]Not find the component in container :" + name, module);
		return componentAdapter.getComponentImplementation();
	}

	public List getComponentInstancesOfType(Class componentType) {
		if (!isStart()) {
			Debug.logError("container not start", module);
			return null;
		}
		return container.getComponentInstancesOfType(componentType);
	}

}
