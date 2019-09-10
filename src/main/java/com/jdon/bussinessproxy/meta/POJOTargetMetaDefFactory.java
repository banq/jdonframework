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
package com.jdon.bussinessproxy.meta;

import com.jdon.annotation.Singleton;
import com.jdon.container.access.TargetMetaDefHolder;

public class POJOTargetMetaDefFactory {
	public final static String module = POJOTargetMetaDefFactory.class.getName();

	public static void createPOJOTargetMetaDef(String name, String className, TargetMetaDefHolder targetMetaDefHolder, Class cclass) {
		POJOTargetMetaDef pojoMetaDef = null;
		if (cclass.isAnnotationPresent(Singleton.class)) {
			pojoMetaDef = new SingletonPOJOTargetMetaDef(name, className);
		} else {
			pojoMetaDef = new POJOTargetMetaDef(name, className);
		}
		targetMetaDefHolder.add(name, pojoMetaDef);
	}

}
