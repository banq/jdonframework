/**
 * Copyright 2003-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jdon.controller.service;

import com.jdon.bussinessproxy.TargetMetaDef;
import com.jdon.container.ContainerWrapper;
import com.jdon.container.access.UserTargetMetaDefFactory;
import com.jdon.container.finder.ContainerCallback;
import com.jdon.controller.context.AppContextWrapper;
import com.jdon.controller.context.ContextHolder;
import com.jdon.controller.context.RequestWrapper;
import com.jdon.util.Debug;

/**
 * the defualt concrete class of ServiceFactory by this class, we can get a
 * service instance; @ {@link com.jdon.controller.service.WebServiceDecorator}
 * 
 * @author banq
 * @see {@link ServiceFactory}
 */
public class WebServiceFactory implements ServiceFactory {

	private final static String module = WebServiceFactory.class.getName();

	private final ContainerCallback containerCallback;
	private final WebServiceAccessor webServiceAccessor;
	private final UserTargetMetaDefFactory userTargetMetaDefFactory;

	public WebServiceFactory(WebServiceAccessor webServiceAccessor, ContainerCallback containerCallback,
			UserTargetMetaDefFactory userTargetMetaDefFactory) {
		this.webServiceAccessor = webServiceAccessor;
		this.containerCallback = containerCallback;
		this.userTargetMetaDefFactory = userTargetMetaDefFactory;
	}

	public Object getService(String name, RequestWrapper request) {
		ContainerWrapper cw = containerCallback.getContainerWrapper();
		if (!cw.isStart()) {
			Debug.logError("JdonFramework not yet started, please try later ", module);
			return null;
		}
		TargetMetaDef targetMetaDef = userTargetMetaDefFactory.getTargetMetaDef(name, cw);
		if (targetMetaDef == null)
			return null;
		Object result = getService(targetMetaDef, request);
		Debug.logVerbose("<===============>found service='" + name + "'  instance and return  it successfully!", module);
		return result;
	}

	/**
	 * get a service instance the service must have a interface and implements
	 * it.
	 * 
	 */
	public Object getService(TargetMetaDef targetMetaDef, RequestWrapper request) {
		userTargetMetaDefFactory.createTargetMetaRequest(targetMetaDef, request.getContextHolder());
		return webServiceAccessor.getService(request);
	}

	public Object getService(TargetMetaDef targetMetaDef, AppContextWrapper acw) {
		userTargetMetaDefFactory.createTargetMetaRequest(targetMetaDef, new ContextHolder(acw, null));
		return webServiceAccessor.getService();
	}

	public Object getService(String name, AppContextWrapper acw) {
		ContainerWrapper cw = containerCallback.getContainerWrapper();
		if (!cw.isStart()) {
			Debug.logError("JdonFramework not yet started, please try later ", module);
			return null;
		}
		TargetMetaDef targetMetaDef = userTargetMetaDefFactory.getTargetMetaDef(name, cw);
		if (targetMetaDef == null)
			return null;
		Debug.logVerbose("[JdonFramework] service=" + name + "  found, and now return the service instance", module);
		Object result = getService(targetMetaDef, acw);
		return result;
	}

}
