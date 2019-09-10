/**
 * Copyright 2003-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain event copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jdon.aop.interceptor;

import com.jdon.container.pico.Startable;
import com.jdon.controller.model.ModelUtil;
import com.jdon.domain.model.cache.ModelKey;
import com.jdon.domain.model.cache.ModelManager;
import com.jdon.util.Debug;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * Cache Interceptor all Interceptors are added in picoContainer
 *
 * method match can be done by this class, CacheInterceptor only interceptor the
 * method getXXXXX.
 *
 *
 *
 *      <p>
 *      </p>
 * @author banq
 */
public class CacheInterceptor implements MethodInterceptor, Startable {
	private final static String module = CacheInterceptor.class.getName();
	public String match_MethodName = "get";
	private ModelManager modelManager;
	private Set isModelCache = new HashSet();

	public CacheInterceptor(ModelManager modelManager) {
		this.modelManager = modelManager;
	}

	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();

		if (!methodMatchsModelGET(method)) {
			// Debug.logVerbose("[JdonFramework] cacheInteceptor don't action, enter next
			// invocation.proceed()",
			// module);
			return invocation.proceed(); // 下一个interceptor
		}
		Debug.logVerbose("[JdonFramework] enter cacheInteceptor method:"
				+ method.getName(), module);
		Class modelClass = method.getReturnType();
		String dataKey = getArguments(invocation);
		if (dataKey == null || modelClass == null)
			return invocation.proceed();
		try {
			ModelKey modelKey = new ModelKey(dataKey, modelClass);
			return getModelofCache(modelKey, invocation);
		} catch (Exception e) {
			Debug.logError("[JdonFramework]CacheInterceptor Exception error:"
					+ e + " method:" + method.getName() + " return class:"
					+ modelClass + " dataKey:" + dataKey, module);
		}
		return invocation.proceed();
	}

	public Object getModelofCache(ModelKey modelKey, MethodInvocation invocation)
			throws Throwable {
		Object existmodel = modelManager.getCache(modelKey);
		Object newmodel = null;
		if (existmodel == null) {
			newmodel = invocation.proceed(); // 下一个interceptor
			if (newmodel != null
					&& invocation.getMethod().getReturnType()
					.isAssignableFrom(newmodel.getClass())) {
				Debug.logVerbose("[JdonFramework] save to cache", module);
				existmodel = modelManager.addCache(modelKey, newmodel);
			}
		}
		return existmodel != null ? existmodel : newmodel;
	}

	/**
	 * 1.check return type if is Model 2.check method name if include "get" 3.
	 * if found them, cache this method
	 *
	 *
	 * @param method
	 *            Method
	 * @return boolean
	 */
	private boolean methodMatchsModelGET(Method method) {
		boolean condition = false;
		try {
			if (isModelCache.contains(method)) {
				condition = true;
				return condition;
			}

			String mehtodName = method.getName();
			if (method.getReturnType() == null)
				return condition; // 无返回值，不做缓存
			Class returnClass = method.getReturnType();
			if (returnClass.getSuperclass() == null)
				return condition; // 无返回值，不做缓存

			Debug.logVerbose(
					"[JdonFramework]methodMatchsModelGET: returnClassName = "
							+ returnClass.getName(), module);
			if (ModelUtil.isModel(returnClass)) {
				if (mehtodName.indexOf(match_MethodName) != -1) {
					condition = true;
					// method name include "getXXXX" and return Type is subClass
					// of Model
					isModelCache.add(method);
				}
			}
		} catch (Exception ex) {
			Debug.logError("[JdonFramework]Exception error:" + ex, module);
		} catch (Throwable the) {
			Debug.logError("[JdonFramework]Throwable error:" + the, module);
		}
		return condition;

	}

	/**
	 * 组合参数数值为一个字符串 这些参数必须实现toString();
	 *
	 * @param invocation
	 *            MethodInvocation
	 * @return String
	 */
	public String getArguments(MethodInvocation invocation) {

		try {
			Object[] args = invocation.getArguments();
			if (args == null)
				return null;
			if (args.length != 1)
				return null;// 参数是一个主键

			if (args[0] == null)
				return null;
			return args[0].toString();
		} catch (Exception ex) {
			Debug.logError("[JdonFramework] method:"
					+ invocation.getMethod().getName() + "  " + ex, module);
			return null;
		}

	}

	public String getMatch_MethodName() {
		return match_MethodName;
	}


	public void start() {
		// TODO Auto-generated method stub

	}

	public void stop() {
		this.isModelCache.clear();
		this.isModelCache = null;
		this.modelManager = null;

	}

}
