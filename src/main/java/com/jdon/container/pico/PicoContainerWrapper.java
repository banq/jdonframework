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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.Parameter;
import org.picocontainer.defaults.ConstantParameter;

import com.jdon.container.ContainerWrapper;
import com.jdon.container.RegistryDirectory;
import com.jdon.container.finder.ContainerCallback;
import com.jdon.domain.advsior.ComponentAdvsior;
import com.jdon.util.Debug;

/**
 * Picocontainer is the implemention of containerWrapper.
 * 
 * 
 * @author banq
 */
public class PicoContainerWrapper implements ContainerWrapper, java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6003045437916961392L;

	public final static String module = PicoContainerWrapper.class.getName();

	private JdonPicoContainer container;

	private RegistryDirectory registryDirectory;

	private volatile boolean start;

	/**
	 * construct a picocontainer without cache.
	 * 
	 */
	public PicoContainerWrapper(ConfigInfo configInfo) {
		this.container = new JdonPicoContainer(new JdonComponentAdapterFactory(configInfo));
		this.registryDirectory = new RegistryDirectory();

	}

	public synchronized void registerContainerCallback() {
		ContainerCallback containerCallback = new ContainerCallback(this);
		register(ContainerCallback.NAME, containerCallback);
		register(ComponentAdvsior.NAME, new ComponentAdvsior(containerCallback));
	}

	public synchronized void register(String name, Class className) {
		try {
			Debug.logVerbose("[JdonFramework]register: name=" + name + " class=" + className.getName(), module);
			container.registerComponentImplementation(name, className);
			registryDirectory.addComponentName(className, name);

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
			registryDirectory.addComponentName(oClass, name);

		} catch (Exception ex) {
			Debug.logWarning(" registe error: " + name + " should be a full class's name", module);
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
				registryDirectory.addComponentName(className, name);
			} else if (types[0].isArray()) { // parameter type is String[]
				Debug.logVerbose("[JdonFramework]parameter type is String[] " + constructors, module);
				ConstantParameter param = new ConstantParameter(constructors);
				Parameter[] params = new Parameter[] { param };
				container.registerComponentImplementation(name, className, params);
				registryDirectory.addComponentName(className, name);
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
			registryDirectory.addComponentName(instance.getClass(), name);
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
			this.container = null;
			this.registryDirectory = null;
		} catch (Exception e) {
			Debug.logError("[JdonFramework] container stop error: " + e, module);
		} finally {
			start = false;
		}
	}

	public boolean isStart() {
		return start;
	}

	public synchronized void setStart(boolean start) {
		if (start) {
			this.notifyAll();
		}
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
		return container.getComponentInstance(name);
	}

	public Object lookupOriginal(String name) {
		Object object = null;
		Map orignals = (Map) lookup(ContainerWrapper.OrignalKey);
		if (orignals != null)
			object = orignals.get(name);
		if (object == null)
			object = lookup(name);
		return object;
	}

	/**
	 * This method will usually create a new instance each time it is called
	 * 
	 * @param name
	 *            component name
	 * @return object new instance
	 */
	public Object getComponentNewInstance(String name) {
		Debug.logVerbose("[JdonFramework]getComponentNewInstance: name=" + name, module);
		ComponentAdapter componentAdapter = container.getComponentAdapter(name);
		if (componentAdapter == null) {
			Debug.logWarning("[JdonFramework]Not find the component in container :" + name, module);
			return null;
		}
		return componentAdapter.getComponentInstance(container);
	}

	public Class getComponentClass(String name) {
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
		List result = new ArrayList();
		Map orignals = (Map) lookup(ContainerWrapper.OrignalKey);
		if (orignals != null)
			for (Object o : orignals.values()) {
				if (componentType.isAssignableFrom(o.getClass())) {
					result.add(o);
				}
			}
		result.addAll(container.getComponentInstancesOfType(componentType));
		return result;
	}

	public RegistryDirectory getRegistryDirectory() {
		if (!start) {
			Debug.logError("container not start, not return RegistryNamesHolder", module);
			return null;
		}
		return registryDirectory;
	}

}
