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
package com.jdon.container.annotation.type;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import com.jdon.annotation.Component;
import com.jdon.annotation.Consumer;
import com.jdon.annotation.Service;
import com.jdon.annotation.model.OnEvent;
import com.jdon.container.ContainerWrapper;
import com.jdon.container.annotation.AnnotationHolder;
import com.jdon.controller.context.AppContextWrapper;
import com.jdon.domain.message.DomainEventHandler;
import com.jdon.domain.message.consumer.ConsumerMethodHolder;
import com.jdon.util.ClassUtil;
import com.jdon.util.Debug;
import com.jdon.util.UtilValidate;

public class ConsumerLoader {
	public final static String module = ConsumerLoader.class.getName();
	public final static String TOPICNAME = "CONSUMER_TOPIC";
	public final static String TOPICNAME2 = "MEHTOD_TOPIC";

	AnnotationScaner annotationScaner;

	public ConsumerLoader(AnnotationScaner annotationScaner) {
		super();
		this.annotationScaner = annotationScaner;
	}

	public void loadAnnotationConsumers(AnnotationHolder annotationHolder, AppContextWrapper context, ContainerWrapper containerWrapper) {
		Set<String> classes = annotationScaner.getScannedAnnotations(context).get(Consumer.class.getName());
		if (classes == null)
			return;
		Debug.logVerbose("[JdonFramework] found Annotation components size:" + classes.size(), module);
		for (String className : classes) {
			createAnnotationConsumerClass(className, annotationHolder, containerWrapper);
		}
	}

	public void createAnnotationConsumerClass(String className, AnnotationHolder annotationHolder, ContainerWrapper containerWrapper) {
		try {
			Class cclass = Utils.createClass(className);
			if (!DomainEventHandler.class.isAssignableFrom(cclass)) {
				Debug.logError("[JdonFramework] " + cclass.getName()
						+ " that with @Consumer annotataion must also implements  com.jdon.domain.message.DomainEventHandler ", module);
				return;
			}
			Consumer consumer = (Consumer) cclass.getAnnotation(Consumer.class);
			Debug.logVerbose("[JdonFramework] load Annotation Consumer name:" + cclass.getName() + " class:" + className, module);

			String topicname = UtilValidate.isEmpty(consumer.value()) ? cclass.getName() : consumer.value();
			String topicKey = ConsumerLoader.TOPICNAME + topicname;
			Collection<String> consumers = getContainerConsumers(topicKey, containerWrapper);
			String name = getConsumerName(cclass);
			consumers.add(name);
			containerWrapper.register(name, cclass);

		} catch (Exception e) {
			Debug.logError("[JdonFramework] createAnnotationComponentClass error:" + e + className, module);

		}
	}

	/**
	 * add the class to consumers annotated with @OnEvent
	 * 
	 * @param cclass
	 * @param containerWrapper
	 */

	public void loadMehtodAnnotations(Class cclass, ContainerWrapper containerWrapper) {
		try {
			for (Method method : ClassUtil.getAllDecaredMethods(cclass)) {
				if (method.isAnnotationPresent(OnEvent.class)) {
					addConsumerMethod(method, cclass, containerWrapper);
					// } else {
					// Method mm = ClassUtil.finddAnnotationForMethod(method,
					// OnEvent.class);
					// if (mm != null) {
					// addConsumerMethod(mm, cclass, containerWrapper);
					// }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void addConsumerMethod(Method method, Class cclass, ContainerWrapper containerWrapper) {
		OnEvent onEvent = method.getAnnotation(OnEvent.class);
		String consumerKey = ConsumerLoader.TOPICNAME2 + onEvent.value();
		Collection consumerMethods = getContainerConsumers(consumerKey, containerWrapper);
		String componentname = getConsumerName(cclass);
		consumerMethods.add(new ConsumerMethodHolder(componentname, method));
	}

	public Collection<String> getContainerConsumers(String topicKey, ContainerWrapper containerWrapper) {

		Collection consumers = (Collection) containerWrapper.lookup(topicKey);
		if (consumers == null) {
			consumers = new ArrayList();
			containerWrapper.register(topicKey, consumers);
		}
		return consumers;
	}

	protected TreeSet createNewSet() {
		return new TreeSet(new Comparator() {
			public int compare(Object num1, Object num2) {
				String inum1, inum2;
				inum1 = num1.getClass().getName();
				inum2 = num2.getClass().getName();
				if (inum1.compareTo(inum2) < 1) {
					return -1; // returning the first object
				} else {

					return 1;
				}
			}

		});
	}

	public Boolean implementsInterface(Class cclass, Class interf) {
		for (Class c : cclass.getInterfaces()) {
			if (c.equals(interf)) {
				return true;
			}
		}
		return false;
	}

	protected String getConsumerName(Class cclass) {
		String name = "";
		// ComponentLoader will do it with @Component;
		if (cclass.isAnnotationPresent(Component.class)) {
			Component cp = (Component) cclass.getAnnotation(Component.class);
			name = UtilValidate.isEmpty(cp.value()) ? cclass.getName() : cp.value();
		} else if (cclass.isAnnotationPresent(Service.class)) {
			Service cp = (Service) cclass.getAnnotation(Service.class);
			name = UtilValidate.isEmpty(cp.value()) ? cclass.getName() : cp.value();
		} else {
			// directly @Consumer with no @Component or @Service
			name = cclass.getName();
		}
		return name;
	}

}
