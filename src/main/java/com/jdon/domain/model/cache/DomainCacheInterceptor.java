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
package com.jdon.domain.model.cache;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.jdon.annotation.pointcut.Around;
import com.jdon.container.pico.Startable;
import com.jdon.controller.model.ModelUtil;
import com.jdon.util.Debug;

/**
 * aspect.xml : <interceptor name="modelCache"
 * class="com.jdon.domain.model.cache.DomainCacheInterceptor" pointcut="domain"
 * />
 * 
 * in Repository/DAO class add:
 * 
 * @Introduce("modelCache") public Class ForumDaoSql{
 * 
 * @Around Forum getForum(Long id){ .... } ...}
 * 
 * 
 * @author BANQ
 * 
 */
// @Interceptor("modelCache")
public class DomainCacheInterceptor implements MethodInterceptor, Startable {
	public final static String module = DomainCacheInterceptor.class.getName();
	private Map<String, String> adviceArounds = new HashMap();

	private ModelManager modelManager;

	public DomainCacheInterceptor(ModelManager modelManager) {
		super();
		this.modelManager = modelManager;
	}

	public Object invoke(MethodInvocation invocation) throws Throwable {
		if (invocation.getThis() == null)
			return invocation.proceed();
		if (!isAdviceAround(invocation.getThis().getClass(), invocation.getMethod()))
			return invocation.proceed();

		ModelKey modelKey = null;
		try {
			Object[] args = invocation.getArguments();
			if (args == null || args.length == 0)
				return invocation.proceed();

			if (args[0] instanceof ModelKey) {
				modelKey = (ModelKey) args[0];
			} else {
				Class modelClass = invocation.getMethod().getReturnType();
				modelKey = new ModelKey(args[0], modelClass);
			}
			Debug.logVerbose("try to get model from cache, cacheKey=" + modelKey.toString(), module);
			Object oexist = modelManager.getCache(modelKey);
			if (oexist != null) {
				return oexist;
			}

			Object onew = invocation.proceed();
			if (onew == null)
				return onew;
			Debug.logVerbose(" get model from database, cacheKey=" + modelKey.toString(), module);

			oexist = modelManager.addCache(modelKey, onew);
			Debug.logVerbose("[JdonFramework] save to cache2", module);
			return oexist != null ? oexist : onew;
		} catch (Exception e) {
			Debug.logError("invoke:" + e, module);
			return null;
		}
	}

	public boolean isAdviceAround(Class targetClass, Method methodx) {
		String methodName = adviceArounds.get(targetClass.getName());
		if (methodName != null && methodName.equals(methodx.getName())) {
			return true;
		}
		try {
			Method aroundMethod = targetClass.getMethod(methodx.getName(), methodx.getParameterTypes());
			if (aroundMethod == null)
				return false;

			if (!aroundMethod.isAnnotationPresent(Around.class))
				return false;

			if (aroundMethod.getReturnType() == null)
				return false; // 无返回值，不做缓存
			Class returnClass = aroundMethod.getReturnType();
			if (returnClass.getSuperclass() == null)
				return false; // 无返回值，不做缓存

			Debug.logVerbose("[JdonFramework]methodMatchsModelGET: returnClassName = " + returnClass.getName(), module);
			if (ModelUtil.isModel(returnClass)) {
				adviceArounds.put(targetClass.getName(), methodx.getName());
				return true;
			}

		} catch (Exception e) {
			Debug.logError("isAdviceAround:" + e, module);
		}
		return false;

	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		if (this.modelManager != null)
			this.modelManager.clearCache();
		this.modelManager = null;
		this.adviceArounds.clear();

	}
}
