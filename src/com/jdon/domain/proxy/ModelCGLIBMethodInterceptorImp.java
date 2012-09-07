/*
 * Copyright 2003-2009 the original author or authors.
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
package com.jdon.domain.proxy;

import java.lang.reflect.Method;
import java.util.List;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.aopalliance.intercept.MethodInvocation;

import com.jdon.container.pico.Startable;
import com.jdon.util.Debug;

public class ModelCGLIBMethodInterceptorImp implements MethodInterceptor, Startable {
	private final static String module = ModelCGLIBMethodInterceptorImp.class.getName();
	private List<org.aopalliance.intercept.MethodInterceptor> methodInterceptors;

	protected Object target;

	public ModelCGLIBMethodInterceptorImp(Object target, List<org.aopalliance.intercept.MethodInterceptor> methodInterceptors) {
		super();
		this.target = target;
		this.methodInterceptors = methodInterceptors;
	}

	public Object intercept(Object object, Method invokedmethod, Object[] args, MethodProxy methodProxy) throws Throwable {
		if (invokedmethod.getName().equals("finalize")) {
			this.finalize();
			return null;
		}

		Object result = null;
		try {
			Debug.logVerbose("[JdonFramework]<----> executing MethodInterceptor for method=" + invokedmethod.getDeclaringClass().getName() + "."
					+ invokedmethod.getName() + " successfully!", module);

			MethodInvocation methodInvocation = new ModelMethodInvocation(target, methodInterceptors, invokedmethod, args, methodProxy);
			result = methodInvocation.proceed();

			Debug.logVerbose("<-----><end:", module);
		} catch (Exception ex) {
			Debug.logError(ex, module);
		} catch (Throwable ex) {
			throw new Throwable(ex);
		}

		return result;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		stop();
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		if (this.methodInterceptors != null) {
			for (Object o : this.methodInterceptors) {
				if (o instanceof Startable) {
					Startable st = (Startable) o;
					try {
						st.stop();
					} catch (Exception e) {
					}
				}

			}
			this.methodInterceptors.clear();
			this.methodInterceptors = null;
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
}