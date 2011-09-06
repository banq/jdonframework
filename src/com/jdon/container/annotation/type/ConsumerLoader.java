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
package com.jdon.container.annotation.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import com.jdon.annotation.Component;
import com.jdon.annotation.Consumer;
import com.jdon.annotation.Service;
import com.jdon.async.disruptor.DisruptorFactory;
import com.jdon.container.ContainerWrapper;
import com.jdon.container.annotation.AnnotationHolder;
import com.jdon.controller.context.AppContextWrapper;
import com.jdon.domain.message.DomainEventHandler;
import com.jdon.util.Debug;
import com.jdon.util.UtilValidate;

public class ConsumerLoader {
	public final static String module = ConsumerLoader.class.getName();

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
			Collection consumers = (Collection) containerWrapper.lookup(DisruptorFactory.TOPICNAME + topicname);
			if (consumers == null) {
				consumers = new ArrayList();
				containerWrapper.register(DisruptorFactory.TOPICNAME + topicname, consumers);
			}
			String name = getConsumerName(consumers, cclass, containerWrapper);
			consumers.add(name);
			containerWrapper.register(name, cclass);

		} catch (Exception e) {
			Debug.logError("[JdonFramework] createAnnotationComponentClass error:" + e + className, module);

		}
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

	protected String getConsumerName(Collection consumers, Class cclass, ContainerWrapper containerWrapper) {
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

	protected TreeSet<Class> createSet() {
		return new TreeSet(new Comparator() {

			public int compare(Object num1, Object num2) {
				String inum1, inum2;
				inum1 = ((Class) num1).getName();
				inum2 = ((Class) num2).getName();
				if (inum1.compareTo(inum2) < 1) {
					return -1; // returning the first object
				} else {

					return 1;
				}
			}

		});

	}

}
