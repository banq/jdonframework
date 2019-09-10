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

package com.jdon.model.cache;

import com.jdon.controller.cache.CacheKey;
import com.jdon.controller.cache.CacheKeyFactory;

public class ModelCacheKeyFactory extends CacheKeyFactory {
	/**
	 * 可缓存的类型: Model对象
	 */
	public final static String CACHE_TYPE_MODEL = "Model";

	public CacheKey createCacheKeyImp(String dataKey, String modelClassName) {
		return new CacheKey(CACHE_TYPE_MODEL, dataKey, modelClassName);
	}

}
