/**
 * Copyright 2003-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jdon.bussinessproxy.dyncproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.jdon.aop.AopClient;
import com.jdon.container.access.TargetMetaRequest;
import com.jdon.util.Debug;

/**
 * Dynamic Proxy Weaving mode Weaving implemention is dynamic proxy Every target
 * service object has its DynamicProxyWeaving object
 * 
 * 动态代理类 可实现AOP拦截
 * 
 * @author banq
 */

public class DynamicProxyWeaving implements InvocationHandler, java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4915858712286729975L;

	private final static String module = DynamicProxyWeaving.class.getName();

	private final AopClient aopClient;

	private final TargetMetaRequest targetMetaRequest;

	public DynamicProxyWeaving(TargetMetaRequest targetMetaRequest, AopClient aopClient) {
		this.aopClient = aopClient;
		this.targetMetaRequest = targetMetaRequest;
	}

	/**
	 * 方法调用 需要拦截方法在这里实现。目前实现arround intercept
	 * 
	 * @param p_proxy
	 *            Object
	 * @param m
	 *            Method
	 * @param args
	 *            Object[]
	 * @throws Throwable
	 * @return Object
	 */
	public Object invoke(Object p_proxy, Method m, Object[] args) throws Throwable {
		Debug.logVerbose("<################################>Action: JdonFramework core entrance", module);
		Debug.logVerbose("[JdonFramework]<################>execute method=" + m.getDeclaringClass().getName() + "." + m.getName(), module);
		Object result = null;
		try {
			result = aopClient.invoke(targetMetaRequest, m, args);
			Debug.logVerbose("[JdonFramework]<################>finish executing method=" + m.getDeclaringClass().getName() + "." + m.getName()
					+ " successfully!", module);
			Debug.logVerbose("<################################><end:", module);
		} catch (Exception ex) {
			Debug.logError(ex, module);
		} catch (Throwable ex) {
			throw new Throwable(ex);
		}

		return result;

	}

}
