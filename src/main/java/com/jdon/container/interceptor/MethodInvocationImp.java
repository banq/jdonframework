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

import com.jdon.util.Debug;
import net.sf.cglib.proxy.MethodProxy;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;

public class MethodInvocationImp implements MethodInvocation {
	private final static String module = MethodInvocationImp.class.getName();

	private final Method method;

	private final Object[] args;

	protected final List interceptors;

	protected Object target;

	protected int currentInterceptorInt = -1;

	protected BeforeAfterMethodTarget beforeAfterMethodTarget;

	protected MethodProxy methodProxy;

	public MethodInvocationImp(Object target, BeforeAfterMethodTarget beforeAfterMethodTarget, List interceptors, Method method, Object[] args,
			MethodProxy methodProxy) {
		this.interceptors = interceptors;
		this.method = method;
		this.args = args;
		this.beforeAfterMethodTarget = beforeAfterMethodTarget;
		this.methodProxy = methodProxy;
		this.target = target;
	}

	/**
	 * Invokes next interceptor/proxy target. now there is no mixin
	 */
	public Object proceed() throws Throwable, Exception {
		if (currentInterceptorInt == interceptors.size() - 1) {
			Debug.logVerbose("[JdonFramework] finish call all inteceptors", module);
			return beforeAfterMethodTarget.invoke(method, args, methodProxy);
		}

		Object interceptor = interceptors.get(++currentInterceptorInt);
		if (interceptor != null) {
			MethodInterceptor methodInterceptor = (MethodInterceptor) interceptor;
			return methodInterceptor.invoke(this);
		} else {
			Debug.logVerbose("[JdonFramework] null finish call all inteceptors", module);
			return beforeAfterMethodTarget.invoke(method, args, methodProxy);
		}
	}

	public Object[] getArguments() {
		return this.args;
	}

	public Object getThis() {
		return target;
	}

	public AccessibleObject getStaticPart() {
		return null;
	}

	public Method getMethod() {
		return method;
	}
}
