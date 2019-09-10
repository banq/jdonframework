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
package com.jdon.container.access;

import com.jdon.bussinessproxy.TargetMetaDef;
import com.jdon.container.ContainerWrapper;
import com.jdon.container.finder.ComponentKeys;
import com.jdon.container.finder.ContainerFinderImp;
import com.jdon.container.visitor.ComponentVisitor;
import com.jdon.container.visitor.VisitorFactory;
import com.jdon.controller.context.AppContextWrapper;
import com.jdon.controller.context.ContextHolder;
import com.jdon.util.Debug;

/**
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 * 
 */
public class UserTargetMetaDefFactory {
	private final static String module = UserTargetMetaDefFactory.class.getName();

	private final ContainerFinderImp servletContainerFinder = new ContainerFinderImp();
	public final TargetMetaRequestsHolder targetMetaRequestsHolder;

	public UserTargetMetaDefFactory(TargetMetaRequestsHolder targetMetaRequestsHolder) {
		this.targetMetaRequestsHolder = targetMetaRequestsHolder;
	}

	public TargetMetaDef getTargetMetaDef(String name, ContainerWrapper cw) {
		TargetMetaDefHolder targetMetaDefHoader = (TargetMetaDefHolder) cw.lookup(ComponentKeys.SERVICE_METAHOLDER_NAME);
		TargetMetaDef targetMetaDef = targetMetaDefHoader.getTargetMetaDef(name);
		if (targetMetaDef == null) {
			Debug.logError("[JdonFramework] not found the service/component for name:" + name, module);
			return null;
		}
		return targetMetaDef;
	}

	/**
	 * create event targetMetaRequest instance.
	 * 
	 * @param containerWrapper
	 * @param targetMetaDef
	 * @param request
	 * @return
	 */
	public void createTargetMetaRequest(TargetMetaDef targetMetaDef, ContextHolder holder) {
		ContainerWrapper containerWrapper = servletContainerFinder.findContainer(holder.getAppContextHolder());
		// get HttpSessionVisitorFactoryImp
		VisitorFactory visitorFactory = (VisitorFactory) containerWrapper.lookup(ComponentKeys.VISITOR_FACTORY);
		// ComponentVisitor is HttpSessionComponentVisitor
		ComponentVisitor cm = visitorFactory.createtVisitor(holder.getSessionHolder(), targetMetaDef);
		TargetMetaRequest targetMetaRequest = new TargetMetaRequest(targetMetaDef, cm);
		targetMetaRequestsHolder.setTargetMetaRequest(targetMetaRequest);
	}

	public void createTargetMetaRequest(TargetMetaDef targetMetaDef, AppContextWrapper acw) {
		ContainerWrapper containerWrapper = servletContainerFinder.findContainer(acw);
		// get HttpSessionVisitorFactoryImp
		VisitorFactory visitorFactory = (VisitorFactory) containerWrapper.lookup(ComponentKeys.VISITOR_FACTORY);
		// ComponentVisitor is HttpSessionComponentVisitor
		ComponentVisitor cm = visitorFactory.createtVisitor(null, targetMetaDef);
		TargetMetaRequest targetMetaRequest = new TargetMetaRequest(targetMetaDef, cm);
		targetMetaRequestsHolder.setTargetMetaRequest(targetMetaRequest);
	}

}
