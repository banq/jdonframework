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
package com.jdon.bussinessproxy.dyncproxy.cglib;

import net.sf.cglib.proxy.Enhancer;

import com.jdon.aop.AopClient;
import com.jdon.bussinessproxy.dyncproxy.ProxyInstanceFactoryVisitable;
import com.jdon.container.access.TargetMetaRequest;
import com.jdon.container.access.TargetMetaRequestsHolder;
import com.jdon.util.Debug;

/**
 * http://www.jdon.com/jivejdon/thread/37330
 */
public class CGLIBProxyInstanceFactoryVisitable extends ProxyInstanceFactoryVisitable {
	private final static String module = CGLIBProxyInstanceFactoryVisitable.class.getName();
	private final AopClient aopClient;
	private final TargetMetaRequestsHolder targetMetaRequestsHolder;

	/**
	 * @param aopClient
	 */
	public CGLIBProxyInstanceFactoryVisitable(AopClient aopClient, TargetMetaRequestsHolder targetMetaRequestsHolder) {
		super(aopClient, targetMetaRequestsHolder);
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
			TargetMetaRequest targetMetaRequest = targetMetaRequestsHolder.getTargetMetaRequest();

			Enhancer enhancer = new Enhancer();
			enhancer.setCallback(new CGLIBDynamicProxyWeaving(targetMetaRequest, aopClient));
			enhancer.setInterfaces(getInterfaces(targetMetaRequest.getTargetMetaDef()));
			dynamicProxy = enhancer.create();
		} catch (Exception ex) {
			Debug.logError("[JdonFramework] Proxy.newProxyInstance error:" + ex, module);
		} catch (Throwable ex) {
			Debug.logError("[JdonFramework] Proxy.newProxyInstance error:" + ex, module);
		}
		return dynamicProxy;
	}

}
