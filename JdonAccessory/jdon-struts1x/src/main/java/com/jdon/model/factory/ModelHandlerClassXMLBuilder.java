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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.jdon.annotation.Component;
import com.jdon.container.access.xml.AppConfigureCollection;
import com.jdon.container.pico.Startable;
import com.jdon.model.config.ConfigureReader;
import com.jdon.model.config.ModelMapping;
import com.jdon.util.Debug;

/**
 * 根据modelmapping.xml生产相应的实例 下面两个方法预先需要执行： loadMapping(); //获取xml
 * createModelClass();//预先创建一些class
 * 
 * ModelFactory是有状态的类。
 * 
 * @author banq
 */
@Component
public final class ModelHandlerClassXMLBuilder implements Startable {
	public final static String module = ModelHandlerClassXMLBuilder.class.getName();

	private final ModelHandlerClassFactory modelHandlerClassFactory;

	private final Map configLoadedList = new HashMap();

	/**
	 * key is formName /value is ModelMapping
	 */
	private final Map mps = new HashMap();

	/**
	 * key is formName /value is Model class
	 */
	private final Map modelClasses = new HashMap();

	/**
	 * key is formName /value is ModelHandler class
	 */
	private final Map handlerClasses = new HashMap();

	public ModelHandlerClassXMLBuilder(AppConfigureCollection appConfigureFiles, ModelHandlerClassFactory handlerClassFactory) {
		this.modelHandlerClassFactory = handlerClassFactory;
		Iterator iter = appConfigureFiles.getConfigList().iterator();
		while (iter.hasNext()) {
			String configFile = (String) iter.next();
			if (!configLoadedList.containsKey(configFile)) {
				ConfigureReader configureReader = new ConfigureReader(configFile);
				Debug.logVerbose("[JdonFramework]init configFile = " + configFile, module);
				configLoadedList.put(configFile, configureReader);
			}
		}
	}

	/**
	 * 将所有的ConfigureLoader包含的内容合并在一起。 本方法相当于start() 参考 {@link #detroy()} method
	 * 
	 */
	public void start() {
		try {
			Iterator iter = configLoadedList.keySet().iterator();
			while (iter.hasNext()) {
				String configFile = (String) iter.next();
				Debug.logVerbose("[JdonFramework] start configFile = " + configFile, module);
				ConfigureReader configureLoader = (ConfigureReader) configLoadedList.get(configFile);
				Map modelMappings = configureLoader.load();
				mps.putAll(modelMappings);

				Iterator mpsIter = modelMappings.keySet().iterator();
				while (mpsIter.hasNext()) {
					String formName = (String) mpsIter.next();
					build(formName);
				}
			}
			configLoadedList.clear();
		} catch (Exception ex) {
			Debug.logError("[JdonFramework] !!!!!!!framework started error: " + ex, module);
		}
	}

	private void build(String formName) {
		Debug.logVerbose("[JdonFramework] start build model for formName = " + formName, module);
		ModelMapping modelMapping = (ModelMapping) mps.get(formName);
		try {

			Class modelClass = modelHandlerClassFactory.createModel(modelMapping);
			modelClasses.put(formName, modelClass);

			Class keyClassType = modelHandlerClassFactory.createModelKeyClassType(modelMapping, modelClass);
			modelMapping.setKeyClassType(keyClassType);

			handlerClasses.put(formName, modelHandlerClassFactory.createHandler(modelMapping));
		} catch (Exception e) {
			Debug.logError("[JdonFramework] build error: " + e, module);
		}
	}

	/**
	 * 清除内存
	 */
	public void stop() {
		mps.clear();
		configLoadedList.clear();
		modelClasses.clear();
		handlerClasses.clear();
	}

	public ModelMapping getModelMapping(String formName) {
		return (ModelMapping) mps.get(formName);
	}

	/**
	 * @return Returns the handlerClasses.
	 */
	public Class getHandlerClasses(String formName) {
		return (Class) handlerClasses.get(formName);
	}

	/**
	 * @return Returns the modelClasses.
	 */
	public Class getModelClasses(String formName) {
		return (Class) modelClasses.get(formName);
	}

}
