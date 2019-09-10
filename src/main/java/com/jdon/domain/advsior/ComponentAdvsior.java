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
package com.jdon.domain.advsior;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.cglib.proxy.MethodInterceptor;

import com.jdon.aop.joinpoint.AdvisorChainFactory;
import com.jdon.container.finder.ComponentKeys;
import com.jdon.container.finder.ContainerCallback;
import com.jdon.container.interceptor.CGLIBMethodInterceptorImp;
import com.jdon.container.interceptor.IntroduceInfo;
import com.jdon.container.interceptor.IntroduceInfoHolder;
import com.jdon.container.interceptor.ProxyFactory;
import com.jdon.util.ClassUtil;
import com.jdon.util.Debug;

/**
 * in container, when event component inject into another component or event model, the
 * advisor will create event proxy for injected class with its interfaces.
 * 
 * the condition for creating proxy:
 * 
 * 1. the inject component class has @Introduce
 * 
 * 2. the inject component class has its interfaces
 * 
 * 3. the inject component class's componet name is in the another class's
 * target parameter with @Interceptor(name="", target="xx,xx")
 * 
 * @author banq
 * 
 */
public class ComponentAdvsior {
	private final static String module = ComponentAdvsior.class.getName();
	public final static String NAME = "componentAdvsior";
	private final ContainerCallback containerCallback;
	private final Map<String, Class[]> interfaceMaps = new HashMap();

	public ComponentAdvsior(ContainerCallback containerCallback) {
		super();
		this.containerCallback = containerCallback;
	}

	public Object createProxy(Object o) {
		if (o == null)
			return o;
		IntroduceInfoHolder introduceInfoHolder = (IntroduceInfoHolder) containerCallback.getContainerWrapper().lookup(IntroduceInfoHolder.NAME);
		if (introduceInfoHolder == null)
			return o;
		if (!introduceInfoHolder.containsThisClass(o.getClass()))
			return o;

		try {
			Class[] interfaces = getInterfaces(o.getClass());
			if (interfaces == null) {
				Debug.logError(" Your class:" + o.getClass()
						+ " has event annotation @Introduce, the class need implement event interface when it be register in container", module);
				return o;
			}

			IntroduceInfo iinfo = introduceInfoHolder.getIntroduceInfoByIntroducer(o.getClass());
			Object interceptor = null;
			if (iinfo != null)
				interceptor = containerCallback.getContainerWrapper().getComponentNewInstance(iinfo.getAdviceName());

			AdvisorChainFactory acf = (AdvisorChainFactory) containerCallback.getContainerWrapper().lookup(ComponentKeys.INTERCEPTOR_CHAIN_FACTORY);
			String targetName = introduceInfoHolder.getTargetName(o.getClass());
			if (targetName == null)
				return o;
			List<org.aopalliance.intercept.MethodInterceptor> methodInterceptors = acf.createTargetAdvice(targetName);

			MethodInterceptor mi = new CGLIBMethodInterceptorImp(o, interceptor, iinfo, methodInterceptors);
			ProxyFactory proxyFactory = (ProxyFactory) containerCallback.getContainerWrapper().lookup(ComponentKeys.DOMAIN_PROXY_FACTORY);
			o = proxyFactory.createProxy(mi, o, interfaces);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return o;
	}

	public Class[] getInterfaces(Class pojoClass) {
		Class[] interfaces = interfaceMaps.get(pojoClass.getName());
		if (interfaces == null) {
			interfaces = ClassUtil.getParentAllInterfaces(pojoClass);
			interfaceMaps.put(pojoClass.getName(), interfaces);
		}

		return interfaces;

	}

}
