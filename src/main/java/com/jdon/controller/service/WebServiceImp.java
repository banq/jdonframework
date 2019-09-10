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

import com.jdon.bussinessproxy.TargetMetaDef;
import com.jdon.bussinessproxy.meta.MethodMetaArgs;
import com.jdon.container.ContainerWrapper;
import com.jdon.container.access.TargetMetaRequest;
import com.jdon.container.access.UserTargetMetaDefFactory;
import com.jdon.container.finder.ContainerCallback;
import com.jdon.controller.context.AppContextWrapper;
import com.jdon.controller.context.RequestWrapper;
import com.jdon.util.Debug;

/**
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 * 
 */
public class WebServiceImp implements Service {

	private final static String module = WebServiceImp.class.getName();
	private final ContainerCallback containerCallback;
	private final WebServiceAccessor webServiceAccessor;
	private final UserTargetMetaDefFactory userTargetMetaDefFactory;

	public WebServiceImp(WebServiceAccessor webServiceAccessor, ContainerCallback containerCallback, UserTargetMetaDefFactory userTargetMetaDefFactory) {
		this.webServiceAccessor = webServiceAccessor;
		this.containerCallback = containerCallback;
		this.userTargetMetaDefFactory = userTargetMetaDefFactory;
	}

	/**
	 * running the service, and return the result
	 */
	public Object execute(String name, MethodMetaArgs methodMetaArgs, RequestWrapper request) throws Exception {
		if ((methodMetaArgs == null) || (methodMetaArgs.getMethodName() == null)) {
			Debug.logWarning(" methodMetaArgs is null. cann't invoke service.execute");
		}
		Debug.logVerbose("++++++++++++++++++++++++++++++<begin: invoking from jdonframework.xml", module);
		Debug.logVerbose("+++++++++++++++execute new service='" + name + "' method='" + methodMetaArgs.getMethodName() + "'", module);
		ContainerWrapper cw = containerCallback.getContainerWrapper();
		TargetMetaDef targetMetaDef = userTargetMetaDefFactory.getTargetMetaDef(name, cw);
		if (targetMetaDef == null)
			return null;
		Object result = execute(targetMetaDef, methodMetaArgs, request);
		Debug.logVerbose("+++++++++++++++execute service='" + name + "' method='" + methodMetaArgs.getMethodName() + "' successfully!", module);
		Debug.logVerbose("++++++++++++++++++++++++++++++<end:", module);
		return result;
	}

	/**
	 * running the service, and return the result
	 */
	public Object execute(TargetMetaDef targetMetaDef, MethodMetaArgs methodMetaArgs, RequestWrapper request) throws Exception {
		Debug.logVerbose("[JdonFramework]enter service execution core ", module);
		userTargetMetaDefFactory.createTargetMetaRequest(targetMetaDef, request.getContextHolder());
		TargetMetaRequest targetMetaRequest = userTargetMetaDefFactory.targetMetaRequestsHolder.getTargetMetaRequest();
		targetMetaRequest.setMethodMetaArgs(methodMetaArgs);
		return webServiceAccessor.execute(request);
	}

	/**
	 * running the service, and return the result without session
	 */
	public Object execute(String name, MethodMetaArgs methodMetaArgs, AppContextWrapper acw) throws Exception {
		if ((methodMetaArgs == null) || (methodMetaArgs.getMethodName() == null)) {
			Debug.logWarning(" methodMetaArgs is null. cann't invoke service.execute");
		}
		Debug.logVerbose("++++++++++++++++++++++++++++++<begin: invoking from jdonframework.xml", module);
		Debug.logVerbose("+++++++++++++++execute new service='" + name + "' method='" + methodMetaArgs.getMethodName() + "'", module);
		ContainerWrapper cw = containerCallback.getContainerWrapper();
		TargetMetaDef targetMetaDef = userTargetMetaDefFactory.getTargetMetaDef(name, cw);
		if (targetMetaDef == null)
			return null;
		Object result = execute(targetMetaDef, methodMetaArgs, acw);
		Debug.logVerbose("+++++++++++++++execute service='" + name + "' method='" + methodMetaArgs.getMethodName() + "' successfully!", module);
		Debug.logVerbose("++++++++++++++++++++++++++++++<end:", module);
		return result;
	}

	/**
	 * running the service, and return the result
	 */
	public Object execute(TargetMetaDef targetMetaDef, MethodMetaArgs methodMetaArgs, AppContextWrapper acw) throws Exception {
		Debug.logVerbose("[JdonFramework]enter service execution core ", module);
		userTargetMetaDefFactory.createTargetMetaRequest(targetMetaDef, acw);
		TargetMetaRequest targetMetaRequest = userTargetMetaDefFactory.targetMetaRequestsHolder.getTargetMetaRequest();
		targetMetaRequest.setMethodMetaArgs(methodMetaArgs);
		return webServiceAccessor.execute();
	}
}
