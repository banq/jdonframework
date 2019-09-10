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
package com.jdon.bussinessproxy.dyncproxy;

import java.lang.reflect.Proxy;

import com.jdon.aop.AopClient;
import com.jdon.bussinessproxy.TargetMetaDef;
import com.jdon.container.access.TargetMetaRequest;
import com.jdon.container.access.TargetMetaRequestsHolder;
import com.jdon.container.visitor.Visitable;
import com.jdon.container.visitor.http.HttpSessionProxyComponentVisitor;
import com.jdon.util.ClassUtil;
import com.jdon.util.Debug;

/**
 * by using Proxy.newProxyInstance, create event DynamicProxyWeaving for event target
 * service.
 * 
 * @todo: create event DynamicProxyWeaving for all services.
 * 
 * @see HttpSessionProxyComponentVisitor HttpSessionVisitorFactoryImp
 * @see com.jdon.container.finder.ComponentKeys.PROXYINSTANCE_FACTORY
 * 
 *      {@link com.jdon.container.access.ServiceAccessorImp#getService(TargetMetaRequest)}
 * 
 * 
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 * 
 */
public class ProxyInstanceFactoryVisitable implements Visitable {
	private final static String module = ProxyInstanceFactoryVisitable.class.getName();
	private final AopClient aopClient;
	private final TargetMetaRequestsHolder targetMetaRequestsHolder;

	/**
	 * @param aopClient
	 */
	public ProxyInstanceFactoryVisitable(AopClient aopClient, TargetMetaRequestsHolder targetMetaRequestsHolder) {
		super();
		this.aopClient = aopClient;
		this.targetMetaRequestsHolder = targetMetaRequestsHolder;
	}

	/**
     * 
     */
	public Object accept() {
		Debug.logVerbose("[JdonFramework]enter Proxy.newProxyInstance ", module);
		Object dynamicProxy = null;
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

			TargetMetaRequest targetMetaRequest = targetMetaRequestsHolder.getTargetMetaRequest();

			DynamicProxyWeaving dynamicProxyWeaving = new DynamicProxyWeaving(targetMetaRequest, aopClient);
			dynamicProxy = Proxy.newProxyInstance(classLoader, getInterfaces(targetMetaRequest.getTargetMetaDef()), dynamicProxyWeaving);
		} catch (Exception ex) {
			Debug.logError("[JdonFramework] Proxy.newProxyInstance error:" + ex, module);
		} catch (Throwable ex) {
			Debug.logError("[JdonFramework] Proxy.newProxyInstance error:" + ex, module);
		}
		return dynamicProxy;
	}

	/**
	 * get the interface of target class if it is EJB, it is ejb local/remote
	 * interface if it is pojo, it is event class .
	 * 
	 * 
	 * @param targetMetaDef
	 * @return
	 */
	protected Class[] getInterfaces(TargetMetaDef targetMetaDef) {
		Class[] interfaces = targetMetaDef.getInterfaces();
		if (interfaces != null)
			return interfaces;
		try {
			interfaces = getPOJOInterfaces(targetMetaDef);
		} catch (Exception ex) {
			Debug.logError("[JdonFramework] getInterfaces error:" + ex, module);
		} catch (Throwable ex) {
			Debug.logError("[JdonFramework] getInterfaces error:" + ex, module);
		}
		if ((interfaces == null) || (interfaces.length == 0)) {
			Debug.logError("[JdonFramework] no find any interface for the service:" + targetMetaDef.getClassName(), module);
		} else {
			targetMetaDef.setInterfaces(interfaces); // cache the result
			Debug.logVerbose("[JdonFramework]found the the below interfaces for the service:" + targetMetaDef.getClassName(), module);
			for (int i = 0; i < interfaces.length; i++) {
				Debug.logVerbose(interfaces[i].getName() + ";", module);
			}
		}
		return interfaces;
	}

	public Class[] getPOJOInterfaces(TargetMetaDef targetMetaDef) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Class pojoClass = null;
		try {
			pojoClass = classLoader.loadClass(targetMetaDef.getClassName());
		} catch (ClassNotFoundException e) {
			Debug.logError("[JdonFramework] getPOJOInterfaces error:" + e, module);
		}
		return ClassUtil.getParentAllInterfaces(pojoClass);
	}

}
