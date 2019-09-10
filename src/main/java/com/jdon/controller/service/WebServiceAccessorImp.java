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
package com.jdon.controller.service;

import com.jdon.container.ContainerWrapper;
import com.jdon.container.access.ServiceAccessor;
import com.jdon.container.access.TargetMetaRequest;
import com.jdon.container.access.TargetMetaRequestsHolder;
import com.jdon.container.finder.ComponentKeys;
import com.jdon.container.finder.ContainerCallback;
import com.jdon.container.visitor.ComponentVisitor;
import com.jdon.container.visitor.data.SessionContext;
import com.jdon.container.visitor.data.SessionContextSetup;
import com.jdon.controller.context.RequestWrapper;
import com.jdon.util.Debug;

/**
 * before accessing container, we can do something and store some state into
 * UserTargetMetaDef object.
 * 
 * @see com.jdon.container.access.ServiceAccessorImp.
 * @author <event href="mailto:banqJdon<AT>jdon.com">banq</event>
 * 
 */

public class WebServiceAccessorImp implements WebServiceAccessor {
	private final static String module = WebServiceAccessorImp.class.getName();

	private final ServiceAccessor serviceAccessor;
	private final ContainerCallback containerCallback;
	private final TargetMetaRequestsHolder targetMetaRequestsHolder;

	/**
	 * @param serviceAccessor
	 */
	public WebServiceAccessorImp(ServiceAccessor serviceAccessor, TargetMetaRequestsHolder targetMetaRequestsHolder,
			ContainerCallback containerCallback) {
		this.serviceAccessor = serviceAccessor;
		this.containerCallback = containerCallback;
		this.targetMetaRequestsHolder = targetMetaRequestsHolder;
	}

	/*
	 * 1. create event SessionContext object in httpsession 2. save the principle
	 * name in the session context.
	 * 
	 * the principle name the request.getPrincipleNme you can save more
	 * attribute in the SessionContext object by replcaing this class in
	 * container.xml
	 * 
	 * SessionContextInterceptor will get the SessionContext into targetObject
	 * implements SessionAcceptable
	 */
	public Object getService(RequestWrapper request) {
		ContainerWrapper cw = containerCallback.getContainerWrapper();
		SessionContextSetup sessionContextSetup = (SessionContextSetup) cw.lookup(ComponentKeys.SESSIONCONTEXT_SETUP);

		TargetMetaRequest targetMetaRequest = targetMetaRequestsHolder.getTargetMetaRequest();
		targetMetaRequest.setVisitableName(ComponentKeys.SESSIONCONTEXT_FACTORY);
		ComponentVisitor componentVisitor = targetMetaRequest.getComponentVisitor();
		SessionContext sessionContext = (SessionContext) componentVisitor.createSessionContext();

		targetMetaRequest.setSessionContext(sessionContext);
		sessionContextSetup.setup(sessionContext, request);
		return serviceAccessor.getService();
	}

	/**
	 * No Session , nothing to do
	 */
	public Object getService() {
		return serviceAccessor.getService();

	}

	/*
	 * 1. create event SessionContext object in httpsession 2. save the principle
	 * name in the session context. the principle name the
	 * request.getPrincipleNme you can save more attribute in the SessionContext
	 * object by replcaing this class in container.xml *
	 * SessionContextInterceptor will get the SessionContext into targetObject
	 * implements SessionAcceptable
	 */
	public Object execute(RequestWrapper request) throws Exception {
		ContainerWrapper cw = containerCallback.getContainerWrapper();
		if (!cw.isStart()) {
			Debug.logError("JdonFramework not yet started, please try later ", module);
			return null;
		}
		SessionContextSetup sessionContextSetup = (SessionContextSetup) cw.lookup(ComponentKeys.SESSIONCONTEXT_SETUP);

		TargetMetaRequest targetMetaRequest = targetMetaRequestsHolder.getTargetMetaRequest();
		ComponentVisitor componentVisitor = targetMetaRequest.getComponentVisitor();
		targetMetaRequest.setVisitableName(ComponentKeys.SESSIONCONTEXT_FACTORY);
		// create sessionContext and save sessionContext into HttpSession hold
		// it
		SessionContext sessionContext = (SessionContext) componentVisitor.createSessionContext();
		// save sesssionConext in targetMetaRequest
		targetMetaRequest.setSessionContext(sessionContext);
		// save remote IP or login account into this sesssionConext
		// * SessionContextInterceptor will get the SessionContext into
		// targetObject implements SessionAcceptable
		sessionContextSetup.setup(sessionContext, request);
		return serviceAccessor.executeService(cw);
	}

	public Object execute() throws Exception {
		ContainerWrapper cw = containerCallback.getContainerWrapper();
		if (!cw.isStart()) {
			Debug.logError("JdonFramework not yet started, please try later ", module);
			return null;
		}
		return serviceAccessor.executeService(cw);
	}

}
