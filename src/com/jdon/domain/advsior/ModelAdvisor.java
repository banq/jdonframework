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
package com.jdon.domain.advsior;

import java.util.ArrayList;
import java.util.List;

import com.jdon.annotation.Introduce;
import com.jdon.container.finder.ContainerCallback;
import com.jdon.domain.proxy.ModelProxyFactory;
import com.jdon.util.Debug;

/**
 * when a model with a @Introduce annotation is injected to another model,
 * the injected model will be enhanced using cglib.
 * 
 * for example:
 * @Introduce("message")
 * public class DomainEvent{
 * 
 *    
 * }
 * 
 * as this,the DomainEvent will be enhanced with MessageInterceptor
 * 
 * @author xmuzyu
 *
 */
public class ModelAdvisor {
	private final static String module = ModelAdvisor.class.getName();

	private final ContainerCallback containerCallback;
	private final ModelProxyFactory modelProxyFactory;

	public ModelAdvisor(ContainerCallback containerCallback, ModelProxyFactory modelProxyFactory) {
		super();
		this.containerCallback = containerCallback;
		this.modelProxyFactory = modelProxyFactory;
	}

	public Object createProxy(Object model) {
		if (!isAcceptable(model.getClass()))
			return model;
		List methodInterceptors = getAdviceName(model);
		return modelProxyFactory.create(model, methodInterceptors);
	}

	public List getAdviceName(Object model) {
		List methodInterceptors = new ArrayList();
		try {
			Introduce introduce = model.getClass().getAnnotation(Introduce.class);
			if (introduce == null)
				return methodInterceptors;
			String[] adviceNames = introduce.value();
			if (adviceNames != null) {
				for (int i = 0; i < adviceNames.length; i++) {
					Object interceptorCustomzied = containerCallback.getContainerWrapper().getComponentNewInstance(adviceNames[i]);
					methodInterceptors.add(interceptorCustomzied);
				}
			}
		} catch (Exception e) {
			Debug.logError(" getAdviceNameAnnotation" + e, module);
		}
		return methodInterceptors;
	}

	public boolean isAcceptable(Class classz) {
		if (classz.isAnnotationPresent(Introduce.class)) {
			return true;
		} else
			return false;

	}

}
