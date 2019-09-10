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
package com.jdon.aop.interceptor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.jdon.aop.reflection.ProxyMethodInvocation;
import com.jdon.bussinessproxy.TargetMetaDef;
import com.jdon.container.ContainerWrapper;
import com.jdon.container.access.TargetMetaRequest;
import com.jdon.container.access.TargetMetaRequestsHolder;
import com.jdon.container.finder.ContainerCallback;
import com.jdon.container.pico.Startable;
import com.jdon.container.visitor.data.SessionContext;
import com.jdon.container.visitor.data.SessionContextAcceptable;
import com.jdon.util.Debug;

/**
 * interceptor the SessionContextAcceptable concrete services, and inject the
 * SessionContext object into them, so these application service can got the
 * datas from session that saved by framework.
 * 
 * this interceptor must be after the targetservice creating interceptors such
 * as StatefulInterceptor or PoolInterceptor,
 * 
 * because this interceptor will inject SessionContext object into the existed
 * targetservice object.
 * 
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 * 
 */

public class SessionContextInterceptor implements MethodInterceptor, Startable {
	private final static String module = SessionContextInterceptor.class.getName();

	private final Set isSessionContextAcceptables = new HashSet();

	private final Map<String, Method> isSessionContextAcceptablesAnnotations = new HashMap();

	private final Set unSessionContextAcceptables = new HashSet();

	private final ContainerCallback containerCallback;

	private final TargetMetaRequestsHolder targetMetaRequestsHolder;

	public SessionContextInterceptor(ContainerCallback containerCallback, TargetMetaRequestsHolder targetMetaRequestsHolder) {
		this.containerCallback = containerCallback;
		this.targetMetaRequestsHolder = targetMetaRequestsHolder;
	}

	/*
	 * inject SessionContext into targetObject.
	 * 
	 * @see
	 * org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept
	 * .MethodInvocation)
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		ProxyMethodInvocation pmi = (ProxyMethodInvocation) invocation;
		TargetMetaRequest targetMetaRequest = targetMetaRequestsHolder.getTargetMetaRequest();
		TargetMetaDef targetMetaDef = targetMetaRequest.getTargetMetaDef();
		if (targetMetaDef.isEJB())
			return invocation.proceed();

		if (!isSessionContextAcceptable(targetMetaDef)) {
			// Debug.logVerbose("[JdonFramework] target service is not SessionContextAcceptable: "
			// + targetMetaDef.getClassName() +
			// " SessionContextInterceptor unactiive", module);
			return invocation.proceed();
		}
		Debug.logVerbose("[JdonFramework] enter SessionContextInterceptor", module);

		Object result = null;
		try {
			Object targetObject = pmi.getThis();
			if (targetObject == null) {
				throw new Exception("targetObject is null, add @Poolable and try again");
			}
			Debug.logVerbose("[JdonFramework] targetObject should be SessionContextAcceptable: " + targetObject.getClass().getName(), module);
			setSessionContext(targetObject, targetMetaRequest);

			result = invocation.proceed();

		} catch (Exception ex) {
			Debug.logError("[JdonFramework]SessionContextInterceptor error: " + ex, module);
		}
		return result;
	}

	public boolean isSessionContextAcceptable(TargetMetaDef targetMetaDef) {
		boolean found = false;
		if (isSessionContextAcceptables.contains(targetMetaDef.getName())
				|| isSessionContextAcceptablesAnnotations.containsKey(targetMetaDef.getName())) {
			found = true;
		} else if (!unSessionContextAcceptables.contains(targetMetaDef.getName())) {
			Debug.logVerbose("[JdonFramework] check if it is event isSessionContextAcceptable", module);
			ContainerWrapper containerWrapper = containerCallback.getContainerWrapper();
			Class thisCLass = containerWrapper.getComponentClass(targetMetaDef.getName());
			if (SessionContextAcceptable.class.isAssignableFrom(thisCLass)) {
				found = true;
				isSessionContextAcceptables.add(targetMetaDef.getName());
			} else {
				for (Method method : thisCLass.getMethods()) {
					if (method.isAnnotationPresent(com.jdon.annotation.intercept.SessionContextAcceptable.class)) {
						isSessionContextAcceptablesAnnotations.put(targetMetaDef.getName(), method);
						found = true;
					}
				}
			}
			if (!found)
				unSessionContextAcceptables.add(targetMetaDef.getName());
		}
		return found;
	}

	// WebServiceAccessorImp create sessionContext and save infomation into it
	private void setSessionContext(Object targetObject, TargetMetaRequest targetMetaRequest) {
		if (isSessionContextAcceptables.contains(targetMetaRequest.getTargetMetaDef().getName())) {
			SessionContextAcceptable myResult = (SessionContextAcceptable) targetObject;
			SessionContext sessionContext = targetMetaRequest.getSessionContext();
			myResult.setSessionContext(sessionContext);
		} else if (isSessionContextAcceptablesAnnotations.containsKey(targetMetaRequest.getTargetMetaDef().getName())) {
			Method method = isSessionContextAcceptablesAnnotations.get(targetMetaRequest.getTargetMetaDef().getName());
			try {
				Object[] sessionContexts = new SessionContext[1];
				sessionContexts[0] = targetMetaRequest.getSessionContext();
				method.invoke(targetObject, sessionContexts);
			} catch (Exception e) {
				Debug.logError("[JdonFramework]the target must has method setSessionContext(SessionContext sessionContext) : " + e, module);
			}

		}
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		this.isSessionContextAcceptables.clear();
		this.isSessionContextAcceptablesAnnotations.clear();
		this.unSessionContextAcceptables.clear();

	}

}
