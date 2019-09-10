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
package com.jdon.container;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * all registered component name in container.
 * 
 * @author banq
 * 
 */
public class RegistryDirectory implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7847181646670354817L;
	private final Map<Class, String> dirs = new HashMap();

	public void addComponentName(Class cClass, String name) {
		dirs.put(cClass, name);
	}

	public Collection<String> getComponentNames() {
		return Collections.synchronizedCollection(dirs.values());
	}

	public Collection<Class> getComponentClasses() {
		return Collections.synchronizedSet(dirs.keySet());
	}

	public String findComponentName(Class cClass) {
		return dirs.get(cClass);
	}

}
