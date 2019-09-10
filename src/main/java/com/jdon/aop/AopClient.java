/**
 * Copyright 2003-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain event copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jdon.aop;

import java.lang.reflect.Method;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.jdon.aop.joinpoint.AdvisorChainFactory;
import com.jdon.aop.reflection.MethodConstructor;
import com.jdon.aop.reflection.ProxyMethodInvocation;
import com.jdon.bussinessproxy.target.TargetServiceFactory;
import com.jdon.container.access.TargetMetaRequest;
import com.jdon.container.access.TargetMetaRequestsHolder;
import com.jdon.container.finder.ContainerCallback;
import com.jdon.util.Debug;

/**
 * Aop Client
 * 
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 * 
 */
public class AopClient {

	private final static String module = AopClient.class.getName();

	private final AdvisorChainFactory advisorChainFactory;

	private final TargetServiceFactory targetServiceFactory;

	private final MethodConstructor methodConstructor;

	private final TargetMetaRequestsHolder targetMetaRequestsHolder;

	public AopClient(ContainerCallback containerCallback, AdvisorChainFactory advisorChainFactory, TargetServiceFactory targetServiceFactory,
			TargetMetaRequestsHolder targetMetaRequestsHolder) {
		this.advisorChainFactory = advisorChainFactory;
		this.targetServiceFactory = targetServiceFactory;
		this.methodConstructor = new MethodConstructor(containerCallback, targetMetaRequestsHolder);
		this.targetMetaRequestsHolder = targetMetaRequestsHolder;
	}

	/**
	 * 
	 * directly called by client with TargetMetaDef such as InvokerServlet:
	 * Object object = (Service)service.execute(targetMetaDef, methodMetaArgs,
	 * requestW); different target service has its Interceptor instance and
	 * MethodInvocation instance
	 * 
	 */
	public Object invoke() throws Throwable {
		TargetMetaRequest targetMetaRequest = targetMetaRequestsHolder.getTargetMetaRequest();
		Debug.logVerbose("[JdonFramework] enter AOP invoker for:" + targetMetaRequest.getTargetMetaDef().getClassName() + " method:"
				+ targetMetaRequest.getMethodMetaArgs().getMethodName(), module);

		Object result = null;
		MethodInvocation methodInvocation = null;
		try {
			List<MethodInterceptor> chain = advisorChainFactory.create(targetMetaRequest.getTargetMetaDef());
			Object[] args = targetMetaRequest.getMethodMetaArgs().getArgs();
			Method method = methodConstructor.createMethod(targetServiceFactory);
			methodInvocation = new ProxyMethodInvocation(chain, targetMetaRequestsHolder, targetServiceFactory, method, args);
			Debug.logVerbose("[JdonFramework] MethodInvocation will proceed ... ", module);
			result = methodInvocation.proceed();
		} catch (Exception ex) {
			Debug.logError(ex, module);
			throw new Exception(ex);
		} catch (Throwable ex) {
			throw new Throwable(ex);
		} finally {
			targetMetaRequestsHolder.clear();
		}
		return result;
	}

	/**
	 * dynamic proxy active this method when client call userService.xxxmethod
	 * 
	 * @param targetMetaRequest
	 * @param method
	 * @param args
	 * @return
	 * @throws Throwable
	 */
	public Object invoke(TargetMetaRequest targetMetaRequest, Method method, Object[] args) throws Throwable {
		targetMetaRequestsHolder.setTargetMetaRequest(targetMetaRequest);
		Debug.logVerbose(
				"[JdonFramework] enter AOP invoker2 for:" + targetMetaRequest.getTargetMetaDef().getClassName() + " method:" + method.getName(),
				module);

		Object result = null;
		MethodInvocation methodInvocation = null;
		try {
			List<MethodInterceptor> chain = advisorChainFactory.create(targetMetaRequest.getTargetMetaDef());
			methodInvocation = new ProxyMethodInvocation(chain, targetMetaRequestsHolder, targetServiceFactory, method, args);
			Debug.logVerbose("[JdonFramework] MethodInvocation will proceed ... ", module);
			result = methodInvocation.proceed();
		} catch (Exception ex) {
			Debug.logError(ex, module);
			throw new Exception(ex);
		} catch (Throwable ex) {
			throw new Throwable(ex);
		} finally {
			targetMetaRequestsHolder.clear();
		}
		return result;

	}

}
