/*
 * Copyright 2003-2006 the original author or authors.
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.ComponentMonitor;
import org.picocontainer.LifecycleManager;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoException;
import org.picocontainer.PicoIntrospectionException;
import org.picocontainer.PicoRegistrationException;
import org.picocontainer.PicoVerificationException;
import org.picocontainer.PicoVisitor;
import org.picocontainer.defaults.AmbiguousComponentResolutionException;
import org.picocontainer.defaults.CachingComponentAdapter;
import org.picocontainer.defaults.CachingComponentAdapterFactory;
import org.picocontainer.defaults.ComponentAdapterFactory;
import org.picocontainer.defaults.ComponentMonitorStrategy;
import org.picocontainer.defaults.DefaultComponentAdapterFactory;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.picocontainer.defaults.DuplicateComponentKeyRegistrationException;
import org.picocontainer.defaults.ImmutablePicoContainerProxyFactory;
import org.picocontainer.defaults.InstanceComponentAdapter;
import org.picocontainer.defaults.VerifyingVisitor;

/**
 * modify the method getComponentInstance of DefaultPicoContainer of
 * picocontainer
 * 
 * @author <a href="mailto:banqiao@jdon.com">banq</a>
 * 
 */
public class JdonPicoContainer implements MutablePicoContainer, ComponentMonitorStrategy, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1951644575769783030L;

	public final static String module = JdonPicoContainer.class.getName();

	private final Map componentKeyToAdapterCache = new HashMap();

	private final Map componentKeyToInstanceCache = new HashMap();

	private ComponentAdapterFactory componentAdapterFactory;
	private PicoContainer parent;
	private final List componentAdapters = new ArrayList();

	// Keeps track of instantiation order.
	private final List orderedComponentAdapters = new ArrayList();

	private LifecycleManager lifecycleManager = new OrderedComponentAdapterLifecycleManager();

	private boolean started = false;
	private boolean disposed = false;

	private final HashSet children = new HashSet();

	// Keeps track of child containers started status
	private Set childrenStarted = new HashSet();

	/**
	 * Creates a new container with a custom ComponentAdapterFactory and a
	 * parent container.
	 * <p/>
	 * <em>
	 * Important note about caching: If you intend the components to be cached, you should pass
	 * in a factory that creates {@link CachingComponentAdapter} instances, such as for example
	 * {@link CachingComponentAdapterFactory}. CachingComponentAdapterFactory can delegate to
	 * other ComponentAdapterFactories.
	 * </em>
	 * 
	 * @param componentAdapterFactory
	 *            the factory to use for creation of ComponentAdapters.
	 * @param parent
	 *            the parent container (used for component dependency lookups).
	 */
	public JdonPicoContainer(ComponentAdapterFactory componentAdapterFactory, PicoContainer parent) {
		if (componentAdapterFactory == null)
			throw new NullPointerException("componentAdapterFactory");
		this.componentAdapterFactory = componentAdapterFactory;
		this.parent = parent == null ? null : ImmutablePicoContainerProxyFactory.newProxyInstance(parent);
	}

	/**
	 * Creates a new container with a (caching)
	 * {@link DefaultComponentAdapterFactory} and a parent container.
	 */
	public JdonPicoContainer(PicoContainer parent, ConfigInfo configInfo) {
		this(new JdonComponentAdapterFactory(configInfo), parent);
	}

	/**
	 * Creates a new container with a custom ComponentAdapterFactory and no
	 * parent container.
	 * 
	 * @param componentAdapterFactory
	 *            the ComponentAdapterFactory to use.
	 */
	public JdonPicoContainer(ComponentAdapterFactory componentAdapterFactory) {
		this(componentAdapterFactory, null);
	}

	/**
	 * Creates a new container with a (caching)
	 * {@link DefaultComponentAdapterFactory} and no parent container.
	 */
	public JdonPicoContainer(ConfigInfo configInfo) {
		this(new JdonComponentAdapterFactory(configInfo), null);
	}

	public Collection getComponentAdapters() {
		return Collections.unmodifiableList(componentAdapters);
	}

	public final ComponentAdapter getComponentAdapter(Object componentKey) throws AmbiguousComponentResolutionException {
		ComponentAdapter adapter = (ComponentAdapter) componentKeyToAdapterCache.get(componentKey);
		if (adapter == null && parent != null) {
			adapter = parent.getComponentAdapter(componentKey);
		}
		return adapter;
	}

	public ComponentAdapter getComponentAdapterOfType(Class componentType) {
		// See http://jira.codehaus.org/secure/ViewIssue.jspa?key=PICO-115
		ComponentAdapter adapterByKey = getComponentAdapter(componentType);
		if (adapterByKey != null) {
			return adapterByKey;
		}

		List found = getComponentAdaptersOfType(componentType);

		if (found.size() == 1) {
			return ((ComponentAdapter) found.get(0));
		} else if (found.size() == 0) {
			if (parent != null) {
				return parent.getComponentAdapterOfType(componentType);
			} else {
				return null;
			}
		} else {
			Class[] foundClasses = new Class[found.size()];
			for (int i = 0; i < foundClasses.length; i++) {
				ComponentAdapter componentAdapter = (ComponentAdapter) found.get(i);
				foundClasses[i] = componentAdapter.getComponentImplementation();
			}

			throw new AmbiguousComponentResolutionException(componentType, foundClasses);
		}
	}

	public List getComponentAdaptersOfType(Class componentType) {
		if (componentType == null) {
			return Collections.EMPTY_LIST;
		}
		List found = new ArrayList();
		for (Iterator iterator = getComponentAdapters().iterator(); iterator.hasNext();) {
			ComponentAdapter componentAdapter = (ComponentAdapter) iterator.next();

			if (componentType.isAssignableFrom(componentAdapter.getComponentImplementation())) {
				found.add(componentAdapter);
			}
		}
		return found;
	}

	/**
	 * {@inheritDoc} This method can be used to override the ComponentAdapter
	 * created by the {@link ComponentAdapterFactory} passed to the constructor
	 * of this container.
	 */
	public ComponentAdapter registerComponent(ComponentAdapter componentAdapter) throws DuplicateComponentKeyRegistrationException {
		Object componentKey = componentAdapter.getComponentKey();
		if (componentKeyToAdapterCache.containsKey(componentKey)) {
			throw new DuplicateComponentKeyRegistrationException(componentKey);
		}
		componentAdapters.add(componentAdapter);
		componentKeyToAdapterCache.put(componentKey, componentAdapter);
		return componentAdapter;
	}

	public ComponentAdapter unregisterComponent(Object componentKey) {
		ComponentAdapter adapter = (ComponentAdapter) componentKeyToAdapterCache.remove(componentKey);
		componentAdapters.remove(adapter);
		orderedComponentAdapters.remove(adapter);
		return adapter;
	}

	/**
	 * {@inheritDoc} The returned ComponentAdapter will be an
	 * {@link InstanceComponentAdapter}.
	 */
	public ComponentAdapter registerComponentInstance(Object component) throws PicoRegistrationException {
		return registerComponentInstance(component.getClass(), component);
	}

	/**
	 * {@inheritDoc} The returned ComponentAdapter will be an
	 * {@link InstanceComponentAdapter}.
	 */
	public ComponentAdapter registerComponentInstance(Object componentKey, Object componentInstance) throws PicoRegistrationException {
		if (componentInstance instanceof MutablePicoContainer) {
			MutablePicoContainer pc = (MutablePicoContainer) componentInstance;
			Object contrivedKey = new Object();
			String contrivedComp = "";
			pc.registerComponentInstance(contrivedKey, contrivedComp);
			try {
				if (this.getComponentInstance(contrivedKey) != null) {
					throw new PicoRegistrationException("Cannot register a container to itself. The container is already implicitly registered.");
				}
			} finally {
				pc.unregisterComponent(contrivedKey);
			}

		}
		ComponentAdapter componentAdapter = new InstanceComponentAdapter(componentKey, componentInstance);
		registerComponent(componentAdapter);
		return componentAdapter;
	}

	/**
	 * {@inheritDoc} The returned ComponentAdapter will be instantiated by the
	 * {@link ComponentAdapterFactory} passed to the container's constructor.
	 */
	public ComponentAdapter registerComponentImplementation(Class componentImplementation) throws PicoRegistrationException {
		return registerComponentImplementation(componentImplementation, componentImplementation);
	}

	/**
	 * {@inheritDoc} The returned ComponentAdapter will be instantiated by the
	 * {@link ComponentAdapterFactory} passed to the container's constructor.
	 */
	public ComponentAdapter registerComponentImplementation(Object componentKey, Class componentImplementation) throws PicoRegistrationException {
		return registerComponentImplementation(componentKey, componentImplementation, (Parameter[]) null);
	}

	/**
	 * {@inheritDoc} The returned ComponentAdapter will be instantiated by the
	 * {@link ComponentAdapterFactory} passed to the container's constructor.
	 */
	public ComponentAdapter registerComponentImplementation(Object componentKey, Class componentImplementation, Parameter[] parameters)
			throws PicoRegistrationException {
		ComponentAdapter componentAdapter = componentAdapterFactory.createComponentAdapter(componentKey, componentImplementation, parameters);
		registerComponent(componentAdapter);
		return componentAdapter;
	}

	/**
	 * Same as
	 * {@link #registerComponentImplementation(java.lang.Object, java.lang.Class, org.picocontainer.Parameter[])}
	 * but with parameters as a {@link List}. Makes it possible to use with
	 * Groovy arrays (which are actually Lists).
	 */
	public ComponentAdapter registerComponentImplementation(Object componentKey, Class componentImplementation, List parameters)
			throws PicoRegistrationException {
		Parameter[] parametersAsArray = (Parameter[]) parameters.toArray(new Parameter[parameters.size()]);
		return registerComponentImplementation(componentKey, componentImplementation, parametersAsArray);
	}

	private void addOrderedComponentAdapter(ComponentAdapter componentAdapter) {
		if (!orderedComponentAdapters.contains(componentAdapter)) {
			orderedComponentAdapters.add(componentAdapter);
		}
	}

	public List getComponentInstances() throws PicoException {
		return getComponentInstancesOfType(Object.class);
	}

	public List getComponentInstancesOfType(Class componentType) throws PicoException {
		if (componentType == null) {
			return Collections.EMPTY_LIST;
		}

		Map adapterToInstanceMap = new HashMap();
		for (Iterator iterator = componentAdapters.iterator(); iterator.hasNext();) {
			ComponentAdapter componentAdapter = (ComponentAdapter) iterator.next();
			if (componentType.isAssignableFrom(componentAdapter.getComponentImplementation())) {
				Object componentInstance = getInstance(componentAdapter);
				adapterToInstanceMap.put(componentAdapter, componentInstance);

				// This is to ensure all are added. (Indirect dependencies will
				// be added
				// from InstantiatingComponentAdapter).
				addOrderedComponentAdapter(componentAdapter);
			}
		}
		List result = new ArrayList();
		for (Iterator iterator = orderedComponentAdapters.iterator(); iterator.hasNext();) {
			Object componentAdapter = iterator.next();
			final Object componentInstance = adapterToInstanceMap.get(componentAdapter);
			if (componentInstance != null) {
				// may be null in the case of the "implicit" adapter
				// representing "this".
				result.add(componentInstance);
			}
		}
		return result;
	}

	public Object getComponentInstance(Object componentKey) throws PicoException {
		ComponentAdapter componentAdapter = getComponentAdapter(componentKey);
		if (componentAdapter != null) {
			return getInstance(componentAdapter);
		} else {
			return null;
		}
	}

	public Object getComponentInstanceOfType(Class componentType) {
		final ComponentAdapter componentAdapter = getComponentAdapterOfType(componentType);
		return componentAdapter == null ? null : getInstance(componentAdapter);
	}

	/**
	 * modify this method of old DefaultPicocontainer
	 * 
	 * @param componentAdapter
	 * @return
	 */
	public Object getInstance(ComponentAdapter componentAdapter) {
		Object componentKey = componentAdapter.getComponentKey();
		Object instance = componentKeyToInstanceCache.get(componentKey);
		if (instance == null) {
			if (componentAdapter != null) {
				instance = getTrueInstance(componentAdapter);
				if (instance != null) {
					componentKeyToInstanceCache.put(componentKey, instance);
				}
			}
		}
		return instance;

	}

	private Object getTrueInstance(ComponentAdapter componentAdapter) {

		// check wether this is our adapter
		// we need to check this to ensure up-down dependencies cannot be
		// followed
		final boolean isLocal = componentAdapters.contains(componentAdapter);

		if (isLocal) {
			Object instance = componentAdapter.getComponentInstance(this);

			addOrderedComponentAdapter(componentAdapter);

			return instance;
		} else if (parent != null) {
			return parent.getComponentInstance(componentAdapter.getComponentKey());
		}

		// TODO: decide .. exception or null?
		// exceptrion: mx: +1, joehni +1
		return null;
	}

	public PicoContainer getParent() {
		return parent;
	}

	public ComponentAdapter unregisterComponentByInstance(Object componentInstance) {
		Collection componentAdapters = getComponentAdapters();
		for (Iterator iterator = componentAdapters.iterator(); iterator.hasNext();) {
			ComponentAdapter componentAdapter = (ComponentAdapter) iterator.next();
			if (getInstance(componentAdapter).equals(componentInstance)) {
				return unregisterComponent(componentAdapter.getComponentKey());
			}
		}
		return null;
	}

	/**
	 * @deprecated since 1.1 - Use new VerifyingVisitor().traverse(this)
	 */
	public void verify() throws PicoVerificationException {
		new VerifyingVisitor().traverse(this);
	}

	/**
	 * Start the components of this PicoContainer and all its logical child
	 * containers. Any component implementing the lifecycle interface
	 * {@link org.picocontainer.Startable} will be started.
	 * 
	 * @see #makeChildContainer()
	 * @see #addChildContainer(PicoContainer)
	 * @see #removeChildContainer(PicoContainer)
	 */
	public void start() {
		if (disposed)
			throw new IllegalStateException("Already disposed");
		if (started)
			throw new IllegalStateException("Already started");
		started = true;
		this.lifecycleManager.start(this);
		childrenStarted.clear();
		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			PicoContainer child = (PicoContainer) iterator.next();
			childrenStarted.add(new Integer(child.hashCode()));
			child.start();
		}
	}

	/**
	 * Stop the components of this PicoContainer and all its logical child
	 * containers. Any component implementing the lifecycle interface
	 * {@link org.picocontainer.Startable} will be stopped.
	 * 
	 * @see #makeChildContainer()
	 * @see #addChildContainer(PicoContainer)
	 * @see #removeChildContainer(PicoContainer)
	 */
	public void stop() {
		if (disposed)
			throw new IllegalStateException("Already disposed");
		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			PicoContainer child = (PicoContainer) iterator.next();
			if (childStarted(child)) {
				child.stop();
			}
		}
		this.lifecycleManager.stop(this);
		started = false;
		for (Object p : componentAdapters) {
			if (p instanceof JdonConstructorInjectionComponentAdapter) {
				JdonConstructorInjectionComponentAdapter c = (JdonConstructorInjectionComponentAdapter) p;
				c.clear();
			} else if (p instanceof InstanceComponentAdapter) {
				InstanceComponentAdapter c = (InstanceComponentAdapter) p;
				c.stop(this);
			}
		}
		componentKeyToAdapterCache.clear();
		componentKeyToInstanceCache.clear();
		componentAdapters.clear();
		orderedComponentAdapters.clear();
		children.clear();
		componentAdapterFactory = null;
		parent = null;
	}

	private boolean childStarted(PicoContainer child) {
		return childrenStarted.contains(new Integer(child.hashCode()));
	}

	/**
	 * Dispose the components of this PicoContainer and all its logical child
	 * containers. Any component implementing the lifecycle interface
	 * {@link org.picocontainer.Disposable} will be disposed.
	 * 
	 * @see #makeChildContainer()
	 * @see #addChildContainer(PicoContainer)
	 * @see #removeChildContainer(PicoContainer)
	 */
	public void dispose() {
		if (disposed)
			throw new IllegalStateException("Already disposed");

		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			PicoContainer child = (PicoContainer) iterator.next();
			child.dispose();
		}
		this.lifecycleManager.dispose(this);
		disposed = true;
	}

	public MutablePicoContainer makeChildContainer() {
		DefaultPicoContainer pc = new DefaultPicoContainer(componentAdapterFactory, this);
		addChildContainer(pc);
		return pc;
	}

	public boolean addChildContainer(PicoContainer child) {
		if (children.add(child)) {
			// @todo Should only be added if child container has also be started
			if (started) {
				childrenStarted.add(new Integer(child.hashCode()));
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean removeChildContainer(PicoContainer child) {
		final boolean result = children.remove(child);
		return result;
	}

	public void accept(PicoVisitor visitor) {
		visitor.visitContainer(this);
		final List componentAdapters = new ArrayList(getComponentAdapters());
		for (Iterator iterator = componentAdapters.iterator(); iterator.hasNext();) {
			ComponentAdapter componentAdapter = (ComponentAdapter) iterator.next();
			componentAdapter.accept(visitor);
		}
		final List allChildren = new ArrayList(children);
		for (Iterator iterator = allChildren.iterator(); iterator.hasNext();) {
			PicoContainer child = (PicoContainer) iterator.next();
			child.accept(visitor);
		}
	}

	/**
	 * Changes monitor in the ComponentAdapterFactory, the component adapters
	 * and the child containers, if these support a ComponentMonitorStrategy.
	 * {@inheritDoc}
	 */
	public void changeMonitor(ComponentMonitor monitor) {
		// will also change monitor in lifecycleStrategyForInstanceRegistrations
		if (componentAdapterFactory instanceof ComponentMonitorStrategy) {
			((ComponentMonitorStrategy) componentAdapterFactory).changeMonitor(monitor);
		}
		for (Iterator i = componentAdapters.iterator(); i.hasNext();) {
			Object adapter = i.next();
			if (adapter instanceof ComponentMonitorStrategy) {
				((ComponentMonitorStrategy) adapter).changeMonitor(monitor);
			}
		}
		for (Iterator i = children.iterator(); i.hasNext();) {
			Object child = i.next();
			if (child instanceof ComponentMonitorStrategy) {
				((ComponentMonitorStrategy) child).changeMonitor(monitor);
			}
		}
	}

	/**
	 * Returns the first current monitor found in the ComponentAdapterFactory,
	 * the component adapters and the child containers, if these support a
	 * ComponentMonitorStrategy. {@inheritDoc}
	 * 
	 * @throws PicoIntrospectionException
	 *             if no component monitor is found in container or its children
	 */
	public ComponentMonitor currentMonitor() {
		if (componentAdapterFactory instanceof ComponentMonitorStrategy) {
			return ((ComponentMonitorStrategy) componentAdapterFactory).currentMonitor();
		}
		for (Iterator i = componentAdapters.iterator(); i.hasNext();) {
			Object adapter = i.next();
			if (adapter instanceof ComponentMonitorStrategy) {
				return ((ComponentMonitorStrategy) adapter).currentMonitor();
			}
		}
		for (Iterator i = children.iterator(); i.hasNext();) {
			Object child = i.next();
			if (child instanceof ComponentMonitorStrategy) {
				return ((ComponentMonitorStrategy) child).currentMonitor();
			}
		}
		throw new PicoIntrospectionException("No component monitor found in container or its children");
	}

	/**
	 * <p>
	 * Implementation of lifecycle manager which delegates to the container's
	 * component adapters. The component adapters will be ordered by dependency
	 * as registered in the container. This LifecycleManager will delegate calls
	 * on the lifecycle methods to the component adapters if these are
	 * themselves LifecycleManagers.
	 * </p>
	 * 
	 * @author Mauro Talevi
	 * @since 1.2
	 */
	private class OrderedComponentAdapterLifecycleManager implements LifecycleManager, Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/** List collecting the CAs which have been successfully started */
		private List startedComponentAdapters = new ArrayList();

		/**
		 * {@inheritDoc} Loops over all component adapters and invokes
		 * start(PicoContainer) method on the ones which are LifecycleManagers
		 */
		public void start(PicoContainer node) {
			Collection adapters = getComponentAdapters();
			for (final Iterator iter = adapters.iterator(); iter.hasNext();) {
				final ComponentAdapter adapter = (ComponentAdapter) iter.next();
				if (adapter instanceof LifecycleManager) {
					LifecycleManager manager = (LifecycleManager) adapter;
					if (manager.hasLifecycle()) {
						// create an instance, it will be added to the ordered
						// CA list
						adapter.getComponentInstance(node);
						addOrderedComponentAdapter(adapter);
					}
				}
			}
			adapters = orderedComponentAdapters;
			// clear list of started CAs
			startedComponentAdapters.clear();
			for (final Iterator iter = adapters.iterator(); iter.hasNext();) {
				final Object adapter = iter.next();
				if (adapter instanceof LifecycleManager) {
					LifecycleManager manager = (LifecycleManager) adapter;
					manager.start(node);
					startedComponentAdapters.add(adapter);
				}
			}
		}

		/**
		 * {@inheritDoc} Loops over started component adapters (in inverse
		 * order) and invokes stop(PicoContainer) method on the ones which are
		 * LifecycleManagers
		 */
		public void stop(PicoContainer node) {
			List adapters = startedComponentAdapters;
			for (int i = adapters.size() - 1; 0 <= i; i--) {
				Object adapter = adapters.get(i);
				if (adapter instanceof LifecycleManager) {
					LifecycleManager manager = (LifecycleManager) adapter;
					manager.stop(node);
				}
			}
		}

		/**
		 * {@inheritDoc} Loops over all component adapters (in inverse order)
		 * and invokes dispose(PicoContainer) method on the ones which are
		 * LifecycleManagers
		 */
		public void dispose(PicoContainer node) {
			List adapters = orderedComponentAdapters;
			for (int i = adapters.size() - 1; 0 <= i; i--) {
				Object adapter = adapters.get(i);
				if (adapter instanceof LifecycleManager) {
					LifecycleManager manager = (LifecycleManager) adapter;
					manager.dispose(node);
				}
			}
		}

		public boolean hasLifecycle() {
			throw new UnsupportedOperationException("Should not have been called");
		}

	}

}