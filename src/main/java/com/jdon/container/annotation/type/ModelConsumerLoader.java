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
import java.util.Set;

import com.jdon.annotation.Model;
import com.jdon.annotation.model.OnCommand;
import com.jdon.container.ContainerWrapper;
import com.jdon.container.annotation.AnnotationHolder;
import com.jdon.controller.context.AppContextWrapper;
import com.jdon.domain.message.consumer.ConsumerMethodHolder;
import com.jdon.domain.message.consumer.ModelConsumerMethodHolder;
import com.jdon.util.ClassUtil;

public class ModelConsumerLoader {

	public final static String TOPICNAME2 = "MEHTOD_TOPIC_COMMAND";

	AnnotationScaner annotationScaner;

	public ModelConsumerLoader(AnnotationScaner annotationScaner) {
		super();
		this.annotationScaner = annotationScaner;
	}

	public void loadAnnotationModels(AnnotationHolder annotationHolder, AppContextWrapper context, ContainerWrapper containerWrapper) {
		Set<String> classes = annotationScaner.getScannedAnnotations(context).get(Model.class.getName());
		if (classes == null)
			return;
		for (String className : classes) {
			Class cclass = Utils.createClass(className);
			loadMehtodAnnotations(cclass, containerWrapper);
		}
	}

	/**
	 * add the class to consumers annotated with @OnCommand
	 * 
	 * @param cclass
	 * @param containerWrapper
	 */

	public void loadMehtodAnnotations(Class cclass, ContainerWrapper containerWrapper) {
		try {

			for (Method method : ClassUtil.getAllDecaredMethods(cclass)) {
				if (method.isAnnotationPresent(OnCommand.class)) {
					addConsumerMethod(method, cclass, containerWrapper);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void addConsumerMethod(Method method, Class cclass, ContainerWrapper containerWrapper) {
		OnCommand onCommand = method.getAnnotation(OnCommand.class);
		String consumerKey = TOPICNAME2 + onCommand.value();
		ModelConsumerMethodHolder modelConsumerMethodHolder = getContainerConsumers(consumerKey, containerWrapper);
		modelConsumerMethodHolder.setConsumerMethodHolder(new ConsumerMethodHolder(cclass.getName(), method));
	}

	public ModelConsumerMethodHolder getContainerConsumers(String topicKey, ContainerWrapper containerWrapper) {
		ModelConsumerMethodHolder modelConsumerMethodHolder = (ModelConsumerMethodHolder) containerWrapper.lookup(topicKey);
		if (modelConsumerMethodHolder == null) {
			modelConsumerMethodHolder = new ModelConsumerMethodHolder();
			containerWrapper.register(topicKey, modelConsumerMethodHolder);
		}
		return modelConsumerMethodHolder;
	}

}
