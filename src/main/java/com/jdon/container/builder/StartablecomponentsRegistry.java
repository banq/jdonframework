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
package com.jdon.container.builder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.jdon.container.ContainerWrapper;
import com.jdon.container.pico.Startable;
import com.jdon.util.Debug;

/**
 * 
 * @author banq
 * @see com.jdon.container.pico.Startable
 */
public class StartablecomponentsRegistry {

	public final static String NAME = "startablecomponentsRegistry";

	protected final List<String> startablecomponentsRegistry;

	public StartablecomponentsRegistry() {
		startablecomponentsRegistry = new ArrayList();
	}

	public void add(Class classz, String name) {
		if (Startable.class.isAssignableFrom(classz))
			startablecomponentsRegistry.add(name);
	}

	public void startStartableComponents(ContainerWrapper containerWrapper) {
		try {
			Method sTART = com.jdon.container.pico.Startable.class.getMethod("start", new Class[0]);
			for (String name : startablecomponentsRegistry) {
				Object o = containerWrapper.lookup(name);
				try {
					if (o instanceof com.jdon.container.pico.Startable)
						sTART.invoke(o, new Object[0]);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	public void stopStartableComponents(ContainerWrapper containerWrapper) {
		try {
			Method sTOP = com.jdon.container.pico.Startable.class.getMethod("stop", new Class[0]);
			for (String name : startablecomponentsRegistry) {
				Object o = containerWrapper.lookup(name);
				try {
					if (o instanceof com.jdon.container.pico.Startable)
						sTOP.invoke(o, new Object[0]);
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			Debug.logError("stopStartableComponents error:" + e, NAME);
		} finally {
			startablecomponentsRegistry.clear();
		}
	}

}
