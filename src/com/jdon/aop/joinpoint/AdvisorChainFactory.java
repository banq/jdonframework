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

package com.jdon.aop.joinpoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;

import com.jdon.aop.interceptor.Advisor;
import com.jdon.aop.interceptor.InterceptorsChain;
import com.jdon.bussinessproxy.TargetMetaDef;
import com.jdon.container.finder.ContainerCallback;
import com.jdon.util.Debug;

/**
 * create the all interceptor instances
 * 
 * @author banq
 */
public class AdvisorChainFactory {

	private final static String module = AdvisorChainFactory.class.getName();

	private final InterceptorsChain interceptorsChain;

	private final ContainerCallback containerCallback;

	private List<MethodInterceptor> interceptors;
	private List<MethodInterceptor> interceptorsForEJB;
	private Map<String, List<MethodInterceptor>> targetInterceptors;

	/**
	 * @param interceptorsChain
	 */
	public AdvisorChainFactory(InterceptorsChain interceptorsChain, ContainerCallback containerCallback) {
		super();
		this.interceptorsChain = interceptorsChain;
		this.containerCallback = containerCallback;
		targetInterceptors = new HashMap();

	}

	/**
	 * create the all interceptor instances, and put them into
	 * interceptorsChain; the interceptors that prointcut is for SERVIERS are in
	 * the front, and then the EJB Interceptors , in the back there are POJO
	 * interceptors. you can change the orders bu replacing this class in
	 * container.xml
	 * 
	 */
	public List<MethodInterceptor> create(TargetMetaDef targetMetaDef) throws Exception {
		Debug.logVerbose("[JdonFramework] enter  create PointcutAdvisor  ", module);
		if (targetMetaDef.isEJB()) {
			if (interceptorsForEJB == null) {
				interceptorsForEJB = new ArrayList();
				createEJBAdvice(targetMetaDef);
			}
			return interceptorsForEJB;
		}
		List<MethodInterceptor> result = new ArrayList();
		if (interceptors == null) {
			interceptors = new ArrayList();
			createPOJOAdvice(targetMetaDef);
		}
		result.addAll(interceptors);

		List<MethodInterceptor> targets = targetInterceptors.get(targetMetaDef.getName());
		if (targets == null) {
			targets = createTargetPOJOAdvice(targetMetaDef.getName());
			targetInterceptors.put(targetMetaDef.getName(), targets);
		}
		result.addAll(targets);
		return interceptors;
	}

	public List<MethodInterceptor> createTargetAdvice(String name) throws Exception {
		Debug.logVerbose("[JdonFramework] enter   createTargetAdvice  ", module);
		List<MethodInterceptor> targets = targetInterceptors.get(name);
		if (targets == null) {
			targets = createTargetPOJOAdvice(name);
			targetInterceptors.put(name, targets);
		}
		return targets;
	}

	protected void createEJBAdvice(TargetMetaDef targetMetaDef) throws Exception {
		if (!targetMetaDef.isEJB())
			return;

		List<Advisor> ejbInterceptorNames = interceptorsChain.getAdvisors(Pointcut.EJB_TARGET_PROPS_SERVICES);
		if (ejbInterceptorNames == null)
			return;
		List<Advisor> alladvices = interceptorsChain.getAdvisors(Pointcut.TARGET_PROPS_SERVICES);
		if (alladvices != null)
			ejbInterceptorNames.addAll(alladvices);

		for (Advisor advisor : ejbInterceptorNames) {
			MethodInterceptor ejbInterceptor = (MethodInterceptor) containerCallback.getContainerWrapper().lookup(advisor.getAdviceName());
			interceptorsForEJB.add(ejbInterceptor);
			Debug.logVerbose("[JdonFramework] find ejbService's interceptos size=" + interceptorsForEJB.size(), module);
		}

	}

	protected void createPOJOAdvice(TargetMetaDef targetMetaDef) throws Exception {
		if (targetMetaDef.isEJB())
			return;
		Debug.logVerbose("[JdonFramework] enter  create PointcutAdvisor  ", module);
		List<Advisor> pojoInterceptorNames = interceptorsChain.getAdvisors(Pointcut.POJO_TARGET_PROPS_SERVICES);
		if (pojoInterceptorNames == null)
			return;
		List<Advisor> alladvices = interceptorsChain.getAdvisors(Pointcut.TARGET_PROPS_SERVICES);
		if (alladvices != null)
			pojoInterceptorNames.addAll(alladvices);

		for (Advisor advisor : pojoInterceptorNames) {
			MethodInterceptor interceptor = (MethodInterceptor) containerCallback.getContainerWrapper().lookup(advisor.getAdviceName());
			interceptors.add(interceptor);
			Debug.logVerbose("[JdonFramework] find pojoService's interceptos size=" + interceptors.size(), module);
		}

	}

	protected List<MethodInterceptor> createTargetPOJOAdvice(String name) throws Exception {
		List<MethodInterceptor> myinterceptors = new ArrayList();

		Debug.logVerbose("[JdonFramework] enter  create PointcutAdvisor2  ", module);
		List<Advisor> pojoInterceptorNames = interceptorsChain.getAdvisors(name);
		if (pojoInterceptorNames == null)
			return myinterceptors;

		for (Advisor advisor : pojoInterceptorNames) {
			MethodInterceptor interceptor = (MethodInterceptor) containerCallback.getContainerWrapper().lookup(advisor.getAdviceName());
			myinterceptors.add(interceptor);
			Debug.logVerbose("[JdonFramework] find pojoService's interceptos size=" + myinterceptors.size(), module);
		}
		return myinterceptors;
	}

}
