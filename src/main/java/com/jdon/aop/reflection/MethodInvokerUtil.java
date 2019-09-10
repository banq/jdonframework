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
package com.jdon.aop.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.jdon.bussinessproxy.TargetMetaDef;
import com.jdon.bussinessproxy.target.TargetServiceFactory;
import com.jdon.container.access.TargetMetaRequest;
import com.jdon.container.access.TargetMetaRequestsHolder;
import com.jdon.container.finder.ComponentKeys;
import com.jdon.container.visitor.ComponentVisitor;
import com.jdon.util.Debug;

/**
 * tools for method invoke
 * 
 * @author <event href="mailto:banqiao@jdon.com">banq </event>
 * 
 */
public class MethodInvokerUtil {
	private final static String module = MethodInvokerUtil.class.getName();

	private final TargetMetaRequestsHolder targetMetaRequestsHolder;

	public MethodInvokerUtil(TargetMetaRequestsHolder targetMetaRequestsHolder) {
		super();
		this.targetMetaRequestsHolder = targetMetaRequestsHolder;
	}

	/**
	 * the service execute by method reflection
	 * 
	 * @param method
	 * @param targetObj
	 * @param p_args
	 * @return
	 * @throws Throwable
	 */
	public Object execute(Method method, Object targetObj, Object[] p_args) throws Throwable {
		try {
			if ((method == null) || (targetObj == null))
				Debug.logError("[JdonFramework] no method or target, please check your configure", module);
			if (p_args == null)
				p_args = new Object[0];
			Debug.logVerbose("[JdonFramework] method invoke: " + targetObj.getClass().getName() + " method=" + method.getName(), module);
			Object result = method.invoke(targetObj, p_args);
			Debug.logVerbose("[JdonFramework] method invoke successfully ", module);
			return result;
		} catch (IllegalArgumentException iex) {
			String errorInfo = "Errors happened in your method:[" + targetObj.getClass().getName() + "." + method.getName() + "]";
			Debug.logError(errorInfo, module);
			errorInfo = "[JdonFramework] method invoke IllegalArgumentException: " + iex + " method argument type :[" + method.getParameterTypes()
					+ "], but method arguments value p_args type:" + p_args.getClass().getName();
			Debug.logError(errorInfo, module);
			throw new Throwable(errorInfo, iex);
		} catch (InvocationTargetException ex) {
			Debug.logError(ex);
			String errorInfo = "Errors happened in your method:[" + targetObj.getClass().getName() + "." + method.getName() + "]";
			Debug.logError(errorInfo, module);
			throw new Throwable(errorInfo);
		} catch (IllegalAccessException ex) {
			String errorInfo = "Errors happened in your method:[" + targetObj.getClass().getName() + "." + method.getName() + "]";
			Debug.logError(errorInfo, module);
			Debug.logError("[JdonFramework] method invoke IllegalAccessException: " + ex, module);
			throw new Throwable("access method:" + method + " " + ex, ex);
		} catch (Exception ex) {
			String errorInfo = "Errors happened in your method:[" + targetObj.getClass().getName() + "." + method.getName() + "]";
			Debug.logError(errorInfo, module);
			Debug.logError("[JdonFramework] method invoke error: " + ex, module);
			throw new Throwable(" method invoke error: " + ex);
		}

	}

	/**
	 * if target service is ejb object, cache it, so this function can active
	 * stateful session bean.
	 * 
	 * @param targetServiceFactory
	 * @param targetMetaDef
	 * @return
	 * @throws Exception
	 */
	public Object createTargetObject(TargetServiceFactory targetServiceFactory) {
		Debug.logVerbose("[JdonFramework] now getTargetObject by visitor ", module);
		Object targetObjRef = null;
		try {
			TargetMetaRequest targetMetaRequest = targetMetaRequestsHolder.getTargetMetaRequest();
			TargetMetaDef targetMetaDef = targetMetaRequest.getTargetMetaDef();
			if (targetMetaDef.isEJB()) { // cache the ejb object
				ComponentVisitor cm = targetMetaRequest.getComponentVisitor();
				targetMetaRequest.setVisitableName(ComponentKeys.TARGETSERVICE_FACTORY);
				Debug.logVerbose(ComponentKeys.TARGETSERVICE_FACTORY + " in action (cache)", module);
				targetObjRef = cm.visit();
			} else {
				Debug.logVerbose("[JdonFramework] not active targer service instance cache !!!!", module);
				targetObjRef = targetServiceFactory.create();
			}
		} catch (Exception e) {
			Debug.logError("[JdonFramework]createTargetObject error: " + e, module);
		}
		return targetObjRef;
	}

}
