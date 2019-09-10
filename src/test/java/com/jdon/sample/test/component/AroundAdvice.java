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
package com.jdon.sample.test.component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.jdon.annotation.Interceptor;
import com.jdon.annotation.pointcut.Around;

// aroundAdvice is called by D annotation Introduce("aroundAdvice")
// if there is no any component call this MethodInterceptor, the MethodInterceptor
// will action for all components called by client (by AppUtil.getService or WebApp.getService)
@Interceptor("aroundAdvice")
public class AroundAdvice implements MethodInterceptor {
	private Map<Class, Boolean> adviceArounds = new HashMap();

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		System.out.print("\n\n this is AroundAdvice  before \n\n");

		if (isAdviceAround(invocation.getThis().getClass()))
			System.out.print("\n\n this is AroundAdvice  myMethod3 \n\n");

		Object o = invocation.proceed();
		int result = 0;
		if (o instanceof Integer) {
			result = (Integer) o + 1;
		}
		System.out.print("\n\n this is AroundAdvice after \n\n");
		return result;
	}

	public boolean isAdviceAround(Class targetClass) {
		Boolean isa = adviceArounds.get(targetClass);
		if (isa != null) {
			return isa.booleanValue();
		}
		Method[] ms = targetClass.getMethods();
		for (int i = 0; i < ms.length; i++) {
			if (ms[i].isAnnotationPresent(Around.class)) {
				adviceArounds.put(targetClass, true);
				isa = true;
				break;
			}
		}
		return isa;

	}

}
