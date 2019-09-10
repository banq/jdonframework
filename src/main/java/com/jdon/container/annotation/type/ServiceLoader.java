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

import java.util.Set;

import com.jdon.annotation.Service;
import com.jdon.annotation.Singleton;
import com.jdon.bussinessproxy.meta.POJOTargetMetaDef;
import com.jdon.bussinessproxy.meta.SingletonPOJOTargetMetaDef;
import com.jdon.container.ContainerWrapper;
import com.jdon.container.access.TargetMetaDefHolder;
import com.jdon.container.annotation.AnnotationHolder;
import com.jdon.controller.context.AppContextWrapper;
import com.jdon.util.Debug;
import com.jdon.util.UtilValidate;

public class ServiceLoader {
	public final static String module = ServiceLoader.class.getName();

	AnnotationScaner annotationScaner;
	ConsumerLoader consumerLoader;

	public ServiceLoader(AnnotationScaner annotationScaner, ConsumerLoader consumerLoader) {
		super();
		this.annotationScaner = annotationScaner;
		this.consumerLoader = consumerLoader;
	}

	public void loadAnnotationServices(AnnotationHolder annotationHolder, AppContextWrapper context, ContainerWrapper containerWrapper) {
		Set<String> classes = annotationScaner.getScannedAnnotations(context).get(Service.class.getName());
		if (classes == null)
			return;
		Debug.logVerbose("[JdonFramework] found Annotation components size:" + classes.size(), module);
		for (Object className : classes) {
			createAnnotationServiceClass((String) className, annotationHolder, containerWrapper);
		}
	}

	public void createAnnotationServiceClass(String className, AnnotationHolder annotationHolder, ContainerWrapper containerWrapper) {
		try {
			Class cclass = Utils.createClass(className);
			Service serv = (Service) cclass.getAnnotation(Service.class);
			Debug.logVerbose("[JdonFramework] load Annotation service name:" + serv.value() + " class:" + className, module);

			String name = UtilValidate.isEmpty(serv.value()) ? cclass.getName() : serv.value();
			annotationHolder.addComponent(name, cclass);
			createPOJOTargetMetaDef(name, className, annotationHolder.getTargetMetaDefHolder(), cclass);

			consumerLoader.loadMehtodAnnotations(cclass, containerWrapper);
		} catch (Exception e) {
			Debug.logError("[JdonFramework] createAnnotationserviceClass error:" + e, module);
		}
	}

	public void createPOJOTargetMetaDef(String name, String className, TargetMetaDefHolder targetMetaDefHolder, Class cclass) {
		POJOTargetMetaDef pojoMetaDef = null;
		if (cclass.isAnnotationPresent(Singleton.class)) {
			pojoMetaDef = new SingletonPOJOTargetMetaDef(name, className);
		} else {
			pojoMetaDef = new POJOTargetMetaDef(name, className);
		}
		targetMetaDefHolder.add(name, pojoMetaDef);
	}
}
