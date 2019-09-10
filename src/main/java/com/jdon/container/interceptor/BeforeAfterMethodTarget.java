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

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodProxy;

import com.jdon.annotation.pointcut.After;
import com.jdon.annotation.pointcut.Before;
import com.jdon.annotation.pointcut.method.Input;
import com.jdon.annotation.pointcut.method.Returning;
import com.jdon.container.pico.Startable;
import com.jdon.util.Debug;

/**
 * @Interceptor("myInterceptor") public class A implements AInterface {
 * @AdviceBefore("testOne") public Object myMethod(@Input() Object inVal,
 * @Returning() Object returnVal) {
 * 
 *              }
 * @Component("myInterceptor") public class MyInterceptor {
 * 
 *                             public Object testOne(Object inVal) { }
 * 
 * 
 * @author banq
 * 
 */
public class BeforeAfterMethodTarget implements Startable {
	private final static String module = BeforeAfterMethodTarget.class.getName();
	private Object target;
	private Object interceptor;
	private IntroduceInfo iinfo;

	public BeforeAfterMethodTarget(Object target, Object interceptor, IntroduceInfo iinfo) {
		super();
		this.target = target;
		this.interceptor = interceptor;
		this.iinfo = iinfo;
	}

	public Object invoke(Method invokedmethod, Object[] args, MethodProxy methodProxy) throws Throwable, Exception {
		if (invokedmethod.getName().equals("finalize")) {
			return null;
		}

		Object result = null;
		try {
			Debug.logVerbose("[JdonFramework] enter FixedMethodInvocation", module);

			if (iinfo != null) {
				Method adviceBeforeTargetMethod = getAdviceBeforeTargetMethod(target, iinfo, invokedmethod);
				if (adviceBeforeTargetMethod != null && adviceBeforeTargetMethod.getName().equals(invokedmethod.getName())) {
					Object returning = doBefore(adviceBeforeTargetMethod, args, interceptor, iinfo);
					injectOriginReturning(returning, adviceBeforeTargetMethod, args, iinfo);
				}
			}

			// result = methodProxy.invoke(target, args);
			result = methodProxy.invoke(target, args);

			if (iinfo != null) {
				Method adviceAfterTargetMethod = getAdviceAfterTargetMethod(target, iinfo, invokedmethod);
				if (adviceAfterTargetMethod != null && adviceAfterTargetMethod.getName().equals(invokedmethod.getName())) {
					result = doAfter(adviceAfterTargetMethod, result, iinfo, interceptor);
				}
			}

			Debug.logVerbose("<----->FixedMethodInvocation end:", module);
		} catch (Exception ex) {
			Debug.logError(ex, module);
			throw new Exception(ex);
		} catch (Throwable ex) {
			throw new Throwable(ex);
		}
		return result;
	}

	private Method getAdviceBeforeTargetMethod(Object target, IntroduceInfo iinfo, Method invokedmethod) {
		Method m = iinfo.getBefores().get(invokedmethod.getName());
		if (m != null)
			return m;
		for (Method method : target.getClass().getMethods()) {
			if (method.isAnnotationPresent(Before.class)) {
				m = method;
				iinfo.getBefores().put(invokedmethod.getName(), method);
			}
		}
		return m;

	}

	private Method getAdviceAfterTargetMethod(Object target, IntroduceInfo iinfo, Method invokedmethod) {
		Method m = iinfo.getAfters().get(invokedmethod.getName());
		if (m != null)
			return m;
		for (Method method : target.getClass().getMethods()) {
			if (method.isAnnotationPresent(After.class)) {
				m = method;
				iinfo.getAfters().put(invokedmethod.getName(), method);
			}
		}
		return m;

	}

	private Object doBefore(Method adviceBeforeTargetMethod, Object[] targetParameters, Object interceptor, IntroduceInfo iinfo) {
		Object returning = null;
		try {
			Before before = adviceBeforeTargetMethod.getAnnotation(Before.class);
			Method interceptorMethod = iinfo.getMethods().get(adviceBeforeTargetMethod);
			if (interceptorMethod == null) {
				for (Method interceptorMethod2 : interceptor.getClass().getMethods()) {
					String mName = interceptorMethod2.getName();
					if (before.value().equals(mName)) {
						interceptorMethod = interceptorMethod2;
						break;
					}
				}
				iinfo.getMethods().put(adviceBeforeTargetMethod, interceptorMethod);
			}
			returning = executeBeforeAdvice(adviceBeforeTargetMethod, targetParameters, interceptorMethod, interceptor, iinfo);
		} catch (Exception e) {
			Debug.logError("[JdonFramework]doBefore error: " + e, module);
		}
		return returning;
	}

	private Object executeBeforeAdvice(Method invokedmethod, Object[] targetParameters, Method interceptorMethod, Object interceptor,
			IntroduceInfo iinfo) {
		Object returning = null;
		try {
			Class[] iParamTypes = interceptorMethod.getParameterTypes();
			Object[] args = new Object[iParamTypes.length];
			Object requiredParam = getInputparameter(invokedmethod, targetParameters, interceptorMethod, iinfo);
			Integer posIn = iinfo.getIntroducedParametersPositions().get(interceptorMethod.getName());
			args[posIn] = requiredParam;
			returning = interceptorMethod.invoke(interceptor, args);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return returning;

	}

	private Object getInputparameter(Method method, Object[] targetParameters, Method interceptorMethod, IntroduceInfo iinfo) {
		Object input = null;
		Integer posIn = iinfo.getInputParametersPositions().get(method.getName());
		if (posIn != null) {
			input = targetParameters[posIn];
			return input;
		}

		try {
			Annotation[][] parameterAnnotations = method.getParameterAnnotations();
			Class[] parameterTypes = method.getParameterTypes();

			int i = 0;
			for (Annotation[] annotations : parameterAnnotations) {
				for (Annotation annotation : annotations) {
					if (annotation instanceof Input) {
						Class[] iParamTypes = interceptorMethod.getParameterTypes();
						for (int j = 0; j < iParamTypes.length; j++) {
							if (iParamTypes[j].isAssignableFrom(parameterTypes[i]) && parameterTypes[i] != null) {
								input = targetParameters[i];
								iinfo.getInputParametersPositions().put(method.getName(), i);
								iinfo.getIntroducedParametersPositions().put(interceptorMethod.getName(), j);
								break;
							}
						}
					}
				}
				i++;
			}
		} catch (Exception e) {
			Debug.logError("getInputparameter" + e, module);
		}

		return input;
	}

	private void injectOriginReturning(Object returning, Method adviceBeforeTargetMethod, Object[] targetParameters, IntroduceInfo iinfo) {
		if (returning == null)
			return;
		Integer posIn = iinfo.getReturnParametersPositions().get(adviceBeforeTargetMethod.getName());
		if (posIn != null) {
			targetParameters[posIn] = returning;
			return;
		}
		try {
			Annotation[][] parameterAnnotations = adviceBeforeTargetMethod.getParameterAnnotations();
			int i = 0;
			for (Annotation[] annotations : parameterAnnotations) {
				for (Annotation annotation : annotations) {
					if (annotation instanceof Returning) {
						if (targetParameters[i] != null && targetParameters[i].getClass().isAssignableFrom(returning.getClass())) {
							targetParameters[i] = returning;
							iinfo.getReturnParametersPositions().put(adviceBeforeTargetMethod.getName(), i);
							break;
						}
					}
				}
				i++;
			}
		} catch (Exception e) {
			Debug.logError("injectOriginReturning" + e, module);
		}
	}

	private Object doAfter(Method adviceAfterTargetMethod, Object result, IntroduceInfo iinfo, Object interceptor) {
		Object resultReturning = null;
		try {
			After after = adviceAfterTargetMethod.getAnnotation(After.class);
			Method interceptorMethod = iinfo.getMethods().get(adviceAfterTargetMethod.getName());
			if (interceptorMethod == null) {
				for (Method interceptorMethod2 : interceptor.getClass().getMethods()) {
					String mName = interceptorMethod2.getName();
					if (after.value().equals(mName)) {
						interceptorMethod = interceptorMethod2;
						break;
					}
				}
				iinfo.getMethods().put(adviceAfterTargetMethod, interceptorMethod);
			}
			resultReturning = execAfterAdvice(result, interceptorMethod, interceptor, iinfo);

		} catch (Exception e) {
			Debug.logError("[JdonFramework]doAfter error: " + e, module);
		}
		return resultReturning;
	}

	private Object execAfterAdvice(Object result, Method interceptorMethod, Object interceptor, IntroduceInfo iinfo) {
		Object resultReturning = null;
		try {
			Object[] args = getInjectResturingIntoAdvice(result, interceptorMethod, iinfo);
			resultReturning = interceptorMethod.invoke(interceptor, args);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return resultReturning;
	}

	private Object[] getInjectResturingIntoAdvice(Object result, Method interceptorMethod, IntroduceInfo iinfo) {
		Class[] iParamTypes = interceptorMethod.getParameterTypes();
		Object[] args = new Object[iParamTypes.length];
		Integer posIn = iinfo.getIntroducedParametersPositions().get(interceptorMethod.getName());
		if (posIn != null) {
			args[posIn] = result;
			return args;
		}
		try {
			for (int i = 0; i < iParamTypes.length; i++) {
				if (iParamTypes[i].isAssignableFrom(result.getClass())) {
					args[i] = result;
					iinfo.getIntroducedParametersPositions().put(interceptorMethod.getName(), i);
				}

			}
		} catch (Exception e) {
			Debug.logError("getInjectResturingIntoAdvice" + e, module);
		}
		return args;
	}

	public void clear() {
		if (iinfo != null) {
			this.iinfo.clear();
			this.iinfo = null;
		}

		if (interceptor != null) {
			if (interceptor instanceof Startable) {
				Startable st = (Startable) interceptor;
				try {
					st.stop();
				} catch (Exception e) {
				}
			}
			this.interceptor = null;
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
