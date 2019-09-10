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

package com.jdon.model.factory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import com.jdon.annotation.Component;
import com.jdon.model.ModelHandler;
import com.jdon.model.config.ModelMapping;
import com.jdon.model.handler.HandlerMetaDef;
import com.jdon.util.Debug;

@Component
public class ModelHandlerClassFactoryXmlImp implements ModelHandlerClassFactory {

	private final static String module = ModelHandlerClassFactoryXmlImp.class.getName();

	private final ModelHandler modelHandler;

	public ModelHandlerClassFactoryXmlImp(ModelHandler modelHandler) {
		this.modelHandler = modelHandler;
	}

	public Class createModel(ModelMapping modelMapping) {
		Debug.logVerbose("[JdonFramework]  create Model :", module);
		String formName = modelMapping.getFormName();
		String className = modelMapping.getClassName();
		Class newClass = null;
		try {
			Debug.logVerbose("[JdonFramework]create model class, key=" + formName + " value=" + className, module);
			newClass = Thread.currentThread().getContextClassLoader().loadClass(className);
			if (newClass == null) {
				throw new Exception(" classLoader problem: " + " please check your config xml or check your pakcage");
			}
		} catch (Exception ex) {
			className = className.replaceAll(" ", "[ ]");
			Debug.logError("[JdonFramework] className=" + className + " error:" + ex, module);
		}
		return newClass;
	}

	public Class createModelKeyClassType(ModelMapping modelMapping, Class modelClass) {
		String keyName = modelMapping.getKeyName();
		Debug.logVerbose("[JdonFramework]  createModelKeyClassType the keyName is  " + keyName, module);
		return getKeyClassType(modelClass, keyName);
	}

	private Class getKeyClassType(Class beanClasses, String propertyName) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(beanClasses);
			PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
			for (int i = 0; i < pds.length; i++) {
				PropertyDescriptor pd = pds[i];
				if (pd.getName().equalsIgnoreCase(propertyName)) {
					Debug.logVerbose("[JdonFramework]found the key Class Type==" + pd.getPropertyType().getName(), module);
					return pd.getPropertyType();
				}
			}
		} catch (Exception e) {
			Debug.logError(e);
		}
		Debug.logVerbose("[JdonFramework]not found the key Class Type, propertyName=" + propertyName, module);
		return Object.class;
	}

	public Class createHandler(ModelMapping modelMapping) {
		Debug.logVerbose("[JdonFramework] create Handler :", module);
		String formName = modelMapping.getFormName();
		String handlerClassName = modelMapping.getHandler();
		HandlerMetaDef handlerMetaDef = modelMapping.getHandlerMetaDef();
		Class newClass = null;
		try {
			if (handlerMetaDef != null) { // there is handler/service configure
				if (handlerClassName == null) {// there is no handler ClassName
												// configure
					// 使用框架中配置的modelHandler，如XmlModelHandler
					handlerClassName = modelHandler.getClass().getName();
					modelMapping.setHandler(handlerClassName);
					newClass = modelHandler.getClass();
				}
			}
			if (handlerClassName != null) {
				newClass = Thread.currentThread().getContextClassLoader().loadClass(handlerClassName);
			}
			Debug.logVerbose("[JdonFramework]create Handler class, key=" + formName + " value=" + handlerClassName, module);
			if (newClass == null) {
				throw new Exception(" classLoader problem: " + " please check your config xml or check your pakcage");
			}
		} catch (Exception ex) {
			Debug.logError("[JdonFramework]  error:" + ex, module);
		}
		return newClass;
	}

}
