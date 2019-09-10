/**
 * Copyright 2003-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain event copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jdon.bussinessproxy.target;

import com.jdon.bussinessproxy.TargetMetaDef;
import com.jdon.container.access.TargetMetaRequestsHolder;
import com.jdon.container.finder.ContainerCallback;
import com.jdon.util.Debug;

/**
 * Factory that create target service object
 * 
 * 
 * @author banq
 */

public class DefaultTargetServiceFactory implements TargetServiceFactory {

	private final static String module = DefaultTargetServiceFactory.class.getName();

	private final TargetMetaRequestsHolder targetMetaRequestsHolder;
	private final ContainerCallback containerCallback;

	public DefaultTargetServiceFactory(ContainerCallback containerCallback, TargetMetaRequestsHolder targetMetaRequestsHolder) {
		this.targetMetaRequestsHolder = targetMetaRequestsHolder;
		this.containerCallback = containerCallback;
	}

	public Object create() {
		Object o = null;
		TargetMetaDef targetMetaDef = null;
		try {
			targetMetaDef = targetMetaRequestsHolder.getTargetMetaRequest().getTargetMetaDef();
			TargetObjectFactory targetObjectFactory = targetMetaDef.getTargetObjectFactory();
			o = targetObjectFactory.create(containerCallback);
		} catch (Exception ex) {
			Debug.logError("[JdonFramework]create error: " + ex + " " + targetMetaDef.getClassName(), module);
		} finally {
		}
		return o;
	}

	public Object destroy() {
		targetMetaRequestsHolder.setTargetMetaRequest(null);
		return null;
	}

}
