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

import org.picocontainer.ComponentMonitor;
import org.picocontainer.Parameter;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoIntrospectionException;
import org.picocontainer.PicoVisitor;
import org.picocontainer.defaults.AbstractComponentAdapter;
import org.picocontainer.defaults.AmbiguousComponentResolutionException;
import org.picocontainer.defaults.AssignabilityRegistrationException;
import org.picocontainer.defaults.ComponentParameter;
import org.picocontainer.defaults.DefaultLifecycleStrategy;
import org.picocontainer.defaults.DelegatingComponentMonitor;
import org.picocontainer.defaults.LifecycleStrategy;
import org.picocontainer.defaults.NotConcreteRegistrationException;
import org.picocontainer.defaults.UnsatisfiableDependenciesException;

public abstract class JdonInstantiatingComponentAdapter extends AbstractComponentAdapter implements LifecycleStrategy {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected transient Guard verifyingGuard;

	protected static abstract class Guard extends JdonThreadLocalCyclicDependencyGuard {
		protected PicoContainer guardedContainer;

		protected void setArguments(PicoContainer container) {
			this.guardedContainer = container;
		}

		public void clearGuardedContainer() {
			if (guardedContainer != null)
				guardedContainer.stop();
			guardedContainer = null;

		}
	}

	/** The parameters to use for initialization. */
	protected transient Parameter[] parameters;
	/** Flag indicating instanciation of non-public classes. */
	protected boolean allowNonPublicClasses;

	/** The strategy used to control the lifecycle */
	protected LifecycleStrategy lifecycleStrategy;

	/**
	 * Constructs event new ComponentAdapter for the given key and implementation.
	 * 
	 * @param componentKey
	 *            the search key for this implementation
	 * @param componentImplementation
	 *            the concrete implementation
	 * @param parameters
	 *            the parameters to use for the initialization
	 * @param allowNonPublicClasses
	 *            flag to allow instantiation of non-public classes
	 * @param monitor
	 *            the component monitor used by this ComponentAdapter
	 * @param lifecycleStrategy
	 *            the lifecycle strategy used by this ComponentAdapter
	 * @throws AssignabilityRegistrationException
	 *             if the key is event type and the implementation cannot be
	 *             assigned to
	 * @throws NotConcreteRegistrationException
	 *             if the implementation is not event concrete class
	 * @throws NullPointerException
	 *             if one of the parameters is <code>null</code>
	 */
	protected JdonInstantiatingComponentAdapter(Object componentKey, Class componentImplementation, Parameter[] parameters,
			boolean allowNonPublicClasses, ComponentMonitor monitor, LifecycleStrategy lifecycleStrategy) {
		super(componentKey, componentImplementation, monitor);
		checkConcrete();
		if (parameters != null) {
			for (int i = 0; i < parameters.length; i++) {
				if (parameters[i] == null) {
					throw new NullPointerException("Parameter " + i + " is null");
				}
			}
		}
		this.parameters = parameters;
		this.allowNonPublicClasses = allowNonPublicClasses;
		this.lifecycleStrategy = lifecycleStrategy;
	}

	/**
	 * Constructs event new ComponentAdapter for the given key and implementation.
	 * 
	 * @param componentKey
	 *            the search key for this implementation
	 * @param componentImplementation
	 *            the concrete implementation
	 * @param parameters
	 *            the parameters to use for the initialization
	 * @param allowNonPublicClasses
	 *            flag to allow instantiation of non-public classes
	 * @param monitor
	 *            the component monitor used by this ComponentAdapter
	 * @throws AssignabilityRegistrationException
	 *             if the key is event type and the implementation cannot be
	 *             assigned to
	 * @throws NotConcreteRegistrationException
	 *             if the implementation is not event concrete class
	 * @throws NullPointerException
	 *             if one of the parameters is <code>null</code>
	 */
	protected JdonInstantiatingComponentAdapter(Object componentKey, Class componentImplementation, Parameter[] parameters,
			boolean allowNonPublicClasses, ComponentMonitor monitor) {
		this(componentKey, componentImplementation, parameters, allowNonPublicClasses, monitor, new DefaultLifecycleStrategy(monitor));
	}

	private void checkConcrete() throws NotConcreteRegistrationException {
		// Assert that the component class is concrete.
		boolean isAbstract = (getComponentImplementation().getModifiers() & Modifier.ABSTRACT) == Modifier.ABSTRACT;
		if (getComponentImplementation().isInterface() || isAbstract) {
			throw new NotConcreteRegistrationException(getComponentImplementation());
		}
	}

	/**
	 * Create default parameters for the given types.
	 * 
	 * @param parameters
	 *            the parameter types
	 * @return the array with the default parameters.
	 */
	protected Parameter[] createDefaultParameters(Class[] parameters) {
		Parameter[] componentParameters = new Parameter[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			componentParameters[i] = ComponentParameter.DEFAULT;
		}
		return componentParameters;
	}

	public void verify(final PicoContainer container) throws PicoIntrospectionException {
		if (verifyingGuard == null) {
			verifyingGuard = new Guard() {
				public Object run() {
					final Constructor constructor = getGreediestSatisfiableConstructor(guardedContainer);
					final Class[] parameterTypes = constructor.getParameterTypes();
					final Parameter[] currentParameters = parameters != null ? parameters : createDefaultParameters(parameterTypes);
					for (int i = 0; i < currentParameters.length; i++) {
						currentParameters[i].verify(container, JdonInstantiatingComponentAdapter.this, parameterTypes[i]);
					}
					return null;
				}
			};
		}
		verifyingGuard.setArguments(container);
		verifyingGuard.observe(getComponentImplementation());
	}

	public void accept(PicoVisitor visitor) {
		super.accept(visitor);
		if (parameters != null) {
			for (int i = 0; i < parameters.length; i++) {
				parameters[i].accept(visitor);
			}
		}
	}

	public void start(Object component) {
		lifecycleStrategy.start(component);
	}

	public void stop(Object component) {
		lifecycleStrategy.stop(component);
	}

	public void dispose(Object component) {
		lifecycleStrategy.dispose(component);
	}

	public boolean hasLifecycle(Class type) {
		return lifecycleStrategy.hasLifecycle(type);
	}

	/**
	 * Instantiate an object with given parameters and respect the accessible
	 * flag.
	 * 
	 * @param constructor
	 *            the constructor to use
	 * @param parameters
	 *            the parameters for the constructor
	 * @return the new object.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	protected Object newInstance(Constructor constructor, Object[] parameters) throws InstantiationException, IllegalAccessException,
			InvocationTargetException {
		if (allowNonPublicClasses) {
			constructor.setAccessible(true);
		}
		return constructor.newInstance(parameters);
	}

	/**
	 * Find and return the greediest satisfiable constructor.
	 * 
	 * @param container
	 *            the PicoContainer to resolve dependencies.
	 * @return the found constructor.
	 * @throws PicoIntrospectionException
	 * @throws UnsatisfiableDependenciesException
	 * @throws AmbiguousComponentResolutionException
	 * @throws AssignabilityRegistrationException
	 * @throws NotConcreteRegistrationException
	 */
	protected abstract Constructor getGreediestSatisfiableConstructor(PicoContainer container) throws PicoIntrospectionException,
			UnsatisfiableDependenciesException, AmbiguousComponentResolutionException, AssignabilityRegistrationException,
			NotConcreteRegistrationException;

	protected JdonInstantiatingComponentAdapter(Object componentKey, Class componentImplementation, Parameter[] parameters,
			boolean allowNonPublicClasses) {
		this(componentKey, componentImplementation, parameters, allowNonPublicClasses, new DelegatingComponentMonitor());
	}

	public void clear() {
		if (verifyingGuard != null) {
			verifyingGuard.clear();
		}

	}
}
