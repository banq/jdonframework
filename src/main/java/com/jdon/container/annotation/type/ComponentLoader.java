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

import com.jdon.annotation.Component;
import com.jdon.bussinessproxy.meta.POJOTargetMetaDef;
import com.jdon.container.ContainerWrapper;
import com.jdon.container.annotation.AnnotationHolder;
import com.jdon.controller.context.AppContextWrapper;
import com.jdon.util.Debug;
import com.jdon.util.UtilValidate;

public class ComponentLoader {
	public final static String module = ComponentLoader.class.getName();

	AnnotationScaner annotationScaner;
	ConsumerLoader consumerLoader;

	public ComponentLoader(AnnotationScaner annotationScaner, ConsumerLoader consumerLoader) {
		super();
		this.annotationScaner = annotationScaner;
		this.consumerLoader = consumerLoader;
	}

	public void loadAnnotationComponents(AnnotationHolder annotationHolder, AppContextWrapper context, ContainerWrapper containerWrapper) {
		Set<String> classes = annotationScaner.getScannedAnnotations(context).get(Component.class.getName());
		if (classes == null)
			return;
		Debug.logVerbose("[JdonFramework] found Annotation components size:" + classes.size(), module);
		for (Object className : classes) {
			createAnnotationComponentClass((String) className, annotationHolder, containerWrapper);
		}
	}

	public void createAnnotationComponentClass(String className, AnnotationHolder annotationHolder, ContainerWrapper containerWrapper) {
		try {
			Class cclass = Utils.createClass(className);
			Component cp = (Component) cclass.getAnnotation(Component.class);
			Debug.logVerbose("[JdonFramework] load Annotation component name:" + cclass.getName() + " class:" + className, module);

			String name = UtilValidate.isEmpty(cp.value()) ? cclass.getName() : cp.value();
			annotationHolder.addComponent(name, cclass);
			POJOTargetMetaDef pojoMetaDef = new POJOTargetMetaDef(name, className);
			annotationHolder.getTargetMetaDefHolder().add(name, pojoMetaDef);

			consumerLoader.loadMehtodAnnotations(cclass, containerWrapper);
		} catch (Exception e) {
			Debug.logError("[JdonFramework] createAnnotationComponentClass error:" + e + className, module);

		}
	}

}
