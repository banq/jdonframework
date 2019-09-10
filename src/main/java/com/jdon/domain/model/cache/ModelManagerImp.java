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

package com.jdon.domain.model.cache;

import com.jdon.domain.advsior.ModelAdvisor;
import com.jdon.domain.model.injection.ModelProxyInjection;
import com.jdon.util.Debug;

/**
 * ModelManager implemention
 * 
 * @author banq
 */
public class ModelManagerImp implements ModelManager {

	public final static String module = ModelManagerImp.class.getName();

	private final ModelCacheManager modelCacheManager;

	private final ModelProxyInjection modelProxyInjection;

	private final ModelAdvisor modelAdvisor;

	public ModelManagerImp(ModelCacheManager modelCacheManager, ModelProxyInjection modelProxyInjection, ModelAdvisor modelAdvisor) {
		this.modelCacheManager = modelCacheManager;
		this.modelProxyInjection = modelProxyInjection;
		this.modelAdvisor = modelAdvisor;
	}

	/**
	 * add the model to the cache
	 * 
	 * @param modelKey
	 * @param model
	 */
	public Object addCache(ModelKey modelKey, Object model) {
		if ((modelKey == null) || (modelKey.getDataKey() == null) || modelKey.getModelClass() == null)
			return null;		 
		try {
			String modelClassName = modelKey.getModelClass().getName();
			// inject the Model's field
			modelProxyInjection.injectProperties(model);
			// create the proxy for the Model
			Object modelProxynew = modelAdvisor.createProxy(model);
			Object modelProxyexsit = modelCacheManager.saveCacheIfAbsent(modelKey.getDataKey(), modelClassName, modelProxynew);
			//多线程问题。
			//modelCacheManager.saveCache(modelKey.getDataKey(), modelClassName, model);
			return modelProxyexsit != null ? modelProxyexsit : modelProxynew;
		} catch (Exception e) {
			Debug.logError("addCache error:" + e, module);
			return null;
		}
		
	}

	/**
	 * add the model to the cache
	 */
	public void addCache(Object key, String className, Object model) {
		if (key == null)
			return;
		modelCacheManager.saveCache(key, className, model);

	}

	/**
	 * get the model instance from the cache
	 */

	public Object getCache(ModelKey modelKey) {
		String modelClassName = null;
		if (modelKey.getModelClass() != null) {
			modelClassName = modelKey.getModelClass().getName();
			return modelCacheManager.getCache(modelKey.getDataKey(), modelClassName);
		} else
			return null;

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

	@Override
	public boolean containInCache(ModelKey modelKey) {
		if (modelKey == null || modelKey.getModelClass() == null)
			return false;
		return modelCacheManager.containInCache(modelKey.getDataKey(), modelKey.getModelClass().getName());
	}

}
