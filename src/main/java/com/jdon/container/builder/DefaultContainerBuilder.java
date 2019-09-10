/**
 * Copyright 2003-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain event copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jdon.container.builder;

import java.util.Iterator;
import java.util.Map;

import com.jdon.aop.interceptor.InterceptorsChain;
import com.jdon.bussinessproxy.TargetMetaDef;
import com.jdon.bussinessproxy.meta.POJOTargetMetaDef;
import com.jdon.container.ContainerWrapper;
import com.jdon.container.access.TargetMetaDefHolder;
import com.jdon.container.access.xml.AppConfigureCollection;
import com.jdon.container.access.xml.TargetMetaDefXmlLoader;
import com.jdon.container.config.ComponentMetaDef;
import com.jdon.container.config.ContainerComponents;
import com.jdon.container.config.aspect.AspectComponentsMetaDef;
import com.jdon.container.finder.ComponentKeys;
import com.jdon.util.Debug;

/**
 * the container builder the methods invoke order is decided by
 * containerDirector.
 * 
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 * 
 */
public class DefaultContainerBuilder implements ContainerRegistryBuilder {
	public final static String module = DefaultContainerBuilder.class.getName();

	protected final ContainerComponents basicComponents;
	protected final ContainerComponents aspectConfigComponents;
	protected final XmlContainerRegistry xmlcontainerRegistry;
	protected final ContainerWrapper containerWrapper;
	protected volatile boolean startup;

	/**
	 * construtor
	 * 
	 * @param containerWrapper
	 * @param basicComponents
	 *            got from configure file
	 * @param interceptorsChain
	 *            got from configure file
	 */
	public DefaultContainerBuilder(ContainerWrapper containerWrapper, ContainerComponents basicComponents, ContainerComponents aspectConfigComponents) {
		this.basicComponents = basicComponents;
		this.aspectConfigComponents = aspectConfigComponents;
		this.xmlcontainerRegistry = new XmlContainerRegistry(containerWrapper);
		this.containerWrapper = containerWrapper;

	}

	public ContainerWrapper getContainerWrapper() {
		return containerWrapper;
	}

	/**
	 * if there are xml configure then add new ones; if not, register it;
	 * 
	 * @param configList
	 *            Collection the configure collection for jdonframework.xml
	 */
	public void registerAppRoot(String configureFileName) throws Exception {
		try {
			AppConfigureCollection existedAppConfigureFiles = (AppConfigureCollection) containerWrapper.lookup(AppConfigureCollection.NAME);
			if (existedAppConfigureFiles == null) {
				xmlcontainerRegistry.registerAppRoot();
				existedAppConfigureFiles = (AppConfigureCollection) containerWrapper.lookup(AppConfigureCollection.NAME);
			}
			if (!existedAppConfigureFiles.getConfigList().contains(configureFileName)) {
				Debug.logInfo("[JdonFramework]found jdonframework configuration:" + configureFileName, module);
				existedAppConfigureFiles.addConfigList(configureFileName);
			}
		} catch (Exception ex) {
			Debug.logError("[JdonFramework] found jdonframework configuration error:" + ex, module);
			throw new Exception(ex);
		}
	}

	/**
	 * register all basic components in container.xml
	 */
	public void registerComponents() throws Exception {
		Debug.logVerbose("[JdonFramework] note: registe all basic components in container.xml size=" + basicComponents.size(), module);
		try {
			Iterator iter = basicComponents.iterator();
			while (iter.hasNext()) {
				String name = (String) iter.next();
				ComponentMetaDef componentMetaDef = basicComponents.getComponentMetaDef(name);
				xmlcontainerRegistry.registerComponentMetaDef(componentMetaDef);
			}
		} catch (Exception ex) {
			Debug.logError("[JdonFramework] register basiceComponents error:" + ex, module);
			throw new Exception(ex);
		}

	}

	/**
	 * register all apsect components in aspect.xml
	 */
	public void registerAspectComponents() throws Exception {
		Debug.logVerbose("[JdonFramework] note: registe aspect components ", module);
		try {
			InterceptorsChain existedInterceptorsChain = (InterceptorsChain) containerWrapper.lookup(ComponentKeys.INTERCEPTOR_CHAIN);

			Iterator iter = aspectConfigComponents.iterator();
			Debug.logVerbose("[JdonFramework] 3 aspectConfigComponents size:" + aspectConfigComponents.size(), module);
			while (iter.hasNext()) {
				String name = (String) iter.next();
				AspectComponentsMetaDef componentMetaDef = (AspectComponentsMetaDef) aspectConfigComponents.getComponentMetaDef(name);
				// registe into container
				xmlcontainerRegistry.registerAspectComponentMetaDef(componentMetaDef);
				// got the interceptor instance;
				// add interceptor instance into InterceptorsChain object
				existedInterceptorsChain.addInterceptor(componentMetaDef.getPointcut(), name);
			}
		} catch (Exception ex) {
			Debug.logError("[JdonFramework] registerAspectComponents error:" + ex, module);
			throw new Exception(ex);
		}

	}

	/**
	 * register user services/components in jdonframework.xml
	 * 
	 * AnnotationContainerBuilder will override this method, and register
	 * annotation services or components.
	 * 
	 */
	public void registerUserService() throws Exception {
		Debug.logVerbose("[JdonFramework] note: registe user pojoservice ", module);
		try {
			TargetMetaDefXmlLoader targetMetaDefXmlLoader = (TargetMetaDefXmlLoader) containerWrapper.lookup(ComponentKeys.SERVICE_METALOADER_NAME);
			targetMetaDefXmlLoader.loadXML();
			TargetMetaDefHolder targetMetaDefHolder = (TargetMetaDefHolder) containerWrapper.lookup(ComponentKeys.SERVICE_METAHOLDER_NAME);
			if (targetMetaDefHolder == null)
				return;
			Map metaDefs = targetMetaDefHolder.loadMetaDefs();
			Iterator iter = metaDefs.keySet().iterator();
			while (iter.hasNext()) {
				String name = (String) iter.next();
				TargetMetaDef tgm = (TargetMetaDef) metaDefs.get(name);
				if (!tgm.isEJB()) {
					xmlcontainerRegistry.registerPOJOTargetMetaDef((POJOTargetMetaDef) tgm);
				}
			}
		} catch (Exception ex) {
			Debug.logError("[JdonFramework] registerUserService error:" + ex, module);
			throw new Exception(ex);
		}
	}

	public void startApp() {
		StartablecomponentsRegistry scr = (StartablecomponentsRegistry) containerWrapper.lookup(StartablecomponentsRegistry.NAME);
		scr.startStartableComponents(containerWrapper);
	}

	public void stopApp() {
		StartablecomponentsRegistry scr = (StartablecomponentsRegistry) containerWrapper.lookup(StartablecomponentsRegistry.NAME);
		scr.stopStartableComponents(containerWrapper);
	}

	public synchronized void setKernelStartup(boolean startup) {
		this.startup = startup;
	}

	public boolean isKernelStartup() {
		return startup;
	}

	public void doAfterStarted() throws Exception {

	}

}
