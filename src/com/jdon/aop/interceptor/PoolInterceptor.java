/*
 * Copyright 2003-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.jdon.aop.reflection.ProxyMethodInvocation;
import com.jdon.bussinessproxy.TargetMetaDef;
import com.jdon.bussinessproxy.target.TargetServiceFactory;
import com.jdon.container.ContainerWrapper;
import com.jdon.container.access.TargetMetaRequestsHolder;
import com.jdon.container.finder.ComponentKeys;
import com.jdon.container.finder.ContainerCallback;
import com.jdon.controller.cache.InstanceCache;
import com.jdon.controller.pool.CommonsPoolFactory;
import com.jdon.controller.pool.Pool;
import com.jdon.controller.pool.PoolConfigure;
import com.jdon.controller.pool.Poolable;
import com.jdon.util.Debug;

/**
 * PoolInterceptor must be the last in Interceptors.
 * this class is active for the pojoServices that 
 * implements com.jdon.controller.pool.Poolable.
 * 
 * @author <a href="mailto:banqiao@jdon.com">banq </a>
 *  
 */
public class PoolInterceptor implements MethodInterceptor {
	private final static String module = PoolInterceptor.class.getName();

	/**
	 * one target object, one pool
	 */
	private final Map poolFactorys;

	private final TargetServiceFactory targetServiceFactory;

	private final ContainerCallback containerCallback;

	private final PoolConfigure poolConfigure;

	private final List isPoolableCache = new ArrayList();

	private final List unPoolableCache = new ArrayList();

	private final TargetMetaRequestsHolder targetMetaRequestsHolder;

	/**
	 * @param containerCallback
	 */
	public PoolInterceptor(TargetServiceFactory targetServiceFactory, TargetMetaRequestsHolder targetMetaRequestsHolder,
			ContainerCallback containerCallback, PoolConfigure poolConfigure) {
		super();
		this.targetServiceFactory = targetServiceFactory;
		this.targetMetaRequestsHolder = targetMetaRequestsHolder;
		this.containerCallback = containerCallback;
		this.poolConfigure = poolConfigure;
		this.poolFactorys = new HashMap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		ProxyMethodInvocation proxyMethodInvocation = (ProxyMethodInvocation) invocation;
		TargetMetaDef targetMetaDef = targetMetaRequestsHolder.getTargetMetaRequest().getTargetMetaDef();
		if (targetMetaDef.isEJB())
			return invocation.proceed();

		if (!isPoolabe(targetMetaDef)) {
			//Debug.logVerbose("[JdonFramework] target service is not Poolable: "
			//        + targetMetaDef.getClassName() + " pool unactiive", module);
			return invocation.proceed(); //下一个interceptor
		}
		Debug.logVerbose("[JdonFramework] enter PoolInterceptor", module);
		CommonsPoolFactory commonsPoolFactory = (CommonsPoolFactory) poolFactorys.get(targetMetaDef.getCacheKey());
		if (commonsPoolFactory == null) {
			commonsPoolFactory = getCommonsPoolFactory(targetMetaDef);
			poolFactorys.put(targetMetaDef.getCacheKey(), commonsPoolFactory);
		}

		Pool pool = commonsPoolFactory.getPool();
		Object poa = null;
		Object result = null;
		try {
			poa = pool.acquirePoolable();
			Debug.logVerbose("[JdonFramework] borrow a object:" + targetMetaDef.getClassName() + " id:" + poa.hashCode()
					+ " from pool", module);
			Debug.logVerbose("[JdonFramework]pool state: active=" + pool.getNumActive() + " free=" + pool.getNumIdle(), module);

			//set the object that borrowed from pool to MethodInvocation
			//so later other Interceptors or MethodInvocation can use it!
			proxyMethodInvocation.setThis(poa);
			result = invocation.proceed();
		} catch (Exception ex) {
			Debug.logError(ex, module);
		} finally {
			if (poa != null) {
				pool.releasePoolable(poa);
				Debug.logVerbose("[JdonFramework] realease a object:" + targetMetaDef.getClassName() + " to pool", module);
			}
		}
		return result;
	}

	/**
	 * every target service has its CommonsPoolFactory, we cache it in
	 * container, next time, we can get the cached Object Pool for the target
	 * service;
	 * 
	 * @param targetMetaDef
	 * @return
	 */
	private CommonsPoolFactory getCommonsPoolFactory(TargetMetaDef targetMetaDef) {
		CommonsPoolFactory commonsPoolFactory = null;
		try {
			ContainerWrapper containerWrapper = containerCallback.getContainerWrapper();
			InstanceCache instanceCache = (InstanceCache) containerWrapper.lookup(ComponentKeys.INSTANCE_CACHE);

			String key = targetMetaDef.getCacheKey() + " CommonsPoolFactory";

			commonsPoolFactory = (CommonsPoolFactory) instanceCache.get(key);
			if (commonsPoolFactory == null) {
				Debug.logVerbose("[JdonFramework] first time call commonsPoolFactory， create it:" + key, module);
				commonsPoolFactory = new CommonsPoolFactory(targetServiceFactory, poolConfigure.getMaxPoolSize());
				instanceCache.put(key, commonsPoolFactory);
			}

		} catch (Exception ex) {
			Debug.logError(ex, module);
		}
		return commonsPoolFactory;
	}

	public boolean isPoolabe(TargetMetaDef targetMetaDef) {
		boolean found = false;
		if (isPoolableCache.contains(targetMetaDef.getName())) {
			found = true;
		} else if (!unPoolableCache.contains(targetMetaDef.getName())) {
			Debug.logVerbose("[JdonFramework] check if it is a Poolable", module);
			ContainerWrapper containerWrapper = containerCallback.getContainerWrapper();
			Class thisCLass = containerWrapper.getComponentClass(targetMetaDef.getName());
			if (Poolable.class.isAssignableFrom(thisCLass) ||
				thisCLass.isAnnotationPresent(com.jdon.annotation.intercept.Poolable.class)) {
				found = true;
				isPoolableCache.add(targetMetaDef.getName());
			} else {
				unPoolableCache.add(targetMetaDef.getName());
			}
		}
		return found;
	}

}
