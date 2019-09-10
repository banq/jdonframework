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

package com.jdon.model;

import com.jdon.annotation.Component;
import com.jdon.controller.model.ModelUtil;
import com.jdon.domain.model.cache.ModelCacheManager;
import com.jdon.domain.model.cache.ModelKey;
import com.jdon.domain.model.injection.ModelProxyInjection;
import com.jdon.model.config.ModelMapping;
import com.jdon.model.factory.ModelHandlerClassXMLBuilder;
import com.jdon.model.handler.HandlerObjectFactory;
import com.jdon.util.Debug;

/**
 * ModelManager implemention
 * 
 * @author banq
 */
@Component("modelHandlerManager")
public class ModelHandlerManagerImp implements ModelHandlerManager {

	public final static String module = ModelHandlerManagerImp.class.getName();

	private final ModelHandlerClassXMLBuilder modelFactory;
	private final ModelCacheManager modelCacheManager;
	private final HandlerObjectFactory handlerObjectFactory;
	private final ModelProxyInjection modelProxyInjection;

	public ModelHandlerManagerImp(ModelHandlerClassXMLBuilder modelXmlLoader, ModelCacheManager modelCacheManager,
			HandlerObjectFactory handlerObjectFactory, ModelProxyInjection modelProxyInjection) {
		this.modelFactory = modelXmlLoader;
		this.modelCacheManager = modelCacheManager;
		this.handlerObjectFactory = handlerObjectFactory;
		this.modelProxyInjection = modelProxyInjection;
	}

	/**
	 * borrow event Handler instance from Modelhandler pool
	 */
	public ModelHandler borrowtHandlerObject(String formName) {
		ModelHandler modelHandler = null;
		try {
			modelHandler = handlerObjectFactory.borrowHandlerObject(formName);
			modelHandler.setModelMapping(modelFactory.getModelMapping(formName));
		} catch (Exception ex) {
			Debug.logError("[JdonFramework]can't get the modelHandler for the formName " + formName, module);
			returnHandlerObject(modelHandler);
		}
		return modelHandler;
	}

	/**
	 * return the Handler instance.
	 * 
	 */
	public void returnHandlerObject(ModelHandler modelHandler) {
		if (modelHandler == null)
			return;
		try {
			handlerObjectFactory.returnHandlerObject(modelHandler);
		} catch (Exception ex) {
			Debug.logError("[JdonFramework] return modelHandler error" + ex, module);
		}

	}

	/**
	 * declareted : get event Model instance from the model pool the model poo size
	 * is decided by the PoolParameter configured in container.xml
	 * 
	 * 　
	 */
	public Object getModelObject(String formName) {
		return makeModelObject(formName);
	}

	/**
	 * create model instance from the model class that read from the xml
	 * configure.
	 * 
	 * @param formName
	 * @return
	 * @throws Exception
	 */
	private Object makeModelObject(String formName) {
		Object object = null;
		Class modelClass = null;
		try {
			modelClass = (Class) modelFactory.getModelClasses(formName);
			if (modelClass == null) {
				throw new Exception(" not found the model in config xml, formName=" + formName);
			}
			object = modelClass.newInstance();
			// inject
			modelProxyInjection.injectProperties(object);
		} catch (Exception e) {
			Debug.logError("[JdonFramework]--> call Model: " + modelClass + " error:" + e, module);
		}
		return object;
	}

	/**
	 * add the model to the cache
	 * 
	 * @param modelKey
	 * @param model
	 */
	public void addCache(ModelKey modelKey, Object model) {
		if ((modelKey == null) || (modelKey.getDataKey() == null))
			return;
		String modelClassName = null;
		try {
			if (modelKey.getModelClass() == null) {
				ModelMapping modelMapping = this.modelFactory.getModelMapping(modelKey.getFormName());
				modelClassName = modelMapping.getClassName();
			} else
				modelClassName = modelKey.getModelClass().getName();
			if (ModelUtil.isModel(model.getClass()))
				modelCacheManager.saveCache(modelKey.getDataKey(), modelClassName, model);
			else
				Debug.logError("addCache error:" + model.getClass() + " not ModelIF or @Model", module);
		} catch (Exception e) {
			Debug.logError("addCache error:" + e, module);
		}

	}

	/**
	 * add the model to the cache
	 */
	public void addCache(Object key, String className, Object model) {
		if (key == null)
			return;
		if (ModelUtil.isModel(model.getClass()))
			modelCacheManager.saveCache(key, className, model);
		else
			Debug.logError("addCache error:" + model.getClass() + " not ModelIF or @Model", module);

	}

	/**
	 * get the model instance from the cache
	 */

	public Object getCache(ModelKey modelKey) {
		String modelClassName = null;
		if (modelKey.getModelClass() == null) {
			ModelMapping modelMapping = this.modelFactory.getModelMapping(modelKey.getFormName());
			modelClassName = modelMapping.getClassName();
		} else
			modelClassName = modelKey.getModelClass().getName();
		return modelCacheManager.getCache(modelKey.getDataKey(), modelClassName);

	}

	/**
	 * get the model instance from the cache
	 */

	public Object getCache(Object key, String className) {
		return modelCacheManager.getCache(key, className);

	}

	/**
	 * remove the model instance from the cache
	 */

	public void removeCache(Object dataKey) throws Exception {
		modelCacheManager.removeCache(dataKey);
	}

	/**
	 * clear all models in the cache.
	 */
	public void clearCache() {
		modelCacheManager.clearCache();
	}

	public boolean isNull(String s) {
		boolean isNull = false;
		if (s == null)
			isNull = true;
		else if (s.equals(""))
			isNull = true;
		else if (s.equals("null"))
			isNull = true;
		return isNull;
	}

}
