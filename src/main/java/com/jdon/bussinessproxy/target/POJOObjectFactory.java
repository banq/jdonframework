/*
 * Copyright 2003-2006 the original author or authors.
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
package com.jdon.bussinessproxy.target;

import com.jdon.bussinessproxy.meta.POJOTargetMetaDef;
import com.jdon.container.ContainerWrapper;
import com.jdon.container.finder.ContainerCallback;
import com.jdon.util.Debug;

/**
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 * 
 */
public class POJOObjectFactory implements TargetObjectFactory {
	private final static String module = POJOObjectFactory.class.getName();

	protected POJOTargetMetaDef pOJOTargetMetaDef;

	/**
	 * @param containerCallback
	 */
	public POJOObjectFactory(POJOTargetMetaDef pOJOTargetMetaDef) {
		super();
		this.pOJOTargetMetaDef = pOJOTargetMetaDef;
	}

	public Object create(ContainerCallback containerCallback) throws Exception {
		Object o = null;
		try {
			Debug.logVerbose("[JdonFramework] create new pojo Object for " + pOJOTargetMetaDef.getName(), module);
			ContainerWrapper containerWrapper = containerCallback.getContainerWrapper();
			o = containerWrapper.getComponentNewInstance(pOJOTargetMetaDef.getName());
			// Debug.logVerbose("[JdonFramework] create new pojo Object id " +
			// o.hashCode(), module);
		} catch (Exception ex) {
			Debug.logError("[JdonFramework]create error: " + ex + " name=" + pOJOTargetMetaDef.getName(), module);
			throw new Exception(ex);
		} catch (Throwable tw) {
			Debug.logError("[JdonFramework]create Throwable error: " + tw + " name=" + pOJOTargetMetaDef.getName(), module);
			throw new Exception(tw);
		}
		return o;
	}

}
