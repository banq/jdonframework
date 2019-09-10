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

package com.jdon.model.handler;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.jdon.annotation.Component;
import com.jdon.bussinessproxy.meta.MethodMetaArgs;
import com.jdon.controller.context.AppContextWrapper;
import com.jdon.controller.context.RequestWrapper;
import com.jdon.controller.context.web.HttpServletRequestWrapper;
import com.jdon.controller.context.web.ServletContextWrapper;
import com.jdon.controller.events.Event;
import com.jdon.controller.events.EventModel;
import com.jdon.controller.service.Service;
import com.jdon.controller.service.ServiceFacade;
import com.jdon.model.ModelForm;
import com.jdon.model.ModelHandler;
import com.jdon.util.Debug;

/**
 * it is event meta ModelHandler, all methods are finished by the definition in
 * jdonframework.xml
 * 
 * XmlModelHandler is loaded by XmlHandlerClassFactory from container.xml
 * 
 * XmlModelHandler is the client of container, XmlModelHandler will be made many
 * instance by HandlerObjectFactory.
 * 
 * @author banq
 */
@Component
public class XmlModelHandler extends ModelHandler {
	private final static String module = XmlModelHandler.class.getName();

	private final HandlerMethodMetaArgsFactory maFactory;;

	private final ServiceFacade serviceFacade;

	/**
	 * @param service
	 */
	public XmlModelHandler() {
		this.maFactory = new HandlerMethodMetaArgsFactory();
		this.serviceFacade = new ServiceFacade();
	}

	/**
	 * if your application need initialize the ModelForm, this method is event
	 * option. extends thie method.
	 */
	public Object initModelIF(EventModel em, ModelForm form, HttpServletRequest request) throws Exception {
		Object result = null;
		try {
			HandlerMetaDef hm = this.modelMapping.getHandlerMetaDef();
			String serviceName = hm.getServiceRef();
			Debug.logVerbose("[JdonFramework] construct the CRUD method for the service:" + serviceName, module);
			MethodMetaArgs methodMetaArgs = maFactory.createinitMethod(hm, em);
			RequestWrapper requestW = new HttpServletRequestWrapper(request);
			Service service = serviceFacade.getService(requestW.getContextHolder().getAppContextHolder());
			if (methodMetaArgs != null)
				result = service.execute(serviceName, methodMetaArgs, requestW);
		} catch (Exception e) {
			Debug.logError("[JdonFramework] initModel error: " + e, module);
			throw new Exception(e);
		}
		return result;
	}

	public Object findModelIF(Object keyValue, HttpServletRequest request) throws java.lang.Exception {
		Object result = null;
		try {
			HandlerMetaDef hm = this.modelMapping.getHandlerMetaDef();
			String serviceName = hm.getServiceRef();
			Debug.logVerbose("[JdonFramework] construct the CRUD method for the service:" + serviceName, module);
			MethodMetaArgs methodMetaArgs = maFactory.createGetMethod(hm, keyValue);
			if (methodMetaArgs.getMethodName() == null)
				throw new Exception("no configure findMethod value, but now you call it: ");
			RequestWrapper requestW = new HttpServletRequestWrapper(request);
			Service service = serviceFacade.getService(requestW.getContextHolder().getAppContextHolder());
			result = service.execute(serviceName, methodMetaArgs, requestW);
		} catch (Exception e) {
			Debug.logError("[JdonFramework] findModelByKey error: " + e + " maybe not configure getMethod", module);
			throw new Exception(e);
		}
		Debug.logVerbose("[JdonFramework] result type:" + result.getClass().getName(), module);
		return result;
	}

	public void serviceAction(EventModel em, HttpServletRequest request) throws java.lang.Exception {
		Debug.logVerbose("[JdonFramework] enter the serviceAction ", module);
		try {
			HandlerMetaDef hm = this.modelMapping.getHandlerMetaDef();
			String serviceName = hm.getServiceRef();
			MethodMetaArgs methodMetaArgs = null;
			switch (em.getActionType()) {
			case Event.CREATE:
				Debug.logVerbose("[JdonFramework] construct the CRUD method for the service:" + serviceName, module);
				methodMetaArgs = maFactory.createCreateMethod(hm, em);
				break;
			case Event.EDIT:
				Debug.logVerbose("[JdonFramework] construct the CRUD method for the service:" + serviceName, module);
				methodMetaArgs = maFactory.createUpdateMethod(hm, em);
				break;
			case Event.DELETE:
				Debug.logVerbose("[JdonFramework] construct the CRUD method for the service:" + serviceName, module);
				methodMetaArgs = maFactory.createDeleteMethod(hm, em);
				break;
			default:
				Debug.logVerbose("[JdonFramework] construct the command method for the service:" + serviceName, module);
				methodMetaArgs = maFactory.createDirectMethod(em.getActionName(), new Object[] { em });
			}
			Debug.logVerbose(" execute the method: " + methodMetaArgs.getMethodName() + " for the service: " + serviceName, module);
			RequestWrapper requestW = new HttpServletRequestWrapper(request);
			Service service = serviceFacade.getService(requestW.getContextHolder().getAppContextHolder());
			service.execute(serviceName, methodMetaArgs, requestW);
		} catch (Exception ex) {
			Debug.logError("[JdonFramework] serviceAction Error: " + ex, module);
			throw new Exception(" serviceAction Error:" + ex);
		}

	}

	/**
	 * if your application need initialize the ModelForm, this method is event
	 * option. extends thie method.
	 */
	public Object initModelIF(EventModel em, ModelForm form, ServletContext scontext) throws Exception {
		Object result = null;
		try {
			HandlerMetaDef hm = this.modelMapping.getHandlerMetaDef();
			String serviceName = hm.getServiceRef();
			Debug.logVerbose("[JdonFramework] construct the CRUD method for the service:" + serviceName, module);
			MethodMetaArgs methodMetaArgs = maFactory.createinitMethod(hm, em);
			AppContextWrapper acw = new ServletContextWrapper(scontext);
			Service service = serviceFacade.getService(acw);
			if (methodMetaArgs != null)
				result = service.execute(serviceName, methodMetaArgs, acw);
		} catch (Exception e) {
			Debug.logError("[JdonFramework] initModel error: " + e, module);
			throw new Exception(e);
		}
		return result;
	}

	public Object findModelIF(Object keyValue, ServletContext scontext) throws java.lang.Exception {
		Object result = null;
		try {
			HandlerMetaDef hm = this.modelMapping.getHandlerMetaDef();
			String serviceName = hm.getServiceRef();
			Debug.logVerbose("[JdonFramework] construct the CRUD method for the service:" + serviceName, module);
			MethodMetaArgs methodMetaArgs = maFactory.createGetMethod(hm, keyValue);
			if (methodMetaArgs.getMethodName() == null)
				throw new Exception("no configure findMethod value, but now you call it: ");
			AppContextWrapper acw = new ServletContextWrapper(scontext);
			Service service = serviceFacade.getService(acw);
			result = service.execute(serviceName, methodMetaArgs, acw);
		} catch (Exception e) {
			Debug.logError("[JdonFramework] findModelByKey error: " + e + " maybe not configure getMethod", module);
			throw new Exception(e);
		}
		Debug.logVerbose("[JdonFramework] result type:" + result.getClass().getName(), module);
		return result;
	}

	public void serviceAction(EventModel em, ServletContext scontext) throws java.lang.Exception {
		Debug.logVerbose("[JdonFramework] enter the serviceAction ", module);
		try {
			HandlerMetaDef hm = this.modelMapping.getHandlerMetaDef();
			String serviceName = hm.getServiceRef();
			MethodMetaArgs methodMetaArgs = null;
			switch (em.getActionType()) {
			case Event.CREATE:
				Debug.logVerbose("[JdonFramework] construct the CRUD method for the service:" + serviceName, module);
				methodMetaArgs = maFactory.createCreateMethod(hm, em);
				break;
			case Event.EDIT:
				Debug.logVerbose("[JdonFramework] construct the CRUD method for the service:" + serviceName, module);
				methodMetaArgs = maFactory.createUpdateMethod(hm, em);
				break;
			case Event.DELETE:
				Debug.logVerbose("[JdonFramework] construct the CRUD method for the service:" + serviceName, module);
				methodMetaArgs = maFactory.createDeleteMethod(hm, em);
				break;
			default:
				Debug.logVerbose("[JdonFramework] construct the command method for the service:" + serviceName, module);
				methodMetaArgs = maFactory.createDirectMethod(em.getActionName(), new Object[] { em });
			}
			Debug.logVerbose(" execute the method: " + methodMetaArgs.getMethodName() + " for the service: " + serviceName, module);
			AppContextWrapper acw = new ServletContextWrapper(scontext);
			Service service = serviceFacade.getService(acw);
			service.execute(serviceName, methodMetaArgs, acw);
		} catch (Exception ex) {
			Debug.logError("[JdonFramework] serviceAction Error: " + ex, module);
			throw new Exception(" serviceAction Error:" + ex);
		}

	}

}
