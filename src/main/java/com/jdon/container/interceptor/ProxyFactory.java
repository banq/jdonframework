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
package com.jdon.container.interceptor;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import com.jdon.util.Debug;

public class ProxyFactory {
	private final static String module = ProxyFactory.class.getName();

	public Object createProxy(MethodInterceptor methodInterceptor, Object target, Class[] interfaces) {
		Debug.logVerbose("[JdonFramework]enter Proxy.newProxyInstance ", module);
		Object dynamicProxy = null;
		try {
			Enhancer enhancer = new Enhancer();
			enhancer.setCallback(methodInterceptor);
			enhancer.setInterfaces(interfaces);
			dynamicProxy = enhancer.create();
		} catch (Exception ex) {
			Debug.logError("[JdonFramework] Proxy.newProxyInstance error:" + ex, module);
		} catch (Throwable ex) {
			Debug.logError("[JdonFramework] Proxy.newProxyInstance error:" + ex, module);
		}
		return dynamicProxy;
	}

}
