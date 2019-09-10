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

import java.util.HashSet;
import java.util.Set;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.jdon.aop.reflection.ProxyMethodInvocation;
import com.jdon.bussinessproxy.TargetMetaDef;
import com.jdon.container.ContainerWrapper;
import com.jdon.container.access.TargetMetaRequest;
import com.jdon.container.access.TargetMetaRequestsHolder;
import com.jdon.container.finder.ComponentKeys;
import com.jdon.container.finder.ContainerCallback;
import com.jdon.container.pico.Startable;
import com.jdon.container.visitor.ComponentVisitor;
import com.jdon.controller.service.Stateful;
import com.jdon.util.Debug;

/**
 * StatefulInterceptor is event Interceptor of creating target object. must be the
 * last in Interceptors.
 * 
 * it active for all pojoServices that implements
 * com.jdon.controller.service.StatefulPOJOService
 * 
 * this class stateful function is using ComponentVisitor's cache, such as
 * HttpSession.
 * 
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 * 
 */
public class StatefulInterceptor implements MethodInterceptor, Startable {
	private final static String module = StatefulInterceptor.class.getName();

	private final Set isStatefulCache = new HashSet();

	private final Set unStatefulCache = new HashSet();

	private final ContainerCallback containerCallback;

	private final TargetMetaRequestsHolder targetMetaRequestsHolder;

	/**
	 * @param containerCallback
	 */
	public StatefulInterceptor(ContainerCallback containerCallback, TargetMetaRequestsHolder targetMetaRequestsHolder) {
		super();
		this.containerCallback = containerCallback;
		this.targetMetaRequestsHolder = targetMetaRequestsHolder;
	}

	/*
	 * check the pojoService if implements StatefulPOJOService using
	 * ComponentVisitor, we can get targetObjRef from httpsession.
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

		if (!isStateful(targetMetaDef)) {
			// Debug.logVerbose("[JdonFramework] target service is not Stateful: "
			// + targetMetaDef.getClassName() +
			// " StatefulInterceptor unactiive", module);
			return invocation.proceed();
		}
		Debug.logVerbose("[JdonFramework] enter StatefulInterceptor", module);
		Object result = null;
		try {
			ComponentVisitor cm = targetMetaRequest.getComponentVisitor();
			targetMetaRequest.setVisitableName(ComponentKeys.TARGETSERVICE_FACTORY);
			Debug.logVerbose(ComponentKeys.TARGETSERVICE_FACTORY + " in action (Stateful)", module);
			Object targetObjRef = cm.visit();
			pmi.setThis(targetObjRef);
			result = invocation.proceed();
		} catch (Exception ex) {
			Debug.logError("[JdonFramework]StatefulInterceptor error: " + ex, module);
		}
		return result;
	}

	public boolean isStateful(TargetMetaDef targetMetaDef) {
		boolean found = false;
		if (isStatefulCache.contains(targetMetaDef.getName())) {
			found = true;
		} else if (!unStatefulCache.contains(targetMetaDef.getName())) {
			Debug.logVerbose("[JdonFramework] check if it is event isStateful", module);
			ContainerWrapper containerWrapper = containerCallback.getContainerWrapper();
			Class thisCLass = containerWrapper.getComponentClass(targetMetaDef.getName());
			if (Stateful.class.isAssignableFrom(thisCLass) || thisCLass.isAnnotationPresent(com.jdon.annotation.intercept.Stateful.class)) {
				found = true;
				isStatefulCache.add(targetMetaDef.getName());
			} else {
				unStatefulCache.add(targetMetaDef.getName());
			}
		}
		return found;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		this.isStatefulCache.clear();
		this.unStatefulCache.clear();

	}

}
