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

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.jdon.annotation.Interceptor;

//@Interceptor("myInterceptor") // for all 
@Interceptor(name = "myInterceptor", pointcut = "event,c")
public class MyInterceptor implements MethodInterceptor {

	public Object invoke(MethodInvocation methodInvocation) throws java.lang.Throwable {
		System.out.print("\n\n this is MyInterceptor  before \n\n");
		if (methodInvocation.getMethod().getName().equals("myMethod")) {
			System.out.print("\n\n this is MyInterceptor for event.myMethod  \n\n");
			if (methodInvocation.getArguments()[0] != null) {
				int i = (Integer) methodInvocation.getArguments()[0];
				i++;
				System.out.print(i);
			}

		}
		Object o = methodInvocation.proceed();
		int result = 0;
		if (o instanceof Integer) {
			result = (Integer) o + 1;
		}
		System.out.print("\n\n this is MyInterceptor after \n\n");
		return result;
	}

}
