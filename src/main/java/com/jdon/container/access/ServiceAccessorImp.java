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
package com.jdon.container.access;

import com.jdon.aop.AopClient;
import com.jdon.container.ContainerWrapper;
import com.jdon.container.finder.ComponentKeys;
import com.jdon.container.visitor.ComponentVisitor;
import com.jdon.util.Debug;

/**
 * 
 * @see com.jdon.bussinessproxy.dyncproxy.ProxyInstanceFactoryVisitable
 * 
 * @author <event href="mailto:banqJdon<AT>jdon.com">banq</event>
 * 
 */

public class ServiceAccessorImp implements ServiceAccessor {
	private final static String module = ServiceAccessorImp.class.getName();
	private final TargetMetaRequestsHolder targetMetaRequestsHolder;

	public ServiceAccessorImp(TargetMetaRequestsHolder targetMetaRequestsHolder) {
		this.targetMetaRequestsHolder = targetMetaRequestsHolder;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jdon.container.access.ServiceAccessor#getService(com.jdon.container.access.UserTargetMetaDef)
	 * 
	 */
	public Object getService() {
		Debug.logVerbose("[JdonFramework] enter getService: " + ComponentKeys.PROXYINSTANCE_FACTORY + " in action", module);
		TargetMetaRequest targetMetaRequest = targetMetaRequestsHolder.getTargetMetaRequest();

		targetMetaRequest.setVisitableName(ComponentKeys.PROXYINSTANCE_FACTORY);
		ComponentVisitor componentVisitor = targetMetaRequest.getComponentVisitor();
		return componentVisitor.visit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jdon.container.access.ServiceAccessor#executeService(com.jdon.container.access.UserTargetMetaDef)
	 */
	public Object executeService(ContainerWrapper cw) throws Exception {
		Debug.logVerbose("[JdonFramework]enter service execution core ", module);
		Object result = null;
		try {
			AopClient aopClient = (AopClient) cw.lookup(ComponentKeys.AOP_CLIENT);
			result = aopClient.invoke();
		} catch (Exception ex) {
			Debug.logError("[JdonFramework] execute the service error: " + ex, module);
			throw new Exception("execute service error: " + ex);
		} catch (Throwable ex) {
			Debug.logError("[JdonFramework]  execute Service error: " + ex, module);
			throw new Exception("  execute Service error: " + ex);
		}
		return result;
	}

}
