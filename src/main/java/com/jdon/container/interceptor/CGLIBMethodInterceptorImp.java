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

import com.jdon.container.pico.Startable;
import com.jdon.util.Debug;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.List;

public class CGLIBMethodInterceptorImp implements MethodInterceptor, Startable {
	private final static String module = CGLIBMethodInterceptorImp.class.getName();
	private List<org.aopalliance.intercept.MethodInterceptor> methodInterceptors;
	private BeforeAfterMethodTarget beforeAfterMethodTarget;
	private Object target;

	public CGLIBMethodInterceptorImp(Object target, Object interceptor, IntroduceInfo iinfo,
			List<org.aopalliance.intercept.MethodInterceptor> methodInterceptors) {
		super();
		this.methodInterceptors = methodInterceptors;
		this.beforeAfterMethodTarget = new BeforeAfterMethodTarget(target, interceptor, iinfo);
		this.target = target;
	}

	public Object intercept(Object object, Method invokedmethod, Object[] objects, MethodProxy methodProxy) throws Throwable {
		if (invokedmethod.getName().equals("finalize")) {
			return null;
		}

		Object result = null;
		try {
			Debug.logVerbose("[JdonFramework]<----> executing MethodInterceptor for method=" + invokedmethod.getDeclaringClass().getName() + "."
					+ invokedmethod.getName() + " successfully!", module);

			MethodInvocation methodInvocation = new MethodInvocationImp(target, beforeAfterMethodTarget, methodInterceptors, invokedmethod, objects,
					methodProxy);
			result = methodInvocation.proceed();

			Debug.logVerbose("<-----><end:", module);
		} catch (Exception ex) {
			Debug.logError(ex, module);
			throw new Exception(ex);
		} catch (Throwable ex) {
			throw new Throwable(ex);
		}

		return result;
	}

	public void clear() {
		if (this.methodInterceptors != null) {
			this.methodInterceptors.clear();
			this.methodInterceptors = null;
		}
		if (beforeAfterMethodTarget != null) {
			this.beforeAfterMethodTarget.clear();
			this.beforeAfterMethodTarget = null;
		}
		if (target != null) {
			if (target instanceof Startable) {
				Startable st = (Startable) target;
				try {
					st.stop();
				} catch (Exception e) {
				}
			}
			this.target = null;
		}
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		clear();

	}

}
