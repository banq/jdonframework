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

package com.jdon.controller.cache;

/**
 * Instances of the ObjectKey class identify the object that will be fetched
 * from the cache.
 * 
 * @author banq
 * @version 1.0
 */
public class CacheKey implements StringKey {
	private final String cacheType;
	private final String dataKey;
	private final String dataTypeName;

	public CacheKey(String cacheType, String dataKey, String dataTypeName) {
		this.cacheType = cacheType;
		this.dataTypeName = dataTypeName;
		this.dataKey = dataKey;
	}

	public String getCacheType() {
		return cacheType;
	}

	public String getDataKey() {
		return dataKey;
	}

	public String getDataTypeName() {
		return dataTypeName;
	}

	// cacheType + dataTypeName + dataKey
	public String getKey() {
		StringBuilder buffer = new StringBuilder(cacheType);
		buffer.append(dataTypeName);
		if (dataKey != null)
			buffer.append(dataKey.toString());
		return buffer.toString();
	}

	public String toString() {
		return getKey();
	}

}
