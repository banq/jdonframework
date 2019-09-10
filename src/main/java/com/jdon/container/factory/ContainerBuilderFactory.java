/*
 * Copyright 2003-2006 the original author or authors.
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
package com.jdon.container.factory;

import com.jdon.container.ContainerWrapper;
import com.jdon.container.annotation.AnnotationContainerBuilder;
import com.jdon.container.annotation.ContainerLoaderAnnotation;
import com.jdon.container.builder.ContainerRegistryBuilder;
import com.jdon.container.config.ContainerComponents;
import com.jdon.controller.context.AppContextWrapper;
import com.jdon.controller.context.application.Application;

/**
 * fetch the all components configures, and create ContainerBuilder Instance.
 * 
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 * 
 */
public class ContainerBuilderFactory {
	public final static String module = ContainerBuilderFactory.class.getName();

	private ContainerLoaderXML containerLoaderXML;
	private ContainerLoaderAnnotation containerLoaderAnnotation;

	public ContainerBuilderFactory() {
		containerLoaderXML = new ContainerLoaderXML();
		containerLoaderAnnotation = new ContainerLoaderAnnotation();
	}

	/**
	 * the main method in this class, read all components include interceptors
	 * from Xml configure file.
	 * 
	 * create event micro container instance. and then returen event ContainerBuilder
	 * instance
	 * 
	 * @param context
	 * @return
	 */
	public synchronized ContainerRegistryBuilder createContainerBuilder(AppContextWrapper context) {
		containerLoaderAnnotation.startScan(context);

		ContainerFactory containerFactory = new ContainerFactory();
		ContainerWrapper cw = containerFactory.create(containerLoaderAnnotation.getConfigInfo());

		ContainerComponents configComponents = containerLoaderXML.loadAllContainerConfig(context);
		ContainerComponents aspectConfigComponents = containerLoaderXML.loadAllAspectConfig(context);

		return createContainerBuilder(context, cw, configComponents, aspectConfigComponents);
	}

	public synchronized ContainerRegistryBuilder createContainerBuilderForTest(String container_configFile, String aspect_configFile) {
		AppContextWrapper context = new Application();
		containerLoaderAnnotation.startScan(context);

		ContainerFactory containerFactory = new ContainerFactory();
		ContainerWrapper cw = containerFactory.create(containerLoaderAnnotation.getConfigInfo());

		ContainerComponents configComponents = containerLoaderXML.loadBasicComponents(container_configFile);
		ContainerComponents aspectConfigComponents = containerLoaderXML.loadAspectComponents(null, aspect_configFile);
		return createContainerBuilder(context, cw, configComponents, aspectConfigComponents);

	}

	public ContainerRegistryBuilder createContainerBuilder(AppContextWrapper context, ContainerWrapper cw, ContainerComponents configComponents,
			ContainerComponents aspectConfigComponents) {
		return new AnnotationContainerBuilder(context, cw, configComponents, aspectConfigComponents, containerLoaderAnnotation);
	}

}
