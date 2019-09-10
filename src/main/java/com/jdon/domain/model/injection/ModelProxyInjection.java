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
package com.jdon.domain.model.injection;

import java.lang.reflect.Field;
import java.util.List;

import com.jdon.annotation.Component;
import com.jdon.annotation.Service;
import com.jdon.annotation.model.Inject;
import com.jdon.container.finder.ContainerCallback;
import com.jdon.domain.advsior.ModelAdvisor;
import com.jdon.util.ClassUtil;
import com.jdon.util.Debug;
import com.jdon.util.ObjectCreator;

/**
 * This class is responsible for inject the fileds marked with @Inject into the
 * models,if the injected fields are also marked with @Introduce,the interceptor
 * will be involved again.
 * 
 * for example:
 * 
 * 
 * public class MyModle{
 * 
 * @Inject MyDomainEvent myDomainEvent;
 * 
 * @Inject MyDomainService MyDomainService; }
 * 
 * @Introduce("message") class MyDomainEvent{
 * 
 *                       }
 * 
 * @author xmuzyu
 * 
 */
public class ModelProxyInjection {
	private final static String module = ModelProxyInjection.class.getName();
	private ModelAdvisor modelAdvisor;
	private ContainerCallback containerCallback;

	public ModelProxyInjection(ModelAdvisor modelAdvisor, ContainerCallback containerCallback) {
		super();
		this.modelAdvisor = modelAdvisor;
		this.containerCallback = containerCallback;
	}

	public void injectProperties(Object targetModel) {
		Class fClass = null;
		try {
			Field[] fields = ClassUtil.getAllDecaredFields(targetModel.getClass());
			if (fields == null)
				return;
			for (Field field : fields) {
				if (field.isAnnotationPresent(Inject.class)) {
					fClass = field.getType();
					Object fieldObject = getInjectObject(targetModel, fClass);
					if (field.getType().isAssignableFrom(fieldObject.getClass())) {
						try {
							field.setAccessible(true);
							field.set(targetModel, fieldObject);
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}

		} catch (Exception e) {
			Debug.logError("inject Properties error:" + e + " in " + targetModel.getClass() + "'s field: " + fClass, module);
		}
	}

	protected Object getInjectObject(Object targetModel, Class fClass) {
		Object o = createTargetComponent(targetModel, fClass);
		if (o == null)
			o = createTargetObject(targetModel, fClass);
		return o;

	}

	protected Object createTargetObject(Object targetModel, Class fClass) {
		Object o = null;
		try {
			o = ObjectCreator.createObject(fClass);
			o = modelAdvisor.createProxy(o);
		} catch (Exception e) {
			Debug.logError("createTargetObject error:" + e + " in " + targetModel.getClass(), module);
		}
		return o;

	}

	protected Object createTargetComponent(Object targetModel, Class fClass) {
		Object o = null;
		try {
			List<Object> objects = containerCallback.getContainerWrapper().getComponentInstancesOfType(fClass);
			// List should be have only one.
			for (Object instance : objects) {
				o = instance;
				break;
			}
			if (o != null)
				o = modelAdvisor.createProxy(o);
		} catch (Exception e) {
			Debug.logError("createTargetComponent error:" + e + " in" + targetModel.getClass(), module);
		}
		return o;

	}

	protected boolean isComponent(Object instance) {
		if (instance.getClass().isAnnotationPresent(Component.class))
			return true;
		if (instance.getClass().isAnnotationPresent(Service.class))
			return true;
		return false;
	}

}
