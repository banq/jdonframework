/**
 * Copyright 2003-2006 the original author or authors. Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.jdon.domain.model.cache;

import com.jdon.container.pico.Startable;
import com.jdon.controller.cache.CacheKey;
import com.jdon.controller.cache.CacheKeyFactory;
import com.jdon.controller.cache.CacheManager;
import com.jdon.controller.model.ModelUtil;

public class ModelCacheManager implements Startable {

	protected CacheManager cacheManager;

	private final CacheKeyFactory cacheKeyFactory;

	public ModelCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
		this.cacheKeyFactory = new ModelCacheKeyFactory();
	}

	public void start() {
	}

	public void stop() {
		cacheManager.clear();
		cacheManager = null;
	}

	public Object getPModel(Class modelClass) {
		System.out.print("o=" + modelClass.getName());
		Class pClass = modelClass.getSuperclass();
		if (pClass == null)
			return modelClass;

		if (pClass.getName().equalsIgnoreCase(Object.class.getName()))
			return modelClass;

		return getPModel(pClass);
	}

	/**
	 * 将数据ID, Model类类型组合成Key, 将对应的Model保存到缓存
	 * 
	 * @param dataKey
	 * @param modelClassName
	 * @param model
	 */
	public void saveCache(Object dataKey, String modelClassName, Object model) {
		CacheKey cachKey = cacheKeyFactory.createCacheKey(dataKey.toString(), modelClassName);
		saveToCache(cachKey, model);
	}

	protected void saveToCache(CacheKey cachKey, Object model) {
		if (!cacheManager.containObject(cachKey))
			cacheManager.putObect(cachKey, model);
	}

	private Object getModelFromCache(CacheKey cachKey) {
		Object model = cacheManager.fetchObject(cachKey);
		if (model != null) {
			if (ModelUtil.isModified(model)) {
				removeCache(cachKey);
				model = null;
			}
		}
		return model;
	}

	/**
	 * 将数据ID, Model类类型组合成Key的对应Model从缓存中取出
	 * 
	 * @param dataKey
	 * @param modelClassName
	 * @return Model
	 */
	public Object getCache(Object dataKey, String modelClassName) {
		CacheKey cachKey = cacheKeyFactory.createCacheKey(dataKey.toString(), modelClassName);
		return getModelFromCache(cachKey);
	}

	public boolean containInCache(Object dataKey, String modelClassName) {
		CacheKey cachKey = cacheKeyFactory.createCacheKey(dataKey.toString(), modelClassName);
		return cacheManager.containObject(cachKey);
	}

	/**
	 * 清除缓存中该dataKey的相关所有缓存数据 dataKey即数据的主键ID数值
	 * 
	 * @param dataKey
	 * @param formName
	 */
	public void removeCache(Object dataKey) {
		cacheManager.removeCache(dataKey);
	}

	/**
	 * 将数据ID, Model类类型组合成Key的对应Model从缓存中取出
	 * 
	 * @param dataKey
	 * @param modelClassName
	 * @return
	 */
	public void removeCache2(Object dataKey, String modelClassName) {
		CacheKey cachKey = cacheKeyFactory.createCacheKey(dataKey.toString(), modelClassName);
		cacheManager.removeObect(cachKey);
	}

	public void clearCache() {
		cacheManager.clear();
	}

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

}
