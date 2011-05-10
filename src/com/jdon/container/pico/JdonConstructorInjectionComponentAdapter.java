/*
 * Copyright 2003-2009 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.jdon.container.pico;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.picocontainer.Parameter;
import org.picocontainer.defaults.AssignabilityRegistrationException;
import org.picocontainer.defaults.ConstructorInjectionComponentAdapter;
import org.picocontainer.defaults.NotConcreteRegistrationException;

import com.jdon.domain.advsior.ComponentAdvsior;

public class JdonConstructorInjectionComponentAdapter extends ConstructorInjectionComponentAdapter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConfigInfo configInfo;

	public JdonConstructorInjectionComponentAdapter(Object componentKey, Class componentImplementation, Parameter parameters[],
			boolean allowNonPublicClasses, ConfigInfo configInfo) throws AssignabilityRegistrationException, NotConcreteRegistrationException {
		super(componentKey, componentImplementation, parameters, allowNonPublicClasses);
		this.configInfo = configInfo;
	}

	public JdonConstructorInjectionComponentAdapter(Object componentKey, Class componentImplementation, Parameter parameters[], ConfigInfo configInfo) {
		this(componentKey, componentImplementation, parameters, false, configInfo);
	}

	public JdonConstructorInjectionComponentAdapter(Object componentKey, Class componentImplementation, ConfigInfo configInf)
			throws AssignabilityRegistrationException, NotConcreteRegistrationException {
		this(componentKey, componentImplementation, null, configInf);
	}

	// overide InstantiatingComponentAdapter 's newInstance
	protected Object newInstance(Constructor constructor, Object[] parameters) throws InstantiationException, IllegalAccessException,
			InvocationTargetException {
		if (allowNonPublicClasses) {
			constructor.setAccessible(true);
		}

		Object o = constructor.newInstance(parameters);
		ComponentAdvsior componentAdvsior = (ComponentAdvsior) configInfo.getContainerWrapper().lookup(ComponentAdvsior.NAME);
		if (componentAdvsior != null)
			o = componentAdvsior.createProxy(o);
		return o;
	}
}
