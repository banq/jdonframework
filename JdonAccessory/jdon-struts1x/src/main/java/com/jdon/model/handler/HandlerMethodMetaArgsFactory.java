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

import com.jdon.annotation.Component;
import com.jdon.bussinessproxy.meta.MethodMetaArgs;
import com.jdon.controller.events.EventModel;
import com.jdon.util.Debug;

/**
 * Meta Method Factory. in jdonframework.xml:
 * 
 * <handler> <service ref="accountService"> <initMethod name="initAccount" />
 * <getMethod name="getAccount" /> <createMethod name="insertAccount" />
 * <updateMethod name="updateAccount" /> <deleteMethod name="deleteAccount" />
 * </service> </handler>
 * 
 * 
 * 
 * 
 * @author banq
 */
@Component
public class HandlerMethodMetaArgsFactory {
	private final static String module = HandlerMethodMetaArgsFactory.class.getName();

	/**
	 * create init method
	 * 
	 * @param handlerMetaDef
	 * @return MethodMetaArgs instance
	 */
	public MethodMetaArgs createinitMethod(HandlerMetaDef handlerMetaDef, EventModel em) {
		String p_methodName = handlerMetaDef.getInitMethod();
		if (p_methodName == null)
			return null;
		return createCRUDMethodMetaArgs(p_methodName, em);
	}

	/**
	 * create find method the service's find method parameter type must be
	 * String type
	 * 
	 * @param handlerMetaDef
	 * @param keyValue
	 * @return MethodMetaArgs instance
	 */
	public MethodMetaArgs createGetMethod(HandlerMetaDef handlerMetaDef, Object keyValue) {

		String p_methodName = handlerMetaDef.getFindMethod();
		if (p_methodName == null) {
			Debug.logError("[JdonFramework] not configure the findMethod parameter: <getMethod name=XXXXX /> ", module);
		}
		if (keyValue == null) {
			Debug.logError("[JdonFramework] not found model's key value:" + handlerMetaDef.getModelMapping().getKeyName()
					+ "=? in request parameters", module);
		}
		return createCRUDMethodMetaArgs(p_methodName, keyValue);
	}

	/**
	 * create insert/create method the service/s method parameter type must be
	 * EventModel type;
	 * 
	 * @param handlerMetaDef
	 * @param em
	 * @return MethodMetaArgs instance
	 */
	public MethodMetaArgs createCreateMethod(HandlerMetaDef handlerMetaDef, EventModel em) {

		String p_methodName = handlerMetaDef.getCreateMethod();
		if (p_methodName == null) {
			Debug.logError("[JdonFramework] not configure the createMethod parameter: <createMethod name=XXXXX /> ", module);
		}
		return createCRUDMethodMetaArgs(p_methodName, em);
	}

	/**
	 * 
	 * create update method the service/s method parameter type must be
	 * EventModel type;
	 * 
	 * @param handlerMetaDef
	 * @param em
	 * @return MethodMetaArgs instance
	 */
	public MethodMetaArgs createUpdateMethod(HandlerMetaDef handlerMetaDef, EventModel em) {

		String p_methodName = handlerMetaDef.getUpdateMethod();
		if (p_methodName == null) {
			Debug.logError("[JdonFramework] not configure the updateMethod parameter: <updateMethod name=XXXXX /> ", module);
		}
		return createCRUDMethodMetaArgs(p_methodName, em);
	}

	/**
	 * create update method the service/s method parameter type must be
	 * EventModel type;
	 * 
	 * @param handlerMetaDef
	 * @param em
	 *            EventModel
	 * @return MethodMetaArgs instance
	 */
	public MethodMetaArgs createDeleteMethod(HandlerMetaDef handlerMetaDef, EventModel em) {

		String p_methodName = handlerMetaDef.getDeleteMethod();
		if (p_methodName == null) {
			Debug.logError("[JdonFramework] not configure the deleteMethod parameter: <deleteMethod name=XXXXX /> ", module);
		}

		return createCRUDMethodMetaArgs(p_methodName, em);
	}

	public MethodMetaArgs createDirectMethod(String methodName, Object[] methodParams) {
		MethodMetaArgs methodMetaArgs = null;
		try {
			if (methodName == null)
				throw new Exception("no configure method value, but now you call it: ");

			Debug.logVerbose("[JdonFramework] construct " + methodName, module);
			Class[] paramTypes = new Class[methodParams.length];
			Object[] p_args = new Object[methodParams.length];
			for (int i = 0; i < methodParams.length; i++) {
				paramTypes[i] = methodParams[i].getClass();
				p_args[i] = methodParams[i];
				Debug.logVerbose("[JdonFramework], parameter type:" + paramTypes[i] + " and parameter value:" + p_args[i], module);
			}
			methodMetaArgs = new MethodMetaArgs(methodName, paramTypes, p_args);

		} catch (Exception ex) {
			Debug.logError("[JdonFramework] createDirectMethod error: " + ex, module);
		}
		return methodMetaArgs;
	}

	private MethodMetaArgs createCRUDMethodMetaArgs(String p_methodName, Object args) {
		MethodMetaArgs methodMetaArgs = null;
		try {
			Class[] paramTypes = new Class[1];
			paramTypes[0] = args.getClass();
			Object[] p_args = new Object[1];
			p_args[0] = args;
			methodMetaArgs = new MethodMetaArgs(p_methodName, paramTypes, p_args);
			Debug.logVerbose("[JdonFramework] construct " + methodMetaArgs.getMethodName() + ", parameter type:" + paramTypes[0], module);
		} catch (Exception ex) {
			Debug.logError("[JdonFramework] create CRUD method error: " + ex, module);
		}
		return methodMetaArgs;
	}

}
