/*
 * Copyright 2003-2009 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain event copy of the License at
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
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.picocontainer.ComponentMonitor;
import org.picocontainer.Parameter;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoInitializationException;
import org.picocontainer.PicoIntrospectionException;
import org.picocontainer.defaults.AmbiguousComponentResolutionException;
import org.picocontainer.defaults.AssignabilityRegistrationException;
import org.picocontainer.defaults.NotConcreteRegistrationException;
import org.picocontainer.defaults.PicoInvocationTargetInitializationException;
import org.picocontainer.defaults.TooManySatisfiableConstructorsException;
import org.picocontainer.defaults.UnsatisfiableDependenciesException;

import com.jdon.container.ContainerWrapper;
import com.jdon.domain.advsior.ComponentAdvsior;

/**
 * Customized ConstructorInjectionComponentAdapter
 * 
 * modify the method getComponentInstance of DefaultPicoContainer of
 * picocontainer
 * 
 * @author banq
 * 
 */
public class JdonConstructorInjectionComponentAdapter extends JdonInstantiatingComponentAdapter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected transient List sortedMatchingConstructors;
	protected transient Guard instantiationGuard;

	private static abstract class Guard extends JdonThreadLocalCyclicDependencyGuard {
		protected PicoContainer guardedContainer;

		private void setArguments(PicoContainer container) {
			this.guardedContainer = container;
		}

	}

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

	/**
	 * difference with picocontainer
	 */
	public Object getComponentInstance(PicoContainer container) throws PicoInitializationException, PicoIntrospectionException,
			AssignabilityRegistrationException, NotConcreteRegistrationException {
		if (instantiationGuard == null) {
			instantiationGuard = new Guard() {
				public Object run() {
					final Constructor constructor;
					try {
						constructor = getGreediestSatisfiableConstructor(guardedContainer);
					} catch (AmbiguousComponentResolutionException e) {
						e.setComponent(getComponentImplementation());
						throw e;
					}
					ComponentMonitor componentMonitor = currentMonitor();
					try {
						Object[] parameters = getConstructorArguments(guardedContainer, constructor);
						componentMonitor.instantiating(constructor);
						long startTime = System.currentTimeMillis();
						Object inst = newInstance(constructor, parameters);
						componentMonitor.instantiated(constructor, System.currentTimeMillis() - startTime);
						return inst;
					} catch (InvocationTargetException e) {
						componentMonitor.instantiationFailed(constructor, e);
						if (e.getTargetException() instanceof RuntimeException) {
							throw (RuntimeException) e.getTargetException();
						} else if (e.getTargetException() instanceof Error) {
							throw (Error) e.getTargetException();
						}
						throw new PicoInvocationTargetInitializationException(e.getTargetException());
					} catch (InstantiationException e) {
						// can't get here because checkConcrete() will catch it
						// earlier, but see PICO-191
						// /CLOVER:OFF
						componentMonitor.instantiationFailed(constructor, e);
						throw new PicoInitializationException("Should never get here");
						// /CLOVER:ON
					} catch (IllegalAccessException e) {
						// can't get here because either filtered or access mode
						// set
						// /CLOVER:OFF
						componentMonitor.instantiationFailed(constructor, e);
						throw new PicoInitializationException(e);
						// /CLOVER:ON
					}
				}
			};
		}
		instantiationGuard.setArguments(container);
		Object result = instantiationGuard.observe(getComponentImplementation());
		instantiationGuard.clear();
		return result;

	}

	// overide InstantiatingComponentAdapter 's newInstance
	protected Object newInstance(Constructor constructor, Object[] parameters) throws InstantiationException, IllegalAccessException,
			InvocationTargetException {
		if (allowNonPublicClasses) {
			constructor.setAccessible(true);
		}

		Object o = constructor.newInstance(parameters);
		ComponentAdvsior componentAdvsior = (ComponentAdvsior) configInfo.getContainerWrapper().lookup(ComponentAdvsior.NAME);
		Object proxy = null;
		if (componentAdvsior != null)
			proxy = componentAdvsior.createProxy(o);

		if (!proxy.getClass().isInstance(o)) {
			Map orignals = getContainerOrignals(configInfo.getContainerWrapper());
			orignals.put((String) this.getComponentKey(), o);
		}

		return proxy;
	}

	public Map getContainerOrignals(ContainerWrapper containerWrapper) {
		Map orignals = (Map) containerWrapper.lookup(ContainerWrapper.OrignalKey);
		if (orignals == null) {
			orignals = new HashMap();
			containerWrapper.register(ContainerWrapper.OrignalKey, orignals);
		}
		return orignals;
	}

	public void clear() {
		super.clear();
		if (instantiationGuard != null) {
			instantiationGuard.clear();
		}

	}

	protected Constructor getGreediestSatisfiableConstructor(PicoContainer container) throws PicoIntrospectionException,
			UnsatisfiableDependenciesException, AmbiguousComponentResolutionException, AssignabilityRegistrationException,
			NotConcreteRegistrationException {
		final Set conflicts = new HashSet();
		final Set unsatisfiableDependencyTypes = new HashSet();
		if (sortedMatchingConstructors == null) {
			sortedMatchingConstructors = getSortedMatchingConstructors();
		}
		Constructor greediestConstructor = null;
		int lastSatisfiableConstructorSize = -1;
		Class unsatisfiedDependencyType = null;
		for (int i = 0; i < sortedMatchingConstructors.size(); i++) {
			boolean failedDependency = false;
			Constructor constructor = (Constructor) sortedMatchingConstructors.get(i);
			Class[] parameterTypes = constructor.getParameterTypes();
			Parameter[] currentParameters = parameters != null ? parameters : createDefaultParameters(parameterTypes);

			// remember: all constructors with less arguments than the given
			// parameters are filtered out already
			for (int j = 0; j < currentParameters.length; j++) {
				// check wether this constructor is statisfiable
				if (currentParameters[j].isResolvable(container, this, parameterTypes[j])) {
					continue;
				}
				unsatisfiableDependencyTypes.add(Arrays.asList(parameterTypes));
				unsatisfiedDependencyType = parameterTypes[j];
				failedDependency = true;
				break;
			}

			if (greediestConstructor != null && parameterTypes.length != lastSatisfiableConstructorSize) {
				if (conflicts.isEmpty()) {
					// we found our match [aka. greedy and satisfied]
					return greediestConstructor;
				} else {
					// fits although not greedy
					conflicts.add(constructor);
				}
			} else if (!failedDependency && lastSatisfiableConstructorSize == parameterTypes.length) {
				// satisfied and same size as previous one?
				conflicts.add(constructor);
				conflicts.add(greediestConstructor);
			} else if (!failedDependency) {
				greediestConstructor = constructor;
				lastSatisfiableConstructorSize = parameterTypes.length;
			}
		}
		if (!conflicts.isEmpty()) {
			throw new TooManySatisfiableConstructorsException(getComponentImplementation(), conflicts);
		} else if (greediestConstructor == null && !unsatisfiableDependencyTypes.isEmpty()) {
			throw new UnsatisfiableDependenciesException(this, unsatisfiedDependencyType, unsatisfiableDependencyTypes, container);
		} else if (greediestConstructor == null) {
			// be nice to the user, show all constructors that were filtered out
			final Set nonMatching = new HashSet();
			final Constructor[] constructors = getConstructors();
			for (int i = 0; i < constructors.length; i++) {
				nonMatching.add(constructors[i]);
			}
			throw new PicoInitializationException("Either do the specified parameters not match any of the following constructors: "
					+ nonMatching.toString() + " or the constructors were not accessible for '" + getComponentImplementation() + "'");
		}
		return greediestConstructor;
	}

	protected Object[] getConstructorArguments(PicoContainer container, Constructor ctor) {
		Class[] parameterTypes = ctor.getParameterTypes();
		Object[] result = new Object[parameterTypes.length];
		Parameter[] currentParameters = parameters != null ? parameters : createDefaultParameters(parameterTypes);

		for (int i = 0; i < currentParameters.length; i++) {
			result[i] = currentParameters[i].resolveInstance(container, this, parameterTypes[i]);
		}
		return result;
	}

	private List getSortedMatchingConstructors() {
		List matchingConstructors = new ArrayList();
		Constructor[] allConstructors = getConstructors();
		// filter out all constructors that will definately not match
		for (int i = 0; i < allConstructors.length; i++) {
			Constructor constructor = allConstructors[i];
			if ((parameters == null || constructor.getParameterTypes().length == parameters.length)
					&& (allowNonPublicClasses || (constructor.getModifiers() & Modifier.PUBLIC) != 0)) {
				matchingConstructors.add(constructor);
			}
		}
		// optimize list of constructors moving the longest at the beginning
		if (parameters == null) {
			Collections.sort(matchingConstructors, new Comparator() {
				public int compare(Object arg0, Object arg1) {
					return ((Constructor) arg1).getParameterTypes().length - ((Constructor) arg0).getParameterTypes().length;
				}
			});
		}
		return matchingConstructors;
	}

	private Constructor[] getConstructors() {
		return (Constructor[]) AccessController.doPrivileged(new PrivilegedAction() {
			public Object run() {
				return getComponentImplementation().getDeclaredConstructors();
			}
		});
	}

}
